/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.io.text.kegg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author pmoreno
 * @deprecated needs clean up using KEGGReactionField's and AttributedEntry similar to KEGGCompoundParser
 */
@Deprecated
public class KEGGReactionParser {

    private BufferedReader reader;
    private String[] nextEntry;
    private boolean thereIsANextEntry;
    private final String entrySep = "///";
    private Logger logger;
    private Pattern pathwayPat;
    private Pattern rpairPat;

    private void init() {
        logger = Logger.getLogger(KEGGReactionParser.class.getName());
        moveToNextEntry();
        pathwayPat = Pattern.compile("PATH: (rn\\d+)");
        rpairPat = Pattern.compile("RP:\\s+(RP\\d+)\\s+(\\S+_\\S+)\\s+(\\S+)");
    }

    private void moveToNextEntry() {
        try {
            this.thereIsANextEntry = this.readNextEntry();
        } catch (IOException ex) {
            logger.warn("Could not load following entry", ex);
            this.thereIsANextEntry = false;
        }
    }

    private enum ReactionFilePrefixes {

        ENTRY, NAME, DEFINITION, EQUATION, RPAIR, COMMENT, ENZYME, PATHWAY;
    }

    public KEGGReactionParser(String reaction_file_path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(reaction_file_path));
        init();
    }

    public KEGGReactionParser(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
        init();
    }

    private boolean readNextEntry() throws IOException {
        String line = reader.readLine();
        if(line==null)
            return false;
        ArrayList<String> lines = new ArrayList<String>();
        while (!line.startsWith(entrySep)) {
            lines.add(line);
            line = reader.readLine();
        }
        this.nextEntry = lines.toArray(new String[lines.size()]);
        return true;
    }

    public boolean hasNextEntry() {
        return this.thereIsANextEntry;
    }

    public KEGGReactionEntry getNextEntry() {
        if(!thereIsANextEntry)
            return null;
        KEGGReactionEntry entry = new KEGGReactionEntry();
        int lineNum = 0;
        while (lineNum < nextEntry.length) {
            if(lineNum < nextEntry.length && nextEntry[lineNum].startsWith(" "))
                ++lineNum;
            if (lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.ENTRY.name())) {
                //ENTRY       R00002                      Reaction
                String tokens[] = nextEntry[lineNum].split("\\s+");
                if (tokens != null && tokens.length > 2) {
                    entry.setReactionID(tokens[1]);
                    lineNum++;
                } else {
                    return null;
                }
            } else if (lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.NAME.name())) {
                //NAME        Reduced ferredoxin:dinitrogen oxidoreductase (ATP-hydrolysing)
                String name = nextEntry[lineNum].replaceFirst(ReactionFilePrefixes.NAME.name() + "\\s+", "");
                if (name != null && name.length() > 0) {
                    entry.setName(name);
                }
                lineNum++;
            } else if (lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.DEFINITION.name())) {
//                //DEFINITION  16 ATP + 16 H2O + 8 Reduced ferredoxin <=> 8 e- + 16 Orthophosphate
//                //            + 16 ADP + 8 Oxidized ferredoxin
//                String definitionLines = this.handleMultiLineField(lineNum);
//                definitionLines = definitionLines.replaceFirst(ReactionFilePrefixes.DEFINITION.name() + "\\s+", "").trim();
//                String[] eqSides = this.parseReaction(definitionLines);
//                // Parse reactants2coeff
//                HashMap<String, Integer> reactants = this.parseReactionSide(eqSides[0], -1);
//                // Parse products
//                HashMap<String, Integer> products = this.parseReactionSide(eqSides[1], 1);
                lineNum++;
            } else if(lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.EQUATION.name())) {
                String eqLines = this.handleMultiLineField(lineNum);
                eqLines = eqLines.replaceFirst(ReactionFilePrefixes.EQUATION.name() + "\\s+", "").trim();
                String[] eqSides = this.parseReaction(eqLines);
                // Parse reactants2coeff
                HashMap<String, Integer> reactants2coeff = this.parseReactionSide(eqSides[0], -1);
                for(String specie : reactants2coeff.keySet())
                    entry.addCompCoeff(specie, reactants2coeff.get(specie));
                // Parse products
                HashMap<String, Integer> products2coeff = this.parseReactionSide(eqSides[1], 1);
                for(String specie : products2coeff.keySet())
                    entry.addCompCoeff(specie, products2coeff.get(specie));
                lineNum++;
            } else if(lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.ENZYME.name())) {
                String enzymeLines = this.handleMultiLineField(lineNum);
                String[] ecsTokens = enzymeLines.replaceFirst(ReactionFilePrefixes.ENZYME.name()+" +", "").trim().split(" ");
                for(String ec : ecsTokens)
                    entry.addEcNumber(ec);
                lineNum++;
            } else if(lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.PATHWAY.name())) {
//PATHWAY     PATH: rn00010  Glycolysis / Gluconeogenesis
//            PATH: rn00020  Citrate cycle (TCA cycle)
//            PATH: rn00290  Valine, leucine and isoleucine biosynthesis
//            PATH: rn00620  Pyruvate metabolism
//            PATH: rn00650  Butanoate metabolism

                String pathwayLines = this.handleMultiLineField(lineNum);
                Matcher pathwayLinesMatcher = pathwayPat.matcher(pathwayLines);
                while(pathwayLinesMatcher.find()) {
                    if(pathwayLinesMatcher.groupCount()==1)
                        entry.addPathway(pathwayLinesMatcher.group(1));
                }
                lineNum++;
            } else if(lineNum < nextEntry.length && nextEntry[lineNum].startsWith(ReactionFilePrefixes.RPAIR.name())) {
                String rpairLines = this.handleMultiLineField(lineNum);
                Matcher rpairLinesMatcher = rpairPat.matcher(rpairLines);
                while(rpairLinesMatcher.find()) {
                    if(rpairLinesMatcher.groupCount() == 3) {
                        entry.addRpair(rpairLinesMatcher.group(1), rpairLinesMatcher.group(2)+"\t"+rpairLinesMatcher.group(3));
                    }
                }
                lineNum++;
            } else {
                lineNum++;
            }
        }
        moveToNextEntry();
        return entry;
    }

    private String handleMultiLineField(int lineNum) {
        String categoryLines = nextEntry[lineNum++];
        while (lineNum < nextEntry.length && nextEntry[lineNum].startsWith(" ")) {
            categoryLines += " " + nextEntry[lineNum++].trim();
        }
        return categoryLines.trim();
    }
