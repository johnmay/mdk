package uk.ac.ebi.chemet.service.schema;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.connection.AbstractDerbyConnection;
import uk.ac.ebi.interfaces.entities.EntityFactory;

import java.sql.*;

/**
 * ReactionSchema - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ReactionSchema {

    private static final Logger LOGGER = Logger.getLogger(ReactionSchema.class);

    private final AbstractDerbyConnection connection;
    private int rcount = 0;
    private int ccount = 0;

    public ReactionSchema(AbstractDerbyConnection connection) {
        this.connection = connection;
        createTables();
    }

    private void createTables() {
        try {
            Connection connection = this.connection.getConnection();

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE reaction (id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), accession VARCHAR(10), ec VARCHAR(20), CONSTRAINT REACTION_PRIMARY_KEY PRIMARY KEY (id), CONSTRAINT accession_index UNIQUE(accession))");
            statement.execute("CREATE TABLE compound (id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), accession VARCHAR(10), CONSTRAINT COMPOUND_PRIMARY_KEY PRIMARY KEY (id), CONSTRAINT compound_accession_index UNIQUE(accession))");
            statement.execute("CREATE TABLE participant (reaction_id INT," +
                                      " compound_id INT," +
                                      " coefficient DOUBLE," +
                                      " CONSTRAINT compound_fk FOREIGN KEY (reaction_id) REFERENCES reaction(id)," +
                                      " CONSTRAINT reaction_fk FOREIGN KEY (compound_id) REFERENCES compound(id))");

            SELECT_REACTION = connection.prepareStatement("SELECT reaction.accession, coefficient, compound.accession FROM reaction JOIN participant ON (reaction_id=reaction.id) JOIN compound ON (compound.id = compound_id) WHERE reaction.accession = ?");

        } catch (Exception ex) {
            LOGGER.error("Could not create tables: ", ex);
        }

    }

    private PreparedStatement SELECT_REACTION;

    private EntityFactory factory;
    
    


    public void insert(String accession, String ec, String[][] left, String[][] right) throws SQLException {

        Connection connection = this.connection.getConnection();
        PreparedStatement newReaction = connection.prepareStatement("INSERT INTO reaction(accession, ec) values(?, ?)");
        PreparedStatement newCompound = connection.prepareStatement("INSERT INTO compound(accession) values(?)");
        PreparedStatement newReactant = connection.prepareStatement("INSERT INTO participant(reaction_id, compound_id, coefficient) values(?,?,?)");

        PreparedStatement getReactionID = connection.prepareStatement("SELECT * FROM reaction WHERE accession = ?");
        getCompoundID = connection.prepareStatement("SELECT * FROM compound WHERE accession = ?");

        newReaction.setString(1, accession);
        newReaction.setString(2, ec);
        newReaction.execute();

        for (String[] participant : left) {

            int cid = getCompoundId(participant[1]);
            if(cid == -1){
                newCompound.setString(1, participant[1]);
                newCompound.execute();
            }

            getReactionID.setString(1, accession);
            getReactionID.execute();
            ResultSet rs = getReactionID.getResultSet();
            rs.next();
            int rid = rs.getInt(1);



            newReactant.setInt(1, rid);
            newReactant.setInt(2, getCompoundId(participant[1]));
            // on the left to we make it negative
            newReactant.setDouble(3, - Double.parseDouble(participant[0]));
            newReactant.execute();

        }

        for (String[] participant : right) {
            int cid = getCompoundId(participant[1]);
            if(cid == -1){
                newCompound.setString(1, participant[1]);
                newCompound.execute();
            }

            getReactionID.setString(1, accession);
            getReactionID.execute();
            ResultSet rs = getReactionID.getResultSet();
            rs.next();
            int rid = rs.getInt(1);



            newReactant.setInt(1, rid);
            newReactant.setInt(2, getCompoundId(participant[1]));
            newReactant.setDouble(3, + Double.parseDouble(participant[0]));
            newReactant.execute();

        }


    }

    private PreparedStatement getCompoundID;


    public int getCompoundId(String accession) throws SQLException {
        getCompoundID.setString(1, accession);
        getCompoundID.execute();
        ResultSet rs = getCompoundID.getResultSet();
        while (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public void dump() throws Exception {

        Connection connection = this.connection.getConnection();
        System.out.println("Reaction:");
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM REACTION");

            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ", " + rs.getString("accession"));
            }
            rs.close();
        }
        System.out.println("Compound:");
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPOUND");

            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ", " + rs.getString("accession"));
            }
            rs.close();
        }
        System.out.println("Reactant:");
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM REACTANT");

            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ", " + rs.getString("coefficient"));
            }
            rs.close();
        }
        System.out.println("Product:");
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT");

            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ", " + rs.getString("coefficient"));
            }
            rs.close();
        }

    }


}
