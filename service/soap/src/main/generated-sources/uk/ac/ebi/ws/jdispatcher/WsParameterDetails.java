/**
 * WsParameterDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;


/**
 * Details about a tool parameter
 */
public class WsParameterDetails  implements java.io.Serializable {
    /* The name of the parameter */
    private java.lang.String name;

    /* A short description of the parameter */
    private java.lang.String description;

    /* The type of the parameter */
    private java.lang.String type;

    /* The list of values available for this parameter */
    private uk.ac.ebi.ws.jdispatcher.WsParameterValues values;

    public WsParameterDetails() {
    }

    public WsParameterDetails(
           java.lang.String name,
           java.lang.String description,
           java.lang.String type,
           uk.ac.ebi.ws.jdispatcher.WsParameterValues values) {
           this.name = name;
           this.description = description;
           this.type = type;
           this.values = values;
    }


    /**
     * Gets the name value for this WsParameterDetails.
     * 
     * @return name   * The name of the parameter
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this WsParameterDetails.
     * 
     * @param name   * The name of the parameter
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this WsParameterDetails.
     * 
     * @return description   * A short description of the parameter
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this WsParameterDetails.
     * 
     * @param description   * A short description of the parameter
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the type value for this WsParameterDetails.
     * 
     * @return type   * The type of the parameter
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this WsParameterDetails.
     * 
     * @param type   * The type of the parameter
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the values value for this WsParameterDetails.
     * 
     * @return values   * The list of values available for this parameter
     */
    public uk.ac.ebi.ws.jdispatcher.WsParameterValues getValues() {
        return values;
    }


    /**
     * Sets the values value for this WsParameterDetails.
     * 
     * @param values   * The list of values available for this parameter
     */
    public void setValues(uk.ac.ebi.ws.jdispatcher.WsParameterValues values) {
        this.values = values;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsParameterDetails)) return false;
        WsParameterDetails other = (WsParameterDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.values==null && other.getValues()==null) || 
             (this.values!=null &&
              this.values.equals(other.getValues())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getValues() != null) {
            _hashCode += getValues().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsParameterDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsParameterDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("values");
        elemField.setXmlName(new javax.xml.namespace.QName("", "values"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsParameterValues"));
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
