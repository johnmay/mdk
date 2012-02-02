/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.metabolite;

import uk.ac.ebi.interfaces.MetaboliteClass;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public enum MetaboliteClassImplementation implements MetaboliteClass {

    PROTEIN("Protein", (byte) 1),
    NUCLEIC_ACID("Nucleic Acid", (byte) 2),
    CHEMICAL_ENTITY("Chemical Entity", (byte) 3),
    UNKNOWN("Other", (byte) 4);

    private String desc;

    private byte index;


    private MetaboliteClassImplementation(String name, byte index) {
        this.desc = name;
        this.index = index;
    }


    @Override
    public String toString() {
        return desc;
    }


    public byte getIndex() {
        return index;
    }


    public static MetaboliteClassImplementation valueOf(Byte index) {
        for (MetaboliteClassImplementation mc : values()) {
            if (mc.index == index) {
                return mc;
            }
        }
        throw new UnsupportedOperationException("Index not found");
    }
}
