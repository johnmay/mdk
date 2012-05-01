/**
 * ChebiWebServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.chebi;

public interface ChebiWebServicePortType extends java.rmi.Remote {
    public uk.ac.ebi.ws.chebi.LiteEntityList getLiteEntity(java.lang.String search, uk.ac.ebi.ws.chebi.SearchCategory searchCategory, int maximumResults, uk.ac.ebi.ws.chebi.StarsCategory stars) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.Entity getCompleteEntity(java.lang.String chebiId) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.Entity[] getCompleteEntityByList(java.lang.String[] listOfChEBIIds) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.OntologyDataItemList getOntologyParents(java.lang.String chebiId) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.OntologyDataItemList getOntologyChildren(java.lang.String chebiId) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.LiteEntityList getAllOntologyChildrenInPath(java.lang.String chebiId, uk.ac.ebi.ws.chebi.RelationshipType relationshipType, boolean structureOnly) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
    public uk.ac.ebi.ws.chebi.LiteEntityList getStructureSearch(java.lang.String structure, uk.ac.ebi.ws.chebi.StructureType type, uk.ac.ebi.ws.chebi.StructureSearchCategory structureSearchCategory, int totalResults, float tanimotoCutoff) throws java.rmi.RemoteException, uk.ac.ebi.ws.chebi.ChebiWebServiceFault;
}
