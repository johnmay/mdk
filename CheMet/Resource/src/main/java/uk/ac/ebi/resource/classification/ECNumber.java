/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.resource.classification;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import uk.ac.ebi.resource.MIRIAMIdentifier;


/**
 * ECNumber.java
 *
 * Object to unify, store and compare the Enzyme Commission (EC) classification system
 *
 * @author johnmay
 * @date Mar 11, 2011
 */
@MIRIAMIdentifier(mir = 4)
public class ECNumber
        extends ClassificationIdentifier implements Comparable<ECNumber> {

    private int enzymeClass;

    private int enzymeSubClass;

    private int enzymeSubSubClass;

    private int enzymeEntry;

    private boolean preliminary = false;

    /**
     * Value to indicate compared values have no match (see. {@link #compare(ECNumber ec1)}) <br/>
     *
     * <b>Example Code:</b> <pre>
     * {@code
     * if( ec1.compare(ec2) == ECNumber.MATCHING_NONE ) {
     *   // logic
     * }
     * </pre>
     */
    public static final int MATCHING_NONE = 0;

    /**
     * Value to indicate compared values are matching at the class level (see. {@link #compare(ECNumber ec1)}) <br/>
     *
     * <b>Example Code:</b> <pre>
     * {@code
     * ECNumber ec1 = new ECNumber("1.2.4.-");
     * ECNumber ec1 = new ECNumber("1.1.-.-");
     * if( ec1.compare(ec2) == ECNumber.MATCHING_CLASS ) {
     *   // logic
     * }
     * </pre>
     */
    public static final int MATCHING_CLASS = 1;

    /**
     * Value to indicate compared values are matching at the class level (see. {@link #compare(ECNumber ec1)}) <br/>
     *
     * <b>Example Code:</b> <pre>
     * {@code
     * ECNumber ec1 = new ECNumber("1.2.2.5");
     * ECNumber ec1 = new ECNumber("1.2.4.1");
     * if( ec1.compare(ec2) == ECNumber.MATCHING_SUBCLASS ) {
     *   // logic
     * }
     * </pre>
     */
    public static final int MATCHING_SUBCLASS = 2;

    /**
     * Value to indicate compared values are matching at the class level (see. {@link #compare(ECNumber ec1)}) <br/>
     *
     * <b>Example Code:</b> <pre>
     * {@code
     * ECNumber ec1 = new ECNumber("1.2.4.5");
     * ECNumber ec1 = new ECNumber("1.2.4.1");
     * if( ec1.compare(ec2) == ECNumber.MATCHING_SUB_SUBCLASS ) {
     *   // logic
     * }
     * </pre>
     */
    public static final int MATCHING_SUB_SUBCLASS = 3;

    /**
     * Value to indicate compared values are matching at the entry level (see. {@link #compare(ECNumber ec1)}) <br/>
     *
     * <b>Example Code:</b> <pre>
     * {@code
     * ECNumber ec1 = new ECNumber("1.2.4.1");
     * ECNumber ec1 = new ECNumber("1.2.4.1");
     * if( ec1.compare(ec2) == ECNumber.MATCHING_ENTRY ) {
     *   // logic
     * }
     * </pre>
     */
    public static final int MATCHING_ENTRY = 4;

    /**
     *
     */
    public static final String EC_SEPERATOR = "\\.";


    /**
     * Default constructor
     */
    public ECNumber() {
    }


    /**
     * Construct a new entry from an EC accession
     * @param ecNumber EC accession (e.g. {@code "1.1.4.3", "1.1.-.-"})
     *
     */
    public ECNumber(String ecNumber) {
        setAccession(ecNumber);
    }


    @Override
    public void setAccession(String accession) {

        // remove prefix chars e.g. EC=, EC: EC\s
        accession = removePrefix(accession, 0);
        accession = removePostfix(accession, accession.length());

        String[] ident = accession.split(EC_SEPERATOR);

        this.enzymeClass = ident.length > 0 ? StringToIdent(ident[0]) : 0;
        this.enzymeSubClass = ident.length > 1 ? StringToIdent(ident[1]) : 0;
        this.enzymeSubSubClass = ident.length > 2 ? StringToIdent(ident[2]) : 0;
        this.enzymeEntry = ident.length > 3 ? StringToIdent(ident[3]) : 0;

        super.setAccession(this.toString());

    }


    /**
     *
     * @param enzymeClass
     * @param enzymeSubClass
     * @param enzymeSubSubClass
     * @param enzymeEntry
     */
    public ECNumber(int enzymeClass, int enzymeSubClass, int enzymeSubSubClass, int enzymeEntry) {
        this.enzymeClass = enzymeClass;
        this.enzymeSubClass = enzymeSubClass;
        this.enzymeSubSubClass = enzymeSubSubClass;
        this.enzymeEntry = enzymeEntry;
    }


    /**
     * Converts a string to the int ident, 0 represents a '-' in the EC number
     * @return
     */
    private int StringToIdent(String value) {

        // check if this is preliminary
        if (value.subSequence(0, 1).equals("n")) {
            preliminary = true;
            value = value.substring(1);
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private String removePrefix(String value, int start) {
        if (start > value.length()) {
            return "-.-.-.-";
        }
        // walk along string until a number is found
        return value.substring(start).matches("[0-9]+.*") ? value.substring(start) : removePrefix(
                value, start + 1);
    }


    private String removePostfix(String value, int end) {
        // walkbackwards until we find ending in .- or .1 .n1 etc..
        return value.substring(0, end).matches(".*?[-0-9]") ? value.substring(0, end)
               : removePostfix(value, end - 1);
    }


    private String StringToIdent(int value) {
        return value > 0 ? Integer.toString(value) : "-";
    }


    /**
     * prints the enzyme number to the provided stream
     * @param stream The stream to print to (e.g. System.out)
     */
    public void print(PrintStream stream) {
        stream.print(this.toString());
    }


    /**
     *
     * @param ec
     * @return
     */
    public int compare(ECNumber ec) {
        return this.compare(this, ec);
    }


    /**
     *
     * @param ec1
     * @param ec2
     * @return
     */
    public static int compare(ECNumber ec1, ECNumber ec2) {
        int[] ec1ValueArray = ec1.getValueArray();
        int[] ec2ValueArray = ec2.getValueArray();
        return compareValues(ec1ValueArray, ec2ValueArray, 0, MATCHING_NONE);
    }


    /**
     * Recursive function to find the depth at which entries match
     * @param ec1ValueArray
     * @param ec2ValueArray
     * @param index
     * @param matchLevel
     * @return
     */
    private static int compareValues(int ec1ValueArray[], int ec2ValueArray[], int index,
                                     int matchLevel) {
        if (index == ec1ValueArray.length || index == ec2ValueArray.length) {
            return matchLevel;
        }
        return ec1ValueArray[index] == ec2ValueArray[index] ? compareValues(ec1ValueArray,
                                                                            ec2ValueArray, index + 1,
                                                                            matchLevel + 1)
               : matchLevel;
    }


    /**
     * Prints out the EC number including any preliminary tags
     * @return
     */
    @Override
    public String toString() {
        return StringToIdent(enzymeClass)
               + "." + StringToIdent(enzymeSubClass)
               + "." + StringToIdent(enzymeSubSubClass)
               + "." + (preliminary ? "n" : "") + StringToIdent(enzymeEntry);
    }


    private int[] getValueArray() {
        return new int[]{
                    enzymeClass,
                    enzymeSubClass,
                    enzymeSubSubClass,
                    enzymeEntry};
    }


    /**
     * Returns whether this EC number is preliminary (e.g. 1.1.1.n1)
     * @return
     */
    public boolean isPreliminary() {
        return preliminary;
    }


    /**
     * Sets whether this EC is preliminary or not
     * @param preliminary
     */
    public void setPreliminary(boolean preliminary) {
        this.preliminary = preliminary;
    }


    /**
     * Creates an array of multiple EC Numbers found in the string (splitting on ';')
     * @param ecContaingString
     * @return
     */
    public static ECNumber[] getMultipleECs(String ecContaingString) {

        String[] split = ecContaingString.split(";");
        ECNumber[] ecs = new ECNumber[split.length];

        for (int i = 0; i < split.length; i++) {
            ecs[i] = new ECNumber(split[i]);
        }

        return ecs;

    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.enzymeClass;
        hash = 67 * hash + this.enzymeSubClass;
        hash = 67 * hash + this.enzymeSubSubClass;
        hash = 67 * hash + this.enzymeEntry;
        hash = 67 * hash + (this.preliminary ? 3 : 5);
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ECNumber other = (ECNumber) obj;
        if (this.enzymeClass != other.enzymeClass) {
            return false;
        }
        if (this.enzymeSubClass != other.enzymeSubClass) {
            return false;
        }
        if (this.enzymeSubSubClass != other.enzymeSubSubClass) {
            return false;
        }
        if (this.enzymeEntry != other.enzymeEntry) {
            return false;
        }
        if (this.preliminary != other.preliminary) {
            return false;
        }
        return true;
    }


    /**
     * @inheritDoc
     */
    @Override
    public ECNumber newInstance() {
        return new ECNumber();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        enzymeClass = in.readInt();
        enzymeSubClass = in.readInt();
        enzymeSubSubClass = in.readInt();
        enzymeEntry = in.readInt();
        preliminary = in.readBoolean();
        super.setAccession(this.toString());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(enzymeClass);
        out.writeInt(enzymeSubClass);
        out.writeInt(enzymeSubSubClass);
        out.writeInt(enzymeEntry);
        out.writeBoolean(preliminary);
    }


    public int compareTo(ECNumber o) {

        if (this.enzymeClass != o.enzymeClass) {
            return this.enzymeClass > o.enzymeClass ? +1 : -1;
        }
        if (this.enzymeSubClass != o.enzymeSubClass) {
            return this.enzymeSubClass > o.enzymeSubClass ? +1 : -1;
        }
        if (this.enzymeSubSubClass != o.enzymeSubSubClass) {
            return this.enzymeSubSubClass > o.enzymeSubSubClass ? +1 : -1;
        }
        if (this.enzymeEntry != o.enzymeEntry) {
            return this.enzymeEntry > o.enzymeEntry ? +1 : -1;
        }

        return 0;
    }


    public int getEnzymeClass() {
        return enzymeClass;
    }


    public int getEnzymeSubClass() {
        return enzymeSubClass;
    }


    public int getEnzymeSubSubClass() {
        return enzymeSubSubClass;
    }


    public int getEnzymeEntry() {
        return enzymeEntry;
    }
}
