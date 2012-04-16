/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ebi.metabolomes.bioObjects.Reaction;

/**
 *
 * @author pmoreno
 */
public abstract class AbstractReactionLineParser extends LineParser{

    protected Reaction rxn;
    protected String startingPrefix;
    private final Pattern coeffNamePattern = Pattern.compile("([\\d|,]+)\\s(.+)");

    @Override
    public String parse(String line) throws IOException {
        boolean keepParsing = true;

        boolean leftSideComplete = false;
        boolean rightSideComplete = false;

        String rightSideBuffer = "";
        String leftSideBuffer = "";
        while (keepParsing) {
            if (line.startsWith(this.startingPrefix)) {
                line = line.replace(this.startingPrefix, "");
                String[] tokens = line.split("=");
                if (tokens.length >= 2 && line.contains("(#")) {
                    // easy case were the rxn is in one line only
                    this.parseRxnSide(tokens[0].trim(), -1);
                    this.parseRxnSide(tokens[1].trim(), 1);
                    leftSideComplete = true;
                    rightSideComplete = true;
                    if (rxn == null) {
                        return null;
                    }
                } else if (tokens.length >= 2 && !line.contains("(#")) {
                    // right hand side is interrupted;
                    this.parseRxnSide(tokens[0], -1);
                    leftSideComplete = true;
                    rightSideBuffer = tokens[1];
                } else if(tokens.length == 1 && line.endsWith(" =")) {
                    this.parseRxnSide(tokens[0], -1);
                    leftSideComplete = true;
                    rightSideBuffer = "";
                } else if (tokens.length == 1) {
                    // left hand side is interrupted;
                    leftSideBuffer = tokens[0];
                }
                line = reader.readLine();

                if (line.length() < 2 || line.startsWith(this.startingPrefix)) {
                    // if we are dealing with an incomplete reaction
                    // but we reach an empty line, then we wrap up everything as
                    // it is in the reaction object and return the line.
                    if(!leftSideComplete) {
                        this.parseRxnSide(leftSideBuffer, -1);
                        leftSideComplete = true;
                        leftSideBuffer = "";
                    }
                    if(!rightSideComplete) {
                        this.parseRxnSide(rightSideBuffer, 1);
                        rightSideComplete=true;
                        rightSideBuffer="";
                    }
                    //this.resolveIncomplete();
                    return line;
                }
            } else if (line.startsWith("\t")) {
                if (!leftSideComplete) {
                    if (line.startsWith("\t=")) {
                        this.parseRxnSide(leftSideBuffer, -1);
                        leftSideComplete = true;
                        leftSideBuffer = "";

                        rightSideBuffer = line.substring(2);

                    } else if (line.endsWith(" =")) {
                        // we only have left hand side portion here:
                        leftSideBuffer = this.completeReactionSideText(leftSideBuffer, line);
                        this.parseRxnSide(leftSideBuffer, -1);
                        leftSideComplete=true;
                        leftSideBuffer="";
                    } else if(line.contains(" = ")) {
                        String tokenSides[] = line.split(" = ");
                        leftSideBuffer = this.completeReactionSideText(leftSideBuffer, tokenSides[0]);
                        this.parseRxnSide(leftSideBuffer, -1);
                        leftSideBuffer="";
                        leftSideComplete=true;

                        if(tokenSides.length>=2)
                            rightSideBuffer = tokenSides[1];
                    } else {
                        leftSideBuffer = this.completeReactionSideText(leftSideBuffer, line);
                    }
                    line = reader.readLine();
                }
                else if(!rightSideComplete) {

                    if(line.length()<2) {
                        this.parseRxnSide(rightSideBuffer, 1);
                        rightSideBuffer="";
                        rightSideComplete=true;
                    } else if(line.contains("(#")){
                        rightSideBuffer = this.completeReactionSideText(rightSideBuffer, line);
                        this.parseRxnSide(rightSideBuffer, 1);
                        rightSideBuffer="";
                        rightSideComplete=true;
                    } else {
                        rightSideBuffer = this.completeReactionSideText(rightSideBuffer, line);
                    }
                    line = reader.readLine();
                }
            }
            if(line.startsWith(this.startingPrefix) || line.length()<2) {
                if(!leftSideComplete) {
                        this.parseRxnSide(leftSideBuffer, -1);
                        leftSideComplete = true;
                        leftSideBuffer = "";
                    }
                    if(!rightSideComplete) {
                        this.parseRxnSide(rightSideBuffer, 1);
                        rightSideComplete=true;
                        rightSideBuffer="";
                    }
                return line;
            }
            if(line.startsWith("\t") && rightSideComplete && leftSideComplete)
                return reader.readLine();
            else if(line.length() <2 && rightSideComplete && leftSideComplete)
                return reader.readLine();

        }
       return null;
    }

    private String completeReactionSideText(String sideBuffer, String line) {
        line = line.substring(1);
        if (sideBuffer.endsWith(" a")) {
            return sideBuffer + " " + line;
        } else if (sideBuffer.endsWith(" +")) {
            return sideBuffer + " " + line;
        } else if (line.startsWith("+ ")) {
            return sideBuffer + " " + line;
        } else if (line.startsWith("(#")) {
            return sideBuffer;
        } else if (Pattern.compile("\\d$").matcher(sideBuffer).find()
                && Pattern.compile("^\\d").matcher(line).find()) {
            return sideBuffer + "," + line;
        }
        return sideBuffer + " " + line;
    }

    private void parseRxnSide(String line, Integer coeff) {
        if (line.contains("(#")) {
            line = line.substring(0, line.indexOf("(#"));
        }

        String coeffMolTokens[] = line.split(" \\+ ");
        for (String coeffMol : coeffMolTokens) {
            coeffMol = this.tidyUpMetaboliteName(coeffMol);
            Matcher coeffNameMatcher = coeffNamePattern.matcher(coeffMol);
            float coefficient = (float) 1.0;
            String metabName = coeffMol;
            if (coeffNameMatcher.find()) {
                try {
                    coefficient = Float.parseFloat(coeffNameMatcher.group(1));
                } catch(NumberFormatException e) {
                    System.err.println("Error with line:"+line);
                    coefficient = (float)0.0;
                }
                metabName = coeffNameMatcher.group(2).trim();
            }
            if (coeff < 0) {
                this.rxn.addReactant(metabName, coefficient);
            } else {
                this.rxn.addProduct(metabName, coefficient);
            }
        }
    }

    private Pattern citationsInMetabName = Pattern.compile(" <[\\d,]+");
    private String tidyUpMetaboliteName(String coeffMol) {
        coeffMol = coeffMol.trim();
        coeffMol = coeffMol.replace("=", "");


        if(this.citationsInMetabName.matcher(coeffMol).find()) {
            coeffMol = coeffMol.substring(0,this.citationsInMetabName.matcher(coeffMol).regionStart());
        }

        return coeffMol;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the rxn
     */
    public Reaction getRxn() {
        return rxn;
    }

}
