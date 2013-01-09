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

package uk.ac.ebi.mdk.domain.entity.metabolite;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;


/**
 * CompartmentalisedMetabolite - 2011.12.05 <br>
 * The class describes a metabolite associated with a specific compartment. This is
 * need as in modeling terms a metabolite in a different compartment is a different
 * entity.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class CompartmentalisedMetabolite {

    private static final Logger LOGGER = Logger.getLogger(CompartmentalisedMetabolite.class);

    public final Metabolite metabolite;

    public final Compartment compartment;


    public CompartmentalisedMetabolite(final Metabolite metabolite, Compartment compartment) {
        this.metabolite = metabolite;
        this.compartment = compartment;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.metabolite != null ? this.metabolite.hashCode() : 0);
        hash = 67 * hash + (this.compartment != null ? this.compartment.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompartmentalisedMetabolite other = (CompartmentalisedMetabolite) obj;
        if (this.metabolite != other.metabolite && (this.metabolite == null || !this.metabolite.equals(other.metabolite))) {
            return false;
        }
        if (this.compartment != other.compartment) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(metabolite.toString()).append(" [").append(compartment).append("]");
        return sb.toString();
    }
}
