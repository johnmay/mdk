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


import java.util.ArrayList;
import java.util.HashMap;
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
public class KEGGReactionEntry {

        private String reactionID;
        private String name;
        private ArrayList<String> compNames;
        private HashMap<String, Integer> compId2coeff;
        private HashMap<String, String> rpair;
        private String comment;
        private ArrayList<String> pathways;
        private ArrayList<String> ecs;

        public KEGGReactionEntry() {
            compNames = new ArrayList<String>();
            compId2coeff = new HashMap<String, Integer>();
            ecs = new ArrayList<String>();
            pathways = new ArrayList<String>();
            rpair = new HashMap<String, String>();
        }

        public Integer numberOfReactants() {
            int count=0;
            for(Integer coeff : this.compId2coeff.values())
                if(coeff < 0) count++;
            return count;
        }
        public Integer numberOfProducts() {
            int count=0;
            for(Integer coeff : this.compId2coeff.values())
                if(coeff > 0) count++;
            return count;
        }
        public Integer numberOfPathways() {
            return this.pathways.size();
        }

        public void addEcNumber(String ec) {
            this.ecs.add(ec);
        }

        public void addCompCoeff(String comp, Integer coeff) {
            this.compId2coeff.put(comp, coeff);
        }

        public HashMap<String, Integer> getCompId2coeff() {
            return compId2coeff;
        }

        public ArrayList<String> getPathways() {
            return pathways;
        }

        public ArrayList<String> getEcNumbers() {
            return ecs;
        }

        public void addPathway(String pathway) {
            this.pathways.add(pathway);
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReactionID() {
            return reactionID;
        }

        public void setReactionID(String reactionID) {
            this.reactionID = reactionID;
        }

        public void addRpair(String pair, String type) {
            this.rpair.put(pair, type);
        }

        public Map<String, String> getRpairs() {
            return this.rpair;
        }
    }
