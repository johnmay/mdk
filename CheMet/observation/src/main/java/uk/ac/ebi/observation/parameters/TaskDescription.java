/**
 * Parameters.java
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
package uk.ac.ebi.observation.parameters;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ebi.interfaces.Identifier;

/**
 * @name    Parameters - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class TaskDescription {

    private String name;
    private String description;
    private Identifier identifier;
    private Date date;
    private File program;
    Set<TaskOption> options = new HashSet();

    public TaskDescription() {
    }

    /**
     * Create a job description with a name, description and identifier (TaskId). The date is automatically timestamped
     * @param name
     * @param description
     * @param identifier
     */
    public TaskDescription(File program, String name, String description, Identifier identifier) {
        this.program = program;
        this.name = name;
        this.description = description;
        this.identifier = identifier;
        this.date = Calendar.getInstance().getTime();
    }

    public boolean add(TaskOption option) {
        return options.add(option);
    }

    public boolean addAll(Collection<TaskOption> options) {
        return options.addAll(options);
    }

    

}
