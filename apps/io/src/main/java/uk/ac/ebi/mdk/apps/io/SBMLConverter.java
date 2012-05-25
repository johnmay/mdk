package uk.ac.ebi.mdk.apps.io;

import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.tool.AutomaticCompartmentResolver;
import uk.ac.ebi.mdk.io.xml.sbml.SBMLReactionReader;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author John May
 */
public class SBMLConverter extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(SBMLConverter.class);

    public static void main(String[] args) {
        new SBMLConverter().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "Input sbml file (.xml)"));
        add(new Option("o", "output", true, "Output Metingear file (.mr)"));
    }

    @Override
    public void process() {

        File input = getFile("i");
        File output = getFile("o");

        try {
            convert(input, output);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void convert(File sbml, File mr) throws IOException, XMLStreamException {

        InputStream in = new FileInputStream(sbml);

        // should be able to change the compartment resolver (e.g. prokaryote)

        SBMLReactionReader reader = new SBMLReactionReader(in,
                                                           DefaultEntityFactory.getInstance(),
                                                           new AutomaticCompartmentResolver());

        Reconstruction reconstruction = DefaultEntityFactory.getInstance().ofClass(Reconstruction.class);

        String id = sbml.getName().substring(0, sbml.getName().indexOf("."));

        reconstruction.setName(id);
        reconstruction.setAbbreviation(id);
        reconstruction.setIdentifier(new ReconstructionIdentifier(id));
        reconstruction.setTaxonomy(new Taxonomy());

        while (reader.hasNext()) {
            try {
                reconstruction.addReaction(reader.next());
            } catch (RuntimeException ex) {
                System.err.println("Skipping reaction... a runtime error occurred" + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Skipping reaction... an error occurred" + ex.getMessage());
            }
        }

        ReconstructionIOHelper.write(reconstruction, mr);

        in.close();


    }


}
