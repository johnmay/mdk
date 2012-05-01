/**
 * InputParameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.ws.jdispatcher;


/**
 * Input parameters for the tool
 */
public class InputParameters  implements java.io.Serializable {
    /* Program [The BLAST program to be used for the Sequence Similarity
     * Search.] */
    private java.lang.String program;

    /* Matrix [(Protein searches) The substitution matrix used for
     * scoring alignments when searching the database.] */
    private java.lang.String matrix;

    /* Alignments [Maximum number of match alignments reported in
     * the result output.] */
    private java.lang.Integer alignments;

    /* Scores [Maximum number of match score summaries reported in
     * the result output.] */
    private java.lang.Integer scores;

    /* Expectation value threshold [Limits the number of scores and
     * alignments reported based on the expectation value. This is the maximum
     * number of times the match is expected to occur by chance.] */
    private java.lang.String exp;

    /* Dropoff [The amount a score can drop before extension of word
     * hits is halted] */
    private java.lang.Integer dropoff;

    /* Match/mismatch scores [(Nucleotide searches) The match score
     * is the bonus to the alignment score when matching the same base. The
     * mismatch is the penalty when failing to match.] */
    private java.lang.String match_scores;

    /* Gap open [Penalty taken away from the score when a gap is created
     * in sequence. Increasing the gap openning penalty will decrease the
     * number of gaps in the final alignment.] */
    private java.lang.Integer gapopen;

    /* Gap extend [Penalty taken away from the score for each base
     * or residue in the gap. Increasing the gap extension penalty favors
     * short gaps in the final alignment, conversly decreasing the gap extension
     * penalty favors long gaps in the final alignment.] */
    private java.lang.Integer gapext;

    /* Filter [Filter regions of low sequence complexity. This can
     * avoid issues with low complexity sequences where matches are found
     * due to composition rather than meaningful sequence similarity. However
     * in some cases filtering also masks regions of interest and so should
     * be used with caution.] */
    private java.lang.String filter;

    /* Sequence range [Specify a range or section of the input sequence
     * to use in the search. Example: Specifying '34-89' in an input sequence
     * of total length 100, will tell BLAST to only use residues 34 to 89,
     * inclusive.] */
    private java.lang.String seqrange;

    /* Gapalign [This is a true/false setting that tells the program
     * the perform optimised alignments within regions involving gaps. If
     * set to true, the program will perform an alignment using gaps. Otherwise,
     * if it is set to false, it will report only individual HSP where two
     * sequence match each other, and thus will not produce alignments with
     * gaps.] */
    private java.lang.Boolean gapalign;

    /* Align views [Formating for the alignments] */
    private java.lang.Integer align;

    /* Translation table [Query Genetic code to use in translation] */
    private java.lang.Integer transltable;

    /* Sequence type [Indicates if the sequence is protein or DNA/RNA.] */
    private java.lang.String stype;

    /* Sequence [The query sequence can be entered directly into this
     * form. The sequence can be be in GCG, FASTA, EMBL, GenBank, PIR, NBRF,
     * PHYLIP or UniProtKB/Swiss-Prot format. A partially formatted sequence
     * is not accepted. Adding a return to the end of the sequence may help
     * certain applications understand the input. Note that directly using
     * data from word processors may yield unpredictable results as hidden/control
     * characters may be present.] */
    private java.lang.String sequence;

    /* Database [Database] */
    private uk.ac.ebi.ws.jdispatcher.ArrayOfString database;

    public InputParameters() {
    }

    public InputParameters(
           java.lang.String program,
           java.lang.String matrix,
           java.lang.Integer alignments,
           java.lang.Integer scores,
           java.lang.String exp,
           java.lang.Integer dropoff,
           java.lang.String match_scores,
           java.lang.Integer gapopen,
           java.lang.Integer gapext,
           java.lang.String filter,
           java.lang.String seqrange,
           java.lang.Boolean gapalign,
           java.lang.Integer align,
           java.lang.Integer transltable,
           java.lang.String stype,
           java.lang.String sequence,
           uk.ac.ebi.ws.jdispatcher.ArrayOfString database) {
           this.program = program;
           this.matrix = matrix;
           this.alignments = alignments;
           this.scores = scores;
           this.exp = exp;
           this.dropoff = dropoff;
           this.match_scores = match_scores;
           this.gapopen = gapopen;
           this.gapext = gapext;
           this.filter = filter;
           this.seqrange = seqrange;
           this.gapalign = gapalign;
           this.align = align;
           this.transltable = transltable;
           this.stype = stype;
           this.sequence = sequence;
           this.database = database;
    }


