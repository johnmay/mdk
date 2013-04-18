/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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
import com.google.common.collect.Sets;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkElementIndex;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.COMMENT;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.ENTRY;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.ENZYME;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.EQUATION;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.NAME;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.PATHWAY;
import static uk.ac.ebi.mdk.io.text.kegg.KEGGReactionField.RPAIR;

/** @author John May */
public final class ReactionEntry {

    private static final String REACTION_ARROW = "<=>";

    private static Pattern ACCESSION = Pattern.compile("([CDGR]\\d+)");

    private static final Pattern PARTICIPANT_SEPARATOR = Pattern
            .compile("(?<=\\s|\\d)\\+(?=\\s|[CDG]|\\d+ [CDG])");

    private final AttributedEntry<KEGGReactionField, String> e;

    // fields here
    private String[][] reactants;
    private String[][] products;

    public ReactionEntry(AttributedEntry<KEGGReactionField, String> e) {
        this.e = e;
        String equation = Joiner.on(" ").join(e.get(EQUATION));
        String[] sides = equation.split(REACTION_ARROW);
        reactants = particpants(sides[0]);
        products = particpants(sides[1]);
    }

    public int reactantCount() {
        return reactants.length;
    }

    public int productCount() {
        return products.length;
    }

    public double reactantCoef(int i) {
        checkElementIndex(i, reactants.length, "invalid reactant index");
        return Double.parseDouble(reactants[i][0]);
    }

    public String reactant(int i) {
        checkElementIndex(i, reactants.length, "invalid reactant index");
        return reactants[i][1];
    }

    public double productCoef(int i) {
        checkElementIndex(i, products.length, "invalid product index");
        return Double.parseDouble(products[i][0]);
    }

    public String product(int i) {
        checkElementIndex(i, products.length, "invalid product index");
        return products[i][1];
    }

    public Set<String> enzymes() {
        return Sets.newHashSet(Joiner.on(" ").join(e.get(ENZYME)).trim()
                                     .split("\\s+"));
    }

    public String comment() {
        return Joiner.on("").join(e.get(COMMENT));
    }

    public Collection<String> pathways() {
        return e.get(PATHWAY);
    }

    public Collection<String> rpairs() {
        return e.get(RPAIR);
    }

    public String name() {
        return Joiner.on(" ").join(e.get(NAME));
    }

    public String accession() {
        return e.getFirst(ENTRY).substring(0, 6);
    }

    static String[][] particpants(String side) {
        String[] participants = PARTICIPANT_SEPARATOR.split(side);
        String[][] parsed = new String[participants.length][2];
        for (int i = 0; i < participants.length; i++) {
            String participant = participants[i];
            Matcher matcher = ACCESSION.matcher(participant);
            if (matcher.find()) {
                parsed[i][1] = matcher.group(1);
                parsed[i][0] = normaliseCoefficient(matcher.replaceAll("")
                                                           .replaceAll("[()]", "")
                                                           .trim());
            }
        }
        return parsed;
    }

    private static Integer DEFAULT_N = 2;
    private static Integer DEFAULT_M = 2;

    private static final Pattern COEF_TIMES_MODIFIER = Pattern
            .compile("\\A(\\d+)[n|m]");
    private static final Pattern COEF_PLUS_MODIFIER = Pattern
            .compile("\\A[n|m]([+|-])(\\d+)");


    static String normaliseCoefficient(String coef) {

        if (coef.isEmpty())
            return "1";

        Matcher matcherTimes = COEF_TIMES_MODIFIER.matcher(coef);
        Matcher plusMinuseMatcher = COEF_PLUS_MODIFIER.matcher(coef);

        if (coef.contains("n")) {
            if (coef.contains("m")) {
                coef = Integer.toString(DEFAULT_N + DEFAULT_M);
            } else if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_N + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_N - Integer.parseInt(val));
                }

            } else if (matcherTimes.find()) {
                coef = Integer.toString(Integer.parseInt(matcherTimes
                                                                 .group(1)) * DEFAULT_N);
            } else {
                coef = DEFAULT_N.toString();
            }
        } else if (coef.contains("m")) {
            if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_M + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_M - Integer.parseInt(val));
                }
            } else {
                coef = DEFAULT_M.toString();
            }
        }
        return coef;
    }


}
