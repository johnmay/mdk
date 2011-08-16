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
package uk.ac.ebi.metabolomes.resource;

/**
 *
 * @author johnmay
 */
public enum BlastProgram {
    BLASTP( "/usr/local/bin/blastall -p blastp" ) ,
    RPSBLAST( "/usr/local/bin/rpsblast" );
    private String programName;

    private BlastProgram( String programName ) {
        this.programName = programName;
    }

    public  String getProgram() {
        return this.programName;
    }

    @Override
    public String toString() {
        return getProgram();
    }
    
    
    
}
