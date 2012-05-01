/**
 * Mol2CSIDResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class Mol2CSIDResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt mol2CSIDResult;

    public Mol2CSIDResponse() {
    }

    public Mol2CSIDResponse(
           com.chemspider.ArrayOfInt mol2CSIDResult) {
           this.mol2CSIDResult = mol2CSIDResult;
    }


    /**
     * Gets the mol2CSIDResult value for this Mol2CSIDResponse.
     * 
     * @return mol2CSIDResult
     */
    public com.chemspider.ArrayOfInt getMol2CSIDResult() {
        return mol2CSIDResult;
    }


    /**
     * Sets the mol2CSIDResult value for this Mol2CSIDResponse.
     * 
     * @param mol2CSIDResult
     */
    public void setMol2CSIDResult(com.chemspider.ArrayOfInt mol2CSIDResult) {
        this.mol2CSIDResult = mol2CSIDResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Mol2CSIDResponse)) return false;
        Mol2CSIDResponse other = (Mol2CSIDResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mol2CSIDResult==null && other.getMol2CSIDResult()==null) || 
             (this.mol2CSIDResult!=null &&
              this.mol2CSIDResult.equals(other.getMol2CSIDResult())));
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
        if (getMol2CSIDResult() != null) {
            _hashCode += getMol2CSIDResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Mol2CSIDResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">Mol2CSIDResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mol2CSIDResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "Mol2CSIDResult"));
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
