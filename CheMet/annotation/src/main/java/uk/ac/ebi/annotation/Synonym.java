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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Annotation;

/**
 *          SynonymAnnotation - 2011.10.24 <br>
 *          An annotation of a synonym/alternate name
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Synonym extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Synonym.class);
    private String synonym;

    public Synonym() {
    }

    public Synonym(String synonym) {
        this.synonym = synonym;
    }

    public Annotation getInstance() {
        return new Synonym();
    }

    @Override
    public String toString() {
        return synonym;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        synonym = in.readUTF();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(synonym);
    }

}
