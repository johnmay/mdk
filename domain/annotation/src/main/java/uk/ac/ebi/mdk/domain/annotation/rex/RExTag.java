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

package uk.ac.ebi.mdk.domain.annotation.rex;

import java.util.Locale;

/** @author John May */
public class RExTag {

    private final int start, length;
    private final Type type;

    enum Type {
        SUBSTRATE,
        PRODUCT,
        ACTION,
        MODIFIER;
    }

    public RExTag(int start, int length, Type type) {
        this.start = start;
        this.length = length;
        this.type = type;
    }

    public RExTag(int start, int length, String type) {
        this.start = start;
        this.length = length;
        this.type = Type.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
