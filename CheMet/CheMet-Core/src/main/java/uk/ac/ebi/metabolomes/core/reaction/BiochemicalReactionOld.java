package uk.ac.ebi.metabolomes.core.reaction;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.ac.ebi.metabolomes.reaction;
//
//import com.sri.biospice.warehouse.database.Warehouse;
//import java.io.IOException;
//import java.sql.PreparedStatement;
//import com.sri.biospice.warehouse.database.WarehouseManager;
//import com.sri.biospice.warehouse.schema.DataSet;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import uk.ac.ebi.metabolomes.biowh.BiowhConnection;
//import uk.ac.ebi.metabolomes.biowh.DataSetProvider;
//import uk.ac.ebi.metabolomes.core.Reaction;
//import uk.ac.ebi.metabolomes.core.reaction.Compound;
//import uk.ac.ebi.metabolomes.identifiers.ECNumber;
//import uk.ac.ebi.metabolomes.util.inchi.InChi;
//
///**
// * BiochemicalReactionOld.java
// *
// *
// * @author johnmay
// * @date Apr 5, 2011
// */
//public class BiochemicalReactionOld extends Reaction {
//
//    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BiochemicalReactionOld.class );
//    // could do this all in one join but then you get multiple rows for some compounds
//    private static PreparedStatement getReactionWIDStatment;
//    private static PreparedStatement getReactantChemicalWIDStatment;
//    private static PreparedStatement getProductChemicalWIDStatment;
//    private static PreparedStatement getCompoundStatement;
//
//    private ECNumber ecNumber;
//
//    public static void prepareStatements() {
//        getReactionWIDStatment = prepareStatment( "SELECT Reaction.WID FROM Reaction WHERE Reaction.ECNumber LIKE ?" );
//        getReactantChemicalWIDStatment = prepareStatment( "SELECT Reactant.OtherWID, Reactant.coefficient FROM Reactant WHERE Reactant.ReactionWID LIKE ?" );
//        getProductChemicalWIDStatment = prepareStatment( "SELECT Product.OtherWID, Product.coefficient FROM Product WHERE Product.ReactionWID LIKE ?" );
//        getCompoundStatement = prepareStatment( "SELECT Chemical.Name, Chemical.InChI, Chemical.InChIKey, Chemical.InChiAuxInfo FROM Chemical WHERE Chemical.WID LIKE ?" );
//    }
//
//    public BiochemicalReactionOld( ECNumber ecNumber ) throws SQLException {
//
//        this.ecNumber = ecNumber;
//
//        // Get Reaction WIDs
//        getReactionWIDStatment.setString( 1 , ecNumber.toString() );
//        ResultSet rs = getReactionWIDStatment.executeQuery();
//        ArrayList<Long> reactionWIDs = new ArrayList<Long>();
//        while ( rs.next() ) {
//            reactionWIDs.add( rs.getLong( "Reaction.WID" ) );
//        }
//        rs.close();
//
//        if (reactionWIDs.size() == 0 ){
//            logger.error("No reactions found for EC " + ecNumber + " setting reactants and products empty");
//            setProducts( new Compound[0] );
//            setReactants( new Compound[0] );
//            return;
//        }
//        // at the moment we are only dealing with ECs with one reaction (would get multiple if more then one dataset)
//        if ( reactionWIDs.size() > 1 ) {
//            logger.error( "More then one reaction found for EC " + ecNumber + " unable to handle this (using first EC Number)" );
//        }
//
//        long reactionWID = reactionWIDs.get( 0 );
//        getReactantChemicalWIDStatment.setLong( 1 , reactionWID );
//        getProductChemicalWIDStatment.setLong( 1 , reactionWID );
//        ResultSet reactantResultSet = getReactantChemicalWIDStatment.executeQuery();
//        ResultSet productResultSet = getProductChemicalWIDStatment.executeQuery();
//
//        super.setReactants( createCompoundArray( reactantResultSet ) );
//        super.setReactants( createCompoundArray( productResultSet ) );
//
//    }
//
//    /**
//     *
//     * @param rs The result from Reactant or Product table
//     * @return array of compounds for the
//     * @throws SQLException
//     */
//    private Compound[] createCompoundArray( ResultSet rs ) throws SQLException {
//
//        // create a compound array the size of the results set
//        Compound[] compounds = new Compound[ countResults( rs ) ];
//
//        while ( rs.next() ) {
//
//            int coeficient = rs.getInt( "coefficient" );
//            Long otherWID = rs.getLong( "OtherWID" );
//
//            getCompoundStatement.setLong( 1 , otherWID );
//            ResultSet compoundResultSet = getCompoundStatement.executeQuery();
//
//            int compoundCount = countResults( compoundResultSet );
//
//            if ( compoundCount < 0 ) {
//                logger.info( "No compounds found for OtherWID " + otherWID + " probably a protein or abstract name (e.g. PolyPhosphate)" );
//            } else if ( compoundCount > 1 ) {
//                logger.error( "More then one compound found for OtherWID " + otherWID );
//            }
//
//            compoundResultSet.first();
//            String chemicalName = compoundResultSet.getString( "Chemical.Name" );
//            String inchistring = compoundResultSet.getString( "Chemical.InChI" );
//            String inchikey = compoundResultSet.getString( "Chemical.InChIKey" );
//            String inchiauxinfo = compoundResultSet.getString( "Chemical.InChIAuxInfo" );
//
//            // create the inchi object
//            InChi inchi = new InChi( inchistring , inchikey , inchiauxinfo );
//
//            compounds[rs.getRow() - 1] = new Compound( coeficient , inchi );
//
//            logger.info( "coefficient: " + coeficient + " name: " + chemicalName + " inchi: " + inchi );
//
//            compoundResultSet.close();
//
//        }
//        rs.close();
//
//        return compounds;
//
//    }
//
//    public static int countResults( ResultSet rs ) throws SQLException {
//        rs.last();
//        int size = rs.getRow();
//        rs.beforeFirst();
//        return size;
//    }
//
//    public static void main( String[] args ) throws IOException , SQLException {
//
//        org.apache.log4j.BasicConfigurator.configure();
//        BiowhConnection bwhc = new BiowhConnection();
//        Warehouse bwh = bwhc.getWarehouseObject();
//        DataSetProvider.loadPropsForCurrentSchema();
//        DataSet ds = DataSetProvider.getDataSetObject( DataSetProvider.DataSetEnum.KEGG );
//
//        // prepare the statments before executing
//        BiochemicalReactionOld.prepareStatements();
//        BiochemicalReactionOld er = new BiochemicalReactionOld( new ECNumber( "2.7.7.1" ) );
//
//    }
//
//    // static method to connect to the biowarehouse and prepare the statement
//    public static PreparedStatement prepareStatment( String statementString ) {
//        PreparedStatement prepStatement = null;
//        try {
//            prepStatement = WarehouseManager.getWarehouse().createPreparedStatement( statementString );
//        } catch ( SQLException e ) {
//            logger.error( e );
//            System.exit( 0 );
//        }
//        return prepStatement;
//    }
//}
