/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util.inchi;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.inchi.InChIGeneratorFactory;

/**
 *
 * This class is a wrapper for InChI generation. It makes a number of assumptions
 * that could be left to the user. It should also provide the ability to set options.
 * The options should be set so that they resemble inchi 1.02 beta generation and
 * not the standard inchi.
 *
 * @author pmoreno
 */
public class InChIProducer102beta extends InChIProducer {

    private Logger logger = Logger.getLogger(InChIProducer102beta.class.getName());
    // TODO Handle exceptions
    public InChIResult calculateInChI(IAtomContainer mol) {
        this.result = new InChIResult();
        try {
            typeMoleculeForInChI(mol);
            addHydrogensToMolecule(mol);
        }catch(CDKException e) {
            logger.error("Could not add hydrogens to molecule "+mol.getID(), e);
            return null;
        }catch(NullPointerException ex) {
            logger.error("Null pointer exception produced downwards by CDK when handling molecule "+mol.getID(),ex);
            return null;
        }
        if(checkMoleculeForInChI(mol)) {
            InChIGeneratorFactory factory=null;
            InChIGenerator cdkGenerator=null;
            try {
                factory = InChIGeneratorFactory.getInstance();
                cdkGenerator = factory.getInChIGenerator(mol);
            } catch (CDKException ex) {
                logger.error("Could not initialize inchi factory or generator for "+mol.getID(), ex);
                return null;
            }

            String inchi = cdkGenerator.getInchi();
            if(inchi != null) {
                this.result.setInchi(inchi);
                String inchiKey;
                try {
                    inchiKey= cdkGenerator.getInchiKey();
                }catch(CDKException e) {
                    logger.error("Could not get inchiKey from molecule "+mol.getID(), e);
                    return null;
                }
                if(inchiKey!=null)
                    this.result.setInchiKey(inchiKey);
                else
                    logger.error("InchiKey is null for molecule "+mol.getID());
                    
                String auxInfo = cdkGenerator.getAuxInfo();
                if(auxInfo!=null)
                    this.result.setAuxInfo(auxInfo);
                else
                    logger.error("AuxInfo is null for molecule "+mol.getID());
                

                return result;
            }
            return null;
        }
        return null;
    }
}
