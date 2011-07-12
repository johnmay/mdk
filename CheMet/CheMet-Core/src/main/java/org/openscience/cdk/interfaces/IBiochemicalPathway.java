package org.openscience.cdk.interfaces;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;

public interface IBiochemicalPathway extends IReactionSet {

	public IReaction[] getConnectedReactionsThroughMetabolite(IReaction currentReac, IAtomContainer mol);
	
	
}
