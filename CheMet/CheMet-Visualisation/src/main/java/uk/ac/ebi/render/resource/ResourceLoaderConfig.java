package uk.ac.ebi.render.resource;

import uk.ac.ebi.caf.component.CalloutDialog;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.DefaultLocationDescription;
import uk.ac.ebi.io.service.loader.ResourceLoader;
import uk.ac.ebi.io.service.loader.location.LocationDescription;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.loader.location.LocationFactory;

import javax.swing.*;
import java.awt.Window;
import java.awt.event.ActionEvent;
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
    private Map<String,JTextField> fieldMap;

    public ResourceLoaderConfig(final Window window, ResourceLoader loader){
        
        super(window);
        this.loader = loader;
        
        JComponent panel = getMainPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        fieldMap = new HashMap<String,JTextField>();

        
        for(Map.Entry<String,LocationDescription> e : loader.getRequiredResources().entrySet() ) {
            
            final JTextField field = FieldFactory.newField(20);
            fieldMap.put(e.getKey(), field);
            
            Box box = Box.createHorizontalBox();

            try{
                field.setText(loader.getLocation(e.getKey()).toString());
            }catch (MissingLocationException ex){
                // do nothing
            }
            
            box.add(LabelFactory.newFormLabel(e.getValue().getName(), e.getValue().getDescription()));
            box.add(Box.createHorizontalStrut(10));
            box.add(field);
            box.add(Box.createHorizontalStrut(10));
            box.add(ButtonFactory.newButton(new AbstractAction("Browse") {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int option = chooser.showOpenDialog(window);
                    if(option == JFileChooser.APPROVE_OPTION){
                        field.setText(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }));
            
            panel.add(box);
        }

        pack();
        
        
        
    }
    
    
    public void configure(){
        
        List<ResourceLocation> locationList = new ArrayList<ResourceLocation>();
        
        for(Map.Entry<String,JTextField> e : fieldMap.entrySet()){
            
            String key   = e.getKey();
            String value = e.getValue().getText().trim();
            
            if(!value.isEmpty()){
                try {
                    
                    try{
                        ResourceLocation location = loader.getLocation(value);
                        if(location.toString().equals(value)){
                            continue;
                        }
                    } catch (MissingLocationException ex){
                        
                    }
                    
                    loader.addLocation(key, LocationFactory.getInstance().newLocation(key, value));

                }catch(IOException ex){
                    
                }
            }
            
        }


    }
    
       
    
    
}
