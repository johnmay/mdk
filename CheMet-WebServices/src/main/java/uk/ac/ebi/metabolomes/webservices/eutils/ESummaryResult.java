/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices.eutils;

import java.util.List;

/**
 *
 * @author pmoreno
 */
public abstract class ESummaryResult {

    /**
     * Returns all the string keywords for which a single string should be stored in the ESummaryResult. This is for
     * parsing XML elements that only have one character field (and don't hold a list of elements). When ever one of this 
     * keywords is recognized, them the ESummaryXMLResponseParser should only parse the next CHARACTER event of the XML.
     * 
     * @return List of keywords that should be processed as single unit in the XML.
     */
    public abstract List<? extends Enum> getScalarKeywords();

    /**
     * Returns all the string keywords for which a list of strings should be stored in the ESummaryResult. This is for
     * parsing XML elements that hold a list of elements. When ever one of this 
     * keywords is recognized, them the ESummaryXMLResponseParser should parse the incoming list of the XML.
     * 
     * @return 
     */
    public abstract List<? extends Enum> getListKeywords();

    /**
     * With this method, a particular ESummaryResult implementation maps the results obtained by the parser for that
     * keyword with fields that the ESummaryResult impl. should fill.
     * 
     * @param keyword
     * @param parseList 
     */
    public abstract void addListForKeyword(Enum keyword, List<String> parseList);

    /**
     * With this method, a particular ESummaryResult implementation maps the results obtained by the parser for that
     * keyword with fields that the ESummaryResult impl. should fill.
     * 
     * @param keyword
     * @param parseList 
     */
    public abstract void addScalarForKeyword(Enum keyword, String followingCharacters);

    /**
     * Here any final wrapping can be done by the ESummaryResult before being stored. For instance, translating sourceNames
     * and Identifiers to CrossReference objects.
     */
    public void wrap() {
        
    }
    
}
