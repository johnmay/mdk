/**
 * ArrayOfAnyType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ArrayOfAnyType  implements java.io.Serializable {
    private java.lang.Object[] anyType;

    public ArrayOfAnyType() {
    }

    public ArrayOfAnyType(
           java.lang.Object[] anyType) {
           this.anyType = anyType;
    }


    /**
     * Gets the anyType value for this ArrayOfAnyType.
     * 
     * @return anyType
     */
    public java.lang.Object[] getAnyType() {
        return anyType;
    }


    /**
     * Sets the anyType value for this ArrayOfAnyType.
     * 
     * @param anyType
     */
    public void setAnyType(java.lang.Object[] anyType) {
        this.anyType = anyType;
    }

    public java.lang.Object getAnyType(int i) {
        return this.anyType[i];
    }

    public void setAnyType(int i, java.lang.Object _value) {
        this.anyType[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfAnyType)) return false;
        ArrayOfAnyType other = (ArrayOfAnyType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anyType==null && other.getAnyType()==null) || 
             (this.anyType!=null &&
              java.util.Arrays.equals(this.anyType, other.getAnyType())));
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
        if (getAnyType() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnyType());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnyType(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfAnyType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfAnyType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anyType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "anyType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
