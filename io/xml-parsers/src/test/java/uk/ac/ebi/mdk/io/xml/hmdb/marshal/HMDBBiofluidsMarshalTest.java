/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

import javax.xml.stream.events.XMLEvent;
import org.codehaus.stax2.XMLStreamReader2;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;

/**
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public class HMDBBiofluidsMarshalTest {
    
    public HMDBBiofluidsMarshalTest() {
    }


    /**
     * Test of marshal method, of class HMDBBiofluidsMarshal.
     */
    @Test
    public void testBioFluidsMarshal() throws Exception {
        XMLStreamReader2 xmlr = Mockito.mock(XMLStreamReader2.class);
        // mocking metabolite would require an interface
        HMDBMetabolite metabolite = new HMDBMetabolite();

        // stubbing
        Mockito.when(xmlr.hasNext()).thenReturn(Boolean.TRUE);
        Mockito.when(xmlr.next()).thenReturn(XMLEvent.START_ELEMENT)
                .thenReturn(XMLEvent.CHARACTERS)
                .thenReturn(XMLEvent.END_ELEMENT);
        Mockito.when(xmlr.getLocalName()).thenReturn("biofluid").thenReturn("biofluid_locations");
        Mockito.when(xmlr.getText()).thenReturn("Blood");

        HMDBBiofluidsMarshal biofluidsMarshal = new HMDBBiofluidsMarshal();
        
        biofluidsMarshal.marshal(xmlr, metabolite);

        assertTrue(metabolite.getBodyFluids().contains("Blood"));
    }
}
