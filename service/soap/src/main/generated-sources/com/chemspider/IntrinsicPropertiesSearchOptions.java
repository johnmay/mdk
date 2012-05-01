/**
 * IntrinsicPropertiesSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider;

public class IntrinsicPropertiesSearchOptions  extends com.chemspider.SearchOptions  implements java.io.Serializable {
    private java.lang.String empiricalFormula;

    private java.lang.Double molWeightMin;

    private java.lang.Double molWeightMax;

    private java.lang.Double nominalMassMin;

    private java.lang.Double nominalMassMax;

    private java.lang.Double averageMassMin;

    private java.lang.Double averageMassMax;

    private java.lang.Double monoisotopicMassMin;

    private java.lang.Double monoisotopicMassMax;

    public IntrinsicPropertiesSearchOptions() {
    }

    public IntrinsicPropertiesSearchOptions(
           java.lang.String empiricalFormula,
           java.lang.Double molWeightMin,
           java.lang.Double molWeightMax,
           java.lang.Double nominalMassMin,
           java.lang.Double nominalMassMax,
           java.lang.Double averageMassMin,
           java.lang.Double averageMassMax,
           java.lang.Double monoisotopicMassMin,
           java.lang.Double monoisotopicMassMax) {
        this.empiricalFormula = empiricalFormula;
        this.molWeightMin = molWeightMin;
        this.molWeightMax = molWeightMax;
        this.nominalMassMin = nominalMassMin;
        this.nominalMassMax = nominalMassMax;
        this.averageMassMin = averageMassMin;
        this.averageMassMax = averageMassMax;
        this.monoisotopicMassMin = monoisotopicMassMin;
        this.monoisotopicMassMax = monoisotopicMassMax;
    }


    /**
     * Gets the empiricalFormula value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return empiricalFormula
     */
    public java.lang.String getEmpiricalFormula() {
        return empiricalFormula;
    }


    /**
     * Sets the empiricalFormula value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param empiricalFormula
     */
    public void setEmpiricalFormula(java.lang.String empiricalFormula) {
        this.empiricalFormula = empiricalFormula;
    }


    /**
     * Gets the molWeightMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return molWeightMin
     */
    public java.lang.Double getMolWeightMin() {
        return molWeightMin;
    }


    /**
     * Sets the molWeightMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param molWeightMin
     */
    public void setMolWeightMin(java.lang.Double molWeightMin) {
        this.molWeightMin = molWeightMin;
    }


    /**
     * Gets the molWeightMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return molWeightMax
     */
    public java.lang.Double getMolWeightMax() {
        return molWeightMax;
    }


    /**
     * Sets the molWeightMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param molWeightMax
     */
    public void setMolWeightMax(java.lang.Double molWeightMax) {
        this.molWeightMax = molWeightMax;
    }


    /**
     * Gets the nominalMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return nominalMassMin
     */
    public java.lang.Double getNominalMassMin() {
        return nominalMassMin;
    }


    /**
     * Sets the nominalMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param nominalMassMin
     */
    public void setNominalMassMin(java.lang.Double nominalMassMin) {
        this.nominalMassMin = nominalMassMin;
    }


    /**
     * Gets the nominalMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return nominalMassMax
     */
    public java.lang.Double getNominalMassMax() {
        return nominalMassMax;
    }


    /**
     * Sets the nominalMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param nominalMassMax
     */
    public void setNominalMassMax(java.lang.Double nominalMassMax) {
        this.nominalMassMax = nominalMassMax;
    }


    /**
     * Gets the averageMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return averageMassMin
     */
    public java.lang.Double getAverageMassMin() {
        return averageMassMin;
    }


    /**
     * Sets the averageMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param averageMassMin
     */
    public void setAverageMassMin(java.lang.Double averageMassMin) {
        this.averageMassMin = averageMassMin;
    }


    /**
     * Gets the averageMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return averageMassMax
     */
    public java.lang.Double getAverageMassMax() {
        return averageMassMax;
    }


    /**
     * Sets the averageMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param averageMassMax
     */
    public void setAverageMassMax(java.lang.Double averageMassMax) {
        this.averageMassMax = averageMassMax;
    }


    /**
     * Gets the monoisotopicMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return monoisotopicMassMin
     */
    public java.lang.Double getMonoisotopicMassMin() {
        return monoisotopicMassMin;
    }


    /**
     * Sets the monoisotopicMassMin value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param monoisotopicMassMin
     */
    public void setMonoisotopicMassMin(java.lang.Double monoisotopicMassMin) {
        this.monoisotopicMassMin = monoisotopicMassMin;
    }


    /**
     * Gets the monoisotopicMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @return monoisotopicMassMax
     */
    public java.lang.Double getMonoisotopicMassMax() {
        return monoisotopicMassMax;
    }


    /**
     * Sets the monoisotopicMassMax value for this IntrinsicPropertiesSearchOptions.
     * 
     * @param monoisotopicMassMax
     */
    public void setMonoisotopicMassMax(java.lang.Double monoisotopicMassMax) {
        this.monoisotopicMassMax = monoisotopicMassMax;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IntrinsicPropertiesSearchOptions)) return false;
        IntrinsicPropertiesSearchOptions other = (IntrinsicPropertiesSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.empiricalFormula==null && other.getEmpiricalFormula()==null) || 
             (this.empiricalFormula!=null &&
              this.empiricalFormula.equals(other.getEmpiricalFormula()))) &&
            ((this.molWeightMin==null && other.getMolWeightMin()==null) || 
             (this.molWeightMin!=null &&
              this.molWeightMin.equals(other.getMolWeightMin()))) &&
            ((this.molWeightMax==null && other.getMolWeightMax()==null) || 
             (this.molWeightMax!=null &&
              this.molWeightMax.equals(other.getMolWeightMax()))) &&
            ((this.nominalMassMin==null && other.getNominalMassMin()==null) || 
             (this.nominalMassMin!=null &&
              this.nominalMassMin.equals(other.getNominalMassMin()))) &&
            ((this.nominalMassMax==null && other.getNominalMassMax()==null) || 
             (this.nominalMassMax!=null &&
              this.nominalMassMax.equals(other.getNominalMassMax()))) &&
            ((this.averageMassMin==null && other.getAverageMassMin()==null) || 
             (this.averageMassMin!=null &&
              this.averageMassMin.equals(other.getAverageMassMin()))) &&
            ((this.averageMassMax==null && other.getAverageMassMax()==null) || 
             (this.averageMassMax!=null &&
              this.averageMassMax.equals(other.getAverageMassMax()))) &&
            ((this.monoisotopicMassMin==null && other.getMonoisotopicMassMin()==null) || 
             (this.monoisotopicMassMin!=null &&
              this.monoisotopicMassMin.equals(other.getMonoisotopicMassMin()))) &&
            ((this.monoisotopicMassMax==null && other.getMonoisotopicMassMax()==null) || 
             (this.monoisotopicMassMax!=null &&
              this.monoisotopicMassMax.equals(other.getMonoisotopicMassMax())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getEmpiricalFormula() != null) {
            _hashCode += getEmpiricalFormula().hashCode();
        }
        if (getMolWeightMin() != null) {
            _hashCode += getMolWeightMin().hashCode();
        }
        if (getMolWeightMax() != null) {
            _hashCode += getMolWeightMax().hashCode();
        }
        if (getNominalMassMin() != null) {
            _hashCode += getNominalMassMin().hashCode();
        }
        if (getNominalMassMax() != null) {
            _hashCode += getNominalMassMax().hashCode();
        }
        if (getAverageMassMin() != null) {
            _hashCode += getAverageMassMin().hashCode();
        }
        if (getAverageMassMax() != null) {
            _hashCode += getAverageMassMax().hashCode();
        }
        if (getMonoisotopicMassMin() != null) {
            _hashCode += getMonoisotopicMassMin().hashCode();
        }
        if (getMonoisotopicMassMax() != null) {
            _hashCode += getMonoisotopicMassMax().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IntrinsicPropertiesSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "IntrinsicPropertiesSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empiricalFormula");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "EmpiricalFormula"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("molWeightMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MolWeightMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("molWeightMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MolWeightMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nominalMassMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "NominalMassMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nominalMassMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "NominalMassMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averageMassMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "AverageMassMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averageMassMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "AverageMassMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monoisotopicMassMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MonoisotopicMassMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monoisotopicMassMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MonoisotopicMassMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
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
