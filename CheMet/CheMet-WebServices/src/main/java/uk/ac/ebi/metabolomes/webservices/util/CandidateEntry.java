/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.util;

/**
 *
 * @author pmoreno
 */



    public class CandidateEntry implements Comparable<CandidateEntry>{
        private String id;
        private String desc;
        private Integer distance;

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(String id) {
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
        public void setDesc(String desc) {
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
        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        @Override
        public int compareTo(CandidateEntry o) {
            if(distance==null || o.distance==null)
                return 0;
            return this.distance.compareTo(o.distance);
        }


    }