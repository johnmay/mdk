/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util.inchi;

import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;


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

    private static final Logger LOGGER = Logger.getLogger(InChIProducer102beta.class);
    private List<INCHI_OPTION> inchiOptions;
    // TODO Handle exceptions
    @Override
    public InChIResult calculateInChI(IAtomContainer mol) {
        this.result = new InChIResult();
        /*if(mol instanceof QueryChemObject) {
            LOGGER.warn("Tried to calculate an InChI for a QueryAtomContainer type of molecule... avoiding it.");
            return null;
        }*/
        try {
            typeMoleculeForInChI(mol);
        } catch(CDKException e) {
            LOGGER.error("Could not type atoms in molecule "+mol.getID(), e);
            //return null;
        } catch(NullPointerException ex) {
            LOGGER.error("Null pointer exception produced downwards by CDK when handling molecule "+mol.getID(),ex);
            return null;
        } 
        try {
            addHydrogensToMolecule(mol);
        }catch(CDKException e) {
            LOGGER.error("Could not add hydrogens to molecule "+mol.getID(), e);
            AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
            //return null;
        }catch(NullPointerException ex) {
            LOGGER.error("Null pointer exception produced downwards by CDK when handling molecule "+mol.getID(),ex);
            return null;
        }

        if(checkMoleculeForInChI(mol)) {
            InChIGeneratorFactory factory=null;
            InChIGenerator cdkGenerator=null;
            try {
                factory = InChIGeneratorFactory.getInstance();
                if(this.inchiOptions!=null)
                    cdkGenerator = factory.getInChIGenerator(mol, inchiOptions);
                else
                    cdkGenerator = factory.getInChIGenerator(mol);
            } catch (CDKException ex) {
                LOGGER.error("Could not initialize inchi factory or generator for "+mol.getID(), ex);
                return null;
            } catch (IllegalArgumentException e) {
                LOGGER.error("JNI InChI couldn't handle molecule "+mol.getID()+" , IllegalArgument",e);
                return null;
            }

            String inchi = cdkGenerator.getInchi();
            if(inchi != null) {
                this.result.setInchi(inchi);
                String inchiKey;
                try {
                    inchiKey= cdkGenerator.getInchiKey();
                }catch(CDKException e) {
                    LOGGER.error("Could not get inchiKey from molecule "+mol.getID(), e);
                    return null;
                }
                if(inchiKey!=null)
                    this.result.setInchiKey(inchiKey);
                else
                    LOGGER.error("InchiKey is null for molecule "+mol.getID());
                    
                String auxInfo = cdkGenerator.getAuxInfo();
                if(auxInfo!=null)
                    this.result.setAuxInfo(auxInfo);
                else
                    LOGGER.error("AuxInfo is null for molecule "+mol.getID());
                

                return result;
            }
            return null;
        }
        return null;
    }

    @Override
    public void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions) {
        this.inchiOptions = inchiConfigOptions;
    }
}
