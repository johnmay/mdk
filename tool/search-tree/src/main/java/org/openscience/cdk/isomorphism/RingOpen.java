package org.openscience.cdk.isomorphism;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.matchers.smarts.SmartsMatchers;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.smarts.parser.SMARTSParser;

/**
 * A simple utility for opening potentially turmeric rings. The method defines a
 * pattern (SMARTS) of a baldwin's favourable ring opening.
 *
 * Currently only a single rule is encoded for hemiacetal opening.
 *
 * @author John May
 * @see <a href="http://www.daylight.com/meetings/emug99/Delany/taut_html/sld036.htm">Daylight
 *      EMUG '99</a>
 */
public class RingOpen {

    // hemiacetal opening
    private final static Pattern pat1 = Pattern.findSubstructure(sma("[OH][C;r5,r6](*)[O,N]*"));

    /**
     * Open the ring in the provided container.
     *
     * @param org input structure
     */
    static void open(IAtomContainer org) {

        // init invariants
        SmartsMatchers.prepare(org, true);

        int[] mapping = pat1.match(org);

        if (mapping.length == 0)
            return;

        // move proton (implicit)
        org.getAtom(mapping[0]).setImplicitHydrogenCount(0);
        org.getAtom(mapping[3]).setImplicitHydrogenCount(org.getAtom(mapping[3]).getImplicitHydrogenCount() + 1);
        org.getBond(org.getAtom(mapping[0]), org.getAtom(mapping[1])).setOrder(IBond.Order.DOUBLE);

        // break bond
        org.removeBond(org.getAtom(mapping[1]), org.getAtom(mapping[3]));
    }

    static IAtomContainer sma(String sma) {
        return SMARTSParser.parse(sma, SilentChemObjectBuilder.getInstance());
    }

}
