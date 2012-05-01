/**
 * OntologyDataItemList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.chebi;

public class OntologyDataItemList  implements java.io.Serializable {
    private uk.ac.ebi.ws.chebi.OntologyDataItem[] listElement;

    public OntologyDataItemList() {
    }

    public OntologyDataItemList(
           uk.ac.ebi.ws.chebi.OntologyDataItem[] listElement) {
           this.listElement = listElement;
    }


    /**
     * Gets the listElement value for this OntologyDataItemList.
     * 
     * @return listElement
     */
    public uk.ac.ebi.ws.chebi.OntologyDataItem[] getListElement() {
        return listElement;
    }


    /**
     * Sets the listElement value for this OntologyDataItemList.
     * 
     * @param listElement
     */
    public void setListElement(uk.ac.ebi.ws.chebi.OntologyDataItem[] listElement) {
        this.listElement = listElement;
    }

    public uk.ac.ebi.ws.chebi.OntologyDataItem getListElement(int i) {
        return this.listElement[i];
    }

    public void setListElement(int i, uk.ac.ebi.ws.chebi.OntologyDataItem _value) {
        this.listElement[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OntologyDataItemList)) return false;
        OntologyDataItemList other = (OntologyDataItemList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.listElement==null && other.getListElement()==null) || 
             (this.listElement!=null &&
              java.util.Arrays.equals(this.listElement, other.getListElement())));
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
        if (getListElement() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListElement());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListElement(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OntologyDataItemList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/webservices/chebi", "OntologyDataItemList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listElement");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/webservices/chebi", "ListElement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/webservices/chebi", "OntologyDataItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
