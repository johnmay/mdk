package uk.ac.ebi.chemet.service.loader.location;

import org.apache.log4j.Logger;
import uk.ac.ebi.service.location.LocationDescription;
import uk.ac.ebi.service.location.ResourceLocation;

import java.util.regex.Pattern;

/**
 * DefaultLocationDescription - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class DefaultLocationDescription implements LocationDescription {

    private static final Logger LOGGER = Logger.getLogger(DefaultLocationDescription.class);

    private String key;
    private String name;
    private String description;
    private Class<? extends ResourceLocation> c;
    private ResourceLocation defaultLocation;


    // key making pattern
    private static final Pattern SPACE_MATCHER = Pattern.compile("\\s+");

    public <T extends ResourceLocation> DefaultLocationDescription(String name,
                                                                   String description,
                                                                   Class<T> c,
                                                                   T defaultLocation) {
        this.name = name;
        this.description = description;
        this.c = c;
        this.defaultLocation = defaultLocation;
        this.key = createKey(name);
    }

    public DefaultLocationDescription(String name, String description, Class<? extends ResourceLocation> c) {
        this.name = name;
        this.description = description;
        this.c = c;
        this.key = createKey(name);
    }

    public static String createKey(String name) {
        return SPACE_MATCHER.matcher(name).replaceAll(".");
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Class<? extends ResourceLocation> getType() {
        return c;
    }

    @Override
    public boolean hasDefault() {
        return defaultLocation != null;
    }

    @Override
    public ResourceLocation getDefault() {
        return defaultLocation;
    }
}
