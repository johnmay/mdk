/**
 * LocalAlignment.java
 *
 * 2011.09.12
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.observation.sequence;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * LocalAlignment – 2011.09.12 <br>
 * A basic annotation of a local alignment
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class LocalAlignment
        extends Alignment
        implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignment.class);
    // mandatory fields
    private String query;
    private String subject;
    //
    private int positive;
    private int identity;
    private int length;
    //
    private int queryStart;
    private int queryEnd;
    private int subjectStart;
    private int subjectEnd;
    private double expected;
    private double bitScore;
    // optional fields
    private Set<Identifier> subjectIdentifiers;
    private String querySequence;
    private String subjectSequence;
    private String alignmentSequence;

    public LocalAlignment() {
    }

    public LocalAlignment(String query, String subject, int positive, int identity, int length, int queryStart, int queryEnd, int subjectStart, int subjectEnd, double expected, double bitScore) {
        this.query = query;
        this.subject = subject;
        this.positive = positive;
        this.identity = identity;
        this.length = length;
        this.queryStart = queryStart;
        this.queryEnd = queryEnd;
        this.subjectStart = subjectStart;
        this.subjectEnd = subjectEnd;
        this.expected = expected;
        this.bitScore = bitScore;
    }

    public double getBitScore() {
        return bitScore;
    }

    public void setBitScore(double bitScore) {
        this.bitScore = bitScore;
    }

    public double getExpected() {
        return expected;
    }

    public void setExpected(double expected) {
        this.expected = expected;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getQueryEnd() {
        return queryEnd;
    }

    public void setQueryEnd(int queryEnd) {
        this.queryEnd = queryEnd;
    }

    public int getQueryStart() {
        return queryStart;
    }

    public void setQueryStart(int queryStart) {
        this.queryStart = queryStart;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getSubjectEnd() {
        return subjectEnd;
    }

    public void setSubjectEnd(int subjectEnd) {
        this.subjectEnd = subjectEnd;
    }

    public int getSubjectStart() {
        return subjectStart;
    }

    public void setSubjectStart(int subjectStart) {
        this.subjectStart = subjectStart;
    }

    public String getQuerySequence() {
        return querySequence;
    }

    public void setQuerySequence(String querySequence) {
        this.querySequence = querySequence;
    }

    public String getSubjectSequence() {
        return subjectSequence;
    }

    public void setSubjectSequence(String subjectSequence) {
        this.subjectSequence = subjectSequence;
    }

    public String getAlignmentSequence() {
        return alignmentSequence;
    }

    public void setAlignmentSequence(String alignmentSequence) {
        this.alignmentSequence = alignmentSequence;
    }

    /**
     * Returns true if query, subject and alignment sequence are not null
     *
     * @return
     */
    public boolean hasSequences() {
        return querySequence != null && alignmentSequence != null && subjectSequence != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(subject.length() * 2);
        return sb.append(subject).append(": e=").append(expected).append(" s=").append(bitScore).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query, subject,
                                positive, identity, length,
                                queryStart, queryEnd,
                                subjectStart, subjectEnd,
                                expected, bitScore,
                                querySequence, subjectSequence, alignmentSequence);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof LocalAlignment) {
            LocalAlignment o = (LocalAlignment) obj;
            return Objects.equal(this.query, o.query)
                    && Objects.equal(this.subject, o.subject)
                    && Objects.equal(this.positive, o.positive)
                    && Objects.equal(this.identity, o.identity)
                    && Objects.equal(this.length, o.length)
                    && Objects.equal(this.queryStart, o.queryStart)
                    && Objects.equal(this.queryEnd, o.queryEnd)
                    && Objects.equal(this.subjectStart, o.subjectStart)
                    && Objects.equal(this.subjectEnd, o.subjectEnd)
                    && Objects.equal(this.expected, o.expected)
                    && Objects.equal(this.bitScore, o.bitScore)
                    && Objects.equal(this.querySequence, o.querySequence)
                    && Objects.equal(this.subjectSequence, o.subjectSequence)
                    && Objects.equal(this.alignmentSequence, o.alignmentSequence);
        }

        return false;

    }

    @Override
    public LocalAlignment getInstance() {
        return new LocalAlignment();
    }

    /**
     * Returns a summary of the scoring information in the format of a HTML table. The intention is the summary
     * can be wrapped in HTML tags and placed in a tool-tip.
     */
    public String getHTMLSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><td><b>Hit Identifier:</b></td><td>").append(subject).
                append("</td></tr><tr><td><b>Expected Value:</b></td><td>").append(expected).
                append("</td></tr><tr><td><b>Bit Score:</b></td><td>").append(bitScore).
                append("</td></tr><tr><td><b>Positive:</b></td><td>").append(positive).append("</td></tr></table>");

        return sb.toString();
    }
}
