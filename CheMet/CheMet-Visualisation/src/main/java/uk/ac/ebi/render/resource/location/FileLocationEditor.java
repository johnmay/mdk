package uk.ac.ebi.render.resource.location;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.chemet.service.loader.location.RemoteLocation;
import uk.ac.ebi.chemet.service.loader.location.SystemLocation;
import uk.ac.ebi.service.location.LocationDescription;
import uk.ac.ebi.service.location.ResourceLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
            implements LocationEditor{

    private static final Logger LOGGER = Logger.getLogger(FileLocationEditor.class);

    private JComboBox selector = ComboBoxFactory.newComboBox("Remote", "Local");

    private LocalFileLocationEditor local = new LocalFileLocationEditor();
    private RemoteFileLocationEditor remote = new RemoteFileLocationEditor();
    private JComponent card;

    public FileLocationEditor() {
        setLayout(new FormLayout("p, p", "p"));
        CellConstraints cc = new CellConstraints();
        add(selector, cc.xy(1,1));

        card = new JPanel(new CardLayout());
        card.add(local, "Local");
        card.add(remote, "Remote");
        card.setBackground(Color.WHITE);
        selector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selection = (String) selector.getSelectedItem();
                show(selection);
            }
        });
        this.add(card, cc.xy(2,1));
    }

    @Override
    public ResourceLocation getResourceLocation() throws IOException {
        String selection = (String) selector.getSelectedItem();
        return selection.equals("Remote") ? remote.getResourceLocation() : local.getResourceLocation();
    }

    private void show(String name){
        CardLayout layout = (CardLayout) card.getLayout();
        layout.show(card, name);
    }
    
    @Override
    public void setup(LocationDescription description) {
        
        ResourceLocation location = description.getDefault();
        if(location instanceof RemoteLocation){
            remote.setup(description);
            selector.setSelectedItem("Remote");
            show("Remote");
        }
        else if(location instanceof SystemLocation){
            local.setup(description);
            selector.setSelectedItem("Local");
            show("Local");
        }
    }
}
