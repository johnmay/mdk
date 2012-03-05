package uk.ac.ebi.render.resource;

import com.jgoodies.forms.layout.*;
import uk.ac.ebi.caf.component.CalloutDialog;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.render.resource.location.DirectoryLocationEditor;
import uk.ac.ebi.render.resource.location.FileLocationEditor;
import uk.ac.ebi.render.resource.location.LocationEditor;
import uk.ac.ebi.service.ResourceLoader;
import uk.ac.ebi.service.location.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ResourceLoaderConfig extends CalloutDialog {

    private ResourceLoader loader;
    private LocationFactory factory;
    private Map<String, LocationEditor> fieldMap;

    public ResourceLoaderConfig(final Window window,
                                ResourceLoader loader,
                                LocationFactory factory) {

        super(window);
        this.loader = loader;

        JComponent panel = getMainPanel();
        FormLayout layout = new FormLayout("right:min, p");
        CellConstraints cc = new CellConstraints();
        panel.setLayout(layout);
        fieldMap = new HashMap<String, LocationEditor>();


        for (Map.Entry<String, LocationDescription> e : loader.getRequiredResources().entrySet()) {


            layout.appendRow(new RowSpec(Sizes.PREFERRED));
            panel.add(LabelFactory.newFormLabel(e.getValue().getName(), e.getValue().getDescription()), cc.xy(1, layout.getRowCount()));

            LocationEditor editor = null;

            Class c = e.getValue().getType();
            if (c.equals(ResourceFileLocation.class)) {
                editor = new FileLocationEditor(factory);
                fieldMap.put(e.getKey(), editor);
                editor.setup(e.getValue());
            } else if (c.equals(ResourceDirectoryLocation.class)) {
                editor = new DirectoryLocationEditor(factory);
                fieldMap.put(e.getKey(), editor);
                editor.setup(e.getValue());
            }

            panel.add((JComponent) editor, cc.xy(1, layout.getRowCount()));

        }

        pack();


    }


    public void configure() {

        List<ResourceLocation> locationList = new ArrayList<ResourceLocation>();

        for (Map.Entry<String, LocationEditor> e : fieldMap.entrySet()) {

            String key = e.getKey();

            try {
                // replace with individual UI components for selecting
                loader.addLocation(key, e.getValue().getResourceLocation());
            } catch (IOException ex) {

            }


        }


    }


}
