/**
 * JDispatcherService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;

public interface JDispatcherService_PortType extends java.rmi.Remote {

    /**
     * Submit an analysis job
     */
    public java.lang.String run(java.lang.String email, java.lang.String title, uk.ac.ebi.ws.jdispatcher.InputParameters parameters) throws java.rmi.RemoteException;

    /**
     * Get the status of a submitted job
     */
    public java.lang.String getStatus(java.lang.String jobId) throws java.rmi.RemoteException;

    /**
     * Get the list of renderers available to output a job result
     * (i.e. the list of available output types)
     */
    public uk.ac.ebi.ws.jdispatcher.WsResultTypes getResultTypes(java.lang.String jobId) throws java.rmi.RemoteException;

    /**
     * Get a job result formatted using a particular renderer
     */
    public byte[] getResult(java.lang.String jobId, java.lang.String type, uk.ac.ebi.ws.jdispatcher.WsRawOutputParameters parameters) throws java.rmi.RemoteException;

    /**
     * List the names of the parameters available before submission
     */
    public uk.ac.ebi.ws.jdispatcher.WsParameters getParameters() throws java.rmi.RemoteException;

    /**
     * Get some details about a parameter (e.g. name, description,
     * values, etc.)
     */
    public uk.ac.ebi.ws.jdispatcher.WsParameterDetails getParameterDetails(java.lang.String parameterId) throws java.rmi.RemoteException;
}
