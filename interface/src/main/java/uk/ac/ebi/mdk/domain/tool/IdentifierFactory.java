package uk.ac.ebi.mdk.domain.tool;

import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @author John May
 */
public interface IdentifierFactory {

    public Identifier ofName(String name);

    public Identifier ofName(String name, String accession);

    public boolean hasSynonym(String synonym);

    public Identifier ofSynonym(String synonym);

    public Identifier ofSynonym(String synonym, String accession);

}
