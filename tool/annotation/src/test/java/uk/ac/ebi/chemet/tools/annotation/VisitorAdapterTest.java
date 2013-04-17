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

package uk.ac.ebi.chemet.tools.annotation;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AnnotationVisitor;
import uk.ac.ebi.mdk.domain.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.classification.GeneOntologyTerm;
import uk.ac.ebi.mdk.domain.observation.Observation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author John May
 */
public class VisitorAdapterTest {

    @Test
    public void testExactMatch() {

        List<CrossReference> annotations = Arrays.asList(
                new CrossReference<GeneOntologyTerm, Observation>(new GeneOntologyTerm("GO:1")),
                new ChEBICrossReference<Observation>(new ChEBIIdentifier("ChEBI:2")),
                new ChEBICrossReference<Observation>(new ChEBIIdentifier("ChEBI:3")),
                new ChEBICrossReference<Observation>(new ChEBIIdentifier("ChEBI:4")),
                new KEGGCrossReference(new KEGGCompoundIdentifier("C00002")),
                new EnzymeClassification(new ECNumber("1.1.1.85")));



        // new visitor for ChEBI CrossReferences
        AnnotationVisitor<Boolean> visitor = new VisitorAdapter<ChEBICrossReference, Boolean>(
                ChEBICrossReference.class,
                Boolean.FALSE) {
            @Override
            public Boolean _visit(ChEBICrossReference annotation) {
                return Boolean.TRUE;
            }
        };

        Iterator<CrossReference> it = annotations.iterator();
        Assert.assertEquals(false, visitor.visit(it.next()));
        Assert.assertEquals(true, visitor.visit(it.next()));
        Assert.assertEquals(true, visitor.visit(it.next()));
        Assert.assertEquals(true, visitor.visit(it.next()));
        Assert.assertEquals(false, visitor.visit(it.next()));
        Assert.assertEquals(false, visitor.visit(it.next()));

    }


}
