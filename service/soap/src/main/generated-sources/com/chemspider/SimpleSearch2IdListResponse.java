/**
 * SimpleSearch2IdListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class SimpleSearch2IdListResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt simpleSearch2IdListResult;

    public SimpleSearch2IdListResponse() {
    }

    public SimpleSearch2IdListResponse(
           com.chemspider.ArrayOfInt simpleSearch2IdListResult) {
           this.simpleSearch2IdListResult = simpleSearch2IdListResult;
    }


    /**
     * Gets the simpleSearch2IdListResult value for this SimpleSearch2IdListResponse.
     * 
     * @return simpleSearch2IdListResult
     */
    public com.chemspider.ArrayOfInt getSimpleSearch2IdListResult() {
        return simpleSearch2IdListResult;
    }


    /**
     * Sets the simpleSearch2IdListResult value for this SimpleSearch2IdListResponse.
     * 
     * @param simpleSearch2IdListResult
     */
    public void setSimpleSearch2IdListResult(com.chemspider.ArrayOfInt simpleSearch2IdListResult) {
        this.simpleSearch2IdListResult = simpleSearch2IdListResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SimpleSearch2IdListResponse)) return false;
        SimpleSearch2IdListResponse other = (SimpleSearch2IdListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.simpleSearch2IdListResult==null && other.getSimpleSearch2IdListResult()==null) || 
             (this.simpleSearch2IdListResult!=null &&
              this.simpleSearch2IdListResult.equals(other.getSimpleSearch2IdListResult())));
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
        if (getSimpleSearch2IdListResult() != null) {
            _hashCode += getSimpleSearch2IdListResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SimpleSearch2IdListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">SimpleSearch2IdListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simpleSearch2IdListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SimpleSearch2IdListResult"));
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
