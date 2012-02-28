package uk.ac.ebi.render.resource.location;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.CheckBoxFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.chemet.service.loader.location.*;
import uk.ac.ebi.service.location.LocationDescription;
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


    public LocalFileLocationEditor() {
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
                suggestCompression();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                suggestCompression();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                suggestCompression();
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

    public void suggestCompression() {
        String text = field.getText().trim();
        if (text.endsWith(".zip")) {
            zipped.setSelected(true);
        } else if (text.endsWith(".gz")) {
            gzipped.setSelected(true);
        } else {
            zipped.setSelected(false);
            gzipped.setSelected(false);
        }
    }

    @Override
    public ResourceLocation getResourceLocation() throws IOException {

        String text = field.getText().trim();

        if (zipped.isSelected()) {
            return new ZIPSystemLocation(text);
        }
        if (gzipped.isSelected()) {
            return new GZIPSystemLocation(text);
        }
        return new SystemLocation(text);
    }
}
