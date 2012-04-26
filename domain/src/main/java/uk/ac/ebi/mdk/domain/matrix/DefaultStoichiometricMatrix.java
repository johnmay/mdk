 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.matrix;

 import uk.ac.ebi.mdk.domain.entity.metabolite.CompartmentalisedMetabolite;
 import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
 import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
 import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
 import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

 import java.util.ArrayList;
 import java.util.List;


 /**
  *
  * @author johnmay
  */
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


     @Override
     public Class<? extends String> getReactionClass() {
         return String.class;
     }


     @Override
     public Class<? extends CompartmentalisedMetabolite> getMoleculeClass() {
         return CompartmentalisedMetabolite.class;
     }


     public int addReaction(MetabolicReaction reaction) {


         return addReaction(reaction.getAbbreviation(),
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
         int i = 0;
         for (Participant<?, Double> p : rxn.getReactants()) {
             coefs[i++] = -p.getCoefficient();
         }
         for (Participant<?, Double> p : rxn.getProducts()) {
             coefs[i++] = +p.getCoefficient();
         }

         return coefs;
     }


     @Override
     public StoichiometricMatrixImpl<CompartmentalisedMetabolite, String> newInstance() {
         throw new UnsupportedOperationException("Not supported yet.");
     }


     @Override
     public StoichiometricMatrixImpl<CompartmentalisedMetabolite, String> newInstance(int n, int m) {
         throw new UnsupportedOperationException("Not supported yet.");
     }
 }
