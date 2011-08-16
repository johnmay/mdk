/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.identifier;

/**
 *
 * @author johnmay
 */
public enum Kingdom {
    ARCHEA( "archea,a" ) , BACTERIA( "bacteria,b" ) , EUKARYOTE( "eukaryote,e" ) , VIRUSES_AND_PHAGES( "virus,phage,v" ) ,
    UNDEFINED( "" );
    private String[] names;

    private Kingdom( String names ) {
        this.names = names.split( "," );
    }

    private boolean matches( String name ) {
        for ( String altName : names ) {
            if ( altName.equals( name ) ) {
                return true;
            }
        }
        return false;
    }

    public static Kingdom getKingdom( String name ) {
        String name_lc = name.toLowerCase();
        for ( Kingdom kingdom : Kingdom.values() ) {
            if ( kingdom.matches( name_lc ) ) {
                return kingdom;
            }
        }
        return Kingdom.UNDEFINED;
    }

}
