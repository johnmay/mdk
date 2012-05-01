/**
 * KEGG.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package jp.genome.ws.kegg;

public interface KEGG extends javax.xml.rpc.Service {
    public java.lang.String getKEGGPortAddress();

    public jp.genome.ws.kegg.KEGGPortType getKEGGPort() throws javax.xml.rpc.ServiceException;

    public jp.genome.ws.kegg.KEGGPortType getKEGGPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
