package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;

/**
 * Provides a dynamic identifier where the brief/description
 * can be specified.
 * TODO This possibly needs improving possibly via
 * TODO registering to a factory which keeps track of the unique brief/descriptions
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DynamicIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(DynamicIdentifier.class);
    
    private String brief;
    private String description;
    private String accession;

    public DynamicIdentifier(String brief, String description, String accession) {
        super(accession);
        this.brief = brief;
        this.description = description;
    }

    public DynamicIdentifier(String brief, String accession) {
        this(brief, "Unavailable", accession);
    }

    public DynamicIdentifier(String accession) {
        this("Unknown", accession);
    }

    public DynamicIdentifier(){
        this("");
    }

    @Override
    public String getShortDescription() {
        return brief;
    }

    @Override
    public String getLongDescription() {
        return description;
    }

    @Override
    public Identifier newInstance() {
        return new DynamicIdentifier();
    }
}
