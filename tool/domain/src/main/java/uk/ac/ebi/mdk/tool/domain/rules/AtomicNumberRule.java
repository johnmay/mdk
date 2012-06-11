package uk.ac.ebi.mdk.tool.domain.rules;

import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

class AtomicNumberRule implements ISequenceSubRule<ILigand> {

    /**
     * {@inheritDoc}
     */
    public int compare(ILigand ligand1, ILigand ligand2) {
        return getAtomicNumber(ligand1).compareTo(getAtomicNumber(ligand2));
    }

    private Integer getAtomicNumber(ILigand ligand) {
        Integer atomNumber = ligand.getLigandAtom().getAtomicNumber();
        if (atomNumber != null) return atomNumber;
        return PeriodicTable.getAtomicNumber(ligand.getLigandAtom().getSymbol());
    }
}