    /**
     * Gets the program value for this InputParameters.
     * 
     * @return program   * Program [The BLAST program to be used for the Sequence Similarity
     * Search.]
     */
    public java.lang.String getProgram() {
        return program;
    }


    /**
     * Sets the program value for this InputParameters.
     * 
     * @param program   * Program [The BLAST program to be used for the Sequence Similarity
     * Search.]
     */
    public void setProgram(java.lang.String program) {
        this.program = program;
    }


    /**
     * Gets the matrix value for this InputParameters.
     * 
     * @return matrix   * Matrix [(Protein searches) The substitution matrix used for
     * scoring alignments when searching the database.]
     */
    public java.lang.String getMatrix() {
        return matrix;
    }


    /**
     * Sets the matrix value for this InputParameters.
     * 
     * @param matrix   * Matrix [(Protein searches) The substitution matrix used for
     * scoring alignments when searching the database.]
     */
    public void setMatrix(java.lang.String matrix) {
        this.matrix = matrix;
    }


    /**
     * Gets the alignments value for this InputParameters.
     * 
     * @return alignments   * Alignments [Maximum number of match alignments reported in
     * the result output.]
     */
    public java.lang.Integer getAlignments() {
        return alignments;
    }


    /**
     * Sets the alignments value for this InputParameters.
     * 
     * @param alignments   * Alignments [Maximum number of match alignments reported in
     * the result output.]
     */
    public void setAlignments(java.lang.Integer alignments) {
        this.alignments = alignments;
    }


    /**
     * Gets the scores value for this InputParameters.
     * 
     * @return scores   * Scores [Maximum number of match score summaries reported in
     * the result output.]
     */
    public java.lang.Integer getScores() {
        return scores;
    }


    /**
     * Sets the scores value for this InputParameters.
     * 
     * @param scores   * Scores [Maximum number of match score summaries reported in
     * the result output.]
     */
    public void setScores(java.lang.Integer scores) {
        this.scores = scores;
    }


    /**
     * Gets the exp value for this InputParameters.
     * 
     * @return exp   * Expectation value threshold [Limits the number of scores and
     * alignments reported based on the expectation value. This is the maximum
     * number of times the match is expected to occur by chance.]
     */
    public java.lang.String getExp() {
        return exp;
    }


    /**
     * Sets the exp value for this InputParameters.
     * 
     * @param exp   * Expectation value threshold [Limits the number of scores and
     * alignments reported based on the expectation value. This is the maximum
     * number of times the match is expected to occur by chance.]
     */
    public void setExp(java.lang.String exp) {
        this.exp = exp;
    }


    /**
     * Gets the dropoff value for this InputParameters.
     * 
     * @return dropoff   * Dropoff [The amount a score can drop before extension of word
     * hits is halted]
     */
    public java.lang.Integer getDropoff() {
        return dropoff;
    }


    /**
     * Sets the dropoff value for this InputParameters.
     * 
     * @param dropoff   * Dropoff [The amount a score can drop before extension of word
     * hits is halted]
     */
    public void setDropoff(java.lang.Integer dropoff) {
        this.dropoff = dropoff;
    }


    /**
     * Gets the match_scores value for this InputParameters.
     * 
     * @return match_scores   * Match/mismatch scores [(Nucleotide searches) The match score
     * is the bonus to the alignment score when matching the same base. The
     * mismatch is the penalty when failing to match.]
     */
    public java.lang.String getMatch_scores() {
        return match_scores;
    }