// TODO pass the reaction responsable, so that any error can be logged to the reaction WID.
    private HashMap<String, Integer> parseReactionSide(String side, int sign) {
        String[] coeffSpecies = side.split(" \\+ ");
        HashMap<String, Integer> compCoeff = new HashMap<String, Integer>();
        for (String coeffSpecie : coeffSpecies) {
            String[] aux = coeffSpecie.split(" +");
            if (aux.length > 1) {
                Integer coeff=null;
                try {
                    coeff = Integer.parseInt(aux[0].trim());
                    compCoeff.put(aux[1].trim(), (int) Math.signum(sign) * coeff);
                } catch(NumberFormatException ex) {
                    logger.error("Tried to parse text that cannot be converted to number:\n"+side, ex);
                    // this symbolizes an error in the equation;
                    compCoeff.put(coeffSpecie.trim(), 0);
                }
            } else {
                compCoeff.put(coeffSpecie.trim(), (int) Math.signum(sign));
            }
        }
        return compCoeff;
    }

    private String[] parseReaction(String reactionLine) {
        String[] sides = reactionLine.split("<=>");
        if (sides.length == 2) {
            sides[0] = sides[0].trim();
            sides[1] = sides[1].trim();
            return sides;
        } else {
            logger.warn("Could not parse reaction line: " + reactionLine);
            return null;
        }
    }

    
}
