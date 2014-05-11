/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.apps.tool;

import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.Subsystem;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John May
 */
public final class FixXtSuffix {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String path = "/Users/johnmay/repo/iAbalhimycina1327.mr";
        Reconstruction reconstruction = ReconstructionIOHelper.read(new File(path));

        for (MetabolicReaction rxn : reconstruction.reactome()) {
            for (Subsystem subsystem : rxn.getAnnotations(Subsystem.class)) {
                subsystem.setValue(subsystem.getValue().replaceAll("#\\s+", ""));
            }
        }
        
        
        Map<String, Metabolite> abrvMap = new HashMap<String, Metabolite>();
        for (Metabolite m : reconstruction.metabolome()) {
            if (abrvMap.put(m.getAbbreviation(), m) != null)
                System.err.println(m.getAbbreviation() + " already added");
        }

        Set<Metabolite> remove = new HashSet<Metabolite>();
        
        for (Metabolite org : reconstruction.metabolome()) {
            if (org.getAbbreviation().endsWith("xt")) {
                String abrv = org.getAbbreviation();
                
                abrv = abrv.substring(0, abrv.length() - 2);
                Metabolite rep = abrvMap.get(abrv);
                
                if (rep == null) {
                    System.out.println(abrv + " not found for " + org.getAbbreviation() + ", " + org.getName() + " could be involved in PTS transport");
                    String adjustedName = org.getName().replaceAll("\\((external|extracellular)\\)", "");
                    if (adjustedName.equals(org)) {
                        continue;
                    } else {
                        org.setName(adjustedName.trim());
                        org.setAbbreviation(abrv);
                        System.out.println("renamed to: " + org.getAbbreviation() + ", " + org);
                    }
                } else {
                    remove.add(org);
                }

                for (MetabolicReaction rxn : reconstruction.participatesIn(org)) {
                    for (MetabolicParticipant participant : rxn.getParticipants()) {
                        if (participant.getMolecule() == org) {
                            participant.setCompartment(Organelle.EXTRACELLULAR);
                            if (rep != null) {
                                participant.setMolecule(rep);
                            }
                        }
                    }
                }
            }
        }

        ReconstructionIOHelper.write(reconstruction, new File(path));
    }

}
