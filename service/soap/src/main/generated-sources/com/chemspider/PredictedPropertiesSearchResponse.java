/**
 * PredictedPropertiesSearchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class PredictedPropertiesSearchResponse  implements java.io.Serializable {
    private java.lang.String predictedPropertiesSearchResult;

    public PredictedPropertiesSearchResponse() {
    }

    public PredictedPropertiesSearchResponse(
           java.lang.String predictedPropertiesSearchResult) {
           this.predictedPropertiesSearchResult = predictedPropertiesSearchResult;
    }


    /**
     * Gets the predictedPropertiesSearchResult value for this PredictedPropertiesSearchResponse.
     * 
     * @return predictedPropertiesSearchResult
     */
    public java.lang.String getPredictedPropertiesSearchResult() {
        return predictedPropertiesSearchResult;
    }


    /**
     * Sets the predictedPropertiesSearchResult value for this PredictedPropertiesSearchResponse.
     * 
     * @param predictedPropertiesSearchResult
     */
    public void setPredictedPropertiesSearchResult(java.lang.String predictedPropertiesSearchResult) {
        this.predictedPropertiesSearchResult = predictedPropertiesSearchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PredictedPropertiesSearchResponse)) return false;
        PredictedPropertiesSearchResponse other = (PredictedPropertiesSearchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.predictedPropertiesSearchResult==null && other.getPredictedPropertiesSearchResult()==null) || 
             (this.predictedPropertiesSearchResult!=null &&
              this.predictedPropertiesSearchResult.equals(other.getPredictedPropertiesSearchResult())));
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
        if (getPredictedPropertiesSearchResult() != null) {
            _hashCode += getPredictedPropertiesSearchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PredictedPropertiesSearchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">PredictedPropertiesSearchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("predictedPropertiesSearchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "PredictedPropertiesSearchResult"));
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
