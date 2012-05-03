package uk.ac.ebi.mdk.service;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.reaction.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.service.connection.AbstractDerbyConnection;
import uk.ac.ebi.mdk.service.connection.reaction.KEGGReactionConnection;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.IdentifierReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * KEGGReactionService - 06.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGReactionService {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionService.class);
    private AbstractDerbyConnection connection;
    private final PreparedStatement statement;
    private final PreparedStatement selectOnEC;
    private EntityFactory factory;

    public KEGGReactionService(EntityFactory factory) throws SQLException {
        connection = new KEGGReactionConnection();
        this.factory = factory;

        statement = connection.getConnection().prepareStatement("SELECT coefficient, compound.accession, ec" +
                                                                        " FROM reaction" +
                                                                        " JOIN participant ON (reaction_id=reaction.id)" +
                                                                        " JOIN compound ON (compound.id = compound_id)" +
                                                                        " WHERE reaction.accession = ?");
        selectOnEC = connection.getConnection().prepareStatement("SELECT coefficient, compound.accession, reaction.accession" +
                                                                         " FROM reaction" +
                                                                         " JOIN participant ON (reaction_id=reaction.id)" +
                                                                         " JOIN compound ON (compound.id = compound_id)" +
                                                                         " WHERE reaction.ec = ?");

    }

    public IdentifierReaction<KEGGCompoundIdentifier> getReaction(String accession) throws SQLException {

        statement.setString(1, accession);
        statement.execute();

        ResultSet rs = statement.getResultSet();

        IdentifierReaction<KEGGCompoundIdentifier> rxn = factory.newInstance(IdentifierReaction.class);


        while (rs.next()) {

            Double coefficient = rs.getDouble(1);
            String cpd = rs.getString(2);
            String ec = rs.getString(3);

            Participant<KEGGCompoundIdentifier, Double> base = factory.newInstance(Participant.class);

            Participant<KEGGCompoundIdentifier, Double> p = (Participant<KEGGCompoundIdentifier, Double>) base.newInstance();
            p.setMolecule(new KEGGCompoundIdentifier(cpd));

            if (coefficient > 0) {
                p.setCoefficient(coefficient);
                rxn.addProduct(p);
            } else if (coefficient < 0) {
                p.setCoefficient(Math.abs(coefficient));
                rxn.addReactant(p);
            }

        }

        return rxn;

    }

    public Collection<IdentifierReaction<KEGGCompoundIdentifier>> getReaction(ECNumber ec) throws SQLException {

        selectOnEC.setString(1, ec.toString());
        selectOnEC.execute();

        ResultSet rs = selectOnEC.getResultSet();

        Collection<IdentifierReaction<KEGGCompoundIdentifier>> reactions = new ArrayList<IdentifierReaction<KEGGCompoundIdentifier>>();

        String currentaccession = "";
        IdentifierReaction<KEGGCompoundIdentifier> rxn = null;

        while (rs.next()) {

            Double coefficient = rs.getDouble(1);
            String cpd = rs.getString(2);
            String accession = rs.getString(3);

            if (!accession.equals(currentaccession)) {

                if (rxn != null)
                    reactions.add(rxn);

                currentaccession = accession;
                rxn = factory.newInstance(IdentifierReaction.class);

                rxn.setIdentifier(new KEGGReactionIdentifier(accession));
                rxn.setName(accession);
                rxn.setAbbreviation(accession);

            }

            Participant<KEGGCompoundIdentifier, Double> base = factory.newInstance(Participant.class);

            Participant<KEGGCompoundIdentifier, Double> p = (Participant<KEGGCompoundIdentifier, Double>) base.newInstance();
            p.setMolecule(new KEGGCompoundIdentifier(cpd));

            if (coefficient > 0) {
                p.setCoefficient(coefficient);
                rxn.addProduct(p);
            } else if (coefficient < 0) {
                p.setCoefficient(Math.abs(coefficient));
                rxn.addReactant(p);
            }




        }

        if(rxn != null)
            reactions.add(rxn);

        return reactions;

    }

}
