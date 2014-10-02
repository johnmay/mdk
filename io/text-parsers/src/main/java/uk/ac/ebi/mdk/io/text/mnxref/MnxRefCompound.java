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
import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An MnxRefCompound holding all information that can be loaded.
 *
 * @author John May
 * @see MnxRefCompoundInput
 */
public final class MnxRefCompound {

    private final String id, name, source, smiles, inchi, formula;
    private final int    charge;
    private final double mass;
    private Set<Xref> xrefs = new HashSet<Xref>();

    private MnxRefCompound(String id,
                           String name,
                           String source,
                           String smiles,
                           String inchi,
                           String formula,
                           int charge,
                           double mass) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.smiles = smiles;
        this.inchi = inchi;
        this.formula = formula;
        this.charge = charge;
        this.mass = mass;
    }

    public void add(Xref xref) {
        xrefs.add(xref);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String source() {
        return source;
    }

    public String smiles() {
        return smiles;
    }

    public String inchi() {
        return inchi;
    }

    public String formula() {
        return formula;
    }

    public int charge() {
        return charge;
    }

    public double mass() {
        return mass;
    }

    public Iterable<Xref> xrefs() {
        return xrefs;
    }

    @Override public String toString() {
        return Joiner.on(", ").join(id, name, source, xrefs);
    }

    /**
     * Create a new MnxRefCompound using a line loaded from (chem_prop.tsv). The
     * expected columns are: id, name, formula, charge, mass, inchi, smiles and
     * source.
     *
     * @param line line from a file (expects 8 columns)
     * @return MnxRef compound
     */
    public static MnxRefCompound parse(String[] line) {
        if (line.length != 8) {
            throw new IllegalArgumentException("line should have 8 columns");
        }
        int charge = line[3].isEmpty() ? 0 : Integer.parseInt(line[3]);
        double mass = line[4].isEmpty() ? 0d : Double.parseDouble(line[4]);

        return new MnxRefCompound(line[0], line[1], line[7], line[6], line[5], line[2], charge, mass);
    }

    public static class Xref {

        private String id, desc;
        private Evidence evidence;

        public enum Evidence {
            Identity,
            Inferred,
            Structural,
            Unknown
        }

        private static final Map<String, Evidence> EVIDENCE_LOOKUP = ImmutableMap
                .<String, Evidence>builder()
                .put("identity", Evidence.Identity)
                .put("inferred", Evidence.Inferred)
                .put("structural", Evidence.Structural)
                .build();

        public Xref(String id, String desc, Evidence evidence) {
            this.id = id;
            this.desc = desc;
            this.evidence = evidence;
        }

        public String id() {
            return id;
        }

        public String description() {
            return desc;
        }

        public Evidence evidence() {
            return evidence;
        }

        /**
         * Create a new MnxRefCompound using a line loaded from (chem_prop.tsv).
         * The expected columns are: ref_id, mnx_id, evidence and description
         *
         * @param line line from a file (expects 8 columns)
         * @return MnxRef compound
         */
        public static Xref parse(String[] line) {
            Evidence evidence = EVIDENCE_LOOKUP.get(line[2]);
            if (evidence == null)
                evidence = Evidence.Unknown;
            return new Xref(line[0], line[3], evidence);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Xref xref = (Xref) o;

            if (desc != null ? !desc.equals(xref.desc) : xref.desc != null)
                return false;
            if (evidence != xref.evidence) return false;
            if (id != null ? !id.equals(xref.id) : xref.id != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            result = 31 * result + (evidence != null ? evidence.hashCode() : 0);
            return result;
        }


        @Override public String toString() {
            return Joiner.on("").join(id, " (", evidence, ")");
        }
    }

}
