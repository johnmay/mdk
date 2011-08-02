/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import uk.ac.ebi.metabolomes.bioObjects.Reaction;

/**
 *
 * @author pmoreno
 */
public class RELineParser extends AbstractReactionLineParser {

    public RELineParser(BufferedReader reader) {
        this.reader = reader;
        this.rxn = new Reaction();
        this.startingPrefix = "RE\t";
    }

    /*private void resolveIncomplete() {
        
    }*/

}
