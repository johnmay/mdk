/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.tree;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author John May
 */
public class AttachMappings {
    public static void main(String[] args) throws IOException {
        String[] row = null;
        CSVReader mapping = new CSVReader(new FileReader("/Users/johnmay/analysis/iYO844-iBus1103-mapping"), '\t', '\0');
//        CSVReader mapping = new CSVReader(new FileReader("/Users/johnmay/analysis/ijr904-metacyc-mapping.tsv"), '\t', '\0');

        final Multimap<String, String> qToT = HashMultimap.create();
        final Multimap<String, String> tToQ = HashMultimap.create();
        while ((row = mapping.readNext()) != null) {
            
            String query  = row[0];
            String target = row[1].toLowerCase(Locale.ENGLISH);
            if (target.isEmpty())
                continue;            
            tToQ.put(target, query);
            qToT.put(query, target);
            if (qToT.get(query).size() > 1)
                System.out.println(qToT.get(query));
            if (tToQ.get(target).size() > 1)
                System.out.println(tToQ.get(target));
        }
        mapping.close();
        CSVReader csvr = new CSVReader(new FileReader("/Users/johnmay/Desktop/matched.tsv"), '\t', '\0');
        CSVWriter csvw = new CSVWriter(new FileWriter("/Users/johnmay/Desktop/matched-mapped.tsv"), '\t', '\0');

        int n = 0;

        Set<String> keys = new HashSet<String>(qToT.keySet());
        Map<String, Counter> counter = new TreeMap<String, Counter>();

        row = csvr.readNext(); // header
        csvw.writeNext(row);
        while ((row = csvr.readNext()) != null) {

            int    number        = Integer.parseInt(row[4]);
            String num           = (number > 2 ? "n" : Integer.toString(number));
            String category      = num + "-" + row[7];
            String query         = row[0].replaceAll("\\(e\\)", "");
            String target        = row[2].toLowerCase(Locale.ENGLISH);
            String correctTarget = Joiner.on(";").join(qToT.get(query));
            String correctQuery  = Joiner.on(";").join(tToQ.get(target));
            
            if (category.equals("0-None") && !row[8].isEmpty() ) {
                category += "+struct";
            }
            
            Set<String> correctTargets = new HashSet<String>(Arrays.asList(correctTarget.split(";")));
            correctTargets.remove("");
            row = Arrays.copyOf(row, row.length + 4);
 
            BinaryClassification binaryClass = !target.isEmpty() ? (correctTargets.contains(target) ? BinaryClassification.TP : (correctTargets.isEmpty() ? BinaryClassification.FP_MISSSING : BinaryClassification.FP)) 
                                                                 : (correctTargets.isEmpty() ? BinaryClassification.TN : BinaryClassification.FN);

            keys.remove(query);
            
            if (binaryClass == BinaryClassification.TP) {
                n++;
            }

            Counter count = counter.get(category);
            Counter countAgg = counter.get(num);
            if (count == null) counter.put(category, count = new Counter());
            if (countAgg == null) counter.put(num, countAgg = new Counter());

            switch (binaryClass){
                case TP:
                    count.incrementTp();
                    countAgg.incrementTp();
                    break;
                case FP: 
                    count.incrementFp();
                    countAgg.incrementFp();
                    break;
                case FP_MISSSING:
                    count.incrementFpMiss();
                    countAgg.incrementFpMiss();
                    break;
                case TN: 
                    count.incrementTn();
                    countAgg.incrementTn();
                    break;
                case FN: 
                    count.incrementFn();
                    countAgg.incrementFn(); 
                    break;
            }
            
            row[row.length - 4] = correctQuery;
            row[row.length - 3] = correctTarget;
            row[row.length - 2] = binaryClass.toString();
            row[row.length - 1] = Integer.toString(n);

            csvw.writeNext(row);

        }
        System.out.println(n + "/" + qToT.size());

        csvr.close();
        csvw.close();

        for (Map.Entry<String,Counter> e : counter.entrySet()) {
            System.out.println(e.getKey() + "\t" + e.getValue());
        }

        System.out.println(Joiner.on('\n').join(Collections2.transform(keys, new Function<String, Object>() {
            @Override public Object apply(String s) {
                return s + "\t" + qToT.get(s);
            }
        })));

    }
    
    enum BinaryClassification {
        TP,
        FP,
        FP_MISSSING,
        TN,
        FN,
        Unknown;
    }

    private static final class Counter {
        int tp = 0, fp = 0, fpMiss = 0, tn = 0, fn = 0;

        public void incrementTp() {
            tp++;
        }

        public void incrementFp() {
            fp++;
        }

        public void incrementFpMiss() {
            fpMiss++;
        }

        public void incrementTn() {
            tn++;
        }

        public void incrementFn() {
            fn++;
        }

        @Override public String toString() {
            return Joiner.on('\t').join(tp, fpMiss, fp, tn, fn);
        }
    }
}
