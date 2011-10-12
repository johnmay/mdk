/**
 * BLASTHomologySearch.java
 *
 * 2011.10.10
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
package uk.ac.ebi.chemet.io.external;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.io.blast.BlastReader;
import uk.ac.ebi.observation.parameters.TaskOption;

/**
 * @name    BLASTHomologySearch - 2011.10.10 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BLASTHomologySearch extends RunnableTask {

    private static final Logger LOGGER = Logger.getLogger(BLASTHomologySearch.class);
    private Map<String, GeneProduct> map;
    private String cmd;

    /**
     *
     * @param options
     * @param accessionMap A HashMap of query accessions to corresponding products
     */
    public BLASTHomologySearch(TaskOptions options, Map<String, GeneProduct> map) {
        super(options);
        this.map = map;
    }

    @Override
    public void prerun() {

        cmd = getCommand();
        LOGGER.info("executing: " + cmd);

    }

    @Override
    public void run() {


        setRunningStatus();

        try {

            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            setCompletedStatus();

        } catch (InterruptedException ex) {
            setErrorStatus();
            LOGGER.info("InterruptedException... add to messages");
        } catch (IOException ex) {
            setErrorStatus();
            LOGGER.info("IO Exception... add to messages");
        }

    }

    @Override
    public void postrun() {
        try {
            TaskOption outputOption = (TaskOption) getOptions().getOptionMap().get("out");
            File output = new File(outputOption.getValue());
            Integer format = Integer.parseInt((String) ((TaskOption) getOptions().getOptionMap().get("outfmt")).getValue());

            String version = Preferences.userNodeForPackage(HomologySearchFactory.class).get("blastp.version", "");

            System.out.println("format:" + format);
            // load results into object
            new BlastReader().load(map, output, format, version, getOptions());

        } catch (Exception ex) {
            LOGGER.info("An error occured: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
