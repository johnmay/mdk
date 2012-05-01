/**
 * ExactSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ExactSearchOptions implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ExactSearchOptions(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eExactMatch = "eExactMatch";
    public static final java.lang.String _eAllTautomers = "eAllTautomers";
    public static final java.lang.String _eSameSkeletonAndH = "eSameSkeletonAndH";
    public static final java.lang.String _eSameSkeleton = "eSameSkeleton";
    public static final java.lang.String _eAllIsomers = "eAllIsomers";
    public static final ExactSearchOptions eExactMatch = new ExactSearchOptions(_eExactMatch);
    public static final ExactSearchOptions eAllTautomers = new ExactSearchOptions(_eAllTautomers);
    public static final ExactSearchOptions eSameSkeletonAndH = new ExactSearchOptions(_eSameSkeletonAndH);
    public static final ExactSearchOptions eSameSkeleton = new ExactSearchOptions(_eSameSkeleton);
    public static final ExactSearchOptions eAllIsomers = new ExactSearchOptions(_eAllIsomers);
    public java.lang.String getValue() { return _value_;}
    public static ExactSearchOptions fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ExactSearchOptions enumeration = (ExactSearchOptions)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ExactSearchOptions fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExactSearchOptions.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExactSearchOptions"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
