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

    private final String id;
    private final int start, length;
    private final Type type;

    public static enum Type {
        SUBSTRATE,
        PRODUCT,
        ACTION,
        MODIFIER;

        @Override public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

    public RExTag(String id, int start, int length, Type type) {
        this.id = id;
        this.start = start;
        this.length = length;
        this.type = type;
    }

    public RExTag(String id, int start, int length, String type) {
        this.id = id;
        this.start = start;
        this.length = length;
        this.type = Type.valueOf(type.toUpperCase(Locale.ENGLISH));
    }

    public String id()
    {
        return id;
    }

    public int start() {
        return start;
    }

    public int length() {
        return length;
    }

    public Type type() {
        return type;
    }
}