    /**
     * Sets the match_scores value for this InputParameters.
     * 
     * @param match_scores   * Match/mismatch scores [(Nucleotide searches) The match score
     * is the bonus to the alignment score when matching the same base. The
     * mismatch is the penalty when failing to match.]
     */
    public void setMatch_scores(java.lang.String match_scores) {
        this.match_scores = match_scores;
    }


    /**
     * Gets the gapopen value for this InputParameters.
     * 
     * @return gapopen   * Gap open [Penalty taken away from the score when a gap is created
     * in sequence. Increasing the gap openning penalty will decrease the
     * number of gaps in the final alignment.]
     */
    public java.lang.Integer getGapopen() {
        return gapopen;
    }


    /**
     * Sets the gapopen value for this InputParameters.
     * 
     * @param gapopen   * Gap open [Penalty taken away from the score when a gap is created
     * in sequence. Increasing the gap openning penalty will decrease the
     * number of gaps in the final alignment.]
     */
    public void setGapopen(java.lang.Integer gapopen) {
        this.gapopen = gapopen;
    }


    /**
     * Gets the gapext value for this InputParameters.
     * 
     * @return gapext   * Gap extend [Penalty taken away from the score for each base
     * or residue in the gap. Increasing the gap extension penalty favors
     * short gaps in the final alignment, conversly decreasing the gap extension
     * penalty favors long gaps in the final alignment.]
     */
    public java.lang.Integer getGapext() {
        return gapext;
    }


    /**
     * Sets the gapext value for this InputParameters.
     * 
     * @param gapext   * Gap extend [Penalty taken away from the score for each base
     * or residue in the gap. Increasing the gap extension penalty favors
     * short gaps in the final alignment, conversly decreasing the gap extension
     * penalty favors long gaps in the final alignment.]
     */
    public void setGapext(java.lang.Integer gapext) {
        this.gapext = gapext;
    }


    /**
     * Gets the filter value for this InputParameters.
     * 
     * @return filter   * Filter [Filter regions of low sequence complexity. This can
     * avoid issues with low complexity sequences where matches are found
     * due to composition rather than meaningful sequence similarity. However
     * in some cases filtering also masks regions of interest and so should
     * be used with caution.]
     */
    public java.lang.String getFilter() {
        return filter;
    }


    /**
     * Sets the filter value for this InputParameters.
     * 
     * @param filter   * Filter [Filter regions of low sequence complexity. This can
     * avoid issues with low complexity sequences where matches are found
     * due to composition rather than meaningful sequence similarity. However
     * in some cases filtering also masks regions of interest and so should
     * be used with caution.]
     */
    public void setFilter(java.lang.String filter) {
        this.filter = filter;
    }


    /**
     * Gets the seqrange value for this InputParameters.
     * 
     * @return seqrange   * Sequence range [Specify a range or section of the input sequence
     * to use in the search. Example: Specifying '34-89' in an input sequence
     * of total length 100, will tell BLAST to only use residues 34 to 89,
     * inclusive.]
     */
    public java.lang.String getSeqrange() {
        return seqrange;
    }


    /**
     * Sets the seqrange value for this InputParameters.
     * 
     * @param seqrange   * Sequence range [Specify a range or section of the input sequence
     * to use in the search. Example: Specifying '34-89' in an input sequence
     * of total length 100, will tell BLAST to only use residues 34 to 89,
     * inclusive.]
     */
    public void setSeqrange(java.lang.String seqrange) {
        this.seqrange = seqrange;
    }


    /**
     * Gets the gapalign value for this InputParameters.
     * 
     * @return gapalign   * Gapalign [This is a true/false setting that tells the program
     * the perform optimised alignments within regions involving gaps. If
     * set to true, the program will perform an alignment using gaps. Otherwise,
     * if it is set to false, it will report only individual HSP where two
     * sequence match each other, and thus will not produce alignments with
     * gaps.]
     */
    public java.lang.Boolean getGapalign() {
        return gapalign;
    }


