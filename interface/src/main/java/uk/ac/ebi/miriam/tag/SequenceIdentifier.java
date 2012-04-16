package uk.ac.ebi.miriam.tag;


import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface for sequence identifiers
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface SequenceIdentifier extends Identifier {

    public SequenceIdentifier ofHeader(Iterator<String> token);

    public Collection<String> getHeaderCodes();

}
