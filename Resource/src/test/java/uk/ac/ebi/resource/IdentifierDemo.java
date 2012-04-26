
/**
 * IdentifierDemo.java
 *
 * 2011.09.15
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.resource;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.basic.BasicReactionIdentifier;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.chemical.ChemicalIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;


/**
 *          IdentifierDemo – 2011.09.15 <br>
 *          Sample of identifier usage
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IdentifierDemo {

    private static final Logger LOGGER = Logger.getLogger(IdentifierDemo.class);


    public static void main(String[] args) {

        ChEBIIdentifier id1 = new ChEBIIdentifier("ChEBI:19435");
        ChEBIIdentifier id2 = new ChEBIIdentifier(19435);

        System.out.println("Resource: " + id1.getResource());

        id1.equals(id2);
        // true   - future support will allow comparisson to a String

        if( id1 instanceof ChemicalIdentifier) {
            // true
        }

        System.out.println(id1.getShortDescription());
        // ChEBI

        System.out.println(id1.getLongDescription());
        /*
         * Chemical Entities of Biological Interest (ChEBI) is a freely available dictionary
         * of molecular entities focused on 'small' chemical compounds.
         */

        System.out.println(id1.getURL());
        // http://www.ebi.ac.uk/chebi/searchId.do?chebiId=ChEBI:19435

        System.out.println(id1.getURN());
        // urn:miriam:obo.chebi:ChEBI%3A19435

        // example for kegg from the factory instance
        Identifier id = DefaultIdentifierFactory.getInstance().ofClass(KEGGCompoundIdentifier.class);
        id.setAccession("C00023");
        System.out.println(id.getURN());
        System.out.println(new ECNumber().getSynonyms());
        System.out.println(new BasicReactionIdentifier().getLongDescription());
        // urn:miriam:kegg.compound:C00023

    }


}

