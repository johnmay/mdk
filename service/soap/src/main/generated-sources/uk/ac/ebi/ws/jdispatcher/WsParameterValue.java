/**
 * WsParameterValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;


/**
 * The details about a parameter values
 */
public class WsParameterValue  implements java.io.Serializable {
    /* A meaningful label for the parameter value */
    private java.lang.String label;

    /* The real value */
    private java.lang.String value;

    /* A flag indicating whether this value is a default value or
     * not */
    private boolean defaultValue;

    /* A set of additional properties associated with the parameter
     * value */
    private uk.ac.ebi.ws.jdispatcher.WsProperties properties;

    public WsParameterValue() {
    }

    public WsParameterValue(
           java.lang.String label,
           java.lang.String value,
           boolean defaultValue,
           uk.ac.ebi.ws.jdispatcher.WsProperties properties) {
           this.label = label;
           this.value = value;
           this.defaultValue = defaultValue;
           this.properties = properties;
    }


    /**
     * Gets the label value for this WsParameterValue.
     * 
     * @return label   * A meaningful label for the parameter value
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this WsParameterValue.
     * 
     * @param label   * A meaningful label for the parameter value
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the value value for this WsParameterValue.
     * 
     * @return value   * The real value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this WsParameterValue.
     * 
     * @param value   * The real value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }


    /**
     * Gets the defaultValue value for this WsParameterValue.
     * 
     * @return defaultValue   * A flag indicating whether this value is a default value or
     * not
     */
    public boolean isDefaultValue() {
        return defaultValue;
    }


    /**
     * Sets the defaultValue value for this WsParameterValue.
     * 
     * @param defaultValue   * A flag indicating whether this value is a default value or
     * not
     */
    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }


    /**
     * Gets the properties value for this WsParameterValue.
     * 
     * @return properties   * A set of additional properties associated with the parameter
     * value
     */
    public uk.ac.ebi.ws.jdispatcher.WsProperties getProperties() {
        return properties;
    }


    /**
     * Sets the properties value for this WsParameterValue.
     * 
     * @param properties   * A set of additional properties associated with the parameter
     * value
     */
    public void setProperties(uk.ac.ebi.ws.jdispatcher.WsProperties properties) {
        this.properties = properties;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsParameterValue)) return false;
        WsParameterValue other = (WsParameterValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            this.defaultValue == other.isDefaultValue() &&
            ((this.properties==null && other.getProperties()==null) || 
             (this.properties!=null &&
              this.properties.equals(other.getProperties())));
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
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        _hashCode += (isDefaultValue() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getProperties() != null) {
            _hashCode += getProperties().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsParameterValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsParameterValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "defaultValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("properties");
        elemField.setXmlName(new javax.xml.namespace.QName("", "properties"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsProperties"));
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
