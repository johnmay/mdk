/**
 * ElementsSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ElementsSearchOptions  extends com.chemspider.SearchOptions  implements java.io.Serializable {
    private boolean includeAll;

    private com.chemspider.ArrayOfString includeElements;

    private com.chemspider.ArrayOfString excludeElements;

    public ElementsSearchOptions() {
    }

    public ElementsSearchOptions(
           boolean includeAll,
           com.chemspider.ArrayOfString includeElements,
           com.chemspider.ArrayOfString excludeElements) {
        this.includeAll = includeAll;
        this.includeElements = includeElements;
        this.excludeElements = excludeElements;
    }


    /**
     * Gets the includeAll value for this ElementsSearchOptions.
     * 
     * @return includeAll
     */
    public boolean isIncludeAll() {
        return includeAll;
    }


    /**
     * Sets the includeAll value for this ElementsSearchOptions.
     * 
     * @param includeAll
     */
    public void setIncludeAll(boolean includeAll) {
        this.includeAll = includeAll;
    }


    /**
     * Gets the includeElements value for this ElementsSearchOptions.
     * 
     * @return includeElements
     */
    public com.chemspider.ArrayOfString getIncludeElements() {
        return includeElements;
    }


    /**
     * Sets the includeElements value for this ElementsSearchOptions.
     * 
     * @param includeElements
     */
    public void setIncludeElements(com.chemspider.ArrayOfString includeElements) {
        this.includeElements = includeElements;
    }


    /**
     * Gets the excludeElements value for this ElementsSearchOptions.
     * 
     * @return excludeElements
     */
    public com.chemspider.ArrayOfString getExcludeElements() {
        return excludeElements;
    }


    /**
     * Sets the excludeElements value for this ElementsSearchOptions.
     * 
     * @param excludeElements
     */
    public void setExcludeElements(com.chemspider.ArrayOfString excludeElements) {
        this.excludeElements = excludeElements;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ElementsSearchOptions)) return false;
        ElementsSearchOptions other = (ElementsSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.includeAll == other.isIncludeAll() &&
            ((this.includeElements==null && other.getIncludeElements()==null) || 
             (this.includeElements!=null &&
              this.includeElements.equals(other.getIncludeElements()))) &&
            ((this.excludeElements==null && other.getExcludeElements()==null) || 
             (this.excludeElements!=null &&
              this.excludeElements.equals(other.getExcludeElements())));
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
        _hashCode += (isIncludeAll() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIncludeElements() != null) {
            _hashCode += getIncludeElements().hashCode();
        }
        if (getExcludeElements() != null) {
            _hashCode += getExcludeElements().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ElementsSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ElementsSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "IncludeAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeElements");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "IncludeElements"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("excludeElements");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExcludeElements"));
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
