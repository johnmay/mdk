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
package uk.ac.ebi.mdk.domain.entity.reaction;


/**
 * Enumeration class for storing reaction reversibility
 * @author johnmay
 */
public enum ReactionTypeImpl
        implements ReactionType {

    ENZYMATIC("Enzymatic Reaction", (byte) 5),
    SPONTANEOUS("Spontaneous Reaction", (byte) 5),
    GENERIC("Generic reaction", (byte) 5),
    TRANSPORT("Transport reaction", (byte) 6),
    EXCHANGE("Exchange reaction", (byte) 7),
    UNKNOWN("Unknown reaction", (byte) 8);

    private String description;

    private byte index;


    private ReactionTypeImpl(String description, byte index) {
        this.description = description;
        this.index = index;
    }


    public byte getIndex() {
        return index;
    }


    public static ReactionTypeImpl valueOf(byte index) {
        for (ReactionTypeImpl reversibility : values()) {
            if (reversibility.index == index) {
                return reversibility;
            }
        }
        throw new UnsupportedOperationException("No type of specified index");
    }


    public String getDescription() {
        return description;
    }
}
