/**
 * ExecutableParameter.java
 *
 * 2011.10.13
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
package uk.ac.ebi.annotation.task;

import java.io.File;
import java.security.InvalidParameterException;
import org.apache.log4j.Logger;

/**
 * @name    ExecutableParameter - 2011.10.13 <br>
 *          A special type of file parameter the describes an executable
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ExecutableParameter extends FileParameter {

    public ExecutableParameter() {
    }

    public ExecutableParameter(String name, String description, File file) {
        super(name, description, "", file);
        if (file.canExecute() == false) {
            throw new InvalidParameterException("Attempt to create an executable file"
                                                + " parameter with an non-executable file");
        }
    }

    public ExecutableParameter newInstance() {
        return new ExecutableParameter();
    }
}
