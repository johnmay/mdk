/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author pmoreno
 */
public abstract class LineParser {
    protected String specie;
    protected BufferedReader reader;

    public abstract String parse(String line) throws IOException;
    public abstract void reset();

    /**
     * @return the specie
     */
    public String getSpecie() {
        return specie;
    }

    /**
     * @param specie the specie to set
     */
    public void setSpecie(String specie) {
        this.specie = specie;
    }

    /**
     * @return the reader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * @param reader the reader to set
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }


}
