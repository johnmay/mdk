package uk.ac.ebi.mdk.ui.component.service.location;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.mdk.service.location.LocationDescription;
import uk.ac.ebi.mdk.service.location.LocationFactory;
import uk.ac.ebi.mdk.service.location.ResourceLocation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * DirectoryLocationEditor - 27.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DirectoryLocationEditor extends JComponent implements LocationEditor{

    private static final Logger LOGGER = Logger.getLogger(DirectoryLocationEditor.class);

    private JTextField field = FieldFactory.newField(20);
    private JComponent component;
    private JButton browse = ButtonFactory.newButton(new AbstractAction("Browse") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choice = chooser.showOpenDialog(component);
            if(choice == JFileChooser.APPROVE_OPTION){
                field.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    });

    private LocationFactory factory;
    
    public DirectoryLocationEditor(LocationFactory factory){
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(field);
        add(browse);
        component = this;
        this.factory = factory;
    }
    
    @Override
    public ResourceLocation getResourceLocation() throws IOException {
        return factory.newDirectoryLocation(field.getText(), LocationFactory.Compression.NONE, LocationFactory.Location.LOCAL_FS);
    }

    @Override
    public void setup(LocationDescription description) {
        if(description.hasDefault()){
            field.setText(description.getDefault().toString());
        }
    }
}
