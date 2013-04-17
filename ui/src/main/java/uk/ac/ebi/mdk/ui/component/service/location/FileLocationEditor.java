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

package uk.ac.ebi.mdk.ui.component.service.location;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.utility.ResourceUtility;
import uk.ac.ebi.mdk.service.location.LocationDescription;
import uk.ac.ebi.mdk.service.location.LocationFactory;
import uk.ac.ebi.mdk.service.location.ResourceLocation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * FileEditor - 27.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class FileLocationEditor
        extends JPanel
        implements LocationEditor {

    private static final Logger LOGGER = Logger.getLogger(FileLocationEditor.class);

    private JTextField field = FieldFactory.newField(15);
    private JButton browse;
    private LocationFactory factory;

    private static final JFileChooser chooser = new JFileChooser();

    public FileLocationEditor(LocationFactory factory) {

        setLayout(new FormLayout("p, min", "p"));

        setOpaque(false);

        this.factory = factory;
        final JComponent component = this;

        browse = ButtonFactory.newCleanButton(ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/images/cutout/browse_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = chooser.showOpenDialog(component);
                if(choice == JFileChooser.APPROVE_OPTION){
                    field.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        browse.setToolTipText("Browse for a selected file");

        CellConstraints cc = new CellConstraints();
        add(field, cc.xy(1, 1));
        add(browse, cc.xy(2, 1));
    }

    @Override
    public ResourceLocation getResourceLocation() throws IOException {
        String text = field.getText();

        LocationFactory.Location location = LocationFactory.Location.LOCAL_FS;
        if (text.startsWith("http")) {
            location = LocationFactory.Location.HTTP;
        } else if (text.startsWith("ftp")) {
            location = LocationFactory.Location.FTP;
        }

        LocationFactory.Compression compression = LocationFactory.Compression.NONE;
        if (text.endsWith("gz")) {
            compression = LocationFactory.Compression.GZIP_ARCHIVE;
        } else if (text.endsWith("zip")) {
            compression = LocationFactory.Compression.ZIP_ARCHIVE;
        }

        return factory.newFileLocation(text, compression, location);

    }


    @Override
    public void setup(LocationDescription description) {
        field.setText(description.hasDefault() ? description.getDefault().toString() : "");
    }

}
