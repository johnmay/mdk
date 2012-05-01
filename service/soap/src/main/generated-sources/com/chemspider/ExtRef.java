/**
 * ExtRef.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class ExtRef  implements java.io.Serializable {
    private int CSID;

    private java.lang.String ds_name;

    private java.lang.String ds_url;

    private java.lang.String ext_id;

    private java.lang.String ext_url;

    public ExtRef() {
    }

    public ExtRef(
           int CSID,
           java.lang.String ds_name,
           java.lang.String ds_url,
           java.lang.String ext_id,
           java.lang.String ext_url) {
           this.CSID = CSID;
           this.ds_name = ds_name;
           this.ds_url = ds_url;
           this.ext_id = ext_id;
           this.ext_url = ext_url;
    }


    /**
     * Gets the CSID value for this ExtRef.
     * 
     * @return CSID
     */
    public int getCSID() {
        return CSID;
    }


    /**
     * Sets the CSID value for this ExtRef.
     * 
     * @param CSID
     */
    public void setCSID(int CSID) {
        this.CSID = CSID;
    }


    /**
     * Gets the ds_name value for this ExtRef.
     * 
     * @return ds_name
     */
    public java.lang.String getDs_name() {
        return ds_name;
    }


    /**
     * Sets the ds_name value for this ExtRef.
     * 
     * @param ds_name
     */
    public void setDs_name(java.lang.String ds_name) {
        this.ds_name = ds_name;
    }


    /**
     * Gets the ds_url value for this ExtRef.
     * 
     * @return ds_url
     */
    public java.lang.String getDs_url() {
        return ds_url;
    }


    /**
     * Sets the ds_url value for this ExtRef.
     * 
     * @param ds_url
     */
    public void setDs_url(java.lang.String ds_url) {
        this.ds_url = ds_url;
    }


    /**
     * Gets the ext_id value for this ExtRef.
     * 
     * @return ext_id
     */
    public java.lang.String getExt_id() {
        return ext_id;
    }


    /**
     * Sets the ext_id value for this ExtRef.
     * 
     * @param ext_id
     */
    public void setExt_id(java.lang.String ext_id) {
        this.ext_id = ext_id;
    }


    /**
     * Gets the ext_url value for this ExtRef.
     * 
     * @return ext_url
     */
    public java.lang.String getExt_url() {
        return ext_url;
    }


    /**
     * Sets the ext_url value for this ExtRef.
     * 
     * @param ext_url
     */
    public void setExt_url(java.lang.String ext_url) {
        this.ext_url = ext_url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtRef)) return false;
        ExtRef other = (ExtRef) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.CSID == other.getCSID() &&
            ((this.ds_name==null && other.getDs_name()==null) || 
             (this.ds_name!=null &&
              this.ds_name.equals(other.getDs_name()))) &&
            ((this.ds_url==null && other.getDs_url()==null) || 
             (this.ds_url!=null &&
              this.ds_url.equals(other.getDs_url()))) &&
            ((this.ext_id==null && other.getExt_id()==null) || 
             (this.ext_id!=null &&
              this.ext_id.equals(other.getExt_id()))) &&
            ((this.ext_url==null && other.getExt_url()==null) || 
             (this.ext_url!=null &&
              this.ext_url.equals(other.getExt_url())));
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
        _hashCode += getCSID();
        if (getDs_name() != null) {
            _hashCode += getDs_name().hashCode();
        }
        if (getDs_url() != null) {
            _hashCode += getDs_url().hashCode();
        }
        if (getExt_id() != null) {
            _hashCode += getExt_id().hashCode();
        }
        if (getExt_url() != null) {
            _hashCode += getExt_url().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtRef.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExtRef"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CSID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ds_name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ds_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ds_url");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ds_url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ext_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ext_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ext_url");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ext_url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
