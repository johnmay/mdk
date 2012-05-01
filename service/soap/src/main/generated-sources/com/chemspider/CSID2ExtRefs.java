/**
 * CSID2ExtRefs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class CSID2ExtRefs  implements java.io.Serializable {
    private int CSID;

    private com.chemspider.ArrayOfString datasources;

    private java.lang.String token;

    public CSID2ExtRefs() {
    }

    public CSID2ExtRefs(
           int CSID,
           com.chemspider.ArrayOfString datasources,
           java.lang.String token) {
           this.CSID = CSID;
           this.datasources = datasources;
           this.token = token;
    }


    /**
     * Gets the CSID value for this CSID2ExtRefs.
     * 
     * @return CSID
     */
    public int getCSID() {
        return CSID;
    }


    /**
     * Sets the CSID value for this CSID2ExtRefs.
     * 
     * @param CSID
     */
    public void setCSID(int CSID) {
        this.CSID = CSID;
    }


    /**
     * Gets the datasources value for this CSID2ExtRefs.
     * 
     * @return datasources
     */
    public com.chemspider.ArrayOfString getDatasources() {
        return datasources;
    }


    /**
     * Sets the datasources value for this CSID2ExtRefs.
     * 
     * @param datasources
     */
    public void setDatasources(com.chemspider.ArrayOfString datasources) {
        this.datasources = datasources;
    }


    /**
     * Gets the token value for this CSID2ExtRefs.
     * 
     * @return token
     */
    public java.lang.String getToken() {
        return token;
    }


    /**
     * Sets the token value for this CSID2ExtRefs.
     * 
     * @param token
     */
    public void setToken(java.lang.String token) {
        this.token = token;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CSID2ExtRefs)) return false;
        CSID2ExtRefs other = (CSID2ExtRefs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.CSID == other.getCSID() &&
            ((this.datasources==null && other.getDatasources()==null) || 
             (this.datasources!=null &&
              this.datasources.equals(other.getDatasources()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getCSID();
        if (getDatasources() != null) {
            _hashCode += getDatasources().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CSID2ExtRefs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">CSID2ExtRefs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CSID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datasources");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "datasources"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "token"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
