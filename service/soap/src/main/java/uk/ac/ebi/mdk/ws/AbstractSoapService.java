package uk.ac.ebi.mdk.ws;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author John May
 */
public abstract class AbstractSoapService<I extends Identifier> implements QueryService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractSoapService.class);

    private MDLV2000Reader reader = new MDLV2000Reader();
    private MDLV2000Writer writer = new MDLV2000Writer();

    private static final IChemObjectBuilder BUILDER = DefaultChemObjectBuilder.getInstance();

    private Integer maxResults = 100;
    private Float   similarity = 0.5f;

    public I getIdentifier(String accession) {
        I identifier =  getIdentifier();
        identifier.setAccession(accession);
        return identifier;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.SOAP_WEB_SERVICE;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public Float getSimilarity() {
        return similarity;
    }

    @Override
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public void setMinSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public String structure2Mol(IAtomContainer structure){

        StringWriter sw = new StringWriter();

        try{
            writer.setWriter(sw);
            writer.write(structure);
        } catch (Exception ex){
            LOGGER.error("Unable to create mol entry from structure");
        }

        return sw.toString();

    }

    /**
     * Convert a MDL molfile string to a CDK strucutre.
     * emtpty structure returned by default
     * @param mol
     * @return
     */
    public IAtomContainer mol2Structure(String mol){

        if(mol == null || mol.isEmpty()){
            return BUILDER.newInstance(IAtomContainer.class);
        }

        StringReader sr = new StringReader(mol);

        try{
            reader.setReader(sr);
            return reader.read(BUILDER.newInstance(IAtomContainer.class));
        } catch (Exception ex){
            LOGGER.error("Unable to create mol entry from structure");
        }

        return BUILDER.newInstance(IAtomContainer.class);
    }

}
