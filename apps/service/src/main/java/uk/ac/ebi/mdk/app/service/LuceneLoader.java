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

package uk.ac.ebi.mdk.app.service;

import com.jgoodies.forms.layout.CellConstraints;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PreferencePanelFactory;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.hsql.Hsqldb;
import uk.ac.ebi.mdk.service.ServicePreferences;
import uk.ac.ebi.mdk.service.loader.crossreference.ChEBICrossReferenceLoader;
import uk.ac.ebi.mdk.service.loader.crossreference.UniProtCrossReferenceLoader;
import uk.ac.ebi.mdk.service.loader.data.ChEBIDataLoader;
import uk.ac.ebi.mdk.service.loader.location.DefaultLocationFactory;
import uk.ac.ebi.mdk.service.loader.multiple.HMDBMetabocardsLoader;
import uk.ac.ebi.mdk.service.loader.multiple.KEGGCompoundLoader;
import uk.ac.ebi.mdk.service.loader.multiple.LipidMapsLoader;
import uk.ac.ebi.mdk.service.loader.multiple.MetaCycCompoundLoader;
import uk.ac.ebi.mdk.service.loader.name.ChEBINameLoader;
import uk.ac.ebi.mdk.service.loader.single.TaxonomyLoader;
import uk.ac.ebi.mdk.service.loader.structure.*;
import uk.ac.ebi.mdk.ui.component.service.LoaderGroupFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * LuceneLoader - 15.03.2012 <br/>
 * <p/>
 * Provides a UI for loading lucene services
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LuceneLoader extends Box {

    public LuceneLoader(Window window) {
        super(BoxLayout.PAGE_AXIS);

        setOpaque(true);
        setBackground(Color.WHITE);

        LoaderGroupFactory factory = new LoaderGroupFactory(window, DefaultLocationFactory.getInstance());
        try {

            add(Box.createGlue());

            JLabel label = LabelFactory.newLabel("Ensure service root is available (read/write). If changing the root" +
                                                         " please restart the application");
            label.setAlignmentX(SwingConstants.LEFT);
            add(Box.createVerticalStrut(15));
            add(label);
            add(Box.createVerticalStrut(15));
            add(PreferencePanelFactory.getPreferencePanel(ServicePreferences.getInstance().getPreference("SERVICE_ROOT")));

            add(factory.createGroup("ChEBI",
                                    new ChEBIStructureLoader(),
                                    new ChEBINameLoader(),
                                    new ChEBIDataLoader(),
                                    new ChEBICrossReferenceLoader()));
            add(factory.createGroup("KEGG",
                                    new KEGGCompoundLoader(),
                                    new KEGGCompoundStructureLoader(),
                                    Hsqldb.keggReactionLoader()));
            add(factory.createGroup("HMDB",
                                    new HMDBMetabocardsLoader(),
                                    new HMDBStructureLoader()));
            add(factory.createGroup("BioCyc",
                                    new MetaCycCompoundLoader(),
                                    new MetaCycStructureLoader()));
            add(factory.createGroup("LIPID MAPS",
                                    new LipidMapsLoader(),
                                    new LipidMapsSDFLoader()));
            add(factory.createGroup("UniProt",
                                    new UniProtCrossReferenceLoader(DefaultEntityFactory.getInstance(),
                                                                    DefaultIdentifierFactory.getInstance()),
                                    new TaxonomyLoader()));

            add(Box.createGlue());

            CellConstraints cc = new CellConstraints();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Lucene Services");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new LuceneLoader(frame));
                frame.setVisible(true);
                frame.pack();
            }
        });
    }

}
