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
package uk.ac.ebi.metabolomes.http.uniprot;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * UniProtSearchQuery.java – MetabolicDevelopmentKit – Jun 22, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class UniProtSearchQuery {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( UniProtSearchQuery.class );
    private String text;
    private Map<UniProtSearchTerm , String> advancedAndSearch;
    private Map<UniProtSearchTerm , String> advancedNotSearch;

    public UniProtSearchQuery( String text ) {
        this( text ,
              new EnumMap<UniProtSearchTerm , String>( UniProtSearchTerm.class ) ,
              new EnumMap<UniProtSearchTerm , String>( UniProtSearchTerm.class ) );
    }

    public UniProtSearchQuery( String text ,
                               Map<UniProtSearchTerm , String> advancedAndSearch ,
                               Map<UniProtSearchTerm , String> advancedNotSearch ) {
        this.text = text;
        this.advancedAndSearch = advancedAndSearch;
        this.advancedNotSearch = advancedNotSearch;
    }

    public void setAndEnzymeCommissionNumber( String ec ) {
        advancedAndSearch.put( UniProtSearchTerm.EC , ec );
    }

    public void setNotEnzymeCommissionNumber( String ec ) {
        advancedNotSearch.put( UniProtSearchTerm.EC , ec );
    }

    public void putAdvancedAndSearchTerm( UniProtSearchTerm term , String value ) {
        advancedAndSearch.put( term , value );
    }

    public void putAdvancedNotSearchTerm( UniProtSearchTerm term , String value ) {
        advancedNotSearch.put( term , value );
    }

    public String buildQuery() {
        StringBuilder query = new StringBuilder();

        query.append( text );

        for ( Entry<UniProtSearchTerm , String> entry : advancedAndSearch.entrySet() ) {
            if ( query.length() != 0 ) {
                query.append( " " ).append( UniProtSearchTerm.AND ).append( " " );
            }
            query.append( entry.getKey().getToken() ).append( ":" ).append( entry.getValue() );
        }

        for ( Entry<UniProtSearchTerm , String> entry : advancedNotSearch.entrySet() ) {
            if ( query.length() != 0 ) {
                query.append( " " ).append( UniProtSearchTerm.NOT ).append( " " );
            }
            query.append( entry.getKey().getToken() ).append( ":" ).append( entry.getValue() );
        }

        return query.toString();
    }

    public static void main( String[] args ) {
        UniProtSearchQuery query = new UniProtSearchQuery( "" );
        query.setAndEnzymeCommissionNumber( "1.1.1.1" );
        System.out.println(
                query.buildQuery() );
    }
}
