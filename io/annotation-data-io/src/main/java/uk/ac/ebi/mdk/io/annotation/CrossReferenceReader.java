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

package uk.ac.ebi.mdk.io.annotation;

import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.io.ObservationInput;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;

/**
 * CrossReferenceReader - 10.03.2012 <br/>
 * <p/>
 * Reads a cross-reference annotation from a tagged input stream. The
 * cross-reference class is provided to the constructor allowing it
 * to read into the specified type (i.e. ChEBICrossReference,
 * EnzymeClassification, etc.). Note: observations are not stored
 * at the moment
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class CrossReferenceReader<X extends CrossReference>
        implements AnnotationReader<X> {

    private static final DefaultAnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private Class<? extends CrossReference> c;
    private IdentifierInput in;
    private ObservationInput observationInput;


    /**
     * Create a new cross-reference reader on the specified tagged input
     * stream
     * @param c   class of the cross-reference (e.g. KEGGCompoundCrossReference.class
     * @param in  tagged input stream to read from
     */
    public CrossReferenceReader(Class<? extends CrossReference> c,
                                IdentifierInput in){
        this(c, in, null);
    }


    public CrossReferenceReader(Class<? extends CrossReference> c,
                                IdentifierInput in,
                                ObservationInput observationInput){
        this.c = c;
        this.in = in;
        this.observationInput = observationInput;
    }

    /**
     * Read a cross-reference annotation from the stream
     *
     * @return CrossReference annotation
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException thrown if the class of identifier cannot be found
     */
    @Override
    @SuppressWarnings("unchecked")
    public X readAnnotation() throws IOException, ClassNotFoundException {

         Identifier identifier = in.read();

         CrossReference xref = ANNOTATION_FACTORY.ofClass(c);
         xref.setIdentifier(identifier);

         if(observationInput != null) {
            xref.addObservations(observationInput.readCollection());
         }

         return (X) xref;

    }
}
