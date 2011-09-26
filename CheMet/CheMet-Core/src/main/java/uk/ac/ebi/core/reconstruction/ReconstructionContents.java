/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.reconstruction;

import java.util.Collection;
import java.util.HashSet;

/**
 * Provides a description of the project contents
 * @author johnmay
 */
public enum ReconstructionContents {
    PROTEIN_PRODUCTS("protein.products"),
    RNA_PRODUCTS("rna.products"),
    SWISSPROT_HOMOLOGY("sp.homology"),
    REACTIONS("reactions"),
    METABOLITES("metabolites");

    private String tag;
    private static final String PROPERTY_SEPARATOR = ",";

    private ReconstructionContents(String tag){
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }

    public static ReconstructionContents getFromTag(String tag){
        for ( ReconstructionContents contents : values() ) {
            if( contents.getTag().equals(tag) ) {
                return contents;
            }
        }
        return null;
    }

    public static String flattenList( Collection<ReconstructionContents> properties ){
        StringBuilder sb = new StringBuilder(properties.size() * 8);
        for ( ReconstructionContents projectContents : properties ) {
            sb.append( projectContents.getTag() ).append(PROPERTY_SEPARATOR);
        }
        return sb.toString();
    }

    public static HashSet<ReconstructionContents> expandList( String propertiesString ){
        HashSet<ReconstructionContents> contents = new HashSet<ReconstructionContents>();
        String[] properties = propertiesString.split(PROPERTY_SEPARATOR);
        for ( String string : properties ) {
            contents.add( getFromTag( string ) );
        }
        return contents;
    }

}
