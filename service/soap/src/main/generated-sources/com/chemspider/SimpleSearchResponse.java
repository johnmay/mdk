/**
 * SimpleSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class SimpleSearchResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt simpleSearchResult;

    public SimpleSearchResponse() {
    }

    public SimpleSearchResponse(
           com.chemspider.ArrayOfInt simpleSearchResult) {
           this.simpleSearchResult = simpleSearchResult;
    }


    /**
     * Gets the simpleSearchResult value for this SimpleSearchResponse.
     * 
     * @return simpleSearchResult
     */
    public com.chemspider.ArrayOfInt getSimpleSearchResult() {
        return simpleSearchResult;
    }


    /**
     * Sets the simpleSearchResult value for this SimpleSearchResponse.
     * 
     * @param simpleSearchResult
     */
    public void setSimpleSearchResult(com.chemspider.ArrayOfInt simpleSearchResult) {
        this.simpleSearchResult = simpleSearchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SimpleSearchResponse)) return false;
        SimpleSearchResponse other = (SimpleSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.simpleSearchResult==null && other.getSimpleSearchResult()==null) || 
             (this.simpleSearchResult!=null &&
              this.simpleSearchResult.equals(other.getSimpleSearchResult())));
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
        if (getSimpleSearchResult() != null) {
            _hashCode += getSimpleSearchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SimpleSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">SimpleSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simpleSearchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SimpleSearchResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfInt"));
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
