/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.core;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;


/**
 * 
 * ReconstructionManager.java
 * Class to manage the multiple reconstructions and maintain an 'active' reconstruction
 *
 * @author johnmay
 * @date Apr 13, 2011
 * 
 */
public class ReconstructionManager {

    private static final org.apache.log4j.Logger LOGGER =
                                                 org.apache.log4j.Logger.getLogger(
      ReconstructionManager.class);
    private AbstractIdentifier activeProjectIdentifier;
    private ArrayList<Reconstruction> projects = new ArrayList<Reconstruction>();
    private LinkedHashMap<AbstractIdentifier, Integer> projectMap =
                                                       new LinkedHashMap<AbstractIdentifier, Integer>();
    private Properties properties = new Properties();
    private File projectManagerPropertiesFiles =
                 new File(ReconstructionManager.class.getClassLoader().getResource(
      "config/projectmanager.properties").getFile());


    private ReconstructionManager() {
    }


    private static class ProjectManagerHolder {

        private static ReconstructionManager INSTANCE = new ReconstructionManager();
    }


    /**
     *
     * Access the singleton instance of the manager
     *
     * @return Instance of the manager
     *
     */
    public static ReconstructionManager getInstance() {
        return ProjectManagerHolder.INSTANCE;
    }


    /**
     *
     * Access the project bank
     * 
     * @param identifier
     * @return The project related to the identifier if none is found null is returned
     * 
     */
    private Reconstruction getProject(AbstractIdentifier identifier) {
        for( Reconstruction project : projects ) {
            if( project.getIdentifier().equals(identifier) ) {
                return project;
            }
        }
        return null;
    }


    /**
     *
     * Access whether the 
     * 
     * @return
     * 
     */
    public boolean hasProjects() {
        return !projects.isEmpty();
    }


    /**
     * 
     * Accessor for the active reconstruction
     *
     * @return
     * 
     */
    public Reconstruction getActiveReconstruction() {
        return getProject(activeProjectIdentifier);
    }


    /**
     *
     * @param activeProjectIdentifier
     */
    public void setActiveReconstruction(AbstractIdentifier activeProjectIdentifier) {
        this.activeProjectIdentifier = activeProjectIdentifier;
    }


    /**
     * 
     * Set the active reconstruction given a reconstruction object
     *
     * @param reconstruction
     *
     */
    public void setActiveReconstruction(Reconstruction reconstruction) {

        // is it keyed? then just get the identifier and set it
        if( projectMap.containsKey(reconstruction.getIdentifier()) ) {
            setActiveReconstruction(reconstruction.getIdentifier());
            return;
        }

        for( Reconstruction entry : projects ) {
            if( entry.getIdentifier().equals(reconstruction) )  {
                setActiveReconstruction(entry.getIdentifier());
                LOGGER.error("found matching project but clashing identifiers stored:" +
                             entry.getIdentifier() +
                             " new:" +
                             reconstruction.getIdentifier());
                return;
            }
        }

        LOGGER.debug(
          "Setting active with a project which can not be found in the current collection. A new entry will be created");

        projectMap.put(reconstruction.getIdentifier(), projects.size());
        projects.add(reconstruction);

        setActiveReconstruction(reconstruction.getIdentifier());

    }


    /**
     * 
     * Access a stored project at the given index
     *
     * @param i
     * @return
     * 
     */
    public Reconstruction getProject(int i) {
        return projects.get(i);
    }


    /**
     *
     * Access the number of project currently managed
     *
     */
    public int size() {
        return projects.size();
    }


}

