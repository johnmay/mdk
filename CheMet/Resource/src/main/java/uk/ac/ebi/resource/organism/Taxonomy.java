/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.resource.organism;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.resource.organism.Kingdom;


/**
 * ProjectIdentifier.java
 *
 *
 * @author johnmay
 * @date Apr 14, 2011
 */
public class Taxonomy extends AbstractIdentifier {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(Taxonomy.class);
    // AADNV V 648330: N=Aedes albopictus densovirus (isolate Boublik/1994)
    //                 C=AalDNV
    private int taxon;
    private String code;
    private Kingdom kingdom;
    private String officialName;
    private String commonName;


    public Taxonomy() {
    }


    public Taxonomy(int taxon, String code, Kingdom kingdom, String officialName, String commonName) {
        this.taxon = taxon;
        this.code = code;
        this.kingdom = kingdom;
        this.officialName = officialName;
        this.commonName = commonName;
        setAccession(code);
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getCommonName() {
        return commonName;
    }


    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }


    public Kingdom getKingdom() {
        return kingdom;
    }


    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }


    public String getOfficialName() {
        return officialName;
    }


    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }


    public int getTaxon() {
        return taxon;
    }


    public void setTaxon(int taxon) {
        this.taxon = taxon;
    }


    /**
     * @inheritDoc
     */
    @Override
    public Taxonomy newInstance() {
        return new Taxonomy();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        code = in.readUTF();
        commonName = in.readUTF();
        officialName = in.readUTF();
        taxon = in.readInt();
        kingdom = Kingdom.valueOf(in.readUTF());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(code);
        out.writeUTF(commonName);
        out.writeUTF(officialName);
        out.writeInt(taxon);
        out.writeUTF(kingdom.toString());
    }


}

