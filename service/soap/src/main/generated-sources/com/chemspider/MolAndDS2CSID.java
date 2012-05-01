/**
 * MolAndDS2CSID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class MolAndDS2CSID  implements java.io.Serializable {
    private java.lang.String mol;

    private com.chemspider.ExactSearchOptions options;

    private com.chemspider.ArrayOfString datasources;

    private java.lang.String token;

    public MolAndDS2CSID() {
    }

    public MolAndDS2CSID(
           java.lang.String mol,
           com.chemspider.ExactSearchOptions options,
           com.chemspider.ArrayOfString datasources,
           java.lang.String token) {
           this.mol = mol;
           this.options = options;
           this.datasources = datasources;
           this.token = token;
    }


    /**
     * Gets the mol value for this MolAndDS2CSID.
     * 
     * @return mol
     */
    public java.lang.String getMol() {
        return mol;
    }


    /**
     * Sets the mol value for this MolAndDS2CSID.
     * 
     * @param mol
     */
    public void setMol(java.lang.String mol) {
        this.mol = mol;
    }


    /**
     * Gets the options value for this MolAndDS2CSID.
     * 
     * @return options
     */
    public com.chemspider.ExactSearchOptions getOptions() {
        return options;
    }


    /**
     * Sets the options value for this MolAndDS2CSID.
     * 
     * @param options
     */
    public void setOptions(com.chemspider.ExactSearchOptions options) {
        this.options = options;
    }


    /**
     * Gets the datasources value for this MolAndDS2CSID.
     * 
     * @return datasources
     */
    public com.chemspider.ArrayOfString getDatasources() {
        return datasources;
    }


    /**
     * Sets the datasources value for this MolAndDS2CSID.
     * 
     * @param datasources
     */
    public void setDatasources(com.chemspider.ArrayOfString datasources) {
        this.datasources = datasources;
    }


    /**
     * Gets the token value for this MolAndDS2CSID.
     * 
     * @return token
     */
    public java.lang.String getToken() {
        return token;
    }


    /**
     * Sets the token value for this MolAndDS2CSID.
     * 
     * @param token
     */
    public void setToken(java.lang.String token) {
        this.token = token;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MolAndDS2CSID)) return false;
        MolAndDS2CSID other = (MolAndDS2CSID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mol==null && other.getMol()==null) || 
             (this.mol!=null &&
              this.mol.equals(other.getMol()))) &&
            ((this.options==null && other.getOptions()==null) || 
             (this.options!=null &&
              this.options.equals(other.getOptions()))) &&
            ((this.datasources==null && other.getDatasources()==null) || 
             (this.datasources!=null &&
              this.datasources.equals(other.getDatasources()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken())));
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
        if (getMol() != null) {
            _hashCode += getMol().hashCode();
        }
        if (getOptions() != null) {
            _hashCode += getOptions().hashCode();
        }
        if (getDatasources() != null) {
            _hashCode += getDatasources().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MolAndDS2CSID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", ">MolAndDS2CSID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "mol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("options");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "options"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExactSearchOptions"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datasources");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "datasources"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "token"));
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
