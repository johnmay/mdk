/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.descriptor.annotation;

/**
 * AnnotationFlag.java
 *
 *
 * @author johnmay
 * @date May 13, 2011
 */
public enum AnnotationFlag {

    RED( "images/flags/red_flag.png" , 0 ),
    AMBER( "images/flags/amber_flag.png" , 1 ),
    GREEN( "images/flags/green_flag.png" , 2 ),
    NONE( "images/flags/white_flag.png" , 3 );
    private String flatPath;
    private int confidence;

    private AnnotationFlag( String imageLocation ,
                            int confidence ) {
        this.flatPath = imageLocation;
        this.confidence = confidence;
    }

    public String getFlagPath() {
        return flatPath;
    }

    /**
     * This is for ordering flags in RED,AMBER,GREEN,NONE from worst to best
     */
    public int getConfidenceRank() {
        return confidence;
    }

}
