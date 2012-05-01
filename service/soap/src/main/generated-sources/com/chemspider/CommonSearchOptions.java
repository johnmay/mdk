/**
 * CommonSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class CommonSearchOptions  extends com.chemspider.SearchOptions  implements java.io.Serializable {
    private com.chemspider.EComplexity complexity;

    private com.chemspider.EIsotopic isotopic;

    private boolean hasSpectra;

    private boolean hasPatents;

    public CommonSearchOptions() {
    }

    public CommonSearchOptions(
           com.chemspider.EComplexity complexity,
           com.chemspider.EIsotopic isotopic,
           boolean hasSpectra,
           boolean hasPatents) {
        this.complexity = complexity;
        this.isotopic = isotopic;
        this.hasSpectra = hasSpectra;
        this.hasPatents = hasPatents;
    }


    /**
     * Gets the complexity value for this CommonSearchOptions.
     * 
     * @return complexity
     */
    public com.chemspider.EComplexity getComplexity() {
        return complexity;
    }


    /**
     * Sets the complexity value for this CommonSearchOptions.
     * 
     * @param complexity
     */
    public void setComplexity(com.chemspider.EComplexity complexity) {
        this.complexity = complexity;
    }


    /**
     * Gets the isotopic value for this CommonSearchOptions.
     * 
     * @return isotopic
     */
    public com.chemspider.EIsotopic getIsotopic() {
        return isotopic;
    }


    /**
     * Sets the isotopic value for this CommonSearchOptions.
     * 
     * @param isotopic
     */
    public void setIsotopic(com.chemspider.EIsotopic isotopic) {
        this.isotopic = isotopic;
    }


    /**
     * Gets the hasSpectra value for this CommonSearchOptions.
     * 
     * @return hasSpectra
     */
    public boolean isHasSpectra() {
        return hasSpectra;
    }


    /**
     * Sets the hasSpectra value for this CommonSearchOptions.
     * 
     * @param hasSpectra
     */
    public void setHasSpectra(boolean hasSpectra) {
        this.hasSpectra = hasSpectra;
    }


    /**
     * Gets the hasPatents value for this CommonSearchOptions.
     * 
     * @return hasPatents
     */
    public boolean isHasPatents() {
        return hasPatents;
    }


    /**
     * Sets the hasPatents value for this CommonSearchOptions.
     * 
     * @param hasPatents
     */
    public void setHasPatents(boolean hasPatents) {
        this.hasPatents = hasPatents;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CommonSearchOptions)) return false;
        CommonSearchOptions other = (CommonSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.complexity==null && other.getComplexity()==null) || 
             (this.complexity!=null &&
              this.complexity.equals(other.getComplexity()))) &&
            ((this.isotopic==null && other.getIsotopic()==null) || 
             (this.isotopic!=null &&
              this.isotopic.equals(other.getIsotopic()))) &&
            this.hasSpectra == other.isHasSpectra() &&
            this.hasPatents == other.isHasPatents();
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
        if (getComplexity() != null) {
            _hashCode += getComplexity().hashCode();
        }
        if (getIsotopic() != null) {
            _hashCode += getIsotopic().hashCode();
        }
        _hashCode += (isHasSpectra() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHasPatents() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CommonSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "CommonSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("complexity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "Complexity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "EComplexity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isotopic");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "Isotopic"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "EIsotopic"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasSpectra");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "HasSpectra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasPatents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "HasPatents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
