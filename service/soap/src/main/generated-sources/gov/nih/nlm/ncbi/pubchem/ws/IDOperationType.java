/**
 * IDOperationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem.ws;

public class IDOperationType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected IDOperationType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eIDOperation_Same = "eIDOperation_Same";
    public static final java.lang.String _eIDOperation_SameStereo = "eIDOperation_SameStereo";
    public static final java.lang.String _eIDOperation_SameIsotope = "eIDOperation_SameIsotope";
    public static final java.lang.String _eIDOperation_SameConnectivity = "eIDOperation_SameConnectivity";
    public static final java.lang.String _eIDOperation_SameParent = "eIDOperation_SameParent";
    public static final java.lang.String _eIDOperation_SameParentStereo = "eIDOperation_SameParentStereo";
    public static final java.lang.String _eIDOperation_SameParentIsotope = "eIDOperation_SameParentIsotope";
    public static final java.lang.String _eIDOperation_SameParentConnectivity = "eIDOperation_SameParentConnectivity";
    public static final java.lang.String _eIDOperation_Similar2D = "eIDOperation_Similar2D";
    public static final java.lang.String _eIDOperation_Similar3D = "eIDOperation_Similar3D";
    public static final IDOperationType eIDOperation_Same = new IDOperationType(_eIDOperation_Same);
    public static final IDOperationType eIDOperation_SameStereo = new IDOperationType(_eIDOperation_SameStereo);
    public static final IDOperationType eIDOperation_SameIsotope = new IDOperationType(_eIDOperation_SameIsotope);
    public static final IDOperationType eIDOperation_SameConnectivity = new IDOperationType(_eIDOperation_SameConnectivity);
    public static final IDOperationType eIDOperation_SameParent = new IDOperationType(_eIDOperation_SameParent);
    public static final IDOperationType eIDOperation_SameParentStereo = new IDOperationType(_eIDOperation_SameParentStereo);
    public static final IDOperationType eIDOperation_SameParentIsotope = new IDOperationType(_eIDOperation_SameParentIsotope);
    public static final IDOperationType eIDOperation_SameParentConnectivity = new IDOperationType(_eIDOperation_SameParentConnectivity);
    public static final IDOperationType eIDOperation_Similar2D = new IDOperationType(_eIDOperation_Similar2D);
    public static final IDOperationType eIDOperation_Similar3D = new IDOperationType(_eIDOperation_Similar3D);
    public java.lang.String getValue() { return _value_;}
    public static IDOperationType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        IDOperationType enumeration = (IDOperationType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static IDOperationType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(IDOperationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "IDOperationType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
