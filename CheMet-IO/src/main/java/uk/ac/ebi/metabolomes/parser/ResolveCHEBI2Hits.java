package uk.ac.ebi.metabolomes.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import uk.ac.ebi.metabolomes.webservices.BrendaHTTPQuery;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

public class ResolveCHEBI2Hits {

	/**
	 * @param args
	 */
	private Hashtable<String, ArrayList<String>> name2ExtID;
	private Hashtable<String, String> solvedName2ExtID;
	private ChEBIWebServiceConnection chebiConnect;
	private BrendaHTTPQuery brendaQuery;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String filename = args[0];
		ResolveCHEBI2Hits r = new ResolveCHEBI2Hits(filename);
	}
	
	private void parseSabioRKNormaWebOutput(String filename) throws IOException {
		BufferedReader bfile = new BufferedReader(new FileReader(filename));
		String line = bfile.readLine();
		line = bfile.readLine(); // Skip header line
		while (line != null) {
			// suppose header like this
			// ID (ExtDB_Newcores) Name (ExtDB_Newcores) Name
			// (human_metabolites_brenda_consolidated.txt)
			String[] tokens = line.split("\t");
			ArrayList<String> extDBIds;
			if (name2ExtID.containsKey(tokens[2].toLowerCase())) {
				extDBIds = name2ExtID.get(tokens[2].toLowerCase());
			} else {
				extDBIds = new ArrayList<String>();
			}

			if (!extDBIds.contains(tokens[0].toLowerCase())) {
				extDBIds.add(tokens[0]);
				name2ExtID.put(tokens[2].toLowerCase(), extDBIds);
			}
			line = bfile.readLine();
		}
	}
	
	private void init() throws Exception {
		this.name2ExtID = new Hashtable<String, ArrayList<String>>();
		this.solvedName2ExtID = new Hashtable<String, String>();
		this.chebiConnect = new ChEBIWebServiceConnection();
		this.brendaQuery = new BrendaHTTPQuery();
	}
	
	public ResolveCHEBI2Hits(String filename) throws Exception {
		
		this.init();
		this.parseSabioRKNormaWebOutput(filename);
		//System.out.println("Metab\tName\tIUPAC\tSYN");
		int resolved=0;
		HashMap<String, String> unresolvedMap = new HashMap<String, String>();
		int totalToResolve=0;
		for(String name : name2ExtID.keySet()) {
			if(name2ExtID.get(name).size() > 1) { // name2ExtID holds the brenda name associated with the ChEBI id
				totalToResolve++;
				String[] idsChebi = new String[name2ExtID.get(name).size()];
				for(int i=0; i<name2ExtID.get(name).size();i++) {
					idsChebi[i] = name2ExtID.get(name).get(i);
				}
				HashMap<String, String> chebiID2InChiKey = chebiConnect.getInChIKeys(idsChebi);
				ArrayList<String> brendaNamesFromInchiKey = new ArrayList<String>();
				String choosenChebiID = null;
				for(String chebiID : chebiID2InChiKey.keySet()) {
					String inchiKey = chebiID2InChiKey.get(chebiID);
					ArrayList<String> aux = brendaQuery.makeInChIKey2NameQuery(inchiKey);
					brendaNamesFromInchiKey.addAll(aux);
					for(String brendaName : aux ) {
						String proc = brendaName.replaceFirst("^a ", "");
						if(name.equalsIgnoreCase(brendaName) || name.equalsIgnoreCase(proc)) {
							choosenChebiID = chebiID;
							break;
						}
					}
					if(choosenChebiID != null) break;
				}
				if(choosenChebiID != null) {
					solvedName2ExtID.put(name, choosenChebiID);
					System.out.println(name+"\t"+choosenChebiID);
					resolved++;
				}
				else {
					unresolvedMap.put(name, this.join(brendaNamesFromInchiKey, "\t"));
				}
				
				
//				HashMap<String, SearchCategory> resultMap = new HashMap<String, SearchCategory>();
//				ArrayList<String> ids_name = new ArrayList<String>();
//				ArrayList<String> ids_iupac = new ArrayList<String>();
//				ArrayList<String> ids_syn = new ArrayList<String>();
//				ArrayList<Entity> hits = this.getCompleteEntities(name2ExtID.get(name));
//				// we check whether the match was made against the name or the synonyms
//				for(Entity e : hits) {
//					if(name.equalsIgnoreCase(e.getChebiAsciiName())) {
//						resultMap.put(e.getChebiId(), SearchCategory.CHEBI_NAME);
//						ids_name.add(e.getChebiId());
//						continue;
//					}
//					List<DataItem> iupacNames = e.getIupacNames();
//					boolean iupacHit = false;
//					for(DataItem d : iupacNames) {
//						if(name.equalsIgnoreCase(d.toString())) {
//							iupacHit = true;
//							resultMap.put(e.getChebiId(), SearchCategory.IUPAC_NAME);
//							ids_iupac.add(e.getChebiId());
//							break;
//						}	
//					}
//					if(iupacHit) continue;
//					List<DataItem> syns = e.getSynonyms();
//					boolean synonymHit = false;
//					for(DataItem d : syns) {
//						if(name.equalsIgnoreCase(d.toString())) {
//							synonymHit = true;
//							resultMap.put(e.getChebiId(), SearchCategory.SYNONYM);
//							ids_syn.add(e.getChebiId());
//							break;
//						}	
//					}
//				}
				//System.out.println(name+"\t"+ids_name.size()+"\t"+ids_iupac.size()+"\t"+ids_syn.size());

				
			}
		}
		System.err.println("Resolved: "+resolved);
		int unresolved = totalToResolve - resolved;
		System.err.println("Unresolved: "+unresolved);
		for(String key : unresolvedMap.keySet()) {
			System.out.println(key+"\t"+unresolvedMap.get(key));
		}

	}
	

	  
	  public static String join(ArrayList<String> enumeration, String delimiter) {
		  StringBuffer buffer = new StringBuffer();
		  Iterator iter = enumeration.iterator();
		  while (iter.hasNext()) {
			  buffer.append(iter.next());
			  if (iter.hasNext()) {
				  buffer.append(delimiter);
			  }
		  }
		  return buffer.toString();
	  }

	  public Hashtable<String, String> getSolvedNames2ExtID() {
		  return this.solvedName2ExtID;
	  }

}
