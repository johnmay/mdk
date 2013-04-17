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

package uk.ac.ebi.mdk.ui.tool.annotation;

import uk.ac.ebi.mdk.domain.entity.Metabolite;

import javax.swing.*;


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
