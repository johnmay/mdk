/**
 * FetchMetabolicModelsMain.java
 *
 * 2011.07.21
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.execs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;
import javax.wsdl.PortType;
import javax.xml.rpc.ServiceException;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.biomodels.BioModelsWebServices;
import uk.ac.ebi.biomodels.BioModelsWebServicesServiceLocator;

import uk.ac.ebi.metabolomes.execs.CommandLineMain;

/**
 * @name    FetchMetabolicModelsMain
 * @date    2011.07.21
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Downloads metabolic models from BioModels
 *
 */
public class FetchMetabolicModelsMain
        extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(FetchMetabolicModelsMain.class);

    public static void main(String[] args) {
        new FetchMetabolicModelsMain(args).process();
    }

    public FetchMetabolicModelsMain(String[] args) {
        super(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("o", "output-directory", true, "The output directory of the SBML models"));
    }

    @Override
    public void process() {
        File outputDirectory = null;
        if (getCmd().hasOption("o")) {
            outputDirectory = new File(getCmd().getOptionValue("o"));
            if (outputDirectory.exists() == false) {
                outputDirectory.mkdirs();
            }
        } else {
            printHelp();
            System.exit(1);
        }


        BioModelsWebServices service = null;
        try {
            service = new BioModelsWebServicesServiceLocator().getBioModelsWebServices();
        } catch (ServiceException ex) {
            LOGGER.error("Could not get BioModels WebService");
        }
        try {
            String[] ids = service.getAllModelsId();

            Pattern metabolicPattern = Pattern.compile("metab|FBA|flux", Pattern.CASE_INSENSITIVE);


            for (String id : ids) {
                String name = service.getModelNameById(id);
                if (metabolicPattern.matcher(name).find()) {
                    String model = service.getModelById(id);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputDirectory, name
                                                                                                        + ".xml")));
                    writer.write(model);
                    writer.close();
                }
            }


        } catch (Exception ex) {
            LOGGER.error("Error fetching models: " + ex.getMessage());
        }
    }
}
