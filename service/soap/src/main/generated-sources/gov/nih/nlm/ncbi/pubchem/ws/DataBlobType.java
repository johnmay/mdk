/**
 * DataBlobType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem.ws;

public class DataBlobType  implements java.io.Serializable {
    private byte[] data;

    private gov.nih.nlm.ncbi.pubchem.ws.BlobFormatType blobFormat;

    private gov.nih.nlm.ncbi.pubchem.ws.CompressType eCompress;

    public DataBlobType() {
    }

    public DataBlobType(
           byte[] data,
           gov.nih.nlm.ncbi.pubchem.ws.BlobFormatType blobFormat,
           gov.nih.nlm.ncbi.pubchem.ws.CompressType eCompress) {
           this.data = data;
           this.blobFormat = blobFormat;
           this.eCompress = eCompress;
    }


    /**
     * Gets the data value for this DataBlobType.
     * 
     * @return data
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Sets the data value for this DataBlobType.
     * 
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }


    /**
     * Gets the blobFormat value for this DataBlobType.
     * 
     * @return blobFormat
     */
    public gov.nih.nlm.ncbi.pubchem.ws.BlobFormatType getBlobFormat() {
        return blobFormat;
    }


    /**
     * Sets the blobFormat value for this DataBlobType.
     * 
     * @param blobFormat
     */
    public void setBlobFormat(gov.nih.nlm.ncbi.pubchem.ws.BlobFormatType blobFormat) {
        this.blobFormat = blobFormat;
    }


    /**
     * Gets the eCompress value for this DataBlobType.
     * 
     * @return eCompress
     */
    public gov.nih.nlm.ncbi.pubchem.ws.CompressType getECompress() {
        return eCompress;
    }


    /**
     * Sets the eCompress value for this DataBlobType.
     * 
     * @param eCompress
     */
    public void setECompress(gov.nih.nlm.ncbi.pubchem.ws.CompressType eCompress) {
        this.eCompress = eCompress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DataBlobType)) return false;
        DataBlobType other = (DataBlobType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              java.util.Arrays.equals(this.data, other.getData()))) &&
            ((this.blobFormat==null && other.getBlobFormat()==null) || 
             (this.blobFormat!=null &&
              this.blobFormat.equals(other.getBlobFormat()))) &&
            ((this.eCompress==null && other.getECompress()==null) || 
             (this.eCompress!=null &&
              this.eCompress.equals(other.getECompress())));
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
        if (getData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBlobFormat() != null) {
            _hashCode += getBlobFormat().hashCode();
        }
        if (getECompress() != null) {
            _hashCode += getECompress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DataBlobType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "DataBlobType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blobFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "BlobFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "BlobFormatType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ECompress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "eCompress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CompressType"));
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
