/**
 * JDispatcherService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;

public class JDispatcherService_ServiceLocator extends org.apache.axis.client.Service implements uk.ac.ebi.ws.jdispatcher.JDispatcherService_Service {

    public JDispatcherService_ServiceLocator() {
    }


    public JDispatcherService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public JDispatcherService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for JDispatcherServiceHttpPort
    private java.lang.String JDispatcherServiceHttpPort_address = "http://www.ebi.ac.uk/Tools/services/soap/ncbiblast";

    public java.lang.String getJDispatcherServiceHttpPortAddress() {
        return JDispatcherServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String JDispatcherServiceHttpPortWSDDServiceName = "JDispatcherServiceHttpPort";

    public java.lang.String getJDispatcherServiceHttpPortWSDDServiceName() {
        return JDispatcherServiceHttpPortWSDDServiceName;
    }

    public void setJDispatcherServiceHttpPortWSDDServiceName(java.lang.String name) {
        JDispatcherServiceHttpPortWSDDServiceName = name;
    }

    public uk.ac.ebi.ws.jdispatcher.JDispatcherService_PortType getJDispatcherServiceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(JDispatcherServiceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getJDispatcherServiceHttpPort(endpoint);
    }

    public uk.ac.ebi.ws.jdispatcher.JDispatcherService_PortType getJDispatcherServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            uk.ac.ebi.ws.jdispatcher.JDispatcherServiceHttpBindingStub _stub = new uk.ac.ebi.ws.jdispatcher.JDispatcherServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getJDispatcherServiceHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setJDispatcherServiceHttpPortEndpointAddress(java.lang.String address) {
        JDispatcherServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (uk.ac.ebi.ws.jdispatcher.JDispatcherService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                uk.ac.ebi.ws.jdispatcher.JDispatcherServiceHttpBindingStub _stub = new uk.ac.ebi.ws.jdispatcher.JDispatcherServiceHttpBindingStub(new java.net.URL(JDispatcherServiceHttpPort_address), this);
                _stub.setPortName(getJDispatcherServiceHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("JDispatcherServiceHttpPort".equals(inputPortName)) {
            return getJDispatcherServiceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "JDispatcherService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "JDispatcherServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("JDispatcherServiceHttpPort".equals(portName)) {
            setJDispatcherServiceHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
