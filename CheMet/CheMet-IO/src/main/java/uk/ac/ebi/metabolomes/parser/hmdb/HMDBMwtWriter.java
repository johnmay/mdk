/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.hmdb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author pmoreno
 */
public class HMDBMwtWriter {


    private BufferedWriter writer;

    private final String HEADER="<mwt>\n<template><z:e sem=\"hmdbid\" ids=\"%1\">%0</z:e></template>\n";
    

    private final String FOOTER=
   "<template>%0</template>\n"+
     "<r><z:[^>]*>(.*</z)!:[^>]*></r>\n"+
     "<r>(([+\\-]|\\+/-|-/\\+)[\\r\\n\\t]*)?[0-9]+([,.][0-9][0-9][0-9])*([.][0-9]*)?[\\r\\n\\t]*(%|[$]|([A-Za-z]+(/[A-Za-z]+)*))?</r>\n"+
     "<r>[Ff]ig(ures?|s?[.])[\\r\\n\\t]*[0-9]+[A-Za-z]*</r>\n"+
     "<r>&lt;/?[A-Za-z_0-9\\-]+[^&gt;]+&gt;</r>\n"+
   "</mwt>";


     
    public HMDBMwtWriter (String fileName) throws IOException {
        writer = new BufferedWriter(new FileWriter(fileName));
       writer.append(HEADER);
    }

    public void closes() throws IOException {
       writer.append(FOOTER);
        writer.close();
    }

    public void writes(String line) throws IOException {
        this.writer.append(line);
        this.writer.newLine();
    }




}
