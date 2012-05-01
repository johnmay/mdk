/**
 * CSID2ExtRefsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class CSID2ExtRefsResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfExtRef CSID2ExtRefsResult;

    public CSID2ExtRefsResponse() {
    }

    public CSID2ExtRefsResponse(
           com.chemspider.ArrayOfExtRef CSID2ExtRefsResult) {
           this.CSID2ExtRefsResult = CSID2ExtRefsResult;
    }


    /**
     * Gets the CSID2ExtRefsResult value for this CSID2ExtRefsResponse.
     * 
     * @return CSID2ExtRefsResult
     */
    public com.chemspider.ArrayOfExtRef getCSID2ExtRefsResult() {
        return CSID2ExtRefsResult;
    }


    /**
     * Sets the CSID2ExtRefsResult value for this CSID2ExtRefsResponse.
     * 
     * @param CSID2ExtRefsResult
     */
    public void setCSID2ExtRefsResult(com.chemspider.ArrayOfExtRef CSID2ExtRefsResult) {
        this.CSID2ExtRefsResult = CSID2ExtRefsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CSID2ExtRefsResponse)) return false;
        CSID2ExtRefsResponse other = (CSID2ExtRefsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CSID2ExtRefsResult==null && other.getCSID2ExtRefsResult()==null) || 
             (this.CSID2ExtRefsResult!=null &&
              this.CSID2ExtRefsResult.equals(other.getCSID2ExtRefsResult())));
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
        if (getCSID2ExtRefsResult() != null) {
            _hashCode += getCSID2ExtRefsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CSID2ExtRefsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">CSID2ExtRefsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSID2ExtRefsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CSID2ExtRefsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfExtRef"));
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
