/**
 * FileParameter.java
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
import org.apache.log4j.Logger;

/**
 * @name    FileParameter - 2011.10.13 <br>
 *          A parameter that uses a file as a value
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class FileParameter extends Parameter {

    private static final Logger LOGGER = Logger.getLogger(FileParameter.class);
    private File file;

    public FileParameter() {
    }

    public FileParameter(String name, String description, String flag, File file) {
        super(name, description, flag, file.getAbsolutePath());
        this.file = file;
    }

    /**
     * Returns the file name
     * @return
     */
    @Override
    public String toString() {
        return file.getName();
    }

    @Override
    public File getValue() {
        return file;
    }

    public FileParameter getInstance() {
        return new FileParameter();
    }
}
