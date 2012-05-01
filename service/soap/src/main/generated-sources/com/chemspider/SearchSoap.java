/**
 * SearchSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public interface SearchSoap extends java.rmi.Remote {

    /**
     * Search by Name, SMILES, InChI, InChIKey,
     *                 etc. Return a list of found IDs (first 100). <span
     * style="color: red; font-weight: bold;">This
     *                 operation is deprecated and will be removed soon -
     * use SimpleSearch
     *                 instead.</span>
     */
    public com.chemspider.ArrayOfInt simpleSearch2IdList(java.lang.String query) throws java.rmi.RemoteException;

    /**
     * Get structure image in PNG format. <span
     *                 style="color: red; font-weight: bold;">This operation
     * is deprecated and will be removed soon - use
     *                 GetCompoundThumbnail instead.</span>
     */
    public byte[] getRecordImage(java.lang.String id) throws java.rmi.RemoteException;

    /**
     * Get record details: CSID, InChIKey, InChI,
     *                 SMILES. <span style="color: red; font-weight: bold;">This
     * operation is deprecated and will be
     *                 removed soon - use GetCompoundInfo instead.</span>
     */
    public com.chemspider.ArrayOfAnyType getRecordDetails(java.lang.String id) throws java.rmi.RemoteException;

    /**
     * Search by Name, SMILES, InChI, InChIKey,
     *                 etc. Returns a list of found CSIDs (first 100 - please
     * use AsyncSimpleSearch instead if you like to get
     *                 the full list). Security token is required.
     */
    public com.chemspider.ArrayOfInt simpleSearch(java.lang.String query, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by Name, SMILES, InChI, InChIKey,
     *                 etc. Returns transaction ID which can be used to access
     * search status and result. Security token is
     *                 required.
     */
    public java.lang.String asyncSimpleSearch(java.lang.String query, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by SMILES or MOL. Returns
     *                 transaction ID which can be used to access search
     * status and result. Security token is required.
     */
    public java.lang.String structureSearch(com.chemspider.ExactStructureSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by SMILES or MOL. Returns
     *                 transaction ID which can be used to access search
     * status and result. Security token is required.
     */
    public java.lang.String substructureSearch(com.chemspider.SubstructureSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by SMILES or MOL. Valid values of
     *                 the Threshold parameter are between 0 and 100. Returns
     * transaction ID which can be used to access search
     *                 status and result. Security token is required.
     */
    public java.lang.String similaritySearch(com.chemspider.SimilaritySearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by intrinsic properties. Returns
     *                 transaction ID which can be used to access search
     * status and result. Security token is required.
     */
    public java.lang.String intrinsicPropertiesSearch(com.chemspider.IntrinsicPropertiesSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by chemical elements. Returns
     *                 transaction ID which can be used to access search
     * status and result. Security token is required.
     */
    public java.lang.String elementsSearch(com.chemspider.ElementsSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by predicted properties. Returns
     *                 transaction ID which can be used to access search
     * status and result. Security token is required.
     */
    public java.lang.String predictedPropertiesSearch(com.chemspider.PredictedPropertiesSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search by LASSO. Input values for
     *                 FamilyMin and FamilyMax should correspond to the short
     * name of the relevant enzyme e.g. ACE (see
     *                 http://www.chemspider.com/FullSearch.aspx for other
     * values). Valid thresholdMin and thresholdMax should
     *                 be between 0 and 0.99. Returns transaction ID which
     * can be used to access search status and result.
     *                 Security token is required.
     */
    public java.lang.String lassoSearch(com.chemspider.LassoSearchOptions options, com.chemspider.CommonSearchOptions commonOptions, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Query asynchronous operation status.
     *                 Requires transaction ID returned by Asynch<SOMETHING>Search
     * operation. Security token is required.
     */
    public com.chemspider.ERequestStatus getAsyncSearchStatus(java.lang.String rid, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Returns the list of CSIDs found by Asynch<SOMETHING>Search
     *                 operation. Security token is required.
     */
    public com.chemspider.ArrayOfInt getAsyncSearchResult(java.lang.String rid, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Return a slice of the list of CSIDs found
     *                 by Asynch<SOMETHING>Search operation. Returns full
     * list if start = 0 and count = -1. If (start +
     *                 count) > (# of results) all results starting at start
     * position are returned. Security token is
     *                 required.
     */
    public com.chemspider.ArrayOfInt getAsyncSearchResultPart(java.lang.String rid, java.lang.String token, int start, int count) throws java.rmi.RemoteException;

    /**
     * Get compound thumbnail in PNG format.
     *                 Security token is required.
     */
    public byte[] getCompoundThumbnail(java.lang.String id, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Get record details (CSID, StdInChIKey,
     *                 StdInChI, SMILES) by CSID. Security token is required.
     */
    public com.chemspider.CompoundInfo getCompoundInfo(int CSID, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search for structure that matches provided
     *                 MOL within the range specified by options. Returns
     * found CSID list. Security token with Service
     *                 Subscriber role is required. The search converts the
     * Mol to an InChI and the options parameter allows
     *                 different layers of the InChI to be searched for against
     * ChemSpider InChIs. For example: eSameSkeleton
     *                 searches on the connection layer alone (not the h
     * layer - just the connectivity of the rest of the
     *                 molecule); eAllTautomers searches on everything but
     * the fixed-hydrogen layer; eAllIsomers searches on
     *                 the molecular formula expression at the beginning
     * of the InChI.
     */
    public com.chemspider.ArrayOfInt mol2CSID(java.lang.String mol, com.chemspider.ExactSearchOptions options, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search for structure that matches provided
     *                 MOL within the range specified by options and within
     * the specified list of datasources. Returns found
     *                 CSID list. Security token with Service Subscriber
     * role is required. The search converts the Mol to an
     *                 InChI and the options parameter allows different layers
     * of the InChI to be searched for against
     *                 ChemSpider InChIs. For example: eSameSkeleton searches
     * on the connection layer alone (not the h layer -
     *                 just the connectivity of the rest of the molecule);
     * eAllTautomers searches on everything but the
     *                 fixed-hydrogen layer; eAllIsomers searches on the
     * molecular formula expression at the beginning of the
     *                 InChI.
     */
    public com.chemspider.ArrayOfInt molAndDS2CSID(java.lang.String mol, com.chemspider.ExactSearchOptions options, com.chemspider.ArrayOfString datasources, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Return a list of external references (data
     *                 sources). Security token with Service Subscriber role
     * is required.
     */
    public com.chemspider.ArrayOfExtRef CSID2ExtRefs(int CSID, com.chemspider.ArrayOfString datasources, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Get list of compounds IDs related to the
     *                 specified Data Slice. Security token is required.
     */
    public com.chemspider.ArrayOfInt getDataSliceCompounds(java.lang.String name, java.lang.String token) throws java.rmi.RemoteException;
}
