/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.core.metabolite;


/**
 *
 * @author johnmay
 */
public enum MetaboliteClass {
    PROTEIN("Protein"), NUCLEIC_ACID("Nucleic Acid"), SMALL_MOLECULE("Small Molecule"), UNKNOWN("Other");
    private String desc;


    private MetaboliteClass(String name) {
        this.desc = name;
    }


    @Override
    public String toString() {
        return desc;
    }

}

