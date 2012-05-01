/**
 * ERequestStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ERequestStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ERequestStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _Created = "Created";
    public static final java.lang.String _Scheduled = "Scheduled";
    public static final java.lang.String _Processing = "Processing";
    public static final java.lang.String _Suspended = "Suspended";
    public static final java.lang.String _PartialResultReady = "PartialResultReady";
    public static final java.lang.String _ResultReady = "ResultReady";
    public static final java.lang.String _Failed = "Failed";
    public static final java.lang.String _TooManyRecords = "TooManyRecords";
    public static final ERequestStatus Unknown = new ERequestStatus(_Unknown);
    public static final ERequestStatus Created = new ERequestStatus(_Created);
    public static final ERequestStatus Scheduled = new ERequestStatus(_Scheduled);
    public static final ERequestStatus Processing = new ERequestStatus(_Processing);
    public static final ERequestStatus Suspended = new ERequestStatus(_Suspended);
    public static final ERequestStatus PartialResultReady = new ERequestStatus(_PartialResultReady);
    public static final ERequestStatus ResultReady = new ERequestStatus(_ResultReady);
    public static final ERequestStatus Failed = new ERequestStatus(_Failed);
    public static final ERequestStatus TooManyRecords = new ERequestStatus(_TooManyRecords);
    public java.lang.String getValue() { return _value_;}
    public static ERequestStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ERequestStatus enumeration = (ERequestStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ERequestStatus fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ERequestStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ERequestStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
