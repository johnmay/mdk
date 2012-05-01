/**
 * SubstructureSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class SubstructureSearchResponse  implements java.io.Serializable {
    private java.lang.String substructureSearchResult;

    public SubstructureSearchResponse() {
    }

    public SubstructureSearchResponse(
           java.lang.String substructureSearchResult) {
           this.substructureSearchResult = substructureSearchResult;
    }


    /**
     * Gets the substructureSearchResult value for this SubstructureSearchResponse.
     * 
     * @return substructureSearchResult
     */
    public java.lang.String getSubstructureSearchResult() {
        return substructureSearchResult;
    }


    /**
     * Sets the substructureSearchResult value for this SubstructureSearchResponse.
     * 
     * @param substructureSearchResult
     */
    public void setSubstructureSearchResult(java.lang.String substructureSearchResult) {
        this.substructureSearchResult = substructureSearchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubstructureSearchResponse)) return false;
        SubstructureSearchResponse other = (SubstructureSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.substructureSearchResult==null && other.getSubstructureSearchResult()==null) || 
             (this.substructureSearchResult!=null &&
              this.substructureSearchResult.equals(other.getSubstructureSearchResult())));
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
        if (getSubstructureSearchResult() != null) {
            _hashCode += getSubstructureSearchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubstructureSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">SubstructureSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("substructureSearchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SubstructureSearchResult"));
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
