/**
 * LassoSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class LassoSearchResponse  implements java.io.Serializable {
    private java.lang.String lassoSearchResult;

    public LassoSearchResponse() {
    }

    public LassoSearchResponse(
           java.lang.String lassoSearchResult) {
           this.lassoSearchResult = lassoSearchResult;
    }


    /**
     * Gets the lassoSearchResult value for this LassoSearchResponse.
     * 
     * @return lassoSearchResult
     */
    public java.lang.String getLassoSearchResult() {
        return lassoSearchResult;
    }


    /**
     * Sets the lassoSearchResult value for this LassoSearchResponse.
     * 
     * @param lassoSearchResult
     */
    public void setLassoSearchResult(java.lang.String lassoSearchResult) {
        this.lassoSearchResult = lassoSearchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LassoSearchResponse)) return false;
        LassoSearchResponse other = (LassoSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lassoSearchResult==null && other.getLassoSearchResult()==null) || 
             (this.lassoSearchResult!=null &&
              this.lassoSearchResult.equals(other.getLassoSearchResult())));
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
        if (getLassoSearchResult() != null) {
            _hashCode += getLassoSearchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LassoSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">LassoSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lassoSearchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "LassoSearchResult"));
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
