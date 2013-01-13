/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.service.query;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * @author John May
 */
public class AbstractRestClient<I extends Identifier>
        implements QueryService<I> {

    private I template;

    public AbstractRestClient(I template) {
        this.template = template;
    }

    @Override public I getIdentifier() {
        return template;
    }

    @Override public ServiceType getServiceType() {
        return ServiceType.REST_WEB_SERVICE;
    }

    @Override public void renew() {

    }

    @Override public void setMaxResults(int maxResults) {

    }

    @Override public void setMinSimilarity(float similarity) {

    }

    @Override public boolean startup() {
        return true;
    }
}
