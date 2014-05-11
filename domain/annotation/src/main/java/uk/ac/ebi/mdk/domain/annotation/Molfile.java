/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.silent.AtomContainer;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * An annotation captures stores the contents of a Molfile - atom containers are
 * lazily loaded when needed. Currently only Molfile V2000 are read/written.
 *
 * @author John May
 */
@Context({Metabolite.class})
@Brief("molfile")
@Description("molfile representation of chemical structure")
public final class Molfile extends AbstractStringAnnotation
        implements ChemicalStructure {

    private volatile IAtomContainer container;

    private final Object lock = new Object();

    public Molfile() {
    }

    public Molfile(String value) {
        super(value);
    }

    @Override public Molfile getInstance(String value) {
        return new Molfile(value);
    }

    @Override public Molfile newInstance() {
        return new Molfile();
    }

    @Override public String toInChI() {
        try {
            InChIGeneratorFactory igf = InChIGeneratorFactory.getInstance();
            igf.setIgnoreAromaticBonds(true);
            // note: ret_status ignored
            return igf.getInChIGenerator(getStructure()).getInchi();
        } catch (Exception e) {
            return "";
        }
    }

    @Override public void setStructure(IAtomContainer structure) {
        if (structure == null) {
            throw new IllegalArgumentException("structure was null");
        }
        StringWriter sw = new StringWriter();
        this.container = structure;
        try {
            new MDLV2000Writer(sw).write(structure);
        } catch (CDKException e) {
            Logger.getLogger(getClass()).error(e);
        }
        setValue(sw.toString());
    }

    @Override public IAtomContainer getStructure() {
        IAtomContainer result = container;
        if (result == null) {
            synchronized (lock) {
                result = container;
                if (result == null) {
                    if (getValue().isEmpty()) {
                        container = result = new AtomContainer(0, 0, 0, 0);
                    }
                    else {
                        try {
                            container = result = new MDLV2000Reader(new StringReader(getValue())).read(new AtomContainer());
                        } catch (Exception e) {
                            container = result = new AtomContainer(0, 0, 0, 0);
                        }
                    }
                }
            }
        }
        return result;
    }
}