    /**
     * Sets the gapalign value for this InputParameters.
     * 
     * @param gapalign   * Gapalign [This is a true/false setting that tells the program
     * the perform optimised alignments within regions involving gaps. If
     * set to true, the program will perform an alignment using gaps. Otherwise,
     * if it is set to false, it will report only individual HSP where two
     * sequence match each other, and thus will not produce alignments with
     * gaps.]
     */
    public void setGapalign(java.lang.Boolean gapalign) {
        this.gapalign = gapalign;
    }


    /**
     * Gets the align value for this InputParameters.
     * 
     * @return align   * Align views [Formating for the alignments]
     */
    public java.lang.Integer getAlign() {
        return align;
    }


    /**
     * Sets the align value for this InputParameters.
     * 
     * @param align   * Align views [Formating for the alignments]
     */
    public void setAlign(java.lang.Integer align) {
        this.align = align;
    }


    /**
     * Gets the transltable value for this InputParameters.
     * 
     * @return transltable   * Translation table [Query Genetic code to use in translation]
     */
    public java.lang.Integer getTransltable() {
        return transltable;
    }


    /**
     * Sets the transltable value for this InputParameters.
     * 
     * @param transltable   * Translation table [Query Genetic code to use in translation]
     */
    public void setTransltable(java.lang.Integer transltable) {
        this.transltable = transltable;
    }


    /**
     * Gets the stype value for this InputParameters.
     * 
     * @return stype   * Sequence type [Indicates if the sequence is protein or DNA/RNA.]
     */
    public java.lang.String getStype() {
        return stype;
    }


    /**
     * Sets the stype value for this InputParameters.
     * 
     * @param stype   * Sequence type [Indicates if the sequence is protein or DNA/RNA.]
     */
    public void setStype(java.lang.String stype) {
        this.stype = stype;
    }


    /**
     * Gets the sequence value for this InputParameters.
     * 
     * @return sequence   * Sequence [The query sequence can be entered directly into this
     * form. The sequence can be be in GCG, FASTA, EMBL, GenBank, PIR, NBRF,
     * PHYLIP or UniProtKB/Swiss-Prot format. A partially formatted sequence
     * is not accepted. Adding a return to the end of the sequence may help
     * certain applications understand the input. Note that directly using
     * data from word processors may yield unpredictable results as hidden/control
     * characters may be present.]
     */
    public java.lang.String getSequence() {
        return sequence;
    }


    /**
     * Sets the sequence value for this InputParameters.
     * 
     * @param sequence   * Sequence [The query sequence can be entered directly into this
     * form. The sequence can be be in GCG, FASTA, EMBL, GenBank, PIR, NBRF,
     * PHYLIP or UniProtKB/Swiss-Prot format. A partially formatted sequence
     * is not accepted. Adding a return to the end of the sequence may help
     * certain applications understand the input. Note that directly using
     * data from word processors may yield unpredictable results as hidden/control
     * characters may be present.]
     */
    public void setSequence(java.lang.String sequence) {
        this.sequence = sequence;
    }


    /**
     * Gets the database value for this InputParameters.
     * 
     * @return database   * Database [Database]
     */
    public uk.ac.ebi.ws.jdispatcher.ArrayOfString getDatabase() {
        return database;
    }


