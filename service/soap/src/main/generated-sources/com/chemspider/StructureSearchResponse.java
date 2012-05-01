/**
 * StructureSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class StructureSearchResponse  implements java.io.Serializable {
    private java.lang.String structureSearchResult;

    public StructureSearchResponse() {
    }

    public StructureSearchResponse(
           java.lang.String structureSearchResult) {
           this.structureSearchResult = structureSearchResult;
    }


    /**
     * Gets the structureSearchResult value for this StructureSearchResponse.
     * 
     * @return structureSearchResult
     */
    public java.lang.String getStructureSearchResult() {
        return structureSearchResult;
    }


    /**
     * Sets the structureSearchResult value for this StructureSearchResponse.
     * 
     * @param structureSearchResult
     */
    public void setStructureSearchResult(java.lang.String structureSearchResult) {
        this.structureSearchResult = structureSearchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StructureSearchResponse)) return false;
        StructureSearchResponse other = (StructureSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.structureSearchResult==null && other.getStructureSearchResult()==null) || 
             (this.structureSearchResult!=null &&
              this.structureSearchResult.equals(other.getStructureSearchResult())));
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
        if (getStructureSearchResult() != null) {
            _hashCode += getStructureSearchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StructureSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">StructureSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("structureSearchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "StructureSearchResult"));
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
