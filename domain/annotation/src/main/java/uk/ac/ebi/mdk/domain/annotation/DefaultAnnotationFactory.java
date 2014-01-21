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

package uk.ac.ebi.mdk.domain.annotation;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.Citation;
import uk.ac.ebi.mdk.domain.annotation.crossreference.Classification;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.task.ExecutableParameter;
import uk.ac.ebi.mdk.domain.annotation.task.FileParameter;
import uk.ac.ebi.mdk.domain.annotation.task.Parameter;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ClassificationIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Context;

import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * AnnotationFactory – 2011.09.12 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class DefaultAnnotationFactory implements AnnotationFactory {

    private static final Logger LOGGER = Logger.getLogger(
        DefaultAnnotationFactory.class);

    // reflective map
    private static Constructor[] constructors = new Constructor[Byte.MAX_VALUE];

    private static Map<Class, Annotation>                                             instances  = new HashMap<Class, Annotation>();
    private        Multimap<Class<? extends Annotation>, Class<? extends Annotation>> subclasses = HashMultimap.create();

    private ListMultimap<Class, Annotation> contextMap = ArrayListMultimap.create();

    private Map<Class, CrossReference> xrefMap = new HashMap<Class, CrossReference>();

    // hold flag annotations
    private Collection<Flag> flags = new ArrayList<Flag>();


    public static DefaultAnnotationFactory getInstance() {
        return AnnotationFactoryHolder.INSTANCE;
    }


    private static class AnnotationFactoryHolder {

        private static DefaultAnnotationFactory INSTANCE = new DefaultAnnotationFactory();
    }


    private DefaultAnnotationFactory() {

        for (Annotation annotation : Arrays.asList(
                new AtomContainerAnnotation(),
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
                new SystematicName(),
                new Locus(),
                new Citation(),
                new FluxLowerBound(),
                new FluxUpperBound(),
                new Source(),
                new ExactMass(),
                new SMILES(),
                new InChI(),
                new Molfile(),
                new Charge(),
                new GibbsEnergy(),
                new Note(),
                Lumped.getInstance(),
                ACPAssociated.getInstance(),
                new RExExtract(),
                new PrimaryName()
            )) {

            instances.put(annotation.getClass(), annotation);

        }

        xrefMap.put(ECNumber.class, new EnzymeClassification());
        xrefMap.put(ClassificationIdentifier.class, new Classification());
        xrefMap.put(ChEBIIdentifier.class, new ChEBICrossReference());
        xrefMap.put(KEGGCompoundIdentifier.class, new KEGGCrossReference());
        xrefMap.put(Identifier.class, new CrossReference());

        // add the flags
        for (Annotation annotation : instances.values()) {
            if (annotation instanceof Flag) {
                flags.add((Flag) annotation);
            }
        }


    }

    /**
     * Access a list of annotations that are specific to the provided entity
     * class. Note: the annotations contained with in the list should only be
     * used to construct new entities and not to store information. The
     * instances_old instead of the classes are returned to avoid extra object
     * creation.
     *
     * @param entity instance of the entity you which to get context annotations
     *               for
     *
     * @return List of annotations that can be added to that class which are
     *         intended for new instantiation only
     */
    public List<Annotation> ofContext(AnnotatedEntity entity) {
        return ofContext(entity.getClass());
    }


    /**
     * Access a list of annotations that are specific to the provided entity
     * class. Note: the annotations contained with in the list should only be
     * used to construct new entities and not to store information. The
     * instances_old instead of the classes are returned to avoid extra object
     * creation.
     *
     * @param entityClass class of the entity you which to get context
     *                    annotations for
     *
     * @return List of annotations that can be added to that class which are
     *         intended for new instantiation only
     */
    @SuppressWarnings("unchecked")
    public List<Annotation> ofContext(Class<? extends AnnotatedEntity> entityClass) {

        if (contextMap.containsKey(entityClass)) {
            return contextMap.get(entityClass);
        }

        Set<Class> visited = new HashSet<Class>();
        List<Annotation> annotations = new ArrayList<Annotation>();

        for (Annotation annotation : instances.values()) {
            Context context = annotation.getClass().getAnnotation(
                Context.class);

            if (context == null) {
                LOGGER.warn(
                    "No @Context for " + annotation.getClass().getSimpleName());
                continue;
            }

            for (Class c : context.value()) {
                if (!visited.contains(
                    annotation.getClass()) && (entityClass.isAssignableFrom(
                    c) || c.isAssignableFrom(entityClass))) {
                    annotations.add(annotation);
                    visited.add(annotation.getClass());
                }
            }
        }

        contextMap.putAll(entityClass, annotations);

        return annotations;

    }


    /**
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
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A ofClass(Class<? extends A> c) {

        Annotation annotation = instances.get(c);

        if (annotation != null) {
            return (A) annotation.newInstance();
        }

        throw new InvalidParameterException(
            "Unable to get instance of annotation class: " + c);
    }


    /**
     * Construct an empty annotation given it's index. It the index returns a
     * null pointer then an InvalidParameterException is thrown informing of the
     * problematic index. The index is given in the uk.ac.ebi.annotation/AnnotationDescription.properties
     * file which in turn is loaded by {
     *
     * @param index
     *
     * @return
     *
     * @see AnnotationLoader}.
     * @deprecated use AnnotationFactory.ofClass(Class)
     */
    @SuppressWarnings("unchecked") @Deprecated
    public <A extends Annotation> A ofIndex(byte index) {
        throw new UnsupportedOperationException(
            "Deprecated method, use AnnotationFactory.ofClass(Class)");
    }

    /**
     * Access a set of annotation flags that could match this entity. This
     * provides suggestion of matching flag's for this entity using the {@see
     * AbstractFlag.matches(AnnotatedEntity)}
     *
     * @param entity the entity to collect matching flags for
     *
     * @return annotations which could be added to the entity (may need user
     *         prompt)
     *
     * @see Flag#matches(uk.ac.ebi.mdk.domain.entity.AnnotatedEntity)
     */
    public Set<Flag> getMatchingFlags(AnnotatedEntity entity) {

        Set<Flag> matching = new HashSet<Flag>();

        for (Flag flag : flags) {
            if (flag.matches(entity)) {
                matching.add(
                    flag); // don't need a new instance as there is not data stored
            }
        }

        return matching;
    }

    /**
     * Builds a cross-reference from the identifier. The cross-reference is
     * designated either a {@see ChEBICrossReference}, {@see
     * KEGGCompoundIdentifier}, {@see EnzymeClassification} or {@see
     * Classification}. If no appropiate cross-reference is available then the
     * default {@see CrossReference} class is return
     *
     * @param identifier
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <I extends Identifier> CrossReference<I, Observation> getCrossReference(I identifier) {

        CrossReference<I, Observation> xref = (CrossReference<I, Observation>) getCrossReference(
            identifier.getClass());

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
            LOGGER.error(
                "More then one potential cross-reference, this should be resolved!");
        }

        return xrefs.isEmpty() ? new CrossReference() : xrefs.iterator().next();
    }

    /** @inheritDoc */
    @Override
    public Collection<Class<? extends Annotation>> getSubclasses(Class<? extends Annotation> c) {

        // check if we have the subclasses already
        if (subclasses.containsKey(c)) {
            return subclasses.get(c);
        }

        // no cache - build the map
        for (Annotation annotation : instances.values()) {

            if (c.isInstance(annotation)) {
                this.subclasses.put(c, annotation.getClass());
            }

        }

        return subclasses.get(c);

    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> Collection<? extends T> getSubclassInstances(Class<T> c) {

        Collection<T> instances = new TreeSet<T>(new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return o1.getBrief().compareTo(o2.getBrief());
            }
        });

        for (Class<? extends Annotation> subclass : getSubclasses(c)) {
            instances.add((T) ofClass(subclass));
        }

        return instances;

    }

    /**
     * Stuff used to generate some wiki pages
     *
     * @return
     */
    private String getWikiHTML() {


        StringBuilder sb = new StringBuilder();
        Annotation[] annotations = instances.values().toArray(
            new Annotation[0]);
        Arrays.sort(annotations, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return o1.getClass().getSimpleName().compareTo(
                    o2.getClass().getSimpleName());
            }
        });
        for (Annotation annotation : annotations) {
            sb.append("<tr>");
            sb.append("<td>").append(annotation.getBrief()).append("</td>");
            sb.append("<td>").append(annotation.getDescription()).append(
                "</td>");
            sb.append("<td>").append(Joiner.on(", ").join(filter(Arrays.asList(
                annotation.getClass().getAnnotation(
                    Context.class).value())))).append("</td>");
            sb.append("<td><code><a href=\"").append(
                "http://www.github.com/johnmay/mdk/tree/develop/domain/annotation/src/main/java/").append(
                annotation.getClass().getName().replaceAll("\\.", "/")).append(
                ".java\">");
            sb.append(annotation.getClass().getSimpleName()).append(
                "</a></code></td>");
            sb.append("</tr>");
        }

        return sb.toString();

    }

    private List<String> filter(List<Class<? extends AnnotatedEntity>> classes) {
        List<String> scopes = new ArrayList<String>();
        for (Class c : classes) {
            if (c != AnnotatedEntity.class) scopes.add(c.getSimpleName());
        }
        return scopes;
    }

    public static void main(String[] args) {
        DefaultAnnotationFactory FACTORY = DefaultAnnotationFactory.getInstance();
        System.out.println(FACTORY.getWikiHTML());
    }


}
