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

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.Classification;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;
import uk.ac.ebi.mdk.io.AnnotationInput;
import uk.ac.ebi.mdk.io.ObservationDataInputStream;
import uk.ac.ebi.mdk.io.ObservationInput;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.classification.GeneOntologyTerm;
import uk.ac.ebi.mdk.domain.observation.Observation;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * StringAnnotationReaderTest - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class CrossReferenceReaderTest {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceReaderTest.class);

    @Test
    public void readAnnotation_ChEBICrossReference() throws IOException, ClassNotFoundException {


        DataInputStream     input   = new DataInputStream(getStream("chebi-xref-annotation"));
        AnnotationInput     reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        ChEBICrossReference xref    = reader.read();

        input.close();

        Assert.assertEquals(new ChEBIIdentifier(14321), xref.getIdentifier());

    }

    @Test
    public void readAnnotation_KEGGCrossReference() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("kegg-xref-annotation"));
        AnnotationInput reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        KEGGCrossReference     xref    = reader.read();

        input.close();

        Assert.assertEquals(new KEGGCompoundIdentifier("C00009"), xref.getIdentifier());

    }

    @Test
    public void readAnnotation_Classification() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("classification-annotation"));
        AnnotationInput reader         = new AnnotationDataInputStream(input, new Version("0.9"));
        Classification         xref    = reader.read();

        input.close();

        Assert.assertEquals(new GeneOntologyTerm("ATP Synthase"), xref.getIdentifier());

    }


    @Test
    public void readAnnotation_EnzymeClassification() throws IOException, ClassNotFoundException {


        DataInputStream      input   = new DataInputStream(getStream("enzyme-classification-annotation"));
        ObservationInput     oin     = new ObservationDataInputStream(input, new Version("0.9"));
        AnnotationInput      reader  = new AnnotationDataInputStream(input, oin, new Version("0.9"));
        EnzymeClassification<Observation> xref    = reader.read();

        Assert.assertEquals(2, xref.getObservations().size());
      

        input.close();

        Assert.assertEquals(new ECNumber("1.1.1.85"), xref.getIdentifier());

    }



    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

}
