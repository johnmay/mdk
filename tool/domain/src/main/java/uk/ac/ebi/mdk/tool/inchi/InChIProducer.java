/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.tool.inchi;

import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @name    InChIProducer
 * @date    2013.03.08
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public interface InChIProducer {

    InChIResult calculateInChI(IAtomContainer mol);

    InChIResult calculateInChI(String mdlMol);

    void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions);
    
}
