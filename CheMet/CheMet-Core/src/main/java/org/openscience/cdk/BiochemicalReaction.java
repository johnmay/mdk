/**
 * 
 */
package org.openscience.cdk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openscience.cdk.interfaces.IAtom;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMoleculeSet;
import uk.ac.ebi.metabolomes.bioObjects.Enzyme;

/**
 * @author pmoreno
 *
 */
public class BiochemicalReaction extends Reaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5026264097092497517L;
	/**
	 * 
	 */
	private Set<Enzyme> cataliticEnzymes;
        private List<String> assignedECNumbers;
	
	public BiochemicalReaction() {
		// TODO Auto-generated constructor stub
		super();
		this.init();
	}

        private void init() {
            this.cataliticEnzymes = new HashSet<Enzyme>();
            this.assignedECNumbers = new ArrayList<String>();
        }

	public int getEnzymeCount() {
		return this.cataliticEnzymes.size();
	}
	public void addEnzyme(Enzyme e) {
		cataliticEnzymes.add(e);
	}
	public boolean removeEnzyme(Enzyme d) {
		for(Enzyme e : cataliticEnzymes) {
			if( e.equals(d) ) {
				cataliticEnzymes.remove(e);
				return true;
			}
		}
		return false;
	}
	public Set<Enzyme> getEnzymes() {
		return this.cataliticEnzymes;
	}
        public void assignECNumber(String ecNumber) {
            this.assignedECNumbers.add(ecNumber);
        }
        public boolean hasECNumberAssigned(String ecNumber) {
            return this.assignedECNumbers.contains(ecNumber);
        }
        public boolean compareByECNumber(BiochemicalReaction n) {
            for(String thisEC : this.assignedECNumbers)
                return n.hasECNumberAssigned(thisEC);
            return false;
        }
        public List getECNumberList() {
            return this.assignedECNumbers;
        }

        public boolean hasNoMolecules() {
            if(this.getProductCount()==0 || this.getReactantCount()==0)
                return true;
            else
                return false;
        }

        public boolean hasEmptyMolecules() {
            IMoleculeSet prods = this.getProducts();
            for(IAtomContainer mol : prods.molecules()) {
                if(mol.getAtomCount()==0)
                    return true;
            }
            IMoleculeSet reacts = this.getReactants();
            for(IAtomContainer mol : reacts.molecules()) {
                if(mol.getAtomCount()==0)
                    return true;
            }
            return false;
        }

        public boolean isGeneric() {
            IMoleculeSet mols = this.getProducts();
            for(IAtomContainer mol : mols.molecules()) {
                for(IAtom atom : mol.atoms()) {
                    if(atom instanceof PseudoAtom)
                        return true;
                }
            }
            mols = this.getReactants();
            for(IAtomContainer mol : mols.molecules()) {
                for(IAtom atom : mol.atoms()) {
                    if(atom instanceof PseudoAtom)
                        return true;
                }
            }
            return false;
        }
//        public boolean compareByRXNTopology(BiochemicalReaction n) {
//            this.getProductCount()
//        }
}
