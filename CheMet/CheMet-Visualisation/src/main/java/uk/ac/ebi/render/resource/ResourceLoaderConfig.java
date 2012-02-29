package uk.ac.ebi.render.resource;

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
                                LocationFactory factory ) {

        super(window);
        this.loader = loader;

        JComponent panel = getMainPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        fieldMap = new HashMap<String, LocationEditor>();


        for (Map.Entry<String, LocationDescription> e : loader.getRequiredResources().entrySet()) {


            Box box = Box.createHorizontalBox();
            //            try{
            //                field.setText(loader.getLocation(e.getKey()).toString());
            //            }catch (MissingLocationException ex){
            //                // do nothing
            //            }

            box.add(LabelFactory.newFormLabel(e.getValue().getName(), e.getValue().getDescription()));
            box.add(Box.createHorizontalStrut(10));

            Class c = e.getValue().getType();
            if (c.equals(ResourceFileLocation.class)) {
                FileLocationEditor editor = new FileLocationEditor(factory);
                fieldMap.put(e.getKey(), editor);
                box.add(editor);
                editor.setup(e.getValue());
            } else if (c.equals(ResourceDirectoryLocation.class)) {
                DirectoryLocationEditor editor = new DirectoryLocationEditor(factory);
                fieldMap.put(e.getKey(), editor);
                box.add(editor);
                editor.setup(e.getValue());
            }


            panel.add(box);
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
