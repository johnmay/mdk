/**
 * ArrayOfExtRef.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ArrayOfExtRef  implements java.io.Serializable {
    private com.chemspider.ExtRef[] extRef;

    public ArrayOfExtRef() {
    }

    public ArrayOfExtRef(
           com.chemspider.ExtRef[] extRef) {
           this.extRef = extRef;
    }


    /**
     * Gets the extRef value for this ArrayOfExtRef.
     * 
     * @return extRef
     */
    public com.chemspider.ExtRef[] getExtRef() {
        return extRef;
    }


    /**
     * Sets the extRef value for this ArrayOfExtRef.
     * 
     * @param extRef
     */
    public void setExtRef(com.chemspider.ExtRef[] extRef) {
        this.extRef = extRef;
    }

    public com.chemspider.ExtRef getExtRef(int i) {
        return this.extRef[i];
    }

    public void setExtRef(int i, com.chemspider.ExtRef _value) {
        this.extRef[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfExtRef)) return false;
        ArrayOfExtRef other = (ArrayOfExtRef) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.extRef==null && other.getExtRef()==null) || 
             (this.extRef!=null &&
              java.util.Arrays.equals(this.extRef, other.getExtRef())));
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
        if (getExtRef() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtRef());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtRef(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfExtRef.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfExtRef"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extRef");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExtRef"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExtRef"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
