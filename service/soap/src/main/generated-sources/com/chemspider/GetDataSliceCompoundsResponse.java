/**
 * GetDataSliceCompoundsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class GetDataSliceCompoundsResponse  implements java.io.Serializable {
    private com.chemspider.ArrayOfInt getDataSliceCompoundsResult;

    public GetDataSliceCompoundsResponse() {
    }

    public GetDataSliceCompoundsResponse(
           com.chemspider.ArrayOfInt getDataSliceCompoundsResult) {
           this.getDataSliceCompoundsResult = getDataSliceCompoundsResult;
    }


    /**
     * Gets the getDataSliceCompoundsResult value for this GetDataSliceCompoundsResponse.
     * 
     * @return getDataSliceCompoundsResult
     */
    public com.chemspider.ArrayOfInt getGetDataSliceCompoundsResult() {
        return getDataSliceCompoundsResult;
    }


    /**
     * Sets the getDataSliceCompoundsResult value for this GetDataSliceCompoundsResponse.
     * 
     * @param getDataSliceCompoundsResult
     */
    public void setGetDataSliceCompoundsResult(com.chemspider.ArrayOfInt getDataSliceCompoundsResult) {
        this.getDataSliceCompoundsResult = getDataSliceCompoundsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDataSliceCompoundsResponse)) return false;
        GetDataSliceCompoundsResponse other = (GetDataSliceCompoundsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getDataSliceCompoundsResult==null && other.getGetDataSliceCompoundsResult()==null) || 
             (this.getDataSliceCompoundsResult!=null &&
              this.getDataSliceCompoundsResult.equals(other.getGetDataSliceCompoundsResult())));
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
        if (getGetDataSliceCompoundsResult() != null) {
            _hashCode += getGetDataSliceCompoundsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDataSliceCompoundsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">GetDataSliceCompoundsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getDataSliceCompoundsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "GetDataSliceCompoundsResult"));
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
