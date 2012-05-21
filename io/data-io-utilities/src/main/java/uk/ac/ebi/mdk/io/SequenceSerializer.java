/**
 * SequenceSerializer.java
 *
 * 2011.10.18
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
package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.RNASequence;

import java.io.*;
import java.security.InvalidParameterException;

/**
 *          SequenceSerializer - 2011.10.18 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class SequenceSerializer {

    private static final Logger LOGGER = Logger.getLogger(SequenceSerializer.class);

    public static void main(String[] args) throws NotSerializableException, IOException {
        File f = File.createTempFile("test", ".ser");
        DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
        RNASequence written = new RNASequence("acugacuacguagcugacugac");
        writeRNASequence(written, out);
        // out.writeUTF("atcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcgaatcgatcgtacgtacgtcgatgcatgctgatcgatgcatgcatgctgactgcatcgatcga");
        out.close();
        RNASequence read = readRNASequence(new DataInputStream(new FileInputStream(f)));
        System.out.println("size: " + f.length());
    }

    public static void writeDNASequence(DNASequence sequence, DataOutput out) throws IOException {
        writeDNASequence(sequence.getSequenceAsString(), out);
    }

    private static void writeDNASequence(String sequence, DataOutput out) throws IOException {
        out.writeInt(sequence.length());
        for (int i = 0; i < sequence.length(); i += 4) {
            String substring = sequence.substring(i, Math.min(i + 4, sequence.length()));
            int value = 0;
            for (int j = 0; j < 4; j++) {
                value += (j < substring.length() ? getSerializedDNABase(substring.charAt(j)) : 0);
                if (j != 3) {
                    value <<= 2;
                }
            }
            out.writeByte(value);
        }
    }

    public static void writeRNASequence(RNASequence sequence, DataOutput out) throws IOException {
        int length = sequence.getLength();
        String string = sequence.getSequenceAsString();
        out.writeInt(length);
        for (int i = 0; i < length; i += 4) {
            String substring = string.substring(i, Math.min(i + 4, length));
            int value = 0;
            for (int j = 0; j < 4; j++) {
                value += (j < substring.length() ? getSerializedRNABase(substring.charAt(j)) : 0);
                if (j != 3) {
                    value <<= 2;
                }
            }
            out.writeByte(value);
        }
    }

    public static RNASequence readRNASequence(DataInput in) throws IOException {
        int size = in.readInt();
        char[] chars = new char[size];

        int completed = 0;
        while (completed < size) {
            int value = in.readUnsignedByte();
            chars[completed++] = getUnserializedRNABase((value >> 6));
            if (completed < size) {
                chars[completed++] = getUnserializedRNABase(((value >> 4) - ((value >> 6) << 2)));
            }
            if (completed < size) {
                chars[completed++] = getUnserializedRNABase(((value >> 2) - ((value >> 4) << 2)));
            }
            if (completed < size) {
                chars[completed++] = getUnserializedRNABase(((value) - ((value >> 2) << 2)));
            }
        }

        return new RNASequence(new String(chars));
    }

    public static DNASequence readDNASequence(DataInput in) throws IOException {
        int size = in.readInt();
        char[] chars = new char[size];

        int completed = 0;
        while (completed < size) {
            int value = in.readUnsignedByte();
            chars[completed++] = getUnserializedDNABase((value >> 6));
            if (completed < size) {
                chars[completed++] = getUnserializedDNABase(((value >> 4) - ((value >> 6) << 2)));
            }
            if (completed < size) {
                chars[completed++] = getUnserializedDNABase(((value >> 2) - ((value >> 4) << 2)));
            }
            if (completed < size) {
                chars[completed++] = getUnserializedDNABase(((value) - ((value >> 2) << 2)));
            }
        }

        return new DNASequence(new String(chars));
    }

    public static void writeProteinSequence(ProteinSequence sequence, DataOutput out) throws IOException {

        String string = sequence.getSequenceAsString();

        out.writeInt(string.length());

        for (int i = 0; i < string.length(); i += 3) {
            String substring = string.substring(i, Math.min(i + 3, string.length()));
            int value = 0;
            for (int j = 0; j < 3; j++) {
                value += (j < substring.length() ? getSerializedAminoAcid(substring.charAt(j)) : 0);
                if (j != 2) {
                    value <<= 5;
                }
            }
            out.writeShort(value);
        }

    }

    public static ProteinSequence readProteinSequence(DataInput in) throws IOException {

        int size = in.readInt();
        char[] chars = new char[size];

        int completed = 0;
        while (completed < size) {
            int value = in.readShort();
            chars[completed++] = getUnserializedAminoAcid((short) (value >> 10));
            if (completed < size) {
                chars[completed++] = getUnserializedAminoAcid((short) ((value >> 5) - ((value >> 10) << 5)));
            }
            if (completed < size) {
                chars[completed++] = getUnserializedAminoAcid((short) ((value) - ((value >> 5) << 5)));
            }
        }

        return new ProteinSequence(new String(chars));
    }

    private static int getSerializedAminoAcid(char c) throws NotSerializableException {
        int index = (short) c;

        index -= 64; // shift index A=1, Z=26

        if (index < 1 || index > 26) {
            switch (c) {
                case ' ':
                    return 27;
                case '+':
                    return 28;
                case '-':
                    return 29;
                default:
                    return 0;
            }
        }

        return index;
    }

    private static char getUnserializedAminoAcid(int index) throws NotSerializableException {
        if (index > 26 || index < 1) {
            switch (index) {
                case 27:
                    return ' ';
                case 28:
                    return '+';
                case 29:
                    return '-';
                default:
                    return Character.UNASSIGNED; // should be checked
            }
        } else {
            index += 64; // shift from A=0, Z=25
        }

        return (char) index;
    }

    private static int getSerializedDNABase(char c) throws NotSerializableException {
        switch (c) {
            case 'a':
                return 0;
            case 'c':
                return 1;
            case 'g':
                return 2;
            case 't':
                return 3;
        }
        throw new InvalidParameterException("character is not a, t, c or g (lowercase)");
    }

    private static char getUnserializedDNABase(int index) throws NotSerializableException {
        switch (index) {
            case 0:
                return 'a';
            case 1:
                return 'c';
            case 2:
                return 'g';
            case 3:
                return 't';
        }
        throw new InvalidParameterException("character is not a, t, c or g (lowercase)");
    }

    private static int getSerializedRNABase(char c) throws NotSerializableException {
        switch (c) {
            case 'a':
                return 0;
            case 'c':
                return 1;
            case 'g':
                return 2;
            case 'u':
                return 3;
        }
        throw new InvalidParameterException("character is not a, t, c or g (lowercase)");
    }

    private static char getUnserializedRNABase(int index) throws NotSerializableException {
        switch (index) {
            case 0:
                return 'a';
            case 1:
                return 'c';
            case 2:
                return 'g';
            case 3:
                return 'u';
        }
        throw new InvalidParameterException("character is not a, t, c or g (lowercase)");
    }
}
