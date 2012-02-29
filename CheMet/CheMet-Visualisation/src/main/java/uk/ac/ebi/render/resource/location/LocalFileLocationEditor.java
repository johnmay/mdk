package uk.ac.ebi.render.resource.location;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.CheckBoxFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.chemet.service.loader.location.*;
import uk.ac.ebi.service.location.LocationDescription;
import uk.ac.ebi.service.location.LocationFactory;
import uk.ac.ebi.service.location.ResourceLocation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * RemoteLocationEditor - 27.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LocalFileLocationEditor
        extends JComponent
        implements LocationEditor {

    private static final Logger LOGGER = Logger.getLogger(LocalFileLocationEditor.class);

    private JTextField field = FieldFactory.newField(20);
    private JButton browse;
    private JCheckBox zipped = CheckBoxFactory.newCheckBox("ZIP");
    private JCheckBox gzipped = CheckBoxFactory.newCheckBox("GZIP");

    private LocationFactory factory;

    public LocalFileLocationEditor(LocationFactory factory) {

        this.factory = factory;

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        final JComponent component = this;

        browse = ButtonFactory.newButton(new AbstractAction("Browse") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int choice = chooser.showOpenDialog(component);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    field.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        add(field);
        add(browse);
        add(zipped);
        add(gzipped);


        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getCompression();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getCompression();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getCompression();
            }
        });

        zipped.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (zipped.isSelected()) {
                    gzipped.setSelected(false);
                }
            }
        });

        gzipped.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (gzipped.isSelected()) {
                    zipped.setSelected(false);
                }
            }
        });


    }

    @Override
    public void setup(LocationDescription description) {
        if (description.hasDefault()) {
            field.setText(description.getDefault().toString());
        }
    }

    public LocationFactory.Compression getCompression() {
        String text = field.getText().trim();
        if (text.endsWith(".zip")) {
            zipped.setSelected(true);
            return LocationFactory.Compression.ZIP_ARCHIVE;
        } else if (text.endsWith(".gz")) {
            gzipped.setSelected(true);
            return LocationFactory.Compression.GZIP_ARCHIVE;
        } else {
            zipped.setSelected(false);
            gzipped.setSelected(false);
            return LocationFactory.Compression.NONE;
        }
    }

    @Override
    public ResourceLocation getResourceLocation() throws IOException {

        String text = field.getText().trim();

        if (zipped.isSelected()) {
            return factory.newFileLocation(text, LocationFactory.Compression.ZIP_ARCHIVE, LocationFactory.Location.LOCAL_FS);
        }
        if (gzipped.isSelected()) {
            return factory.newFileLocation(text, LocationFactory.Compression.GZIP_ARCHIVE, LocationFactory.Location.LOCAL_FS);
        }
        return factory.newFileLocation(text, LocationFactory.Compression.NONE, LocationFactory.Location.LOCAL_FS);
    }
}
