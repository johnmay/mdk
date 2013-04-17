/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io.xml.hmdb;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.io.xml.hmdb.marshal.HMDBDefaultMarshals;
import uk.ac.ebi.mdk.io.xml.hmdb.marshal.HMDBXMLMarshal;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A parser for Human Metabolome Database (HMDB) XML files. The parser can be
 * configured with one or more {@link HMDBXMLMarshal}s. These marshals take the
 * XML stream and marshal the contents into an {@link HMDBMetabolite} container.
 * <p/>
 *
 * Custom marshals can be defined but currently all entries in the {@link
 * HMDBMetabolite} can be loaded with {@link HMDBDefaultMarshals}. For
 * convenience there are several methods to load entices directly from a {@link
 * File}. <p/>
 *
 * <b>IMPORTANT: to use the {@link HMDBDefaultMarshals#ACCESSION} marshal you
 * must also include the {@link HMDBDefaultMarshals#SECONDARY_ACCESSION} /
 * {@link HMDBDefaultMarshals#SECOUNDARY_ACCESSION} (typo in xml tag) marshals
 * as they all use the same start tag ({@code <accession>...</accession>}).</b>
 *
 * <blockquote><pre>{@code
 *
 * // load a single file
 * HMDBMetabolite entry = HMDBParser.load("HMDB00001.xml");
 *
 * // load only common name from an xml file with 10 entries
 * Iterator<HMDBMetabolite> entries = HMDBParser.loadDirectory("/db/hmdb/xml");
 * while(entries.hasNext()){
 *    HMDBMetabolite entry = entries.next();
 * }
 *
 * }</pre></blockquote>
 *
 * @author John May
 * @see <a href="http://www.hmdb.ca>HMDB</a>
 * @see HMDBXMLMarshal
 * @see #load(java.io.File)
 * @see #loadDirectory(java.io.File)
 */
public final class HMDBParser implements Closeable {

    /* xml input factory for building the stream reader */
    private static final XMLInputFactory2 XML_FACTORY;

    static {
        XML_FACTORY = (XMLInputFactory2) XMLInputFactory2.newInstance();
        XML_FACTORY.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        XML_FACTORY.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XML_FACTORY.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        XML_FACTORY.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        XML_FACTORY.configureForSpeed();
    }

    /**
     * main input source
     */
    private final Reader reader;

    /**
     * the xml stream
     */
    private final XMLStreamReader2 xml;

    /**
     * entry storage
     */
    private final List<HMDBMetabolite> metabolites = new ArrayList<HMDBMetabolite>(1); // normally the XML only contains a single entry

    /**
     * loaded marshals
     */
    private final Map<String, HMDBXMLMarshal> marshals = new TreeMap<String, HMDBXMLMarshal>();

    /**
     * Create a new parser from a specified {@link Reader} and 0 or more {@link
     * HMDBXMLMarshal}s.
     *
     * <blockquote><pre>{@code
     * HMDBParser parser = new HMDBParser(new FileReader("HMDB00001.xml")
     *                                    HMDBDefaultMarshals.values());
     * Collection<HMDBMetabolite> entries = parser.metabolites();
     * }</pre></blockquote>
     *
     * @param reader   data input
     * @param marshals the marshals to load the entry with
     * @throws XMLStreamException       low level xml error
     * @throws IllegalArgumentException if the provided reader was null
     * @see HMDBDefaultMarshals
     * @see HMDBXMLMarshal
     */
    public HMDBParser(Reader reader,
                      HMDBXMLMarshal... marshals) throws
                                                  XMLStreamException {

        this.reader = reader;

        if (reader == null)
            throw new IllegalArgumentException("reader must not be null");
        if (marshals.length == 0)
            throw new IllegalArgumentException("parser requires one or more marshal");

        xml = (XMLStreamReader2) XML_FACTORY.createXMLStreamReader(reader);

        for (HMDBXMLMarshal marshal : marshals) {
            if (marshal == null)
                throw new IllegalArgumentException("one of the provided marshals was null");
            this.marshals.put(marshal.tag(), marshal);
        }

        // check if ACCESSION is being used without the SECONDARY_ACCESSION(s)
        if (this.marshals.containsKey(HMDBDefaultMarshals.ACCESSION.tag())
                && !(this.marshals.containsKey(HMDBDefaultMarshals.SECONDARY_ACCESSION.tag())
                && this.marshals.containsKey(HMDBDefaultMarshals.SECOUNDARY_ACCESSION.tag())))
            throw new IllegalArgumentException("When parsing ACCESSION the SECONDARY_ACCESSION(s) must be included, refer to javadoc");

        parse();
    }

    /**
     * parse the xml stream.
     *
     * @throws XMLStreamException low level stream exception
     */
    private void parse() throws XMLStreamException {

        HMDBMetabolite metabolite = null;

        while (xml.hasNext()) {
            int event = xml.next();
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    String tag = xml.getLocalName();
                    if (tag.equals("metabolite")) {

                        if (metabolite != null)
                            metabolites.add(metabolite);

                        metabolite = new HMDBMetabolite();

                    } else {
                        HMDBXMLMarshal handler = marshals.get(tag);
                        if (handler != null) {
                            handler.marshal(xml, metabolite);
                        }
                    }
                    break;
            }
        }

        if (metabolite != null)
            metabolites.add(metabolite);

    }

    /**
     * Access the metabolites loaded by this reader.
     *
     * @return immutable collection of metabolites
     */
    public Collection<HMDBMetabolite> metabolites() {
        return Collections.unmodifiableList(metabolites);
    }

    /**
     * Load a single metabolite entry from a single xml file. This method will
     * use all default marshal (see {@link HMDBDefaultMarshals}).
     *
     * <blockquote><pre>
     * HMDBMetabolite entry = HMDBParser.load("HMDB00001.xml");
     * </pre></blockquote>
     *
     * @param file an xml file
     * @return a single metabolite
     * @throws IOException              low-level io or xml error
     * @throws IllegalArgumentException the file contained more then one entry,
     *                                  contained no entries, did not exist or
     *                                  was a directory
     */
    public static HMDBMetabolite load(File file) throws IOException {
        return load(file, HMDBDefaultMarshals.values());
    }

    /**
     * Load a single metabolite entry from a single xml file. This method will
     * use the specified marshals.
     *
     * <blockquote><pre>
     * // load the common name only
     * HMDBMetabolite entry = HMDBParser.load("HMDB00001.xml",
     *                                        HMDBDefaultMarshals.COMMON_NAME);
     * </pre></blockquote>
     *
     * @param file     an xml file
     * @param marshals 0 or more marshal to load data into an entry
     * @return a single metabolite
     * @throws IOException              low-level io or xml error
     * @throws IllegalArgumentException the file contained more then one entry,
     *                                  contained no entries, did not exist or
     *                                  was a directory
     */
    public static HMDBMetabolite load(File file, HMDBXMLMarshal... marshals) throws
                                                                             IOException {
        Collection<HMDBMetabolite> metabolites = loadAll(file, marshals);

        if (metabolites.isEmpty())
            throw new IllegalArgumentException("file did not contain any metabolite entries");
        if (metabolites.size() > 1)
            throw new IllegalArgumentException("file contained multiple entries, use loadAll()");

        // we have exactly one entry
        return metabolites.iterator().next();
    }

    /**
     * Load all metabolite entries from a single xml file. This method will use
     * the specified marshals.
     *
     * <blockquote><pre>{@code
     * // load the common name from an xml file with 10 entries
     * Collection<HMDBMetabolite> entries = HMDBParser.loadAll("HMDB00001-HMDB00010.xml",
     *                                                         HMDBDefaultMarshals.COMMON_NAME);
     * }</pre></blockquote>
     *
     * @param file     an xml file
     * @param marshals 0 or more marshal to load data into an entry
     * @return a single metabolite
     * @throws IOException              low-level io or xml error
     * @throws IllegalArgumentException the file was null, did not exist or was
     *                                  a directory
     */
    public static Collection<HMDBMetabolite> loadAll(File file,
                                                     HMDBXMLMarshal... marshals) throws
                                                                                 IOException {
        if (file == null)
            throw new IllegalArgumentException("file cannot be null");
        if (!file.exists())
            throw new IllegalArgumentException("file does not exist");
        if (file.isDirectory())
            throw new IllegalArgumentException("file must not be a directory");


        HMDBParser reader = null;

        try {
            reader = new HMDBParser(new FileReader(file),
                                    marshals);
            return reader.metabolites();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Lazily load single metabolite entries from a directory of XML files. This
     * method will use the specified marshals.
     *
     * <blockquote><pre>{@code
     * // load the common name from an xml file with 10 entries
     * Iterator<HMDBMetabolite> entries = HMDBParser.loadDirectory("/db/hmdb/xml",
     *                                                             HMDBDefaultMarshals.COMMON_NAME);
     * while(entries.hasNext()){
     *    HMDBMetabolite entry = entries.next();
     * }
     * }</pre></blockquote>
     *
     * @param directory a directory of XML files
     * @throws IOException              low-level io or xml error
     * @throws IllegalArgumentException a file contained more then one entry or
     *                                  contained no entries
     */
    public static Iterator<HMDBMetabolite> loadDirectory(File directory) throws
                                                                         IOException {
        return loadDirectory(directory, HMDBDefaultMarshals.values());
    }

    /**
     * Lazily load single metabolite entries from a directory of XML files. This
     * method will use all default marshal (see {@link HMDBDefaultMarshals}).
     *
     * <blockquote><pre>{@code
     * // load the common name from an xml file with 10 entries
     * Iterator<HMDBMetabolite> entries = HMDBParser.loadDirectory("/db/hmdb/xml");
     * while(entries.hasNext()){
     *    HMDBMetabolite entry = entries.next();
     * }
     * }</pre></blockquote>
     *
     * @param directory a directory of XML files
     * @throws IOException              low-level io or xml error
     * @throws IllegalArgumentException the directory was not a directory, a
     *                                  file (of the directory) contained more
     *                                  then one entry or contained no entries
     */
    public static Iterator<HMDBMetabolite> loadDirectory(File directory,
                                                         final HMDBXMLMarshal... marshals) throws
                                                                                           IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("expected a driectory");
        }

        // only get xml files
        List<File> files = Arrays.asList(directory.listFiles(new FilenameFilter() {
            @Override public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        }));

        final Iterator<File> fileIterator = files.iterator();

        // use an iterator to read/parse each file one at a time
        return new Iterator<HMDBMetabolite>() {

            @Override public boolean hasNext() {
                return fileIterator.hasNext();
            }

            @Override public HMDBMetabolite next() {
                try {
                    return HMDBParser.load(fileIterator.next(), marshals);
                } catch (IOException e) {
                    return new HMDBMetabolite();
                }
            }

            @Override public void remove() {
                throw new IllegalArgumentException("cannot remove entry");
            }
        };

    }

    /**
     * @inheritDoc
     */
    @Override public void close() throws IOException {
        reader.close();
    }
}
