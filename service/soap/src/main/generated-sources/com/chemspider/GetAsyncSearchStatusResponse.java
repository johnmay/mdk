/**
 * GetAsyncSearchStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class GetAsyncSearchStatusResponse  implements java.io.Serializable {
    private com.chemspider.ERequestStatus getAsyncSearchStatusResult;

    public GetAsyncSearchStatusResponse() {
    }

    public GetAsyncSearchStatusResponse(
           com.chemspider.ERequestStatus getAsyncSearchStatusResult) {
           this.getAsyncSearchStatusResult = getAsyncSearchStatusResult;
    }


    /**
     * Gets the getAsyncSearchStatusResult value for this GetAsyncSearchStatusResponse.
     * 
     * @return getAsyncSearchStatusResult
     */
    public com.chemspider.ERequestStatus getGetAsyncSearchStatusResult() {
        return getAsyncSearchStatusResult;
    }


    /**
     * Sets the getAsyncSearchStatusResult value for this GetAsyncSearchStatusResponse.
     * 
     * @param getAsyncSearchStatusResult
     */
    public void setGetAsyncSearchStatusResult(com.chemspider.ERequestStatus getAsyncSearchStatusResult) {
        this.getAsyncSearchStatusResult = getAsyncSearchStatusResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAsyncSearchStatusResponse)) return false;
        GetAsyncSearchStatusResponse other = (GetAsyncSearchStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAsyncSearchStatusResult==null && other.getGetAsyncSearchStatusResult()==null) || 
             (this.getAsyncSearchStatusResult!=null &&
              this.getAsyncSearchStatusResult.equals(other.getGetAsyncSearchStatusResult())));
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
        if (getGetAsyncSearchStatusResult() != null) {
            _hashCode += getGetAsyncSearchStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAsyncSearchStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">GetAsyncSearchStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAsyncSearchStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "GetAsyncSearchStatusResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ERequestStatus"));
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
