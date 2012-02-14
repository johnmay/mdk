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
package uk.ac.ebi.annotation;

import uk.ac.ebi.annotation.util.AnnotationLoader;
import java.io.*;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.vistors.AnnotationVisitor;
import uk.ac.ebi.core.AbstractDescriptor;


/**
 *          AbstractAnnotation – 2011.09.08 <br>
 *          Abstract implementation of the Annotation interface
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractAnnotation
  extends AbstractDescriptor
  implements Annotation, Externalizable {

    private static final Logger LOGGER = Logger.getLogger(AbstractAnnotation.class);


    public AbstractAnnotation() {
        super(AnnotationLoader.getInstance());
    }


    /**
     *
     * Accepts an annotation visitor
     *
     * @param visitor The visitor to accept
     *
     */
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visit(this);
    }


    /**
     * @inheritDoc
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }


    /**
     * @inheritDoc
     */
    public void writeExternal(ObjectOutput out) throws IOException {
    }






}

