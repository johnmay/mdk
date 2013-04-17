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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.io.text.brenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class PolymericReaction extends Reaction {

    private HashMap<String, String> reactantName2PolymerCoeff;
    private HashMap<String, String> productName2PolymerCoeff;
    private static Pattern preNPattern = Pattern.compile("^\\(([nmNM][+-]\\d)\\)[^-]");
    private static Pattern preNNoParentPattern = Pattern.compile("^([nmNM][+-]\\d)\\s");
    private static Pattern preNSpPattern = Pattern.compile("^(\\d{0,1}[nmMN])\\s");
    private static Pattern postNPattern = Pattern.compile("\\(([nmNM][+-]\\d)\\)$");
    // avoid: glypican-1$ syndecan-3$ claudin-3$ Lin-3$
    private static Pattern postNNoParentPattern = Pattern.compile("(Glu|[A-Z\\]])([nmNM][+-]\\d)$");
    private static Pattern postNSpPattern = Pattern.compile("(\\s|\\])([nmMN])$");

    public PolymericReaction(Reaction r) {
        super();
        this.reactantName2PolymerCoeff = new HashMap<String, String>();
        this.productName2PolymerCoeff = new HashMap<String, String>();
        HashMap<String, Float> reactProds = r.getReactantsProducts();

        ArrayList<Matcher> matchers = new ArrayList<Matcher>();

        Matcher preNMatcher = null;
        Matcher preNNoParentMatcher = null;
        Matcher preNSpMatcher = null;
        Matcher postNMatcher = null;
        Matcher postNNoParentMatcher = null;
        Matcher postNSpMatcher = null;

        for (String metabolite : reactProds.keySet()) {
            Float coefficient = reactProds.get(metabolite);

            matchers.clear();

            preNMatcher = preNPattern.matcher(metabolite);
            preNNoParentMatcher = preNNoParentPattern.matcher(metabolite);
            preNSpMatcher = preNSpPattern.matcher(metabolite);
            postNMatcher = postNPattern.matcher(metabolite);
            postNNoParentMatcher = postNNoParentPattern.matcher(metabolite);
            postNSpMatcher = postNSpPattern.matcher(metabolite);

            matchers.add(preNSpMatcher);
            matchers.add(preNNoParentMatcher);
            matchers.add(postNMatcher);
            matchers.add(postNNoParentMatcher);
            matchers.add(preNMatcher);
            matchers.add(postNSpMatcher);


            for (Matcher matcher : matchers) {
                int group = 1;
                if (matcher.equals(postNSpMatcher)) {
                    group = 2;
                }
                if (matcher.find()) {
                    String coeffPol = matcher.group(group);
                    metabolite = matcher.replaceAll("");
                    if (coefficient < 0) {
                        this.reactantName2PolymerCoeff.put(metabolite, coeffPol);
                    } else {
                        this.productName2PolymerCoeff.put(metabolite, coeffPol);
                    }

                    break;
                }
            }

            if (coefficient < 0) {
                super.addReactant(metabolite, Math.abs(coefficient));
            } else {
                super.addProduct(metabolite, coefficient);
            }

        }
    }

    public static boolean isPolymeric(Reaction r) {
        if(r instanceof PolymericReaction)
            return true;

        int count = 0;
        for (String metab : r.getMetabolites()) {
            if (preNPattern.matcher(metab).find()
                    || preNNoParentPattern.matcher(metab).find()
                    || preNSpPattern.matcher(metab).find()
                    || postNPattern.matcher(metab).find()
                    || postNNoParentPattern.matcher(metab).find()
                    || postNSpPattern.matcher(metab).find()) {
                count++;
            }
        }
        return count > 0;
    }
}
