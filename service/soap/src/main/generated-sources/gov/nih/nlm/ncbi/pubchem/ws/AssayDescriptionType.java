/**
 * AssayDescriptionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem.ws;

public class AssayDescriptionType  implements java.io.Serializable {
    private java.lang.String name;

    private gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString description;

    private gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString protocol;

    private gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString comment;

    private int numberOfTIDs;

    private boolean hasScore;

    private java.lang.String method;

    private gov.nih.nlm.ncbi.pubchem.ws.ArrayOfTargets targets;

    private java.lang.Integer version;

    private java.lang.Integer revision;

    private java.lang.Integer lastDataChange;

    private java.lang.Integer SIDCountAll;

    private java.lang.Integer SIDCountActive;

    private java.lang.Integer SIDCountInactive;

    private java.lang.Integer SIDCountInconclusive;

    private java.lang.Integer SIDCountUnspecified;

    private java.lang.Integer SIDCountProbe;

    private java.lang.Integer CIDCountAll;

    private java.lang.Integer CIDCountActive;

    private java.lang.Integer CIDCountInactive;

    private java.lang.Integer CIDCountInconclusive;

    private java.lang.Integer CIDCountUnspecified;

    private java.lang.Integer CIDCountProbe;

    public AssayDescriptionType() {
    }

    public AssayDescriptionType(
           java.lang.String name,
           gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString description,
           gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString protocol,
           gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString comment,
           int numberOfTIDs,
           boolean hasScore,
           java.lang.String method,
           gov.nih.nlm.ncbi.pubchem.ws.ArrayOfTargets targets,
           java.lang.Integer version,
           java.lang.Integer revision,
           java.lang.Integer lastDataChange,
           java.lang.Integer SIDCountAll,
           java.lang.Integer SIDCountActive,
           java.lang.Integer SIDCountInactive,
           java.lang.Integer SIDCountInconclusive,
           java.lang.Integer SIDCountUnspecified,
           java.lang.Integer SIDCountProbe,
           java.lang.Integer CIDCountAll,
           java.lang.Integer CIDCountActive,
           java.lang.Integer CIDCountInactive,
           java.lang.Integer CIDCountInconclusive,
           java.lang.Integer CIDCountUnspecified,
           java.lang.Integer CIDCountProbe) {
           this.name = name;
           this.description = description;
           this.protocol = protocol;
           this.comment = comment;
           this.numberOfTIDs = numberOfTIDs;
           this.hasScore = hasScore;
           this.method = method;
           this.targets = targets;
           this.version = version;
           this.revision = revision;
           this.lastDataChange = lastDataChange;
           this.SIDCountAll = SIDCountAll;
           this.SIDCountActive = SIDCountActive;
           this.SIDCountInactive = SIDCountInactive;
           this.SIDCountInconclusive = SIDCountInconclusive;
           this.SIDCountUnspecified = SIDCountUnspecified;
           this.SIDCountProbe = SIDCountProbe;
           this.CIDCountAll = CIDCountAll;
           this.CIDCountActive = CIDCountActive;
           this.CIDCountInactive = CIDCountInactive;
           this.CIDCountInconclusive = CIDCountInconclusive;
           this.CIDCountUnspecified = CIDCountUnspecified;
           this.CIDCountProbe = CIDCountProbe;
    }


    /**
     * Gets the name value for this AssayDescriptionType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this AssayDescriptionType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this AssayDescriptionType.
     * 
     * @return description
     */
    public gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString getDescription() {
        return description;
    }


    /**
     * Sets the description value for this AssayDescriptionType.
     * 
     * @param description
     */
    public void setDescription(gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString description) {
        this.description = description;
    }


    /**
     * Gets the protocol value for this AssayDescriptionType.
     * 
     * @return protocol
     */
    public gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString getProtocol() {
        return protocol;
    }


    /**
     * Sets the protocol value for this AssayDescriptionType.
     * 
     * @param protocol
     */
    public void setProtocol(gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString protocol) {
        this.protocol = protocol;
    }


    /**
     * Gets the comment value for this AssayDescriptionType.
     * 
     * @return comment
     */
    public gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this AssayDescriptionType.
     * 
     * @param comment
     */
    public void setComment(gov.nih.nlm.ncbi.pubchem.ws.ArrayOfString comment) {
        this.comment = comment;
    }


    /**
     * Gets the numberOfTIDs value for this AssayDescriptionType.
     * 
     * @return numberOfTIDs
     */
    public int getNumberOfTIDs() {
        return numberOfTIDs;
    }


    /**
     * Sets the numberOfTIDs value for this AssayDescriptionType.
     * 
     * @param numberOfTIDs
     */
    public void setNumberOfTIDs(int numberOfTIDs) {
        this.numberOfTIDs = numberOfTIDs;
    }


    /**
     * Gets the hasScore value for this AssayDescriptionType.
     * 
     * @return hasScore
     */
    public boolean isHasScore() {
        return hasScore;
    }


    /**
     * Sets the hasScore value for this AssayDescriptionType.
     * 
     * @param hasScore
     */
    public void setHasScore(boolean hasScore) {
        this.hasScore = hasScore;
    }


    /**
     * Gets the method value for this AssayDescriptionType.
     * 
     * @return method
     */
    public java.lang.String getMethod() {
        return method;
    }


    /**
     * Sets the method value for this AssayDescriptionType.
     * 
     * @param method
     */
    public void setMethod(java.lang.String method) {
        this.method = method;
    }


    /**
     * Gets the targets value for this AssayDescriptionType.
     * 
     * @return targets
     */
    public gov.nih.nlm.ncbi.pubchem.ws.ArrayOfTargets getTargets() {
        return targets;
    }


    /**
     * Sets the targets value for this AssayDescriptionType.
     * 
     * @param targets
     */
    public void setTargets(gov.nih.nlm.ncbi.pubchem.ws.ArrayOfTargets targets) {
        this.targets = targets;
    }


    /**
     * Gets the version value for this AssayDescriptionType.
     * 
     * @return version
     */
    public java.lang.Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this AssayDescriptionType.
     * 
     * @param version
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }


    /**
     * Gets the revision value for this AssayDescriptionType.
     * 
     * @return revision
     */
    public java.lang.Integer getRevision() {
        return revision;
    }


    /**
     * Sets the revision value for this AssayDescriptionType.
     * 
     * @param revision
     */
    public void setRevision(java.lang.Integer revision) {
        this.revision = revision;
    }


    /**
     * Gets the lastDataChange value for this AssayDescriptionType.
     * 
     * @return lastDataChange
     */
    public java.lang.Integer getLastDataChange() {
        return lastDataChange;
    }


    /**
     * Sets the lastDataChange value for this AssayDescriptionType.
     * 
     * @param lastDataChange
     */
    public void setLastDataChange(java.lang.Integer lastDataChange) {
        this.lastDataChange = lastDataChange;
    }


    /**
     * Gets the SIDCountAll value for this AssayDescriptionType.
     * 
     * @return SIDCountAll
     */
    public java.lang.Integer getSIDCountAll() {
        return SIDCountAll;
    }


    /**
     * Sets the SIDCountAll value for this AssayDescriptionType.
     * 
     * @param SIDCountAll
     */
    public void setSIDCountAll(java.lang.Integer SIDCountAll) {
        this.SIDCountAll = SIDCountAll;
    }


    /**
     * Gets the SIDCountActive value for this AssayDescriptionType.
     * 
     * @return SIDCountActive
     */
    public java.lang.Integer getSIDCountActive() {
        return SIDCountActive;
    }


    /**
     * Sets the SIDCountActive value for this AssayDescriptionType.
     * 
     * @param SIDCountActive
     */
    public void setSIDCountActive(java.lang.Integer SIDCountActive) {
        this.SIDCountActive = SIDCountActive;
    }


    /**
     * Gets the SIDCountInactive value for this AssayDescriptionType.
     * 
     * @return SIDCountInactive
     */
    public java.lang.Integer getSIDCountInactive() {
        return SIDCountInactive;
    }


    /**
     * Sets the SIDCountInactive value for this AssayDescriptionType.
     * 
     * @param SIDCountInactive
     */
    public void setSIDCountInactive(java.lang.Integer SIDCountInactive) {
        this.SIDCountInactive = SIDCountInactive;
    }


    /**
     * Gets the SIDCountInconclusive value for this AssayDescriptionType.
     * 
     * @return SIDCountInconclusive
     */
    public java.lang.Integer getSIDCountInconclusive() {
        return SIDCountInconclusive;
    }


    /**
     * Sets the SIDCountInconclusive value for this AssayDescriptionType.
     * 
     * @param SIDCountInconclusive
     */
    public void setSIDCountInconclusive(java.lang.Integer SIDCountInconclusive) {
        this.SIDCountInconclusive = SIDCountInconclusive;
    }


    /**
     * Gets the SIDCountUnspecified value for this AssayDescriptionType.
     * 
     * @return SIDCountUnspecified
     */
    public java.lang.Integer getSIDCountUnspecified() {
        return SIDCountUnspecified;
    }


    /**
     * Sets the SIDCountUnspecified value for this AssayDescriptionType.
     * 
     * @param SIDCountUnspecified
     */
    public void setSIDCountUnspecified(java.lang.Integer SIDCountUnspecified) {
        this.SIDCountUnspecified = SIDCountUnspecified;
    }


    /**
     * Gets the SIDCountProbe value for this AssayDescriptionType.
     * 
     * @return SIDCountProbe
     */
    public java.lang.Integer getSIDCountProbe() {
        return SIDCountProbe;
    }


    /**
     * Sets the SIDCountProbe value for this AssayDescriptionType.
     * 
     * @param SIDCountProbe
     */
    public void setSIDCountProbe(java.lang.Integer SIDCountProbe) {
        this.SIDCountProbe = SIDCountProbe;
    }


    /**
     * Gets the CIDCountAll value for this AssayDescriptionType.
     * 
     * @return CIDCountAll
     */
    public java.lang.Integer getCIDCountAll() {
        return CIDCountAll;
    }


    /**
     * Sets the CIDCountAll value for this AssayDescriptionType.
     * 
     * @param CIDCountAll
     */
    public void setCIDCountAll(java.lang.Integer CIDCountAll) {
        this.CIDCountAll = CIDCountAll;
    }


    /**
     * Gets the CIDCountActive value for this AssayDescriptionType.
     * 
     * @return CIDCountActive
     */
    public java.lang.Integer getCIDCountActive() {
        return CIDCountActive;
    }


    /**
     * Sets the CIDCountActive value for this AssayDescriptionType.
     * 
     * @param CIDCountActive
     */
    public void setCIDCountActive(java.lang.Integer CIDCountActive) {
        this.CIDCountActive = CIDCountActive;
    }


    /**
     * Gets the CIDCountInactive value for this AssayDescriptionType.
     * 
     * @return CIDCountInactive
     */
    public java.lang.Integer getCIDCountInactive() {
        return CIDCountInactive;
    }


    /**
     * Sets the CIDCountInactive value for this AssayDescriptionType.
     * 
     * @param CIDCountInactive
     */
    public void setCIDCountInactive(java.lang.Integer CIDCountInactive) {
        this.CIDCountInactive = CIDCountInactive;
    }


    /**
     * Gets the CIDCountInconclusive value for this AssayDescriptionType.
     * 
     * @return CIDCountInconclusive
     */
    public java.lang.Integer getCIDCountInconclusive() {
        return CIDCountInconclusive;
    }


    /**
     * Sets the CIDCountInconclusive value for this AssayDescriptionType.
     * 
     * @param CIDCountInconclusive
     */
    public void setCIDCountInconclusive(java.lang.Integer CIDCountInconclusive) {
        this.CIDCountInconclusive = CIDCountInconclusive;
    }


    /**
     * Gets the CIDCountUnspecified value for this AssayDescriptionType.
     * 
     * @return CIDCountUnspecified
     */
    public java.lang.Integer getCIDCountUnspecified() {
        return CIDCountUnspecified;
    }


    /**
     * Sets the CIDCountUnspecified value for this AssayDescriptionType.
     * 
     * @param CIDCountUnspecified
     */
    public void setCIDCountUnspecified(java.lang.Integer CIDCountUnspecified) {
        this.CIDCountUnspecified = CIDCountUnspecified;
    }


    /**
     * Gets the CIDCountProbe value for this AssayDescriptionType.
     * 
     * @return CIDCountProbe
     */
    public java.lang.Integer getCIDCountProbe() {
        return CIDCountProbe;
    }


    /**
     * Sets the CIDCountProbe value for this AssayDescriptionType.
     * 
     * @param CIDCountProbe
     */
    public void setCIDCountProbe(java.lang.Integer CIDCountProbe) {
        this.CIDCountProbe = CIDCountProbe;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AssayDescriptionType)) return false;
        AssayDescriptionType other = (AssayDescriptionType) obj;
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
            ((this.protocol==null && other.getProtocol()==null) || 
             (this.protocol!=null &&
              this.protocol.equals(other.getProtocol()))) &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              this.comment.equals(other.getComment()))) &&
            this.numberOfTIDs == other.getNumberOfTIDs() &&
            this.hasScore == other.isHasScore() &&
            ((this.method==null && other.getMethod()==null) || 
             (this.method!=null &&
              this.method.equals(other.getMethod()))) &&
            ((this.targets==null && other.getTargets()==null) || 
             (this.targets!=null &&
              this.targets.equals(other.getTargets()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.revision==null && other.getRevision()==null) || 
             (this.revision!=null &&
              this.revision.equals(other.getRevision()))) &&
            ((this.lastDataChange==null && other.getLastDataChange()==null) || 
             (this.lastDataChange!=null &&
              this.lastDataChange.equals(other.getLastDataChange()))) &&
            ((this.SIDCountAll==null && other.getSIDCountAll()==null) || 
             (this.SIDCountAll!=null &&
              this.SIDCountAll.equals(other.getSIDCountAll()))) &&
            ((this.SIDCountActive==null && other.getSIDCountActive()==null) || 
             (this.SIDCountActive!=null &&
              this.SIDCountActive.equals(other.getSIDCountActive()))) &&
            ((this.SIDCountInactive==null && other.getSIDCountInactive()==null) || 
             (this.SIDCountInactive!=null &&
              this.SIDCountInactive.equals(other.getSIDCountInactive()))) &&
            ((this.SIDCountInconclusive==null && other.getSIDCountInconclusive()==null) || 
             (this.SIDCountInconclusive!=null &&
              this.SIDCountInconclusive.equals(other.getSIDCountInconclusive()))) &&
            ((this.SIDCountUnspecified==null && other.getSIDCountUnspecified()==null) || 
             (this.SIDCountUnspecified!=null &&
              this.SIDCountUnspecified.equals(other.getSIDCountUnspecified()))) &&
            ((this.SIDCountProbe==null && other.getSIDCountProbe()==null) || 
             (this.SIDCountProbe!=null &&
              this.SIDCountProbe.equals(other.getSIDCountProbe()))) &&
            ((this.CIDCountAll==null && other.getCIDCountAll()==null) || 
             (this.CIDCountAll!=null &&
              this.CIDCountAll.equals(other.getCIDCountAll()))) &&
            ((this.CIDCountActive==null && other.getCIDCountActive()==null) || 
             (this.CIDCountActive!=null &&
              this.CIDCountActive.equals(other.getCIDCountActive()))) &&
            ((this.CIDCountInactive==null && other.getCIDCountInactive()==null) || 
             (this.CIDCountInactive!=null &&
              this.CIDCountInactive.equals(other.getCIDCountInactive()))) &&
            ((this.CIDCountInconclusive==null && other.getCIDCountInconclusive()==null) || 
             (this.CIDCountInconclusive!=null &&
              this.CIDCountInconclusive.equals(other.getCIDCountInconclusive()))) &&
            ((this.CIDCountUnspecified==null && other.getCIDCountUnspecified()==null) || 
             (this.CIDCountUnspecified!=null &&
              this.CIDCountUnspecified.equals(other.getCIDCountUnspecified()))) &&
            ((this.CIDCountProbe==null && other.getCIDCountProbe()==null) || 
             (this.CIDCountProbe!=null &&
              this.CIDCountProbe.equals(other.getCIDCountProbe())));
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
        if (getProtocol() != null) {
            _hashCode += getProtocol().hashCode();
        }
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        _hashCode += getNumberOfTIDs();
        _hashCode += (isHasScore() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMethod() != null) {
            _hashCode += getMethod().hashCode();
        }
        if (getTargets() != null) {
            _hashCode += getTargets().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getRevision() != null) {
            _hashCode += getRevision().hashCode();
        }
        if (getLastDataChange() != null) {
            _hashCode += getLastDataChange().hashCode();
        }
        if (getSIDCountAll() != null) {
            _hashCode += getSIDCountAll().hashCode();
        }
        if (getSIDCountActive() != null) {
            _hashCode += getSIDCountActive().hashCode();
        }
        if (getSIDCountInactive() != null) {
            _hashCode += getSIDCountInactive().hashCode();
        }
        if (getSIDCountInconclusive() != null) {
            _hashCode += getSIDCountInconclusive().hashCode();
        }
        if (getSIDCountUnspecified() != null) {
            _hashCode += getSIDCountUnspecified().hashCode();
        }
        if (getSIDCountProbe() != null) {
            _hashCode += getSIDCountProbe().hashCode();
        }
        if (getCIDCountAll() != null) {
            _hashCode += getCIDCountAll().hashCode();
        }
        if (getCIDCountActive() != null) {
            _hashCode += getCIDCountActive().hashCode();
        }
        if (getCIDCountInactive() != null) {
            _hashCode += getCIDCountInactive().hashCode();
        }
        if (getCIDCountInconclusive() != null) {
            _hashCode += getCIDCountInconclusive().hashCode();
        }
        if (getCIDCountUnspecified() != null) {
            _hashCode += getCIDCountUnspecified().hashCode();
        }
        if (getCIDCountProbe() != null) {
            _hashCode += getCIDCountProbe().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AssayDescriptionType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AssayDescriptionType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Protocol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfTIDs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "NumberOfTIDs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "HasScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("method");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Method"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targets");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Targets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ArrayOfTargets"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("revision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Revision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastDataChange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "LastDataChange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountActive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountInactive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountInactive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountInconclusive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountInconclusive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountUnspecified");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountUnspecified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SIDCountProbe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SIDCountProbe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountAll"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountActive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountInactive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountInactive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountInconclusive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountInconclusive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountUnspecified");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountUnspecified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CIDCountProbe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "CIDCountProbe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
