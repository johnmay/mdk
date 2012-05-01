/**
 * CompoundInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class CompoundInfo  implements java.io.Serializable {
    private int CSID;

    private java.lang.String inChI;

    private java.lang.String inChIKey;

    private java.lang.String SMILES;

    public CompoundInfo() {
    }

    public CompoundInfo(
           int CSID,
           java.lang.String inChI,
           java.lang.String inChIKey,
           java.lang.String SMILES) {
           this.CSID = CSID;
           this.inChI = inChI;
           this.inChIKey = inChIKey;
           this.SMILES = SMILES;
    }


    /**
     * Gets the CSID value for this CompoundInfo.
     * 
     * @return CSID
     */
    public int getCSID() {
        return CSID;
    }


    /**
     * Sets the CSID value for this CompoundInfo.
     * 
     * @param CSID
     */
    public void setCSID(int CSID) {
        this.CSID = CSID;
    }


    /**
     * Gets the inChI value for this CompoundInfo.
     * 
     * @return inChI
     */
    public java.lang.String getInChI() {
        return inChI;
    }


    /**
     * Sets the inChI value for this CompoundInfo.
     * 
     * @param inChI
     */
    public void setInChI(java.lang.String inChI) {
        this.inChI = inChI;
    }


    /**
     * Gets the inChIKey value for this CompoundInfo.
     * 
     * @return inChIKey
     */
    public java.lang.String getInChIKey() {
        return inChIKey;
    }


    /**
     * Sets the inChIKey value for this CompoundInfo.
     * 
     * @param inChIKey
     */
    public void setInChIKey(java.lang.String inChIKey) {
        this.inChIKey = inChIKey;
    }


    /**
     * Gets the SMILES value for this CompoundInfo.
     * 
     * @return SMILES
     */
    public java.lang.String getSMILES() {
        return SMILES;
    }


    /**
     * Sets the SMILES value for this CompoundInfo.
     * 
     * @param SMILES
     */
    public void setSMILES(java.lang.String SMILES) {
        this.SMILES = SMILES;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CompoundInfo)) return false;
        CompoundInfo other = (CompoundInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.CSID == other.getCSID() &&
            ((this.inChI==null && other.getInChI()==null) || 
             (this.inChI!=null &&
              this.inChI.equals(other.getInChI()))) &&
            ((this.inChIKey==null && other.getInChIKey()==null) || 
             (this.inChIKey!=null &&
              this.inChIKey.equals(other.getInChIKey()))) &&
            ((this.SMILES==null && other.getSMILES()==null) || 
             (this.SMILES!=null &&
              this.SMILES.equals(other.getSMILES())));
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
        if (getInChI() != null) {
            _hashCode += getInChI().hashCode();
        }
        if (getInChIKey() != null) {
            _hashCode += getInChIKey().hashCode();
        }
        if (getSMILES() != null) {
            _hashCode += getSMILES().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CompoundInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "CompoundInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CSID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inChI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "InChI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inChIKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "InChIKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SMILES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SMILES"));
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
