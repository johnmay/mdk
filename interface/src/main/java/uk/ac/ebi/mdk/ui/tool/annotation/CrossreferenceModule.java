/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.ui.tool.annotation;

import javax.swing.JComponent;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 * @author johnmay
 */
public interface CrossreferenceModule {

    /**
     * Access a description of the module
     *
     * @return
     */
    public String getDescription();


    /**
     * Access the graphical component
     *
     * @return
     */
    public JComponent getComponent();


    /**
     * Set ups the module for the specified metabolite
     *
     * @param metabolite
     */
    public void setup(Metabolite metabolite);


    public boolean canTransferAnnotations();


    public void transferAnnotations() throws Exception;
}
