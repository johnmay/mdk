/**
 * Search.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public interface Search extends javax.xml.rpc.Service {

/**
 * <h3>Please send all feedback to
 *             development-at-chemspider-dot-com</h3><i>NOTE: Some operations
 * require a "Security Token". To
 *             obtain a token please complete the <a href="/Register.aspx">registration</a>
 * process. Security
 *             Token is listed at <a href="/UserProfile.aspx">Profile</a>
 * page.</i>
 */
    public java.lang.String getSearchSoapAddress();

    public com.chemspider.SearchSoap getSearchSoap() throws javax.xml.rpc.ServiceException;

    public com.chemspider.SearchSoap getSearchSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getSearchSoap12Address();

    public com.chemspider.SearchSoap getSearchSoap12() throws javax.xml.rpc.ServiceException;

    public com.chemspider.SearchSoap getSearchSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
