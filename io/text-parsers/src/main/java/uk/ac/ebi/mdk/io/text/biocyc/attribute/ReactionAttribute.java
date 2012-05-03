package uk.ac.ebi.mdk.io.text.biocyc.attribute;

/**
 * Attributes for BioCyc reactions.dat file
 * @author John May
 * @link http://bioinformatics.ai.sri.com/ptools/flatfile-format.html#compounds.dat
 */
public enum ReactionAttribute implements Attribute {
    UNIQUE_ID,
    TYPES,
    COMMON_NAME,
    CITATIONS,
    COMMENT,
    DELTAG0,
    EC_NUMBER,
    ENZYMATIC_REACTION,
    IN_PATHWAY,
    LEFT,
    OFFICIAL_EC("OFFICIAL_EC?", "OFFICIAL_EC\\?"),
    ORPHAN("ORPHAN?", "ORPHAN\\?"),
    RIGHT,
    SIGNAL,
    SPECIES,
    SPONTANEOUS("SPONTANEOUS?", "SPONTANEOUS\\?"),
    SYNONYMS;

    private String name;
    private String pattern;

    ReactionAttribute() {
        this.name = name().replaceAll("_", "-");
        this.pattern = name;
    }

    ReactionAttribute(String name) {
        this(name, name);
    }

    ReactionAttribute(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}