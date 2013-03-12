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
package uk.ac.ebi.mdk.tool.inchi;

/**
 *
 * @author pmoreno
 */
public class InChIResult {
    private String inchi;
    private String inchiKey;
    private String auxInfo;

    public InChIResult() {
    }

    public InChIResult( String inchi , String inchiKey , String auxInfo ) {
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.auxInfo = auxInfo;
    }  

    public String getAuxInfo() {
        return auxInfo;
    }

    protected void setAuxInfo(String auxInfo) {
        this.auxInfo = auxInfo.replaceFirst("AuxInfo=", "");
    }

    public String getInchi() {
        return inchi;
    }

    protected void setInchi(String inchi) {
        //this.inchi = inchi.replaceFirst("InChI=", "");
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    protected void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey.replaceFirst("InChIKey=", "");
    }


}
