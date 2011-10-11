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
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.interfaces.TaskOptions;

/**
 *          LocalAlignment – 2011.09.12 <br>
 *          A basic annotation of a local alignment
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class LocalAlignment
        extends Alignment {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignment.class);
    // mandatory fields
    private String query;
    private String subject;
    private float percentage;
    private int length;
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

    public LocalAlignment(String query, String subject, float percentage, int length, int queryStart, int queryEnd, int subjectStart, int subjectEnd, double expected, double bitScore) {
        this.query = query;
        this.subject = subject;
        this.percentage = percentage;
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

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(subject.length() * 2);
        return sb.append(subject).append(": e=").append(expected).append(" s=").append(bitScore).toString();
    }

    @Override
    public void readExternal(ObjectInput in, List<TaskOptions> options) throws IOException, ClassNotFoundException {

        super.readExternal(in, options);
        query = in.readUTF();
        subject = in.readUTF();

        percentage = in.readFloat();
        length = in.readInt();

        queryStart = in.readInt();
        queryEnd = in.readInt();
        subjectStart = in.readInt();
        subjectEnd = in.readInt();

        expected = in.readDouble();
        bitScore = in.readDouble();

        // read sequences if available
        boolean hasSequence = in.readBoolean();
        if (hasSequence) {
            querySequence = in.readUTF();
            subjectSequence = in.readUTF();
            alignmentSequence = in.readUTF();
        }

    }

    @Override
    public void writeExternal(ObjectOutput out, List<TaskOptions> options) throws IOException {

        super.writeExternal(out, options);
        out.writeUTF(query);
        out.writeUTF(subject);

        out.writeFloat(percentage);
        out.writeInt(length);

        out.writeInt(queryStart);
        out.writeInt(queryEnd);
        out.writeInt(subjectStart);
        out.writeInt(subjectEnd);

        out.writeDouble(expected);
        out.writeDouble(bitScore);

        if (querySequence != null) {
            out.writeBoolean(true);
            out.writeUTF(querySequence);
            out.writeUTF(subjectSequence);
            out.writeUTF(alignmentSequence);
        } else {
            out.writeBoolean(false);
        }

    }

    @Override
    public LocalAlignment getInstance() {
        return new LocalAlignment();
    }
}
