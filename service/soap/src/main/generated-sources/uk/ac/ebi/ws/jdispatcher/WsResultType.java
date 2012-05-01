/**
 * WsResultType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;


/**
 * Details about a renderer used to output the result of the job
 */
public class WsResultType  implements java.io.Serializable {
    /* A short description of the renderer */
    private java.lang.String description;

    /* A suggested file suffix to be used when saving the data formatted
     * by the renderer */
    private java.lang.String fileSuffix;

    /* The renderer identifier to be used when invoking the getResult()
     * method */
    private java.lang.String identifier;

    /* A more appropriate name for the renderer (more meaningful than
     * the identifier) */
    private java.lang.String label;

    /* The media type (MIME) of the renderer's output */
    private java.lang.String mediaType;

    public WsResultType() {
    }

    public WsResultType(
           java.lang.String description,
           java.lang.String fileSuffix,
           java.lang.String identifier,
           java.lang.String label,
           java.lang.String mediaType) {
           this.description = description;
           this.fileSuffix = fileSuffix;
           this.identifier = identifier;
           this.label = label;
           this.mediaType = mediaType;
    }


    /**
     * Gets the description value for this WsResultType.
     * 
     * @return description   * A short description of the renderer
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this WsResultType.
     * 
     * @param description   * A short description of the renderer
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the fileSuffix value for this WsResultType.
     * 
     * @return fileSuffix   * A suggested file suffix to be used when saving the data formatted
     * by the renderer
     */
    public java.lang.String getFileSuffix() {
        return fileSuffix;
    }


    /**
     * Sets the fileSuffix value for this WsResultType.
     * 
     * @param fileSuffix   * A suggested file suffix to be used when saving the data formatted
     * by the renderer
     */
    public void setFileSuffix(java.lang.String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }


    /**
     * Gets the identifier value for this WsResultType.
     * 
     * @return identifier   * The renderer identifier to be used when invoking the getResult()
     * method
     */
    public java.lang.String getIdentifier() {
        return identifier;
    }


    /**
     * Sets the identifier value for this WsResultType.
     * 
     * @param identifier   * The renderer identifier to be used when invoking the getResult()
     * method
     */
    public void setIdentifier(java.lang.String identifier) {
        this.identifier = identifier;
    }


    /**
     * Gets the label value for this WsResultType.
     * 
     * @return label   * A more appropriate name for the renderer (more meaningful than
     * the identifier)
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this WsResultType.
     * 
     * @param label   * A more appropriate name for the renderer (more meaningful than
     * the identifier)
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the mediaType value for this WsResultType.
     * 
     * @return mediaType   * The media type (MIME) of the renderer's output
     */
    public java.lang.String getMediaType() {
        return mediaType;
    }


    /**
     * Sets the mediaType value for this WsResultType.
     * 
     * @param mediaType   * The media type (MIME) of the renderer's output
     */
    public void setMediaType(java.lang.String mediaType) {
        this.mediaType = mediaType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsResultType)) return false;
        WsResultType other = (WsResultType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.fileSuffix==null && other.getFileSuffix()==null) || 
             (this.fileSuffix!=null &&
              this.fileSuffix.equals(other.getFileSuffix()))) &&
            ((this.identifier==null && other.getIdentifier()==null) || 
             (this.identifier!=null &&
              this.identifier.equals(other.getIdentifier()))) &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.mediaType==null && other.getMediaType()==null) || 
             (this.mediaType!=null &&
              this.mediaType.equals(other.getMediaType())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getFileSuffix() != null) {
            _hashCode += getFileSuffix().hashCode();
        }
        if (getIdentifier() != null) {
            _hashCode += getIdentifier().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getMediaType() != null) {
            _hashCode += getMediaType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsResultType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "wsResultType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileSuffix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fileSuffix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identifier");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mediaType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mediaType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
