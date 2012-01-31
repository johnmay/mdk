/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.metabolite;


/**
 *
 * @author johnmay
 */
public enum MetaboliteClass {

    PROTEIN("Protein", (byte) 1),
    NUCLEIC_ACID("Nucleic Acid", (byte) 2),
    CHEMICAL_ENTITY("Chemical Entity", (byte) 3),
    UNKNOWN("Other", (byte) 4);

    private String desc;

    private Byte index;


    private MetaboliteClass(String name, byte index) {
        this.desc = name;
        this.index = index;
    }


    @Override
    public String toString() {
        return desc;
    }


    public Byte getIndex() {
        return index;
    }


    public static MetaboliteClass valueOf(Byte index) {
        for (MetaboliteClass mc : values()) {
            if (mc.index == index) {
                return mc;
            }
        }
        throw new UnsupportedOperationException("Index not found");
    }
}
