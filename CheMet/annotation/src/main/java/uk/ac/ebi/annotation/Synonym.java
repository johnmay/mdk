/**
 * SynonymAnnotation.java
 *
 * 2011.10.24
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
package uk.ac.ebi.annotation;

import org.apache.log4j.Logger;

/**
 *          SynonymAnnotation - 2011.10.24 <br>
 *          An annotation of a synonym/alternate name
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Synonym extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Synonym.class);

    public Synonym() {
    }

    public Synonym(String synonym) {
        super(synonym);
    }

    public Synonym getInstance() {
        return new Synonym();
    }

    public Synonym getInstance(String synonym) {
        return new Synonym(synonym);
    }
}
