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

package uk.ac.ebi.mdk.ui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ResourceUtility;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.io.File;

/** @author John May */
public class ReconstructionFileChooser extends JFileChooser {

    private static final Logger LOGGER = Logger
            .getLogger(ReconstructionFileChooser.class);

    public ReconstructionFileChooser() {
        super();

        final ImageIcon icon = ResourceUtility
                .getIcon("/uk/ac/ebi/chemet/render/images/networkbuilder_16x16.png");
        final FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Metabolic Reconstruction";
            }
        };
        
        setAcceptAllFileFilterUsed(false);
        setFileFilter(filter);
        
        setFileSelectionMode(FILES_AND_DIRECTORIES);
        setFileView(new FileView() {

            @Override public Icon getIcon(File f) {
                if (isReconstruction(f))
                    return icon;
                return super.getIcon(f);
            }

            @Override public Boolean isTraversable(File f) {
                if (isReconstruction(f))
                    return false;
                return super.isTraversable(f);
            }
        });
    }

    private static boolean isReconstruction(File f) {
        String name = f.getName();
        if (name.endsWith(Reconstruction.RECONSTRUCTION_FILE_EXTENSION))
            return true;
        return false;
    }


}
