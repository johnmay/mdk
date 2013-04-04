/*
 * Copyright (C) 2013 EMBL-EBI
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

package uk.ac.ebi.mdk.domain.entity.reaction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Rating;
import uk.ac.ebi.mdk.domain.entity.collection.ObservationManager;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Observation;

/**
 * @date    2013.04.03
 * @author  Pablo Moreno <pablacious at users.sf.net>
 */
public class BiochemRxnImpl implements BiochemicalReaction {


    private MetabolicReaction r;
    private Collection<GeneProduct> modifiers;

    public BiochemRxnImpl(MetabolicReaction metabolicReaction) {
        this.r = metabolicReaction;
        initModifiers();
    }
    
    public BiochemRxnImpl(UUID uuid){
        this.r = new MetabolicReactionImpl(uuid);
        initModifiers();
    }
    
    public BiochemRxnImpl() {
        this.r = new MetabolicReactionImpl();
        initModifiers();
    }
    
    public BiochemRxnImpl(Identifier identifier, String abbreviation, String name) {
        this.r = new MetabolicReactionImpl(identifier, abbreviation, name);
        initModifiers();
    }
    
    private void initModifiers() {
        this.modifiers = new LinkedList<GeneProduct>();
    }
    
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addModifier(GeneProduct g) {
        return modifiers.add(g);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean removeModifier(GeneProduct g) {
        return modifiers.remove(g);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<GeneProduct> getModifiers() {
        return new LinkedList<GeneProduct>(modifiers);
    }
    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean remove(Metabolite m) {
        return r.remove(m);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean addReactant(Metabolite reactant) {
        return r.addReactant(reactant);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean addProduct(Metabolite product) {
        return r.addProduct(product);
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public List<MetabolicParticipant> getReactants() {
        return r.getReactants();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public List<MetabolicParticipant> getProducts() {
        return r.getProducts();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public List<MetabolicParticipant> getParticipants() {
        return r.getParticipants();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean addReactant(MetabolicParticipant participant) {
        return r.addReactant(participant);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean addProduct(MetabolicParticipant participant) {
        return r.addProduct(participant);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean removeReactant(MetabolicParticipant participant) {
        return r.removeReactant(participant);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean removeProduct(MetabolicParticipant participant) {
        return r.removeProduct(participant);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int getReactantCount() {
        return r.getReactantCount();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int getProductCount() {
        return r.getProductCount();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int getParticipantCount() {
       return r.getParticipantCount();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Direction getDirection() {
       return r.getDirection();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void setDirection(Direction direction) {
       r.setDirection(direction);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void transpose() {
        r.transpose();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void clear() {
        r.clear();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void addAnnotations(Collection<? extends Annotation> annotations) {
        r.addAnnotations(annotations);
    }

    @Override
    public void addAnnotation(Annotation annotation) {
       r.addAnnotation(annotation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Collection<Annotation> getAnnotations() {
       return r.getAnnotations();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean hasAnnotation(Annotation annotation) {
       return r.hasAnnotation(annotation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean hasAnnotation(Class<? extends Annotation> c) {
       return r.hasAnnotation(c);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Collection<Class> getAnnotationClasses() {
       return r.getAnnotationClasses();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public <T> Collection<T> getAnnotations(Class<T> type) {
       return r.getAnnotations(type);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public <T extends Annotation> Set<T> getAnnotationsExtending(T base) {
       return r.getAnnotationsExtending(base);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public <T extends Annotation> Set<T> getAnnotationsExtending(Class<T> c) {
       return r.getAnnotationsExtending(c);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean removeAnnotation(Annotation annotation) {
       return r.removeAnnotation(annotation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public ObservationManager getObservationManager() {
       return r.getObservationManager();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean addObservation(Observation observation) {
       return r.addObservation(observation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean removeObservation(Observation observation) {
       return r.removeObservation(observation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Collection<Class<? extends Observation>> getObservationClasses() {
       return r.getObservationClasses();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Collection<Observation> getObservations(Class<? extends Observation> c) {
       return r.getObservations(c);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Enum<? extends Rating> getRating() {
       return r.getRating();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void setRating(Enum<? extends Rating> rating) {
       r.setRating(rating);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public UUID uuid() {
        return r.uuid();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public String getName() {
        return r.getName();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public String getAbbreviation() {
       return r.getAbbreviation();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Identifier getIdentifier() {
       return r.getIdentifier();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void setIdentifier(Identifier identifier) {
        r.setIdentifier(identifier);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void setName(String name) {
        r.setName(name);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void setAbbreviation(String abbreviation) {
       r.setAbbreviation(abbreviation);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public String getAccession() {
       return r.getAccession();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public BiochemicalReaction newInstance() {
        return new BiochemRxnImpl(new MetabolicReactionImpl());
    }

    @Override
    public MetabolicReaction getMetabolicReaction() {
        return r;
    }

   
}
