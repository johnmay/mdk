package uk.ac.ebi.mdk.io.text.biocyc.attribute;

/**
 * Attributes for BioCyc compounds.dat file.
 * @author John May
 * @link http://bioinformatics.ai.sri.com/ptools/flatfile-format.html#compounds.dat
 */
public enum CompoundAttribute implements Attribute {
    COMMON_NAME,
    COFACTORS_OR_PROSTHETIC_GROUPS_OF,
    COMPONENTS,
    MONOISOTOPIC_MW,
    CHEMICAL_FORMULA,
    N1_NAME("N+1-NAME", "N\\+1-NAME"),
    INCHI,
    TYPES,
    SYSTEMATIC_NAME,
    GO_TERMS,
    IN_MIXTURE,
    MOLECULAR_WEIGHT,
    SUPERATOMS,
    ABBREV_NAME,
    UNIQUE_ID,
    ANTICODON,
    COFACTORS_OF,
    N_NAME,
    SYNONYMS,
    COMPONENT_OF,
    HAS_NO_STRUCTURE("HAS-NO-STRUCTURE?", "HAS-NO-STRUCTURE\\?"),
    CHARGE,
    ATOM_CHARGES,
    SMILES,
    PKA2,
    PKA3,
    CREDITS,
    PKA1,
    COMMENT,
    N_1_NAME,
    CITATIONS,
    DBLINKS,
    REGULATES,
    PROSTHETIC_GROUPS_OF;

    private String name;
    private String pattern;

    CompoundAttribute() {
        this.name = name().replaceAll("_", "-");
        this.pattern = name;
    }

    CompoundAttribute(String name) {
        this(name, name);
    }

    CompoundAttribute(String name, String pattern) {
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