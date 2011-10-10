/**
 * TaskDescription.java
 *
 * 2011.10.07
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
package uk.ac.ebi.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Map;
import javax.swing.text.html.Option;

/**
 * @name    TaskDescription - 2011.10.07 <br>
 *          Interface description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface TaskOptions {

    public void writeExternal(ObjectOutput out) throws IOException;

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;

    /**
     * Returns the task name
     */
    public String getName();

    /**
     * Returns the task description
     */
    public String getDescription();

    /**
     * Returns the task identifier
     * @return
     */
    public Identifier getIdentifier();

    /**
     * Returns the initialisation date of the task
     */
    public Date getInitialisationDate();

    /**
     * Returns a map of options for the task. The key is the flag e.g. -'i' or --'input'
     */
    public Map getOptionMap();

    /**
     * Returns the program file (if needed)
     * @return
     */
    public File getProgram();
}
