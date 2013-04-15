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

package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.AnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Used converting output from SummariseReferences from TSV to SDF
 *
 * @author John May
 */
public class SubliminalTSV2SDF extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(SubliminalTSV2SDF.class);

    public static void main(String[] args) {
        new SubliminalTSV2SDF().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "Input directory of TSV containing the columns; query.accession, query.name, xref.accession, xref.resource and xref.mir)"));
        add(new Option("o", "output", true, "SDF file output"));
    }

    @Override
    public void process() {

        File output = getFile("o");
        File intput = getFile("i");

        File[] files = intput.listFiles();

        Map<String, Metabolite> globalMap = new HashMap<String, Metabolite>();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                for (Map.Entry<String, Metabolite> e : load(file).entrySet()) {
                    if (!globalMap.containsKey(e.getKey())) {
                        globalMap.put(e.getKey(), e.getValue());
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error reading file " + file.getName(), e);
            }
        }

        try {
            writeSDF(output, globalMap);
        } catch (IOException e) {
            LOGGER.error("Error writing SDF file " + output.getName(), e);
        }

    }

    /**
     * Output the collapsed map to an SDF file
     */
    private void writeSDF(File output, Map<String, Metabolite> map) throws IOException {

        ServiceManager manager = DefaultServiceManager.getInstance();

        SDFWriter writer = new SDFWriter(new FileWriter(output));

        int size = map.size();
        int missed = 0;
        int written = 0;

        for (Map.Entry<String, Metabolite> e : map.entrySet()) {

            Metabolite m = e.getValue();


            for (CrossReference xref : m.getAnnotationsExtending(CrossReference.class)) {

                Identifier identifier = xref.getIdentifier();

                if (manager.hasService(identifier, StructureService.class)) {

                    StructureService service = manager.getService(identifier, StructureService.class);

                    IAtomContainer atomContainer = service.getStructure(identifier);

                    if (atomContainer.getAtomCount() > 0) {

                        atomContainer.setProperty("Subliminal.Name", m.getName());
                        atomContainer.setProperty("Subliminal.Identifier", m.getAccession());
                        atomContainer.setProperty("Identifier.URN", identifier.getURN());
                        atomContainer.setProperty("Identifier.URL", identifier.getURL());

                        try {
                            writer.write(atomContainer);
                            written++;
                        } catch (CDKException ex) {
                            LOGGER.warn("Could not writer " + m.getName(), ex);
                        }

                    } else {
                        missed++;
                        System.err.println("No structure found for " + identifier);
                    }

                } else {
                    //System.err.println("No StructureService available for " + identifier.getShortDescription());
                }

            }


        }

        System.out.println("written " + written + "/" + size);
        System.out.println("missed  " + missed + "/" + size);



        writer.close();

    }

    private Map<String, Metabolite> load(File file) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(file), '\t', '\0');

        Map<String, Integer> headers = createMap(reader.readNext());

        Map<String, Metabolite> metaboliteMap = new HashMap<String, Metabolite>();

        IdentifierFactory identifierFactory = DefaultIdentifierFactory.getInstance();
        EntityFactory entityFactory = DefaultEntityFactory.getInstance();
        AnnotationFactory annotationFactory = DefaultAnnotationFactory.getInstance();

        String[] row = null;
        while ((row = reader.readNext()) != null) {


            String id = row[headers.get("query.accession")];
            String name = row[headers.get("query.name")];

            if (!metaboliteMap.containsKey(name)) {
                Metabolite m = entityFactory.ofClass(Metabolite.class);
                metaboliteMap.put(name, m);
                m.setIdentifier(new BasicChemicalIdentifier(id));
                m.setName(name);
                m.setAbbreviation(name);
            }

            Metabolite m = metaboliteMap.get(name);

            Identifier identifier = identifierFactory.ofName(row[headers.get("xref.resource")],
                                                             row[headers.get("xref.accession")]);
            if(identifier != IdentifierFactory.EMPTY_IDENTIFIER)
                m.addAnnotation(annotationFactory.getCrossReference(identifier));


        }

        reader.close();

        return metaboliteMap;

    }


    private Map<String, Integer> createMap(String[] row) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < row.length; i++) {
            map.put(normalise(row[i]), Integer.valueOf(i));
        }
        return map;
    }


    private String normalise(String value) {
        value = value.toLowerCase(Locale.ENGLISH).trim();
        return value.replaceAll("\\s+", ".");
    }

}
