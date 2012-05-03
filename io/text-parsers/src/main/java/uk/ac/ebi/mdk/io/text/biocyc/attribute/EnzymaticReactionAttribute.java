package uk.ac.ebi.mdk.io.text.biocyc.attribute;

/**
 * Attributes for BioCyc enzrxns.dat file
 *
 * @author John May
 * @link http://bioinformatics.ai.sri.com/ptools/flatfile-format.html#enzrxns.dat
 */
public enum EnzymaticReactionAttribute implements Attribute {
    UNIQUE_ID,
    TYPES,
    COMMON_NAME,
    ALTERNATIVE_COFACTORS,
    ALTERNATIVE_SUBSTRATES,
    CITATIONS,
    COFACTOR_BINDING_COMMENT,
    COFACTORS,
    COFACTORS_OR_PROSTHETIC_GROUPS,
    COMMENT,
    ENZYME,
    KM,
    PH_OPT,
    PROSTHETIC_GROUPS,
    REACTION,
    REACTION_DIRECTION,
    REGULATED_BY,
    REQUIRED_PROTEIN_COMPLEX,
    SYNONYMS,
    TEMPERATURE_OPT;

    private String name;
    private String pattern;

    EnzymaticReactionAttribute() {
        this.name = name().replaceAll("_", "-");
        this.pattern = name;
    }

    EnzymaticReactionAttribute(String name) {
        this(name, name);
    }

    EnzymaticReactionAttribute(String name, String pattern) {
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