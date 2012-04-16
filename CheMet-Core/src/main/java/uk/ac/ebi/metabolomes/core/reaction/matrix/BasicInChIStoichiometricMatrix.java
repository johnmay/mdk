/**
 * BasicInChIStoichiometricMatrix.java
 *
 * 2011.07.21
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.core.reaction.matrix;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.chemical.InChI;


/**
 * @name BasicInChIStoichiometricMatrix @date 2011.07.21
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version) @brief ...class description...
 *
 */
public class BasicInChIStoichiometricMatrix extends StoichiometricMatrix<InChI, String> {

    private static final Logger LOGGER = Logger.getLogger(BasicInChIStoichiometricMatrix.class);


    protected BasicInChIStoichiometricMatrix() {
    }


    protected BasicInChIStoichiometricMatrix(int n, int m) {
        super(n, m);
    }


    @Override
    public BasicInChIStoichiometricMatrix init() {
        return (BasicInChIStoichiometricMatrix) super.init();
    }


    public static BasicInChIStoichiometricMatrix create(int m, int n) {
        return new BasicInChIStoichiometricMatrix(n, m).init();
    }


    public static BasicInChIStoichiometricMatrix create() {
        return new BasicInChIStoichiometricMatrix().init();
    }


    @Override
    public Class<? extends String> getReactionClass() {
        return String.class;
    }


    @Override
    public Class<? extends InChI> getMoleculeClass() {
        return InChI.class;
    }

    


    @Override
    public StoichiometricMatrix<InChI, String> newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public StoichiometricMatrix<InChI, String> newInstance(int n, int m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
