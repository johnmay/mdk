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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.metabolite.MetaboliteClass;


/**
 *
 * @author johnmay
 */
public enum MetaboliteClassImplementation implements MetaboliteClass {

    PROTEIN("Protein", (byte) 1),
    NUCLEIC_ACID("Nucleic Acid", (byte) 2),
    CHEMICAL_ENTITY("Chemical Entity", (byte) 3),
    UNKNOWN("Other", (byte) 4);

    private String desc;

    private byte index;


    private MetaboliteClassImplementation(String name, byte index) {
        this.desc = name;
        this.index = index;
    }


    @Override
    public String toString() {
        return desc;
    }


    public byte getIndex() {
        return index;
    }


    public static MetaboliteClassImplementation valueOf(Byte index) {
        for (MetaboliteClassImplementation mc : values()) {
            if (mc.index == index) {
                return mc;
            }
        }
        throw new UnsupportedOperationException("Index not found");
    }
}
