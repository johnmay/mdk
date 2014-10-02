/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.io.text.kegg;


import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @name    KEGGReactionEntry
 * @date    2013.03.06
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public final class KEGGReactionEntry extends AttributedEntry<KEGGReactionField, String> {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionEntry.class);

    private Multimap<String, String> compId2coeff;
    private Map<String, String> rpair;
    private List<String> pathways;
    private List<String> ecs;

    public KEGGReactionEntry() {
        ecs = new ArrayList<String>();
        pathways = new ArrayList<String>();
        rpair = new HashMap<String, String>();
    }

    /**
     * @deprecated this is only valid for the output of the deprecated {@link KEGGReactionParser}. Use {@link #get(Enum)}
     * and parse appropiately instead
     *
     * @return
     */
    public Integer numberOfReactants() {
        int count=0;
        if(this.compId2coeff==null)
            getCompId2coeff();
        for(String coeff : this.compId2coeff.values())
            if(coeff.charAt(0)=='-') count++;
        return count;
    }

    /**
     *
     *
     * @return
     */
    public Integer numberOfProducts() {
        int count=0;
        if(this.compId2coeff==null)
            getCompId2coeff();
        for(String coeff : this.compId2coeff.values())
            if(coeff.charAt(0)!='-') count++;
        return count;
    }

    /**
     * @deprecated this is only valid for the output of the deprecated {@link KEGGReactionParser}. Use {@link #get(Enum)}
     * and parse appropiately instead
     *
     * @return
     */
    public Integer numberOfPathways() {
        return this.pathways.size();
    }

    /**
     * @deprecated this is only valid for the output of the deprecated {@link KEGGReactionParser}.
     *
     * @return
     */
    public void addEcNumber(String ec) {
        this.ecs.add(ec);
    }

    /**
     * Produces a representation of the reaction as a Multimap, where the keys are compound identifiers and values their
     * coefficient as strings, having reactants a minus sign before the number. Multimaps are required because polymeric
     * reactions have the same compounds at both sides. The map doesn't use numbers for the same reason (m, n, m+1, n+1,
     * coefficients used in polymeric reaction case).
     *
     * @return reaction represented as a multimap, keys are compound ids.
     */
    public Multimap<String, String> getCompId2coeff() {
        if(compId2coeff==null) {
            Joiner joiner = Joiner.on(" ");
            Multimap<String,String> comp2coeff = HashMultimap.create();
            String eqLines = joiner.join(get(KEGGReactionField.EQUATION));
            if(eqLines.length()>0) {
                String[] eqSides = this.parseReaction(eqLines);
                if(eqSides.length==2) {
                    comp2coeff.putAll(this.parseReactionSide(eqSides[0], "-"));
                    comp2coeff.putAll(this.parseReactionSide(eqSides[1], ""));
                }
            }
            this.compId2coeff = comp2coeff;
        }
        return compId2coeff;
    }

    private String[] parseReaction(String reactionLine) {
        String[] sides = reactionLine.split("<=>");
        if (sides.length == 2) {
            sides[0] = sides[0].trim();
            sides[1] = sides[1].trim();
        } else
            LOGGER.warn("Could not parse reaction line: " + reactionLine);
        return sides;
    }

    private Multimap<String, String> parseReactionSide(String side, String sign) {
        String[] coeffSpecies = side.split(" \\+ ");
        Multimap<String, String> compCoeff = HashMultimap.create();
        for (String coeffSpecie : coeffSpecies) {
            String[] aux = coeffSpecie.split(" +");
            if (aux.length > 1) {
                compCoeff.put(aux[1].trim(), sign+aux[0].trim());
            } else {
                // no number, case for 1 normally.
                compCoeff.put(coeffSpecie.trim(), sign + "1.0");
            }
        }
        return compCoeff;
    }

    /**
     * Returns a map of pathway identifiers to pathway names.
     *
     * @return map from identifiers to names.
     */
    public Map<String, String> getPathways() {
        Map<String,String> id2pathName = new HashMap<String, String>();
        for(String line : get(KEGGReactionField.PATHWAY)) {
            try {
                String[] tokens = line.split("\\s\\s");
                id2pathName.put(tokens[0],tokens[1]);
            } catch (Exception e) {
                LOGGER.error("PATHWAY line "+line+" does not seem to conform to expected pattern.");
            }
        }
        return id2pathName;
    }

    /**
     * Produces the list of EC Numbers associated to this entry.
     *
     * @return list of EC Numbers as String.
     */
    public List<String> getECNumbers() {
        List<String> ecNums = new ArrayList<String>();
        String[] ecsTokens = Joiner.on(" ").join(get(KEGGReactionField.ENZYME)).split(" ");
        for(String ec : ecsTokens)
            ecNums.add(ec);
        return ecNums;
    }



    /**
     * Returns the name of the reaction.
     *
     * @return
     */
    public String getName() {
        if(get(KEGGReactionField.NAME).size()==1)
            return getFirst(KEGGReactionField.NAME);
        Joiner joiner = Joiner.on(" ");
        return joiner.join(get(KEGGReactionField.NAME));
    }


    /**
     *
     *
     * @return
     */
    public String getReactionID() {
        try {
            String tokens[] = getFirst(KEGGReactionField.ENTRY).split("\\s+");
            return tokens[0];
        } catch (NullPointerException e) {
            LOGGER.error("No ENTRY field present for entry or entry is null");
            return null;
        }
    }


    /**
     * @deprecated this is only valid for the output of the deprecated {@link KEGGReactionParser}.
     *
     * @return
     */
    public void addRpair(String pair, String type) {
        this.rpair.put(pair, type);
    }

    /**
     * @deprecated this is only valid for the output of the deprecated {@link KEGGReactionParser}. Use {@link #get(Enum)}
     * and parse appropiately instead
     *
     * @return
     */
    public Map<String, String> getRpairs() {
        return this.rpair;
    }

    public String getComment() {
        return Joiner.on(" ").join(get(KEGGReactionField.COMMENT));
    }
}
