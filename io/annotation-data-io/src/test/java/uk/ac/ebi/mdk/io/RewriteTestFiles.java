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

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.io.annotation.RExCompoundWriterTest;
import uk.ac.ebi.mdk.io.annotation.RExExtractWriterTest;
import uk.ac.ebi.mdk.io.annotation.primitive.DoubleAnnotationWriterTest;
import uk.ac.ebi.mdk.io.annotation.primitive.StringAnnotationWriterTest;
import uk.ac.ebi.mdk.io.annotation.AtomContainerAnnotationWriter085Test;
import uk.ac.ebi.mdk.io.annotation.CrossReferenceWriterTest;
import uk.ac.ebi.mdk.io.annotation.GibbsEnergyWriterTest;

import java.io.IOException;

/**
 * RewriteTestFiles - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class RewriteTestFiles {


    public static void main(String[] args) throws IOException {

        new StringAnnotationWriterTest().rewrite();
        new DoubleAnnotationWriterTest().rewrite();
        new AtomContainerAnnotationWriter085Test().rewrite();
        new CrossReferenceWriterTest().rewrite();
        new GibbsEnergyWriterTest().rewrite();
        new RExExtractWriterTest().rewrite();
        new RExCompoundWriterTest().rewrite();

    }

}
