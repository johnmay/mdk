/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.descriptor.annotation;

import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;

/**
 * AbstractAnnotation.java
 *
 *
 * @author johnmay
 * @date May 7, 2011
 */
public class AbstractAnnotation
        implements Serializable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( AbstractAnnotation.class );
    private static final long serialVersionUID = 5510470219307414393L;
    private Object annotation;
    private ObservationCollection evidence;
    private AnnotationType type;
    private GeneProduct product;
    private String description;
    private AnnotationFlag flag = AnnotationFlag.NONE;

    public AbstractAnnotation( Object annotation ,
                               AnnotationType type ,
                               String description ,
                               ObservationCollection evidence ) {
        this.annotation = annotation;
        this.type = type;
        this.evidence = evidence;
        this.description = description;
    }

    public Object getAnnotation() {
        return annotation;
    }

    public void setAnnotation( Object annotation ) {
        this.annotation = annotation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public AnnotationType getType() {
        return type;
    }

    public void setType( AnnotationType type ) {
        this.type = type;
    }

    public void setEvidence( List<AbstractObservation> evidence ) {
        Collections.copy( evidence , this.evidence );
    }

    public ObservationCollection getEvidence() {
        return evidence;
    }

    public void setProduct( GeneProduct product ) {
        this.product = product;
    }

    public GeneProduct getProduct() {
        return product;
    }

    public void setFlag(AnnotationFlag flag){
        this.flag = flag;
    }

    public AnnotationFlag getFlag() {
        return flag;
    }
}
