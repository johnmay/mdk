/**
 * GetAsyncSearchResultPartResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class GetAsyncSearchResultPartResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt getAsyncSearchResultPartResult;

    public GetAsyncSearchResultPartResponse() {
    }

    public GetAsyncSearchResultPartResponse(
           com.chemspider.ArrayOfInt getAsyncSearchResultPartResult) {
           this.getAsyncSearchResultPartResult = getAsyncSearchResultPartResult;
    }


    /**
     * Gets the getAsyncSearchResultPartResult value for this GetAsyncSearchResultPartResponse.
     * 
     * @return getAsyncSearchResultPartResult
     */
    public com.chemspider.ArrayOfInt getGetAsyncSearchResultPartResult() {
        return getAsyncSearchResultPartResult;
    }


    /**
     * Sets the getAsyncSearchResultPartResult value for this GetAsyncSearchResultPartResponse.
     * 
     * @param getAsyncSearchResultPartResult
     */
    public void setGetAsyncSearchResultPartResult(com.chemspider.ArrayOfInt getAsyncSearchResultPartResult) {
        this.getAsyncSearchResultPartResult = getAsyncSearchResultPartResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAsyncSearchResultPartResponse)) return false;
        GetAsyncSearchResultPartResponse other = (GetAsyncSearchResultPartResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAsyncSearchResultPartResult==null && other.getGetAsyncSearchResultPartResult()==null) || 
             (this.getAsyncSearchResultPartResult!=null &&
              this.getAsyncSearchResultPartResult.equals(other.getGetAsyncSearchResultPartResult())));
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
        if (getGetAsyncSearchResultPartResult() != null) {
            _hashCode += getGetAsyncSearchResultPartResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAsyncSearchResultPartResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">GetAsyncSearchResultPartResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAsyncSearchResultPartResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "GetAsyncSearchResultPartResult"));
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
