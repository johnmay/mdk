/**
 * ProgramOption.java
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

import com.google.common.base.Objects;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @name    ProgramOption - 2011.10.07 <br>
 *          A class to hold a program option and it's name.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class TaskOption implements Externalizable {

    private String description;
    private String flag;
    private String value;
    private boolean longFlag;

    public TaskOption() {
    }

    public TaskOption(String description, String flag, String value) {
        this.description = description;
        this.flag = flag;
        this.value = value;
    }

    public String getFlagValuePair() {
        StringBuilder sb = new StringBuilder(flag.length() + value.length() + 4);
        return sb.append(longFlag ? "--" : '-').append(flag).append(' ').append(value).toString();

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(description.length() + value.length() + 1);
        return sb.append(description).append(":").append(value).toString();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        description = in.readUTF();
        flag = in.readUTF();
        value = in.readUTF();
        longFlag = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(description);
        out.writeUTF(flag);
        out.writeUTF(value);
        out.writeBoolean(longFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description, flag, value, longFlag);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskOption other = (TaskOption) obj;
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.flag == null) ? (other.flag != null) : !this.flag.equals(other.flag)) {
            return false;
        }
        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
            return false;
        }
        if (this.longFlag != other.longFlag) {
            return false;
        }
        return true;
    }

    public String getFlag() {
        return flag;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    
}
