/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.annotation.task;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Task;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;

import java.io.File;

/**
 * @name    FileParameter - 2011.10.13 <br>
 *          A parameter that uses a file as a value
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context(Task.class)
@Brief("File")
@Description("A parameter for a task (not necessarily external) that uses a local file")
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

    @Override
    public FileParameter newInstance() {
        return new FileParameter();
    }
}
