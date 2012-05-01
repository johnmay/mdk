/**
 * AssayOutcomeFilterType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem.ws;

public class AssayOutcomeFilterType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AssayOutcomeFilterType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eAssayOutcome_All = "eAssayOutcome_All";
    public static final java.lang.String _eAssayOutcome_Inactive = "eAssayOutcome_Inactive";
    public static final java.lang.String _eAssayOutcome_Active = "eAssayOutcome_Active";
    public static final java.lang.String _eAssayOutcome_Inconclusive = "eAssayOutcome_Inconclusive";
    public static final java.lang.String _eAssayOutcome_Unspecified = "eAssayOutcome_Unspecified";
    public static final AssayOutcomeFilterType eAssayOutcome_All = new AssayOutcomeFilterType(_eAssayOutcome_All);
    public static final AssayOutcomeFilterType eAssayOutcome_Inactive = new AssayOutcomeFilterType(_eAssayOutcome_Inactive);
    public static final AssayOutcomeFilterType eAssayOutcome_Active = new AssayOutcomeFilterType(_eAssayOutcome_Active);
    public static final AssayOutcomeFilterType eAssayOutcome_Inconclusive = new AssayOutcomeFilterType(_eAssayOutcome_Inconclusive);
    public static final AssayOutcomeFilterType eAssayOutcome_Unspecified = new AssayOutcomeFilterType(_eAssayOutcome_Unspecified);
    public java.lang.String getValue() { return _value_;}
    public static AssayOutcomeFilterType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        AssayOutcomeFilterType enumeration = (AssayOutcomeFilterType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static AssayOutcomeFilterType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(AssayOutcomeFilterType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AssayOutcomeFilterType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
