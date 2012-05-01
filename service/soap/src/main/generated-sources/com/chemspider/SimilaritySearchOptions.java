/**
 * SimilaritySearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class SimilaritySearchOptions  extends com.chemspider.StructureSearchOptions  implements java.io.Serializable {
    private com.chemspider.ESimilarityType similarityType;

    private float threshold;

    public SimilaritySearchOptions() {
    }

    public SimilaritySearchOptions(
           java.lang.String molecule,
           com.chemspider.ESimilarityType similarityType,
           float threshold) {
        super(
            molecule);
        this.similarityType = similarityType;
        this.threshold = threshold;
    }


    /**
     * Gets the similarityType value for this SimilaritySearchOptions.
     * 
     * @return similarityType
     */
    public com.chemspider.ESimilarityType getSimilarityType() {
        return similarityType;
    }


    /**
     * Sets the similarityType value for this SimilaritySearchOptions.
     * 
     * @param similarityType
     */
    public void setSimilarityType(com.chemspider.ESimilarityType similarityType) {
        this.similarityType = similarityType;
    }


    /**
     * Gets the threshold value for this SimilaritySearchOptions.
     * 
     * @return threshold
     */
    public float getThreshold() {
        return threshold;
    }


    /**
     * Sets the threshold value for this SimilaritySearchOptions.
     * 
     * @param threshold
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SimilaritySearchOptions)) return false;
        SimilaritySearchOptions other = (SimilaritySearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.similarityType==null && other.getSimilarityType()==null) || 
             (this.similarityType!=null &&
              this.similarityType.equals(other.getSimilarityType()))) &&
            this.threshold == other.getThreshold();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getSimilarityType() != null) {
            _hashCode += getSimilarityType().hashCode();
        }
        _hashCode += new Float(getThreshold()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SimilaritySearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "SimilaritySearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("similarityType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SimilarityType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ESimilarityType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("threshold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "Threshold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
