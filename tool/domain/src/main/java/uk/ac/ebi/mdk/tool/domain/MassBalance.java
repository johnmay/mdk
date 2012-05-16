package uk.ac.ebi.mdk.tool.domain;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author John May
 */
public class MassBalance {

    private static final Logger LOGGER = Logger.getLogger(MassBalance.class);

    public enum BalanceType {
        HEAVY_LEFT("Unbalanced - left side is heavier then the right, potentially missing participants on the right"),
        HEAVY_RIGHT("Unbalanced - right side is heavier then the left, potentially missing participants on the left"),
        UNBALANCED("Unbalanced - unclear which side could be missing participants"),
        BALANCED("Balanced"),
        UNKNOWN("Unknown - no metabolites have a formula");

        private String description;

        private BalanceType(String description) {
            this.description = description;
        }


        @Override
        public String toString() {
            return description;
        }
    }

    public static BalanceType getBalanceClassification(MetabolicReaction reaction) {

        Map<String, Double> left = getSymbolMap(reaction.getReactants());
        Map<String, Double> right = getSymbolMap(reaction.getProducts());

        int leftWins = 0;
        int equal = 0;
        int rightWins = 0;

        HashSet<String> elementSet = new HashSet<String>();
        elementSet.addAll(left.keySet());
        elementSet.addAll(right.keySet());

        // e: element, lc: left count, rc: right count
        for (String e : elementSet) {

            double lc = left.containsKey(e) ? left.get(e) : 0d;
            double rc = right.containsKey(e) ? right.get(e) : 0d;

            if (lc == rc) {
                equal++;
            } else if (lc > rc) {
                leftWins++;
            } else if (rc > lc) {
                rightWins++;
            }

        }

        if (leftWins == 0 && rightWins == 0 && equal != 0) {
            return BalanceType.BALANCED;
        } else if (leftWins > 0 && rightWins == 0) {
            return BalanceType.HEAVY_LEFT;
        } else if (rightWins > 0 && leftWins == 0) {
            return BalanceType.HEAVY_RIGHT;
        } else if (rightWins > 0 && leftWins > 0) {
            return BalanceType.UNBALANCED;
        }


        return BalanceType.UNKNOWN;

    }

    /**
     * Constructs a map of symbols to their counts (adjusted with the participant coefficients)
     *
     * @param participants
     *
     * @return
     */
    public static Map<String, Double> getSymbolMap(Collection<MetabolicParticipant> participants) {

        Map<String, Double> map = new HashMap<String, Double>();

        for (MetabolicParticipant participant : participants) {
            Metabolite m = participant.getMolecule();

            for (MolecularFormula formula : m.getAnnotations(MolecularFormula.class)) {
                IMolecularFormula mf = formula.getFormula();

                for (IElement element : MolecularFormulaManipulator.elements(mf)) {

                    String symbol = element.getSymbol();
                    double count = MolecularFormulaManipulator.getElementCount(mf, element) * participant.getCoefficient();

                    if (!map.containsKey(symbol)) {
                        map.put(symbol, 0d);
                    }

                    map.put(symbol,
                            map.get(symbol) + count);

                }

                break;

            }
        }

        return map;

    }

}
