package uk.ac.ebi.mdk.ui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ResourceUtility;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * @author John May
 */
public class ReconstructionFileChooser extends JFileChooser {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionFileChooser.class);

    public ReconstructionFileChooser() {
        super();

        final ImageIcon icon = ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/images/networkbuilder_16x16.png");
        final FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() && f.getName().endsWith(Reconstruction.RECONSTRUCTION_FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return "Metabolic Reconstruction";
            }
        };

        addChoosableFileFilter(filter);
        setFileView(new FileView() {
            @Override
            public Icon getIcon(File f) {
                if (f.getName().endsWith(Reconstruction.RECONSTRUCTION_FILE_EXTENSION)) {
                    return icon;
                }
                return super.getIcon(f);
            }


            @Override
            public Boolean isTraversable(File f) {
                return f.isDirectory() && !filter.accept(f);
            }
        });
    }


}
