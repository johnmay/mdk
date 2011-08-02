/**
 * 
 */
package uk.ac.ebi.metabolomes.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import com.wcohen.ss.*;
import uk.ac.ebi.metabolomes.bioObjects.*;
/**
 * @author pmoreno
 *
 */
public class runParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String brendaFile = args[0];
		String org = args[1];
		String version = args[2];
		BrendaParser b = new BrendaParser(brendaFile,org,version);
		System.out.println("Number of enzymes parsed: "+b.numberOfEnzymesParsed());
		Set<String> metabolites = b.getAllDistinctMetabolites();
		System.out.println("Number of different metabolites: "+metabolites.size());
		Set<String> metabFromNSP = b.getAllDistinctMetabolitesFromNSP();
		System.out.println("Number of different NSP metabolites: "+metabFromNSP.size());
		String logs = "/tmp/brendaParser";
		BufferedWriter metabsLog = new BufferedWriter(new FileWriter(logs+".metabolites.log"));
		BufferedWriter metabsLogNSP = new BufferedWriter(new FileWriter(logs+".metabolitesNSP.log"));
		BufferedWriter metabsLogCons = new BufferedWriter(new FileWriter(logs+".metabolitesConsolidated.log"));
		BufferedWriter protLinksLog = new BufferedWriter(new FileWriter(logs+".protlinks.log"));
		SmithWaterman compSW = new SmithWaterman();
		String previousM="";
		String repeated = "REPEATED";
		Set<String> metabCons = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for(String a : metabolites ) {
			double s = compSW.score(a, previousM);
			if(Math.abs(a.length()-previousM.length()) < 3 && a.length()*0.85*2 < s)
				metabsLog.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\t"+repeated+"\n");
			else
				metabsLog.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\n");
			previousM = a;
			metabCons.add(a);
		}
		metabsLog.close();
		previousM = "";
		for(String a : metabFromNSP ) {
			double s = compSW.score(a, previousM);
			if(Math.abs(a.length()-previousM.length()) < 3 && a.length()*0.85*2 < s)
				metabsLogNSP.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\t"+repeated+"\n");
			else
				metabsLogNSP.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\n");
			previousM = a;
			metabCons.add(a);
		}
		metabsLogNSP.close();
		previousM = "";
		int repeat=0;
		for(String a : metabCons) {
			double s = compSW.score(a, previousM);
			if(Math.abs(a.length()-previousM.length()) < 3 && a.length()*0.85*2 < s) {
				metabsLogCons.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\t"+repeated+"\n");
				repeat++;
			}
			else
				metabsLogCons.write(a+"\t"+s+"\t"+a.length()+"\t"+Math.abs(a.length()-previousM.length())+"\n");
			previousM = a;			
		}
		System.out.println("Number of different CONS metabolites: "+metabCons.size());
		System.out.println("Suspected repeated in CONS: "+repeat+"\n");
		ArrayList<String> protLinks = b.getProteinLinks();
		int numProtLinks=0;
		for(String a : protLinks) {
				protLinksLog.write(a+"\n");
				numProtLinks++;
		}
		System.out.println("Number of protlinks:\t"+numProtLinks);
		Set<Citation> cits = b.getAllDistinctCitations();
		System.out.println("Number of citations:\t"+cits.size());
		int hasPubMedID=0;
		for(Citation c : cits) {
			if(c.hasPubMedID())
				hasPubMedID++;
		}
		System.out.println("Number of cits with pubmedId:\t"+hasPubMedID);
		metabsLogCons.close();
		protLinksLog.close();
	}

}
