/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.mdk.domain.annotation.task;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.AbstractAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * @name    Parameter - 2011.10.13 <br>
 *          A class to hold information of parameters for a task
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context
@Brief("Parameter")
@Description("A parameter for a task (not necessarily external)")
public class Parameter extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Parameter.class);

    private String name;

    private String description;

    private String flag;

    private String value;


    public Parameter() {
    }


    public Parameter(String name, String description, String flag, String value) {
        this.name = name;
        this.description = description;
        this.flag = flag;
        this.value = value;
    }


    @Override
    public String getShortDescription() {
        return name;
    }


    @Override
    public String getLongDescription() {
        return description;
    }


    public String getFlag() {
        return flag;
    }


    public Object getValue() {
        return value;
    }


    public Parameter newInstance() {
        return new Parameter();
    }


    @Override
    public String toString() {
        return getValue().toString();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        name = in.readUTF();
        description = in.readUTF();
        flag = in.readUTF();
        value = in.readUTF();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(name);
        out.writeUTF(description);
        out.writeUTF(flag);
        out.writeUTF(value);
    }
}
