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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class TransportReaction extends Reaction {

    private HashMap<String,String> participantName2Compartment;
    private static final Pattern inPattern = Pattern.compile("\\/in$");
    private static final Pattern outPattern = Pattern.compile("\\/out$");

    public TransportReaction(Reaction r) {
        super();
        this.participantName2Compartment = new HashMap<String, String>();
        HashMap<String,Float> reactProds = r.getReactantsProducts();

        for (String metabolite : reactProds.keySet()) {
            Float coefficient = reactProds.get(metabolite);
            Matcher inMatcher = inPattern.matcher(metabolite);
            if(inMatcher.find()) {
                metabolite = inMatcher.replaceAll("");
                this.participantName2Compartment.put(metabolite, "in");
            } else {
                Matcher outMatcher = outPattern.matcher(metabolite);
                if(outMatcher.find()) {
                    metabolite = outMatcher.replaceAll("");
                    this.participantName2Compartment.put(metabolite, "out");
                }
            }

            if(coefficient < 0) {
                super.addReactant(metabolite, coefficient);
            } else {
                super.addProduct(metabolite, coefficient);
            }

        }
    }

    public static boolean isATransportReaction(Reaction r) {
        if(r instanceof TransportReaction)
            return true;

        int withPattern=0;
        for (String metabs : r.getMetabolites()) {
            if(inPattern.matcher(metabs).find())
                withPattern++;
            else if(outPattern.matcher(metabs).find())
                withPattern++;
        }
        if(withPattern>=2)
            return true;
        return false;
    }
}
