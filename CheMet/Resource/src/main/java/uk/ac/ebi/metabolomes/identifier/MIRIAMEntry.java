/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.identifier;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Resource;

/**
 * MIRIAMEntry.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * A Miaram Registry entry (http://www.ebi.ac.uk/miriam/main/) that stores various data about
 * base urn/definition/name etc. The main use is in generating URN annotation for identifiers
 * which is of particular use in SBML Resource Description
 * <pre>
 * <code> MIRIAMEntry chebiRDF = MIRIAMResource.CHEBI.getEntry(); // CheMet-IO (atm) </code>
 * <code> chebiRDF.getBaseURN()                                   // urn:miriam:obo.chebi </code>
 * <code> chebiRDF.getURN("CHEBI:15442")                          // urn:miriam:obo.chebi:CHEBI%A315442 </code>
 * </pre>
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 *
 *
 *
 */
public class MIRIAMEntry
        implements Resource {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(MIRIAMEntry.class);
    private String id;
    private Pattern pattern;
    private String resouceName;
    private String definition;
    private String urn;
    private String url;
    private Collection<String> synonyms;

    public MIRIAMEntry(String id, String regex, String resouceName, String definition, String urn,
                       String url, Collection<String> synonyms) {
        this(id, Pattern.compile(regex), resouceName, definition, urn, url, synonyms);
    }

    public MIRIAMEntry(String id, Pattern pattern, String resouceName, String definition, String urn,
                       String url, Collection<String> synonyms) {
        this.id = id;
        this.pattern = pattern;
        this.resouceName = resouceName;
        this.definition = definition;
        this.urn = urn;
        this.url = url;
        this.synonyms = synonyms;
    }

    public String getDescription() {
        return definition;
    }

    /**
     * @deprecated use {@see getDescription()}
     */
    @Deprecated
    public String getDefinition() {
        return definition;
    }

    public String getId() {
        return id;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public Pattern getCompiledPattern() {
        return pattern;
    }

    public String getName() {
        return resouceName;
    }

    /**
     * @deprecated use {@see getName()}
     */
    @Deprecated
    public String getResourceName() {
        return resouceName;
    }

    public String getBaseURN() {
        return urn;
    }

    public String getURN(String accession) {
        StringBuilder sb = new StringBuilder(urn.length());

        sb.append(urn).append(':').append(accession.replace(":", "%3A"));
        return sb.toString();
    }

    public String getBaseURL() {
        return this.url;
    }

    public URL getURL(String accession) {
        try {
            String url = this.url;
            return new URL(url.replaceAll("\\$id", accession));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Collection<String> getSynonyms() {
        return synonyms;
    }
}
