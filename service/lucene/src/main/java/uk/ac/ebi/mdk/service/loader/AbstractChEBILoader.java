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

package uk.ac.ebi.mdk.service.loader;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractChEBILoader - 28.02.2012 <br/>
 * <p/>
 * Provides methods for resolving ChEBI secondary identifiers to their primary identifier
 * and the status of an entry. The loader requires the ChEBI compounds.tsv flat-file from ChEBI.
 *
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractChEBILoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractChEBILoader.class);

    private Map<String, String>     parentMap = new HashMap<String, String>();
    private Multimap<String, String> childMap = HashMultimap.create();
    private Map<String,Character>   statusMap = new HashMap<String,Character>();

    /**
     * @inheritDoc
     */
    public AbstractChEBILoader(LuceneIndex index) throws IOException {
        super(index);

        addRequiredResource("ChEBI Compounds",
                            "compound.tsv file from ChEBI, specifying preferred names, secondary and primary accessions",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv"));

    }

    /**
     * Creates a map from the ChEBI Compounds resources of secondary to primary parentMap.
     *
     * @throws IOException problem reading file
     * @throws MissingLocationException if ChEBI Compounds resource location is missing
     */
    public void createMap() throws IOException, MissingLocationException {


        ResourceFileLocation location = getLocation("ChEBI Compounds");
        CSVReader csv = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');

        List<String> header = Arrays.asList(csv.readNext());
        int accessionIndex  = header.indexOf("CHEBI_ACCESSION");
        int parentIndex     = header.indexOf("PARENT_ID");
        int statusIndex     = header.indexOf("STATUS");

        Pattern NULL_PATTERN = Pattern.compile("null");
        Pattern ACCESSION_PATTERN = Pattern.compile("(?:C[hH]EBI:)?(\\d+)");

        String[] row = null;
        while ((row = csv.readNext()) != null) {

            String    accession = row[accessionIndex];
            String    parent    = row[parentIndex];
            Character status    = row[statusIndex].charAt(0);

            Matcher accessionMatcher = ACCESSION_PATTERN.matcher(accession);
            Matcher parentMatcher = ACCESSION_PATTERN.matcher(parent);

            if (accessionMatcher.find()) {
                
                String childAcc  = accessionMatcher.group(1);
                String parentAcc = parentMatcher.find() ? parentMatcher.group(1) : childAcc;

                childMap.put(parentAcc, "CHEBI:" + childAcc);
                childMap.put("CHEBI:" + parentAcc, "CHEBI:" + childAcc);

                parentMap.put(childAcc, "CHEBI:" + parentAcc);
                parentMap.put("CHEBI:" + childAcc, "CHEBI:" + parentAcc);

                statusMap.put(childAcc, status);
                statusMap.put("CHEBI:" + childAcc, status);

            }

        }

        location.close();
        csv.close();

    }

    /**
     * Access whether the accession is active using the STATUS column in the compounds.tsv file.
     * If status is matches to 'C' then the entry is active. (default = false)
     * @see #getStatus(String)
     * @param accession
     * @return
     * @throws MissingLocationException thrown if compounds.tsv is not provided
     * @throws IOException
     */
    public boolean isActive(String accession) throws IOException {
        if(statusMap.isEmpty())
            createMap();

        if(statusMap.containsKey(accession)){
            return statusMap.get(accession).equals('C');
        }
        if(statusMap.containsKey(getPrimaryIdentifier(accession))){
            return statusMap.get(accession).equals('C');
        }

        return false;

    }

    /**
     * Access the status of the entry,
     * E=Exists, C=Checked, S=Submitted, O=Obsolete, D=Deleted.
     * @param accession
     * @return
     * @throws IOException
     * @throws MissingLocationException thrown if compounds.tsv is not provided
     */
    public Character getStatus(String accession) throws IOException, MissingLocationException {
        if(statusMap.isEmpty())
            createMap();

        if(statusMap.containsKey(accession)){
            return statusMap.get(accession);
        }
        if(statusMap.containsKey(getPrimaryIdentifier(accession))){
            return statusMap.get(accession);
        }

        return '\0';
    }

    /**
     * Access the primary identifier for the given accession. The accession can be a straight number
     * or prefixed with "CHEBI:". If the map is not build then a new map is build (if you have already
     * opened the 'ChEBI Compounds' resource this method may fail. To avoid this, call {@see createMap()}
     * as the first thing in the update() method). If no identifier mapping is found the provided accession
     * is returned.
     *
     * @param accession
     * @return
     * @throws IOException
     * @throws MissingLocationException
     */
    public String getPrimaryIdentifier(String accession) throws IOException, MissingLocationException {

        if (parentMap.isEmpty())
            createMap();

        if (parentMap.containsKey(accession)) {
            return parentMap.get(accession);
        }

        return accession;

    }
    
    public Collection<String> getAllIdentifiers(String accession) throws IOException, MissingLocationException {
        String primary = getPrimaryIdentifier(accession);
        if(childMap.get(primary).size() == 0){
            System.out.println(primary);
        }
        return childMap.get(primary);
    }
    
    
    public Map<String,Integer> getHeaderMap(String[] row){
        Map<String,Integer> map = new HashMap<String,Integer>();
        for(int i = 0; i < row.length ; i++){
            map.put(row[i], i);
        }
        return map;
    }

}