    /**
     * Sets the database value for this InputParameters.
     * 
     * @param database   * Database [Database]
     */
    public void setDatabase(uk.ac.ebi.ws.jdispatcher.ArrayOfString database) {
        this.database = database;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InputParameters)) return false;
        InputParameters other = (InputParameters) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.program==null && other.getProgram()==null) || 
             (this.program!=null &&
              this.program.equals(other.getProgram()))) &&
            ((this.matrix==null && other.getMatrix()==null) || 
             (this.matrix!=null &&
              this.matrix.equals(other.getMatrix()))) &&
            ((this.alignments==null && other.getAlignments()==null) || 
             (this.alignments!=null &&
              this.alignments.equals(other.getAlignments()))) &&
            ((this.scores==null && other.getScores()==null) || 
             (this.scores!=null &&
              this.scores.equals(other.getScores()))) &&
            ((this.exp==null && other.getExp()==null) || 
             (this.exp!=null &&
              this.exp.equals(other.getExp()))) &&
            ((this.dropoff==null && other.getDropoff()==null) || 
             (this.dropoff!=null &&
              this.dropoff.equals(other.getDropoff()))) &&
            ((this.match_scores==null && other.getMatch_scores()==null) || 
             (this.match_scores!=null &&
              this.match_scores.equals(other.getMatch_scores()))) &&
            ((this.gapopen==null && other.getGapopen()==null) || 
             (this.gapopen!=null &&
              this.gapopen.equals(other.getGapopen()))) &&
            ((this.gapext==null && other.getGapext()==null) || 
             (this.gapext!=null &&
              this.gapext.equals(other.getGapext()))) &&
            ((this.filter==null && other.getFilter()==null) || 
             (this.filter!=null &&
              this.filter.equals(other.getFilter()))) &&
            ((this.seqrange==null && other.getSeqrange()==null) || 
             (this.seqrange!=null &&
              this.seqrange.equals(other.getSeqrange()))) &&
            ((this.gapalign==null && other.getGapalign()==null) || 
             (this.gapalign!=null &&
              this.gapalign.equals(other.getGapalign()))) &&
            ((this.align==null && other.getAlign()==null) || 
             (this.align!=null &&
              this.align.equals(other.getAlign()))) &&
            ((this.transltable==null && other.getTransltable()==null) || 
             (this.transltable!=null &&
              this.transltable.equals(other.getTransltable()))) &&
            ((this.stype==null && other.getStype()==null) || 
             (this.stype!=null &&
              this.stype.equals(other.getStype()))) &&
            ((this.sequence==null && other.getSequence()==null) || 
             (this.sequence!=null &&
              this.sequence.equals(other.getSequence()))) &&
            ((this.database==null && other.getDatabase()==null) || 
             (this.database!=null &&
              this.database.equals(other.getDatabase())));
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
        if (getProgram() != null) {
            _hashCode += getProgram().hashCode();
        }
        if (getMatrix() != null) {
            _hashCode += getMatrix().hashCode();
        }
        if (getAlignments() != null) {
            _hashCode += getAlignments().hashCode();
        }
        if (getScores() != null) {
            _hashCode += getScores().hashCode();
        }
        if (getExp() != null) {
            _hashCode += getExp().hashCode();
        }
        if (getDropoff() != null) {
            _hashCode += getDropoff().hashCode();
        }
        if (getMatch_scores() != null) {
            _hashCode += getMatch_scores().hashCode();
        }
        if (getGapopen() != null) {
            _hashCode += getGapopen().hashCode();
        }
        if (getGapext() != null) {
            _hashCode += getGapext().hashCode();
        }
        if (getFilter() != null) {
            _hashCode += getFilter().hashCode();
        }
        if (getSeqrange() != null) {
            _hashCode += getSeqrange().hashCode();
        }
        if (getGapalign() != null) {
            _hashCode += getGapalign().hashCode();
        }
        if (getAlign() != null) {
            _hashCode += getAlign().hashCode();
        }
        if (getTransltable() != null) {
            _hashCode += getTransltable().hashCode();
        }
        if (getStype() != null) {
            _hashCode += getStype().hashCode();
        }
        if (getSequence() != null) {
            _hashCode += getSequence().hashCode();
        }
        if (getDatabase() != null) {
            _hashCode += getDatabase().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InputParameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "InputParameters"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("program");
        elemField.setXmlName(new javax.xml.namespace.QName("", "program"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matrix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matrix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alignments");
        elemField.setXmlName(new javax.xml.namespace.QName("", "alignments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dropoff");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dropoff"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("match_scores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "match_scores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gapopen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gapopen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gapext");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gapext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seqrange");
        elemField.setXmlName(new javax.xml.namespace.QName("", "seqrange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gapalign");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gapalign"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("align");
        elemField.setXmlName(new javax.xml.namespace.QName("", "align"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transltable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transltable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("database");
        elemField.setXmlName(new javax.xml.namespace.QName("", "database"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://soap.jdispatcher.ebi.ac.uk", "ArrayOfString"));
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
