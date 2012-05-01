/**
 * LassoSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class LassoSearchOptions  extends com.chemspider.SearchOptions  implements java.io.Serializable {
    private double thresholdMin;

    private java.lang.String familyMin;

    private double thresholdMax;

    private com.chemspider.ArrayOfString familyMax;

    public LassoSearchOptions() {
    }

    public LassoSearchOptions(
           double thresholdMin,
           java.lang.String familyMin,
           double thresholdMax,
           com.chemspider.ArrayOfString familyMax) {
        this.thresholdMin = thresholdMin;
        this.familyMin = familyMin;
        this.thresholdMax = thresholdMax;
        this.familyMax = familyMax;
    }


    /**
     * Gets the thresholdMin value for this LassoSearchOptions.
     * 
     * @return thresholdMin
     */
    public double getThresholdMin() {
        return thresholdMin;
    }


    /**
     * Sets the thresholdMin value for this LassoSearchOptions.
     * 
     * @param thresholdMin
     */
    public void setThresholdMin(double thresholdMin) {
        this.thresholdMin = thresholdMin;
    }


    /**
     * Gets the familyMin value for this LassoSearchOptions.
     * 
     * @return familyMin
     */
    public java.lang.String getFamilyMin() {
        return familyMin;
    }


    /**
     * Sets the familyMin value for this LassoSearchOptions.
     * 
     * @param familyMin
     */
    public void setFamilyMin(java.lang.String familyMin) {
        this.familyMin = familyMin;
    }


    /**
     * Gets the thresholdMax value for this LassoSearchOptions.
     * 
     * @return thresholdMax
     */
    public double getThresholdMax() {
        return thresholdMax;
    }


    /**
     * Sets the thresholdMax value for this LassoSearchOptions.
     * 
     * @param thresholdMax
     */
    public void setThresholdMax(double thresholdMax) {
        this.thresholdMax = thresholdMax;
    }


    /**
     * Gets the familyMax value for this LassoSearchOptions.
     * 
     * @return familyMax
     */
    public com.chemspider.ArrayOfString getFamilyMax() {
        return familyMax;
    }


    /**
     * Sets the familyMax value for this LassoSearchOptions.
     * 
     * @param familyMax
     */
    public void setFamilyMax(com.chemspider.ArrayOfString familyMax) {
        this.familyMax = familyMax;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LassoSearchOptions)) return false;
        LassoSearchOptions other = (LassoSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.thresholdMin == other.getThresholdMin() &&
            ((this.familyMin==null && other.getFamilyMin()==null) || 
             (this.familyMin!=null &&
              this.familyMin.equals(other.getFamilyMin()))) &&
            this.thresholdMax == other.getThresholdMax() &&
            ((this.familyMax==null && other.getFamilyMax()==null) || 
             (this.familyMax!=null &&
              this.familyMax.equals(other.getFamilyMax())));
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
        _hashCode += new Double(getThresholdMin()).hashCode();
        if (getFamilyMin() != null) {
            _hashCode += getFamilyMin().hashCode();
        }
        _hashCode += new Double(getThresholdMax()).hashCode();
        if (getFamilyMax() != null) {
            _hashCode += getFamilyMax().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LassoSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "LassoSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thresholdMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ThresholdMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("familyMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "FamilyMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thresholdMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ThresholdMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("familyMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "FamilyMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfString"));
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
