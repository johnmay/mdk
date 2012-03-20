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
package uk.ac.ebi.annotation.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.IOException;
import java.io.ObjectInput;
import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;
import java.util.*;

import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.annotation.reaction.GibbsEnergyError;
import uk.ac.ebi.interfaces.Annotation;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.*;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.annotation.chemical.*;
import uk.ac.ebi.annotation.crossreference.Citation;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.annotation.model.*;
import uk.ac.ebi.annotation.task.ExecutableParameter;
import uk.ac.ebi.annotation.task.FileParameter;
import uk.ac.ebi.annotation.task.Parameter;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.resource.classification.ClassificationIdentifier;
import uk.ac.ebi.resource.classification.ECNumber;


/**
 * AnnotationFactory – 2011.09.12 <br> Class description
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 */
public class AnnotationFactory {

    private static final Logger LOGGER = Logger.getLogger(AnnotationFactory.class);

    // reflective map
    private static Constructor[] constructors = new Constructor[Byte.MAX_VALUE];

    private static Map<Class, Annotation> instances = new HashMap<Class, Annotation>();

    private ListMultimap<Class, Annotation> contextMap = ArrayListMultimap.create();

    private Map<Class, CrossReference> xrefMap = new HashMap<Class, CrossReference>();


    public static AnnotationFactory getInstance() {
        return AnnotationFactoryHolder.INSTANCE;
    }


    private static class AnnotationFactoryHolder {

        private static AnnotationFactory INSTANCE = new AnnotationFactory();
    }


    private AnnotationFactory() {

        for (Annotation annotation : Arrays.asList(new AtomContainerAnnotation(),
                                                   new MolecularFormula(),
                                                   new AuthorAnnotation(),
                                                   new CrossReference(),
                                                   new Classification(),
                                                   new EnzymeClassification(),
                                                   new ChEBICrossReference(),
                                                   new KEGGCrossReference(),
                                                   new Subsystem(),
                                                   new ExecutableParameter(),
                                                   new FileParameter(),
                                                   new Parameter(),
                                                   new Synonym(),
                                                   new Locus(),
                                                   new Citation(),
                                                   new FluxLowerBound(),
                                                   new FluxUpperBound(),
                                                   new Source(),
                                                   new ExactMass(),
                                                   new SMILES(),
                                                   new InChI(),
                                                   new Charge(),
                                                   new GibbsEnergy(),
                                                   new GibbsEnergyError())) {

            instances.put(annotation.getClass(), annotation);

        }

        xrefMap.put(ECNumber.class, new EnzymeClassification());
        xrefMap.put(ClassificationIdentifier.class, new Classification());
        xrefMap.put(ChEBIIdentifier.class, new ChEBICrossReference());
        xrefMap.put(KEGGCompoundIdentifier.class, new KEGGCrossReference());
        xrefMap.put(Identifier.class, new CrossReference());


    }

    /**
     * 
     * Access a list of annotations that are specific to the
     * provided entity class. Note: the annotations contained
     * with in the list should only be used to construct
     * new entities and not to store information. The instances_old
     * instead of the classes are returned to avoid extra object
     * creation.
     * 
     * @param  entity instance of the entity you which to get context
     *                annotations for
     * 
     * @return List of annotations that can be added to that class which
     *         are intended for new instantiation only
     *         
     * 
     */
    public List<Annotation> ofContext(AnnotatedEntity entity) {
        return ofContext(entity.getClass());
    }


    /**
     * 
     * Access a list of annotations that are specific to the
     * provided entity class. Note: the annotations contained
     * with in the list should only be used to construct
     * new entities and not to store information. The instances_old
     * instead of the classes are returned to avoid extra object
     * creation.
     * 
     * @param  entityClass class of the entity you which to get context
     *                     annotations for
     * 
     * @return List of annotations that can be added to that class which
     *         are intended for new instantiation only
     *         
     * 
     */
    public List<Annotation> ofContext(Class<? extends AnnotatedEntity> entityClass) {

        if (contextMap.containsKey(entityClass)) {
            return contextMap.get(entityClass);
        }

        Set<Class> visited = new HashSet<Class>();
        List<Annotation> annotations = new ArrayList<Annotation>();

        for (Annotation annotation : instances.values()) {
            Context context = annotation.getClass().getAnnotation(Context.class);

            if (context == null) {
                LOGGER.warn("No @Context for " + annotation.getClass().getSimpleName());
                continue;
            }

            for (Class c : context.value()) {
                if (!visited.contains(annotation.getClass())
                    && (entityClass.isAssignableFrom(c) || c.isAssignableFrom(entityClass))) {
                    annotations.add(annotation);
                    visited.add(annotation.getClass());
                }
            }
        }

        contextMap.putAll(entityClass, annotations);

        return annotations;

    }


    /**
     *
     * Construct an empty annotation of the given class type. Note there is an
     * overhead off using this method over {@ofIndex(Byte)} as the Byte index is
     * first looked up in the AnnotationLoader. The average speed reduction is
     * 1800 % slower (note this is still only about 1/3 second for 100 000
     * objects).
     *
     * @param c
     *
     * @return
     */
    public <A extends Annotation> A ofClass(Class<? extends A> c) {

        Annotation annotation = instances.get(c);

        if (annotation != null) {
            return (A) annotation.newInstance();
        }

        throw new InvalidParameterException("Unable to get instance of annotation class: "
                                                    + c);
    }


    /**
     *
     * Construct an empty annotation given it's index. It the index returns a
     * null pointer then an InvalidParameterException is thrown informing of the
     * problematic index. The index is given in the
     * uk.ac.ebi.annotation/AnnotationDescription.properties file which in turn
     * is loaded by {
     *
     * @see AnnotationLoader}.
     *
     * @param index
     *
     * @return
     *
     * @deprecated use AnnotationFactory.ofClass(Class)
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <A extends Annotation> A ofIndex(byte index) {
        throw new UnsupportedOperationException("Deprecated method, use AnnotationFactory.ofClass(Class)");
    }


    /**
     * 
     * Builds a cross-reference from the identifier. The cross-reference is
     * designated either a {@see ChEBICrossReference}, 
     * {@see KEGGCompoundIdentifier}, {@see EnzymeClassification} or
     * {@see Classification}. If no appropiate cross-reference is
     * available then the default {@see CrossReference} class is return
     * 
     * 
     * @param identifier
     * @return 
     * 
     */
    public <I extends Identifier> CrossReference<I, Observation> getCrossReference(I identifier) {

        CrossReference<I, Observation> xref = ( CrossReference<I, Observation>) getCrossReference(identifier.getClass());

        xref.setIdentifier(identifier);

        return xref;


    }

    @SuppressWarnings("unchecked")
    public <I extends Identifier> CrossReference<I, Observation> getCrossReference(Class<I> c) {


        if (xrefMap.containsKey(c)) {
            return xrefMap.get(c).newInstance();
        }

        List<Class> classes = new ArrayList<Class>();

        Class sc = c.getSuperclass();

        for (Class i : c.getInterfaces()) {
            if (Identifier.class.isAssignableFrom(i.getClass())) {
                classes.add(sc);
            }
        }

        if (Identifier.class.isAssignableFrom(sc)) {
            classes.add(sc);
        }

        Set<CrossReference> xrefs = new HashSet<CrossReference>();
        for (Class c1 : classes) {
            xrefs.add(getCrossReference(c1));
        }

        if (xrefs.size() > 1) {
            LOGGER.error("More then one potential cross-reference, this should be resolved!");
        }

        return xrefs.isEmpty() ? new CrossReference() : xrefs.iterator().next();
    }


}
