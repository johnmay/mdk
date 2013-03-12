/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.tool.inchi;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.tool.inchi.InChIMoleculeChecker.InChIMoleculeCheckerResult;


/**
 *
 * This class is a wrapper for InChI generation. It makes a number of assumptions
 * that could be left to the user. It should also provide the ability to set options.
 * The options should be set so that they resemble inchi 1.02 beta generation and
 * not the standard inchi.
 *
 * @author pmoreno
 */
public class InChIProducer102beta extends AbstractInChIProducer implements InChIProducer {

    private static final Logger LOGGER = Logger.getLogger(InChIProducer102beta.class);
    private List<INCHI_OPTION> inchiOptions;
    private IChemObjectBuilder builder;
    
    public InChIProducer102beta() {
        this.builder = DefaultChemObjectBuilder.getInstance();
    }
    
    public InChIProducer102beta(IChemObjectBuilder builder) {
        this.builder = builder;
    }
    
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
            //return null;
        }catch(NullPointerException ex) {
            LOGGER.error("Null pointer exception produced downwards by CDK when handling molecule "+mol.getID(),ex);
            return null;
        }
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);

        InChIMoleculeCheckerResult resCheck = InChIMoleculeChecker.getInstance().checkMolecule(mol);

        if(resCheck.isInChIFit()) {
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
        } else {
            if(resCheck.isGenericAtom())
                LOGGER.trace("Skipping generic molecule");
            else
                LOGGER.trace("Molecule has null bonds, atoms or is emtpy");
        }
        return null;
    }

    @Override
    public void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions) {
        this.inchiOptions = inchiConfigOptions;
    }

    @Override
    public InChIResult calculateInChI(String mdlMol) {
        MDLV2000Reader reader = new MDLV2000Reader(new InputStreamReader(new ByteArrayInputStream(mdlMol.getBytes())));
        try {
            IAtomContainer mol = reader.read(builder.newInstance(IAtomContainer.class));
        
            return calculateInChI(mol);
        } catch(CDKException e) {
            LOGGER.error("Could not read mdl mol string into an AtomContainer", e);
        }
        return null;
    }
}
