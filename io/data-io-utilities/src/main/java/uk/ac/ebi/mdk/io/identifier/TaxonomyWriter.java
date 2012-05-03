package uk.ac.ebi.mdk.io.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.IdentifierWriter;
import uk.ac.ebi.resource.organism.Taxonomy;

import java.io.DataOutput;
import java.io.IOException;

/**
 * TaxonomyWriter - 13.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class TaxonomyWriter implements IdentifierWriter<Taxonomy> {

    private static final Logger LOGGER = Logger.getLogger(TaxonomyWriter.class);

    private DataOutput out;
    private EnumWriter enumWriter;

    public TaxonomyWriter(DataOutput out) {
        this.out = out;
        this.enumWriter = new EnumWriter(out);
    }

    @Override
    public void write(Taxonomy identifier) throws IOException {
        out.writeInt(identifier.getTaxon());
        out.writeUTF(identifier.getCode());
        out.writeUTF(identifier.getCommonName());
        out.writeUTF(identifier.getOfficialName());
        enumWriter.writeEnum(identifier.getKingdom());
    }
}
