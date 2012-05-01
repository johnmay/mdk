/**
 * SabiorkServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.h_its.sabiork;

public class SabiorkServiceLocator extends org.apache.axis.client.Service implements org.h_its.sabiork.SabiorkService {

    public SabiorkServiceLocator() {
    }


    public SabiorkServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SabiorkServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for sabiorkPort
    private java.lang.String sabiorkPort_address = "http://sabiork.h-its.org:80/sabiork";

    public java.lang.String getsabiorkPortAddress() {
        return sabiorkPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String sabiorkPortWSDDServiceName = "sabiorkPort";

    public java.lang.String getsabiorkPortWSDDServiceName() {
        return sabiorkPortWSDDServiceName;
    }

    public void setsabiorkPortWSDDServiceName(java.lang.String name) {
        sabiorkPortWSDDServiceName = name;
    }

    public org.h_its.sabiork.Sabiork getsabiorkPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(sabiorkPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsabiorkPort(endpoint);
    }

    public org.h_its.sabiork.Sabiork getsabiorkPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.h_its.sabiork.SabiorkPortBindingStub _stub = new org.h_its.sabiork.SabiorkPortBindingStub(portAddress, this);
            _stub.setPortName(getsabiorkPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsabiorkPortEndpointAddress(java.lang.String address) {
        sabiorkPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.h_its.sabiork.Sabiork.class.isAssignableFrom(serviceEndpointInterface)) {
                org.h_its.sabiork.SabiorkPortBindingStub _stub = new org.h_its.sabiork.SabiorkPortBindingStub(new java.net.URL(sabiorkPort_address), this);
                _stub.setPortName(getsabiorkPortWSDDServiceName());
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
        if ("sabiorkPort".equals(inputPortName)) {
            return getsabiorkPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://sabio.eml.org/", "sabiorkService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://sabio.eml.org/", "sabiorkPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("sabiorkPort".equals(portName)) {
            setsabiorkPortEndpointAddress(address);
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
