/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.resource.organism;


/**
 *
 * @author johnmay
 */
public enum Kingdom {

    ARCHAEA("archaea,a"),
    BACTERIA("bacteria,b"),
    EUKARYOTA("eukaryote,eukaryota,e"),
    VIRUSES_AND_PHAGES(
    "virus,viruses,phage,phages,v"),
    UNDEFINED("");
    private String[] names;


    private Kingdom(String names) {
        this.names = names.split(",");
    }


    private boolean matches(String name) {
        for( String altName : names ) {
            if( altName.equals(name) ) {
                return true;
            }
        }
        return false;
    }


    public static Kingdom getKingdom(String name) {
        String name_lc = name.toLowerCase();
        for( Kingdom kingdom : Kingdom.values() ) {
            if( kingdom.matches(name_lc) ) {
                return kingdom;
            }
        }
        return Kingdom.UNDEFINED;
    }


}

