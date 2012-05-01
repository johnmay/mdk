/**
 * MolAndDS2CSIDResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class MolAndDS2CSIDResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt molAndDS2CSIDResult;

    public MolAndDS2CSIDResponse() {
    }

    public MolAndDS2CSIDResponse(
           com.chemspider.ArrayOfInt molAndDS2CSIDResult) {
           this.molAndDS2CSIDResult = molAndDS2CSIDResult;
    }


    /**
     * Gets the molAndDS2CSIDResult value for this MolAndDS2CSIDResponse.
     * 
     * @return molAndDS2CSIDResult
     */
    public com.chemspider.ArrayOfInt getMolAndDS2CSIDResult() {
        return molAndDS2CSIDResult;
    }


    /**
     * Sets the molAndDS2CSIDResult value for this MolAndDS2CSIDResponse.
     * 
     * @param molAndDS2CSIDResult
     */
    public void setMolAndDS2CSIDResult(com.chemspider.ArrayOfInt molAndDS2CSIDResult) {
        this.molAndDS2CSIDResult = molAndDS2CSIDResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MolAndDS2CSIDResponse)) return false;
        MolAndDS2CSIDResponse other = (MolAndDS2CSIDResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.molAndDS2CSIDResult==null && other.getMolAndDS2CSIDResult()==null) || 
             (this.molAndDS2CSIDResult!=null &&
              this.molAndDS2CSIDResult.equals(other.getMolAndDS2CSIDResult())));
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
        if (getMolAndDS2CSIDResult() != null) {
            _hashCode += getMolAndDS2CSIDResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MolAndDS2CSIDResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">MolAndDS2CSIDResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("molAndDS2CSIDResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MolAndDS2CSIDResult"));
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
