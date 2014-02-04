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
*/ /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.matrix;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.metabolite.CompartmentalisedMetabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

import java.util.ArrayList;
import java.util.List;


/** @author johnmay */
public class DefaultStoichiometricMatrix
        extends StoichiometricMatrixImpl<CompartmentalisedMetabolite, String> {

    protected DefaultStoichiometricMatrix() {
    }


    protected DefaultStoichiometricMatrix(int n, int m) {
        super(n, m);
    }


    @Override
    public DefaultStoichiometricMatrix init() {
        return (DefaultStoichiometricMatrix) super.init();
    }


    public static DefaultStoichiometricMatrix create() {
        return new DefaultStoichiometricMatrix().init();
    }


    public static DefaultStoichiometricMatrix create(int n, int m) {
        return new DefaultStoichiometricMatrix(n, m).init();
    }

    public static DefaultStoichiometricMatrix create(Reconstruction recon) {
        DefaultStoichiometricMatrix s = new DefaultStoichiometricMatrix(recon.metabolome().size(), recon.reactome().size()).init();
        for (MetabolicReaction rxn : recon.reactome())
            s.addReaction(rxn);
        return s;
    }


    @Override
    public Class<? extends String> getReactionClass() {
        return String.class;
    }


    @Override
    public Class<? extends CompartmentalisedMetabolite> getMoleculeClass() {
        return CompartmentalisedMetabolite.class;
    }


    public int addReaction(MetabolicReaction reaction) {
        return addReaction(reaction.getAccession(),
                           getMetabolites(reaction),
                           getStoichiometries(reaction),
                           ((Direction) reaction.getDirection()).isReversible());
    }


    public CompartmentalisedMetabolite[] getMetabolites(MetabolicReaction rxn) {

        List<CompartmentalisedMetabolite> list = new ArrayList<CompartmentalisedMetabolite>();
        for (MetabolicParticipant p : rxn.getParticipants()) {
            list.add(new CompartmentalisedMetabolite(p.getMolecule(), p.getCompartment()));
        }

        return list.toArray(new CompartmentalisedMetabolite[0]);
    }


    public Double[] getStoichiometries(MetabolicReaction rxn) {

        Double[] coefs = new Double[rxn.getParticipants().size()];
        
        int fact = rxn.getDirection() == Direction.BACKWARD ? -1 : 1; 
        
        int i = 0;
        for (Participant<?, Double> p : rxn.getReactants()) {
            coefs[i++] = fact * -p.getCoefficient();
        }
        for (Participant<?, Double> p : rxn.getProducts()) {
            coefs[i++] = fact * +p.getCoefficient();
        }

        return coefs;
    }


    @Override
    public StoichiometricMatrixImpl<CompartmentalisedMetabolite, String> newInstance() {
        return DefaultStoichiometricMatrix.create();
    }


    @Override
    public StoichiometricMatrixImpl<CompartmentalisedMetabolite, String> newInstance(int n, int m) {
        return DefaultStoichiometricMatrix.create(n, m);
    }
}
