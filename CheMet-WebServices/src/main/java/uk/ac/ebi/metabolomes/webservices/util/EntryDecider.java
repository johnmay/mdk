/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author pmoreno
 */
public class EntryDecider {

    private Pattern prefixPattern;

    /**
     * 
     * @param query
     * @param candidates
     * @return
     * @deprecated {@see getOrderedCandidates(String, Map<String , String> candidates)} This uses a list and thus does not colapse entries with duplicate
     */
    @Deprecated
    public Set<CandidateEntry> decideBestCandidate( String query , Map<String , String> candidates ) {

        Set<CandidateEntry> orderedCand = new TreeSet<CandidateEntry>();
        for ( String identifier : candidates.keySet() ) {
            CandidateEntry candidateT = new CandidateEntry();
            candidateT.setId( identifier );
            candidateT.setDesc( candidates.get( identifier ) );
            String prefix = this.identiferPrefix( identifier );
            if ( prefix != null && candidateT.getDesc().contains( prefix ) ) {
                candidateT.setDesc( candidateT.getDesc().replace( prefix , "" ) );
            }
            candidateT.setDistance( StringUtils.getLevenshteinDistance( query.toLowerCase().trim() ,
                                                                        candidateT.getDesc().toLowerCase().trim() ) );

            orderedCand.add( candidateT );
        }

        return orderedCand;
    }
    
    public List<CandidateEntry> getOrderedCandidates( String query , Map<String , String> candidates ) {

        List<CandidateEntry> orderedCand = new ArrayList<CandidateEntry>();
        for ( String identifier : candidates.keySet() ) {
            CandidateEntry candidateT = new CandidateEntry();
            candidateT.setId( identifier );
            candidateT.setDesc( candidates.get( identifier ) );
            String prefix = this.identiferPrefix( identifier );
            if ( prefix != null && candidateT.getDesc().contains( prefix ) ) {
                candidateT.setDesc( candidateT.getDesc().replace( prefix , "" ) );
            }
            candidateT.setDistance( StringUtils.getLevenshteinDistance( query.toLowerCase().trim() ,
                                                                        candidateT.getDesc().toLowerCase().trim() ) );

            orderedCand.add( candidateT );
        }
        
        Collections.sort( orderedCand );

        return orderedCand;
    }

    /**
     *
     * @param query
     * @param candidates
     * @return
     * @deprecated {@see getOrderedCandidates(String, Collection<String>)} This uses a list and thus does not colapse entries with duplicate
     */
    @Deprecated
    public Set<CandidateEntry> decideBestCandidate( String query , Collection<String> candidates ) {
        Set<CandidateEntry> orderedCand = new TreeSet<CandidateEntry>();
        for ( String candidate : candidates ) {
            CandidateEntry candidateT = new CandidateEntry();
            //candidateT.setId(candidate);
            candidateT.setDesc( candidate );
            String prefix = this.identiferPrefix( candidate );
            if ( prefix != null && candidateT.getDesc().contains( prefix ) ) {
                candidateT.setDesc( candidateT.getDesc().replace( prefix , "" ) );
            }
            candidateT.setDistance( StringUtils.getLevenshteinDistance( query.toLowerCase().trim() ,
                                                                        candidateT.getDesc().toLowerCase().trim() ) );

            orderedCand.add( candidateT );
        }

        return orderedCand;
    }

    public List<CandidateEntry> getOrderedCandidates( String query , Collection<String> candidates ) {

        String formatedQuery = query.toLowerCase( Locale.ENGLISH ).trim();

        List<CandidateEntry> orderedCand = new ArrayList<CandidateEntry>();

        for ( String candidate : candidates ) {

            String description = removePrefix( candidate );
            Integer distance = StringUtils.getLevenshteinDistance( formatedQuery ,
                                                                   description.toLowerCase( Locale.ENGLISH ).trim() );
            orderedCand.add( new CandidateEntry( "" ,
                                                 description ,
                                                 distance ,
                                                 "" ) );
        }

        Collections.sort( orderedCand );

        return orderedCand;
    }

    private String removePrefix( String description ) {
        String prefix = this.identiferPrefix( description );
        if ( prefix != null && description.contains( prefix ) ) {
            return description.replace( prefix , "" );
        }
        return description;
    }

    public EntryDecider() {
        this.prefixPattern = Pattern.compile( "([A-Z]+:)\\d" );
    }

    private String identiferPrefix( String identifer ) {
        Matcher prefixMatcher = this.prefixPattern.matcher( identifer );
        if ( prefixMatcher.find() ) {
            return prefixMatcher.group( 1 );
        } else {
            return null;
        }
    }
}
