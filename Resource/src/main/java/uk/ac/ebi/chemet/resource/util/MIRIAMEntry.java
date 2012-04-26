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
package uk.ac.ebi.chemet.resource.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

import uk.ac.ebi.mdk.domain.identifier.Resource;

/**
 * MIRIAMEntry.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * A Miaram Registry entry (http://www.ebi.ac.uk/miriam/main/) that stores various data about
 * base urn/definition/name etc. The main use is in generating URN annotation for identifiers
 * which is of particular use in SBML Resource MetaInfo
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
    private String name;
    private String definition;
    private String urn;
    private String url;
    private Boolean mapped;
    private Collection<String> synonyms;

    public MIRIAMEntry(String id, String regex, String resouceName, String definition, String urn,
                       String url, Collection<String> synonyms, Boolean mapped) {
        this(id, Pattern.compile(regex), resouceName, definition, urn, url, synonyms, mapped);
    }

    public MIRIAMEntry(String id, Pattern pattern, String resouceName, String definition, String urn,
                       String url, Collection<String> synonyms,  Boolean mapped) {
        this.id = id;
        this.pattern = pattern;
        this.name = resouceName;
        this.definition = definition;
        this.urn = urn;
        this.url = url;
        this.synonyms = synonyms;
        this.mapped = mapped;
    }

    /**
     * @inheritDoc
     */
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

    public String getPattern() {
        return pattern.pattern();
    }

    /**
     * @inheritDoc
     */
    public Pattern getCompiledPattern() {
        return pattern;
    }

    /**
     * @inheritDoc
     */
    public String getName() {
        return name;
    }

    /**
     * @deprecated use {@see getName()}
     */
    @Deprecated
    public String getResourceName() {
        return name;
    }

    public String getBaseURN() {
        return urn;
    }

    /**
     * @inheritDoc
     */
    public String getURN(String accession) {
        StringBuilder sb = new StringBuilder(urn.length());

        sb.append(urn).append(':').append(accession.replace(":", "%3A"));
        return sb.toString();
    }

    public String getBaseURL() {
        return this.url;
    }

    @Override
    public Boolean isMapped() {
        return mapped;
    }

    /**
     * @inheritDoc
     */
    public URL getURL(String accession) {
        try {
            String url = this.url;
            return new URL(url.replaceAll("\\$id", accession));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    public Collection<String> getSynonyms() {
        return synonyms;
    }
}
