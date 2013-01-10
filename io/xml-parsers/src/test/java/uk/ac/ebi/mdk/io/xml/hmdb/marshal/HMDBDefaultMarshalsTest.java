/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.io.xml.hmdb.marshal;

import org.codehaus.stax2.XMLStreamReader2;
import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGDrugIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 */
public class HMDBDefaultMarshalsTest {


    @Test public void testInChI() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("inchi-dummy");

        HMDBDefaultMarshals.INCHI.marshal(xmlr, metabolite);

        assertThat(metabolite.getInChI(), is("inchi-dummy"));

    }

    @Test public void testInChI_Empty() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.INCHI.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testSMILES() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("smiles-dummy");

        HMDBDefaultMarshals.SMILES.marshal(xmlr, metabolite);

        assertThat(metabolite.getSMILES(), is("smiles-dummy"));

    }

    @Test public void testSMILES_Empty() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.SMILES.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testIUPAC_NAME() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("iupac-name");

        HMDBDefaultMarshals.IUPAC_NAME.marshal(xmlr, metabolite);

        assertThat(metabolite.getIUPACName(), is("iupac-name"));

    }

    @Test public void testIUPAC_NAME_Empty() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.IUPAC_NAME.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testAccession() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("accession");

        HMDBDefaultMarshals.ACCESSION.marshal(xmlr, metabolite);

        assertThat(metabolite.getAccession(), is("accession"));

    }

    @Test public void testAccession_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.ACCESSION.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testFormula() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("C2H5");

        HMDBDefaultMarshals.FORMULA.marshal(xmlr, metabolite);

        assertThat(metabolite.getMolecularFormula(), is("C2H5"));

    }

    @Test public void testFormula_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.FORMULA.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testCharge() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("0.0");

        HMDBDefaultMarshals.CHARGE.marshal(xmlr, metabolite);

        assertThat(metabolite.getFormalCharge(), is("0.0"));

    }

    @Test public void testCharge_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.CHARGE.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testSynonyms() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("a-name")
               .thenReturn("b-name");

        // invoked twice for two synonyms
        HMDBDefaultMarshals.SYNONYMS.marshal(xmlr, metabolite);
        HMDBDefaultMarshals.SYNONYMS.marshal(xmlr, metabolite);

        assertTrue(metabolite.getSynonyms().contains("a-name"));
        assertTrue(metabolite.getSynonyms().contains("b-name"));

    }

    @Test public void testChEBI() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("CHEBI:12");

        HMDBDefaultMarshals.CHEBI.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new ChEBIIdentifier("ChEBI:12")));

    }

    @Test public void testChEBI_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.CHEBI.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testKEGGCompound() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("C00009");

        HMDBDefaultMarshals.KEGG_COMPOUND.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new KEGGCompoundIdentifier("C00009")));

    }

    @Test public void testKEGGCompound_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.KEGG_COMPOUND.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testKEGGCompound_InvalidID_Drug() throws
                                                        XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("D00009");

        HMDBDefaultMarshals.KEGG_COMPOUND.marshal(xmlr, metabolite);

        assertTrue(metabolite.getCrossReferences().isEmpty());

    }

    @Test public void testKEGGCompound_InvalidID_Glycan() throws
                                                          XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("G00009");

        HMDBDefaultMarshals.KEGG_COMPOUND.marshal(xmlr, metabolite);

        assertTrue(metabolite.getCrossReferences().isEmpty());

    }

    @Test public void testKEGGDrug() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("D00009");

        HMDBDefaultMarshals.KEGG_DRUG.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new KEGGDrugIdentifier("D00009")));

    }

    @Test public void testKEGGDrug_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.KEGG_DRUG.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testKEGGGlycan() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("G00009");

        HMDBDefaultMarshals.KEGG_GLYCAN.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new KeggGlycanIdentifier("G00009")));

    }

    @Test public void testKEGGGlycan_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.KEGG_GLYCAN.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testPubChemCompound() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("12345");

        HMDBDefaultMarshals.PUBCHEM_COMPOUND.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new PubChemCompoundIdentifier("12345")));

    }

    @Test public void testPubChemCompound_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.PUBCHEM_COMPOUND.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

    @Test public void testBioCyc() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.CHARACTERS);
        Mockito.when(xmlr.getText()).thenReturn("ATP");

        HMDBDefaultMarshals.BIOCYC.marshal(xmlr, metabolite);

        assertThat(metabolite.getCrossReferences().iterator().next(),
                   is((Identifier) new BioCycChemicalIdentifier("ATP")));

    }

    @Test public void testBioCyc_EMPTY() throws XMLStreamException {

        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.END_ELEMENT);

        HMDBDefaultMarshals.BIOCYC.marshal(xmlr, metabolite);

        // verify getText() was not called
        Mockito.verify(xmlr, Mockito.times(0)).getText();

    }

}
