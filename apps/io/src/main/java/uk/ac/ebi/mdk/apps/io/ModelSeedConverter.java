/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.apps.io;

import org.apache.commons.cli.Option;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.io.text.seed.ModelSeedCompound;
import uk.ac.ebi.mdk.io.text.seed.ModelSeedCompoundInput;
import uk.ac.ebi.mdk.io.text.seed.ModelSeedReaction;
import uk.ac.ebi.mdk.io.text.seed.ModelSeedReactionInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author John May */
public final class ModelSeedConverter extends CommandLineMain {

    final EntityFactory           entities    = DefaultEntityFactory
            .getInstance();
    final Map<String, Metabolite> metabolites = new HashMap<String, Metabolite>();

    @Override
    public void setupOptions() {
        add(new Option("r", true, "reactions (TSV) from model-SEED"));
        add(new Option("c", true, "compounds (TSV) from model-SEED"));
        add(new Option("o", true, "output model"));
    }

    @Override public void process() {
        try {

            loadMetabolites();

            List<ModelSeedReaction> rxns = ModelSeedReactionInput
                    .fromPath(getFile("r").getPath());

            Reconstruction recon = entities.newReconstruction();
            recon.setIdentifier(new ReconstructionIdentifier("model-SEED"));
            recon.setTaxonomy(new Taxonomy());

            System.out.println("processing reactions...");
            for (ModelSeedReaction rxn : rxns) {
                try {
                    recon.addReaction(reaction(rxn));
                } catch (Exception e) {
                    System.out.println(e.getMessage() + ": " + rxn.equation());
                }
            }

            System.out.println("writing reaconstruction");
            ReconstructionIOHelper.write(recon, getFile("o"));

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ModelSeedConverter().process(args);
    }

    private static String normalise(String name) {
        return name.replaceAll("\\s+", "").toLowerCase(Locale.ENGLISH);
    }

    private void loadMetabolites() throws IOException {
        List<ModelSeedCompound> cpds = ModelSeedCompoundInput
                .fromPath(getFile("c")
                                  .getPath());
        for (ModelSeedCompound cpd : cpds) {
            Metabolite m = entities.metabolite();
            m.setIdentifier(BasicChemicalIdentifier.nextIdentifier());
            m.setAbbreviation(cpd.id());
            m.setName(cpd.name());
            for (String synonym : cpd.synonyms())
                m.addAnnotation(new Synonym(synonym));
            m.addAnnotation(new MolecularFormula(cpd.formula()));

            for (String cid : cpd.keggIds()) {
                if (cid.startsWith("C"))
                    m.addAnnotation(new KEGGCrossReference<Observation>(new KEGGCompoundIdentifier(cid)));
            }

            metabolites.put(normalise(cpd.name()), m);
            for (String synonym : cpd.synonyms())
                metabolites.put(normalise(synonym), m);
        }
    }

    private MetabolicReaction reaction(ModelSeedReaction modelSEEDRxn) {
        String equation = modelSEEDRxn.equation();
        MetabolicReaction rxn = new MetabolicReactionImpl(BasicReactionIdentifier
                                                                  .nextIdentifier(),
                                                          modelSEEDRxn.id(),
                                                          modelSEEDRxn.name());
        try {
            String[] sides = equation.split("<=>|=>|<=");
            String substrate = sides[0];
            String products = sides[1];

            for (String s : splitParticipants(substrate)) {
                rxn.addReactant(participant(s));
            }
            for (String r : splitParticipants(products)) {
                rxn.addProduct(participant(r));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Could not parse reaction equation: '" + rxn
                    .getAbbreviation() + "'");
        }
        return rxn;
    }

    Pattern coefficient = Pattern.compile("^(\\(?\\d+(?:\\.\\d+)?\\)?)");
    Pattern compartment = Pattern.compile("\\[([A-z])\\]");

    private MetabolicParticipant participant(String str) {


        double coef = 1d;
        String comp = "";

        if (str.isEmpty())
            return null;
        Matcher coefMatcher = coefficient.matcher(str);
        if (coefMatcher.find()) {
            String coefStr = coefMatcher.group(1);
            str = str.substring(coefStr.length()).trim();
            if (coefStr.charAt(0) == '(')
                coefStr = coefStr.substring(1, coefStr.length() - 1);
            coef = Double.parseDouble(coefStr);
        }
        String name = str.trim().substring(1, str.length() - 1);
        Matcher matcher = compartment.matcher(str);
        if (matcher.find() && matcher.end() == str.length() - 1) {
            comp = matcher.group(1);
            name = name.substring(0, matcher.start() - 1);
        }

        return new MetabolicParticipantImplementation(metabolite(name),
                                                      coef,
                                                      compartment(comp, str));
    }

    private static List<String> splitParticipants(String reactionSide) {
        StringBuilder sb = new StringBuilder();
        List<String> participants = new ArrayList<String>();
        boolean inCompound = false;
        for (int i = 0; i < reactionSide.length(); i++) {
            char c = reactionSide.charAt(i);
            if (c == '|') {
                sb.append(c);
                if (inCompound) {
                    participants.add(sb.toString());
                    sb = new StringBuilder();
                }
                inCompound = !inCompound;
            } else if (inCompound) {
                sb.append(reactionSide.charAt(i));
            } else if (c != '+' && (c != ' ' || sb.length() > 0)) {
                sb.append(reactionSide.charAt(i));
            }
        }
        return participants;
    }

    private Compartment compartment(String compartment, String name) {
        if (compartment.isEmpty())
            return Organelle.CYTOPLASM;
        char c = compartment.charAt(0);
        switch (c) {
            case 'e':
                return Organelle.EXTRACELLULAR;
            case 'p':
                return Organelle.PERIPLASM;
            case 'c':
                return Organelle.CYTOPLASM;
            case 'a':
                return Organelle.APICOPLAST;
            case 'g':
                return Organelle.GLYCOSOME;
            case 'm':
                return Organelle.MITOCHONDRION;
            case 'v':
                return Organelle.VACUOLE;
            case 'x':
                return Organelle.PEROXISOME;
            case 'n':
                return Organelle.NUCLEUS;
            case 'h':
                return Organelle.CHLOROPLAST;
            case 'r':
                return Organelle.ENDOPLASMIC_RETICULUM;
            default:
                System.err.println("unknown compartment: " + c);
                return Organelle.CYTOPLASM;
        }
    }

    private Metabolite metabolite(String name) {
        Metabolite m = metabolites.get(normalise(name));
        if (m == null) {
            System.out
                  .println("no previously loaded metabloite by name: " + name);
            m = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(),
                                   name,
                                   name);
            metabolites.put(normalise(name), m);
        }
        return m;
    }

}
