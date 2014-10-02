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

package uk.ac.ebi.mdk.io.text.mnxref;

import com.google.common.base.Joiner;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds all information that can be loaded about reactions from MnxRef.
 *
 * @author John May
 * @see uk.ac.ebi.mdk.io.text.mnxref.MnxRefReactionInput
 */
public final class MnxRefReaction {

    private final String id, equation, description, enzymeCode, source;
    private final boolean balanced;
    private Set<String> xrefs = new HashSet<String>();

    public MnxRefReaction(String id,
                          String equation,
                          String description,
                          String enzymeCode,
                          String source,
                          boolean balanced) {
        this.id = id;
        this.equation = equation;
        this.description = description;
        this.enzymeCode = enzymeCode;
        this.balanced = balanced;
        this.source = source;
    }

    public void add(String xref) {
        xrefs.add(xref);
    }

    public String id() {
        return id;
    }

    public String equation() {
        return equation;
    }

    public String description() {
        return description;
    }

    public String enzymeCode() {
        return enzymeCode;
    }

    public boolean balanced() {
        return balanced;
    }

    public String source() {
        return source;
    }

    public Iterable<String> xrefs() {
        return xrefs;
    }

    @Override public String toString() {
        return Joiner.on(", ").join(id, equation, enzymeCode);
    }

    /**
     * Create a new MnxRefCompound using a line loaded from (chem_prop.tsv). The
     * expected columns are: id, name, formula, charge, mass, inchi, smiles and
     * source.
     *
     * @param line line from a file (expects 8 columns)
     * @return MnxRef compound
     */
    public static MnxRefReaction parse(String[] line) {
        if (line.length != 6) {
            throw new IllegalArgumentException("line should have 6 columns");
        }
        boolean balanced =
                line[3].isEmpty() ? false : Boolean.parseBoolean(line[3]);

        return new MnxRefReaction(line[0], line[1], line[2], line[4], line[5], balanced);
    }


}
