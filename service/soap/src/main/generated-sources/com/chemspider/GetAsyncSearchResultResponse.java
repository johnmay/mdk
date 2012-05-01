/**
 * GetAsyncSearchResultResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class GetAsyncSearchResultResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt getAsyncSearchResultResult;

    public GetAsyncSearchResultResponse() {
    }

    public GetAsyncSearchResultResponse(
           com.chemspider.ArrayOfInt getAsyncSearchResultResult) {
           this.getAsyncSearchResultResult = getAsyncSearchResultResult;
    }


    /**
     * Gets the getAsyncSearchResultResult value for this GetAsyncSearchResultResponse.
     * 
     * @return getAsyncSearchResultResult
     */
    public com.chemspider.ArrayOfInt getGetAsyncSearchResultResult() {
        return getAsyncSearchResultResult;
    }


    /**
     * Sets the getAsyncSearchResultResult value for this GetAsyncSearchResultResponse.
     * 
     * @param getAsyncSearchResultResult
     */
    public void setGetAsyncSearchResultResult(com.chemspider.ArrayOfInt getAsyncSearchResultResult) {
        this.getAsyncSearchResultResult = getAsyncSearchResultResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAsyncSearchResultResponse)) return false;
        GetAsyncSearchResultResponse other = (GetAsyncSearchResultResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAsyncSearchResultResult==null && other.getGetAsyncSearchResultResult()==null) || 
             (this.getAsyncSearchResultResult!=null &&
              this.getAsyncSearchResultResult.equals(other.getGetAsyncSearchResultResult())));
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
        if (getGetAsyncSearchResultResult() != null) {
            _hashCode += getGetAsyncSearchResultResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAsyncSearchResultResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">GetAsyncSearchResultResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAsyncSearchResultResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "GetAsyncSearchResultResult"));
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
