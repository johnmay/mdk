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
package uk.ac.ebi.metabolomes.utilities;

import java.util.List;

/**
 * Util.java
 *
 *
 * @author johnmay
 * @date May 11, 2011
 */
public class Util {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Util.class );

    public static String join( List objects , char seperator ) {
        return join( objects , seperator , true );
    }

    /**
     * Joins a list of objects using the specified 'seperator'. The boolean 'space' variable indicates
     * whether to add a space when concatenating or not.
     * @param objects
     * @param seperator
     * @param space
     * @return
     */
    public static String join( List objects , char seperator , boolean space ) {

        if ( objects == null || objects.isEmpty() ) {
            return "";
        }

        StringBuilder sb = new StringBuilder( objects.size() * 5 );
        Object last = objects.get( objects.size() - 1 );
        for ( Object object : objects ) {
            sb.append( object.toString() );
            if ( object != last ) {
                sb.append( seperator );
                if ( space ) {
                    sb.append( " " );
                }

            }
        }

        return sb.toString();
    }

    public static String join( List objects , String seperator ) {

        if ( objects == null || objects.isEmpty() ) {
            return "";
        }

        StringBuilder sb = new StringBuilder( objects.size() * 5 );
        Object last = objects.get( objects.size() - 1 );
        for ( Object object : objects ) {
            sb.append( object.toString() );
            if ( object != last ) {
                sb.append( seperator );
            }
        }

        return sb.toString();
    }

    public static String join( List objects ) {
        return join( objects , ',' );
    }

    public static String encodeURN( String s ) {
        StringBuilder sb = new StringBuilder( s.length() );
        for ( int i = 0; i < s.length(); i++ ) {
            char c = s.charAt( i );
            if ( c > 127 || c == '"' || c == '<' || c == '>' ) {
                sb.append( "&#" ).append( ( int ) c );
            } else {
                sb.append( c );
            }
        }
        return sb.toString();
    }
}
