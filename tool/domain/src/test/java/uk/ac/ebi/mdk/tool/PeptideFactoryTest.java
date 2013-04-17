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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.tool.domain.PeptideFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author johnmay
 */
public class PeptideFactoryTest {

    public PeptideFactoryTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testBondIndex() throws IOException, Exception {
        PeptideFactory factory = new PeptideFactory(DefaultEntityFactory.getInstance());
        {
            Metabolite m = factory.generateMetabolite(PeptideFactory.AminoAcid.D_ALA, PeptideFactory.AminoAcid.L_ALA, PeptideFactory.AminoAcid.L_ALA, PeptideFactory.AminoAcid.D_ALA);


            File tmp = File.createTempFile("peptide", ".mol", new File("/tmp"));
            MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(tmp));
            writer.write(m.getStructures().iterator().next().getStructure());
            writer.close();
            System.out.println(tmp);
        }
        {
            Metabolite m =
                       factory.generateMetabolite(PeptideFactory.AminoAcid.L_ILE, PeptideFactory.AminoAcid.D_ALA);

            File tmp = File.createTempFile("peptide", ".mol", new File("/tmp"));
            MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(tmp));
            writer.write(m.getStructures().iterator().next().getStructure());
            writer.close();
            System.out.println(tmp);
        }

    }
}
