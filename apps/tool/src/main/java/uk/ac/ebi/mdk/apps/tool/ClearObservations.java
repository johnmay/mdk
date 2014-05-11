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
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.io.File;
import java.io.IOException;

/**
 * @author John May
 */
public class ClearObservations {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        File f = new File(args[0]);

        Reconstruction recon = ReconstructionIOHelper.read(f);
        
        for (Metabolite m : recon.metabolome())
            m.clearObservations();
        for (MetabolicReaction r : recon.reactome())
            r.clearObservations();
        
        ReconstructionIOHelper.writeSafe(recon, f);
    }
    
}
