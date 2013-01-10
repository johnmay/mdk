/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.service.query;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * QueryService - 2011.10.26 <br>
 * <p/>
 * Interface describes a base for query service interfaces. The service provides
 * the {@see getIdentifier()} method for creation specific identifiers as well as
 * max result and fuzzy search configuration.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface QueryService<I extends Identifier> {

    public enum ServiceType {
        LUCENE_INDEX(1, false),
        RELATIONAL_DATABASE(2, false),
        REST_WEB_SERVICE(3, true),
        SOAP_WEB_SERVICE(4, true),
        AGGREGATED(5, true);

        private Integer priority;
        private boolean remote;

        private ServiceType(int priority, boolean remote) {
            this.priority = priority;
            this.remote = remote;
        }

        public Integer getPriority() {
            return priority;
        }

        public boolean remote(){
            return remote;
        }

    }

    /**
     * Identifier term should be used to create and search any identifier
     * field in an index. This ensures naming consistency
     */
    public static final Term IDENTIFIER = new Term("Identifier");

    /**
     * Provides an instance of a identifier usable by the service.
     *
     * @return instance of identifier specific to this service
     */
    public I getIdentifier();

    public ServiceType getServiceType();

    /**
     * Used in benchmarking only
     */
    public void renew();

    /**
     * Set the maximum number of results to collection. This will limit
     * the query to return at maximum this number of results
     *
     * @param maxResults number of results
     */
    public void setMaxResults(int maxResults);

    /**
     * Set the default minimum similarity for approximate searches.
     *
     * @param similarity new minimum similarity
     */
    public void setMinSimilarity(float similarity);

    /**
     * Determine whether the service is available for use. In the case
     * of a database/webservice connection this method should return false
     * if a connection could not be made. In the case of a lucene index
     * service the index is checked whether it is available
     *
     * @return whether the service is available
     */
    public boolean startup();

}
