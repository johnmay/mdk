/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.util;

import java.util.Collection;
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

    public Set<CandidateEntry> decideBestCandidate(String query, Map<String,String> candidates) {

        Set<CandidateEntry> orderedCand = new TreeSet<CandidateEntry>();
        for (String identifier : candidates.keySet()) {
            CandidateEntry candidateT = new CandidateEntry();
            candidateT.setId(identifier);
            candidateT.setDesc(candidates.get(identifier));
            String prefix = this.identiferPrefix(identifier);
            if(prefix!=null && candidateT.getDesc().contains(prefix))
                candidateT.setDesc(candidateT.getDesc().replace(prefix, ""));
            candidateT.setDistance(StringUtils.getLevenshteinDistance(query.toLowerCase().trim(), candidateT.getDesc().toLowerCase().trim()));

            orderedCand.add(candidateT);
            }

        return orderedCand;
        }

    public Set<CandidateEntry> decideBestCandidate(String query, Collection<String> candidates) {
        Set<CandidateEntry> orderedCand = new TreeSet<CandidateEntry>();
        for (String candidate : candidates) {
            CandidateEntry candidateT = new CandidateEntry();
            //candidateT.setId(candidate);
            candidateT.setDesc(candidate);
            String prefix = this.identiferPrefix(candidate);
            if(prefix!=null && candidateT.getDesc().contains(prefix))
                candidateT.setDesc(candidateT.getDesc().replace(prefix, ""));
            candidateT.setDistance(StringUtils.getLevenshteinDistance(query.toLowerCase().trim(), candidateT.getDesc().toLowerCase().trim()));

            orderedCand.add(candidateT);
        }

        return orderedCand;
    }

    public EntryDecider() {
        this.prefixPattern = Pattern.compile("([A-Z]+:)\\d");
    }

    private String identiferPrefix(String identifer) {
        Matcher prefixMatcher = this.prefixPattern.matcher(identifer);
        if(prefixMatcher.find())
            return prefixMatcher.group(1);
        else
            return null;
        }

    }
