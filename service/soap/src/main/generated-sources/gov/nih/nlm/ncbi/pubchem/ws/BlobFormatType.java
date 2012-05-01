/**
 * BlobFormatType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem.ws;

public class BlobFormatType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BlobFormatType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eBlobFormat_Unspecified = "eBlobFormat_Unspecified";
    public static final java.lang.String _eBlobFormat_ASNB = "eBlobFormat_ASNB";
    public static final java.lang.String _eBlobFormat_ASNT = "eBlobFormat_ASNT";
    public static final java.lang.String _eBlobFormat_XML = "eBlobFormat_XML";
    public static final java.lang.String _eBlobFormat_SDF = "eBlobFormat_SDF";
    public static final java.lang.String _eBlobFormat_CSV = "eBlobFormat_CSV";
    public static final java.lang.String _eBlobFormat_Text = "eBlobFormat_Text";
    public static final java.lang.String _eBlobFormat_HTML = "eBlobFormat_HTML";
    public static final java.lang.String _eBlobFormat_PNG = "eBlobFormat_PNG";
    public static final java.lang.String _eBlobFormat_Other = "eBlobFormat_Other";
    public static final BlobFormatType eBlobFormat_Unspecified = new BlobFormatType(_eBlobFormat_Unspecified);
    public static final BlobFormatType eBlobFormat_ASNB = new BlobFormatType(_eBlobFormat_ASNB);
    public static final BlobFormatType eBlobFormat_ASNT = new BlobFormatType(_eBlobFormat_ASNT);
    public static final BlobFormatType eBlobFormat_XML = new BlobFormatType(_eBlobFormat_XML);
    public static final BlobFormatType eBlobFormat_SDF = new BlobFormatType(_eBlobFormat_SDF);
    public static final BlobFormatType eBlobFormat_CSV = new BlobFormatType(_eBlobFormat_CSV);
    public static final BlobFormatType eBlobFormat_Text = new BlobFormatType(_eBlobFormat_Text);
    public static final BlobFormatType eBlobFormat_HTML = new BlobFormatType(_eBlobFormat_HTML);
    public static final BlobFormatType eBlobFormat_PNG = new BlobFormatType(_eBlobFormat_PNG);
    public static final BlobFormatType eBlobFormat_Other = new BlobFormatType(_eBlobFormat_Other);
    public java.lang.String getValue() { return _value_;}
    public static BlobFormatType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        BlobFormatType enumeration = (BlobFormatType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static BlobFormatType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(BlobFormatType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "BlobFormatType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
