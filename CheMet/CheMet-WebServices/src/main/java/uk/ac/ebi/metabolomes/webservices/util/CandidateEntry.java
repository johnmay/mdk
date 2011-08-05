/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices.util;

/**
 *
 * @author pmoreno
 */
public class CandidateEntry implements Comparable<CandidateEntry> {

    private String id;
    private String desc;
    private Integer distance;
    private String comment;


    /**
     * @brief Default constructor
     */
    public CandidateEntry() {
    }

    /**
     * @brief Convenience constructor
     * @param id        Candidate Identifier
     * @param desc      Description of the candidate (e.g. compound name)
     * @param distance  The Levenshtein distance (number of changes needed), as calculated by Apache Lang StringUtils
     * @param comment   Additional comments on the candidate
     */
    public CandidateEntry( String id , String desc , Integer distance , String comment ) {
        this.id = id;
        this.desc = desc;
        this.distance = distance;
        this.comment = comment;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc( String desc ) {
        this.desc = desc;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance( Integer distance ) {
        this.distance = distance;
    }

    @Override
    public int compareTo( CandidateEntry o ) {
        if ( distance == null || o.distance == null ) {
            return 0;
        }
        return this.distance.compareTo( o.distance );
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
}