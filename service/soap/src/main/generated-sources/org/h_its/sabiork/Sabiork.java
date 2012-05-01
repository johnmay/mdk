/**
 * Sabiork.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.h_its.sabiork;

public interface Sabiork extends java.rmi.Remote {
    public java.lang.String[] searchCompounds(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getTissue(int arg0) throws java.rmi.RemoteException;
    public int getPubmed(int arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getPathwayNames(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDs(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getGeneralReactionEquation(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getReactionEquation(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getKineticLaw(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getParametersXML(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getNormalizedParametersXML(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDFromCompound(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String[] searchEnzymesByName(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String[] searchEnzymesByECNumber(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getECByName(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDFromEnzyme(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getOrganismFromKLID(int arg0) throws java.rmi.RemoteException;
    public int getCompoundID(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getCompoundName(int arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getKEGGID(int arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getCHEBIID(int arg0) throws java.rmi.RemoteException;
    public int getCompoundIDFromCHEBIID(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getCompoundIDsFromCHEBIID(java.lang.String arg0) throws java.rmi.RemoteException;
    public int getCompoundIDFromKEGGID(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getCompoundIDsFromKEGGID(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getECFromReactionID(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getExpConditions(int arg0) throws java.rmi.RemoteException;
    public int getLocationID(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getLocationName(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getSBML(java.lang.Integer[] arg0, java.lang.Integer[] arg1, int arg2, int arg3, java.lang.String arg4, boolean arg5) throws java.rmi.RemoteException;
    public java.lang.Integer[] getMultipleCompoundIDs(java.lang.String[] arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getSubstratesSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getProductsSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getActivatorsSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getInhibitorsSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getCofactorsSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getCatalystsSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getUnknownModifiersSpeciesIDs(int arg0) throws java.rmi.RemoteException;
    public int getCompoundIDFromSpeciesID(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getKinLawIDFromPubmed(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionInstanceIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDsFromCompoundID(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getKEGGReactionID(int arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getKEGGReactionIDs(int arg0) throws java.rmi.RemoteException;
    public int getReactionIDByKEGG(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDsByKEGG(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String getEnzymeProtein(int arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getEnzymeProteinList(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionInstanceIDsFromProtein(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String[] getAllUniProtIDs() throws java.rmi.RemoteException;
    public java.lang.String getEnzymeVariant(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionIDFromEC(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getReactionInstancesFromUniprotID(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getAllCompoundIDs() throws java.rmi.RemoteException;
    public java.lang.Integer[] getAllReactionIDs() throws java.rmi.RemoteException;
    public java.lang.String[] getAllEnzymes() throws java.rmi.RemoteException;
    public java.lang.String[] getAllPathways() throws java.rmi.RemoteException;
    public java.lang.String[] getAllUnits() throws java.rmi.RemoteException;
    public java.lang.Integer[] getKinLawIDs(int arg0) throws java.rmi.RemoteException;
    public java.lang.Integer[] getKinLawIDsNotNull(int arg0) throws java.rmi.RemoteException;
    public java.lang.String getDataSourceID(int arg0) throws java.rmi.RemoteException;
}
