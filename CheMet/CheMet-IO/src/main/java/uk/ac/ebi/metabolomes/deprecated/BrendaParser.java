package uk.ac.ebi.metabolomes.deprecated;

import java.util.*;
import java.util.regex.*;
//import java.util.HashMap;
import java.io.*;


@Deprecated
public class BrendaParser {

    // OLD BRENDA PARSER, just left here for copy-paste of some useful regexp,
    // if any.
    /*
	private String organism;
	private String version;
	private int taxid;
	private Vector<BrendaEntryEnzyme> enzymes;
	private Pattern speciePat;
	private final String logs = "/tmp/brendaParser";
	private Pattern stoichCommaPat;
	private Pattern validCommaPat;
	private BufferedWriter commaReplace;
	
	public BrendaParser(String filename, String organism, String version) throws IOException {
		BufferedReader bfile = new BufferedReader(new FileReader(filename));
		BufferedWriter rxnLog = new BufferedWriter(new FileWriter(logs+".reaction.log"));
		BufferedWriter prLog = new BufferedWriter(new FileWriter(logs+".protein.log"));
		commaReplace = new BufferedWriter(new FileWriter(logs+".commaRep.log"));
		this.organism = organism;
		this.version = version;
		enzymes = new Vector<BrendaEntryEnzyme>();
		//Enzyme currentEnz = null;
		ArrayList<BrendaEntryEnzyme> currentEnzs = new ArrayList<BrendaEntryEnzyme>();
		ArrayList<String> currentEnzsIndex = new ArrayList<String>();
		speciePat = Pattern.compile("#(\\d+)#\\s"+this.organism+"(.+)<([\\d,]+)>");
		stoichCommaPat = Pattern.compile("^\\d+,(.)");
		validCommaPat = Pattern.compile("(a,c-diamide|N{0,1}(alpha|beta|omega),N{0,1}(\\d+|alpha|beta|omega)|(E|R|S),(R|E|S)|\\d(S|R|E|Z),\\d|\\d,\\d|\\d',\\d|\\d\\?,\\d|N\\d,N\\d|N,N|\\d\",\\d|(\\d+-){0,1}(cis|trans|O),(\\d+-){0,1}(cis|trans|O)|P\\d+,P\\d+)");
		Pattern simpleSpeciePat = Pattern.compile(this.organism);
		Pattern plusComma = Pattern.compile("[\\s,]\\+[,\\s]"); // to get out commas that are next to plus symbols in reactions
		Pattern referenceNumbers = Pattern.compile("RF\t<(\\d+)>\\s(.+)\\{Pubmed:(\\d*)\\}");
		Pattern swissProtLinkPat = Pattern.compile("(\\S{6,7})\\sSwiss\\s{0,1}prot",Pattern.CASE_INSENSITIVE);
		Pattern tremblProtLinkPat = Pattern.compile("\\s(\\S+)\\strembl",Pattern.CASE_INSENSITIVE);
		Pattern genbankProtLinkPat = Pattern.compile("\\s(\\S+)\\sgenbank",Pattern.CASE_INSENSITIVE);
		Pattern uniprotProtLinkPat = Pattern.compile("\\s(\\S+)\\suniprot",Pattern.CASE_INSENSITIVE);
		Pattern emblProtLinkPat = Pattern.compile("\\s(\\S+)\\sembl", Pattern.CASE_INSENSITIVE);
		// missing GenBank, TrEMBL, UniProt, EMBL
		boolean organismFound = false;
		boolean isRecName = false;
		
		int auxNumUniProtLinks=0;
		int auxNumEMBLLinks=0;
		int auxNumTrEMBLLinks=0;
		
		//String currentOrgNum = "";
		//ArrayList<String> currentCitations;
		String line = bfile.readLine();
		int seenEnzymes=0;
		String currentIdLine = "";
		String previousLine = "";
		while( line != null ) {
			while(previousLine.equals(line)) {
				line = bfile.readLine();
			}
			if(line.startsWith("ID")) {
				// read EC
				String[] tokens = line.split("\t");
				//for(Enzyme e : currentEnzs) {
				//if( currentEnz != null && organismFound ) {
				if(currentEnzs.size() > 0 && organismFound) {
					for(BrendaEntryEnzyme e : currentEnzs) {
						enzymes.add(e);
						prLog.write("Enzyme "+seenEnzymes+" added for line: "+currentIdLine+"\n");
					}
					//enzymes.add(currentEnz); ************
				}
				//}
				currentIdLine = line;
				//Empty the vector and create new initial enzyme
				//currentEnzs = null;
				//currentEnzs.add(new Enzyme());
				currentEnzs = new ArrayList<BrendaEntryEnzyme>();
				currentEnzsIndex = new ArrayList<String>();
				currentEnzs.add(new BrendaEntryEnzyme());
				//currentEnz = new Enzyme();
				organismFound = false;
				// We need to address this kind of cases here
				// EC number expected: 1.1.1.109 (transferred to EC 1.3.1.28)
				int finalECChar = tokens[1].length();
				if(tokens[1].indexOf(" ") > 0) {
					finalECChar = tokens[1].indexOf(" ");
					//Add the comment to the initial enzyme, if we found a new one in PR, we add the registration data from [0] to the i-th new enzyme.
					//currentEnz.addEcComment(tokens[1].substring(finalECChar));
					currentEnzs.get(0).addEcComment(tokens[1].substring(finalECChar)); // this needs to be copied afterwards to new instances.
				}
					
				String ecInLine = tokens[1].substring(0, finalECChar);
				if( ecInLine.split("\\.").length == 4 ) {
					// It has the EC structure partly
					//Add the comment to the initial enzyme, if we found a new one in PR, we add the registration data from [0] to the i-th new enzyme.
					//currentEnz.addEcNumber(ecInLine);
					currentEnzs.get(0).addEcNumber(ecInLine); // this needs to be copied to new instances
				}
				else {
					System.err.println("EC number expected: "+ecInLine+"\n");
				}
			}
			else if(line.startsWith("PR\t")) {
				// here we can find more than one entry per organism (in different lines), watch out
				String[] specTokens = line.split("\t");
				Matcher matchSpec = speciePat.matcher(specTokens[1]);
				if(matchSpec.find()) {
					String currentOrgNum = matchSpec.group(1);
					String proteinAndComments = matchSpec.group(2);
					String citationList = matchSpec.group(3);
					BrendaEntryEnzyme e;
					if(currentEnzsIndex.size() < 1) {
						currentEnzsIndex.add(currentOrgNum);
						e = currentEnzs.get(0);
						//currentEnzs.get(0).addCitationList(Arrays.asList(citationList.split(",")));
					}
					else {
						// we already have one or more, hence new enzymes need to be created;
						currentEnzsIndex.add(currentOrgNum);
						int currentIndexPos = currentEnzsIndex.size() - 1; // current index position is where we are standing in currentEnzsIndex and currentEnzs
						currentEnzs.add(new BrendaEntryEnzyme());
						e = currentEnzs.get(currentIndexPos);
						e.copyEssentialsFrom(currentEnzs.get(0));
					}
					e.addCitationList(Arrays.asList(citationList.split(",")));
					if(proteinAndComments.length() > 7) {
						Matcher swProtLink = swissProtLinkPat.matcher(proteinAndComments);
						Matcher gbProtLink = genbankProtLinkPat.matcher(proteinAndComments);
						Matcher upProtLink = uniprotProtLinkPat.matcher(proteinAndComments);
						Matcher tmProtLink = tremblProtLinkPat.matcher(proteinAndComments);
						Matcher emProtLink = emblProtLinkPat.matcher(proteinAndComments);
						if(swProtLink.find())
							e.addProteinCrossRef("SwissProt",swProtLink.group(1));
						else if(gbProtLink.find())
							e.addProteinCrossRef("GenBank",gbProtLink.group(1));
						else if(upProtLink.find()) {
							e.addProteinCrossRef("UniProt", upProtLink.group(1));
							auxNumUniProtLinks++;
						}
						else if(tmProtLink.find()) {
							e.addProteinCrossRef("TrEMBL", tmProtLink.group(1));
							auxNumTrEMBLLinks++;
						}
						else if(emProtLink.find()) {
							e.addProteinCrossRef("EMBL", emProtLink.group(1));
							auxNumEMBLLinks++;
						}
						
					}
					//currentEnz.addCitationList( Arrays.asList(matchSpec.group(2).split(",")) );
					organismFound = true;
					seenEnzymes++;
					//prLog.write(line+"\n");
				}
				//else {
					//Matcher matchSimple = simpleSpeciePat.matcher(line);
					//if(matchSimple.find()) {
					//	prLog.write(line+"\n");
					//}
				//}
				//else {
				//	System.err.println("Specie line with problems:"+line+"\n");
				//}
			}
			else if(line.startsWith("RECOMMENDED_NAME")) {
				if(!organismFound) {
					line = bfile.readLine();
					continue;	
				}
				isRecName = true;
			}
			else if(line.startsWith("RN\t") && isRecName) {
				String[] tokensRN = line.split("\t");
				for(BrendaEntryEnzyme e : currentEnzs) {
					if(tokensRN.length > 1)
						e.addRecommendedName(tokensRN[1]);
				}
				//currentEnz.addRecommendedName(tokensRN[1]);
				isRecName = false;
			}
			else if(line.startsWith("SN\t")) {
				String[] tokensSN = line.split("\t");
				if(tokensSN.length >= 2) {
					//currentEnz.setSystematicName(tokensSN[1]);
					for(BrendaEntryEnzyme e : currentEnzs) {
						e.setSystematicName(tokensSN[1]);
					}					
				}
				else {
					System.err.println("Problems with line, no systematic name:\n"+line);
				}
				
			}
			else if(line.startsWith("RE\t")) {
				Reaction rxn = new Reaction();
				String react;
				if( line.indexOf("(#") > 0 )
					react = line.substring(3,line.indexOf("(#"));
				else react = line.substring(3);
				Matcher reacPlusComma = plusComma.matcher(react);
				react = reacPlusComma.replaceAll(" + ");
				String[] tokensReact = react.split(" = ");
				if( tokensReact.length < 2 ) {
					rxnLog.write("No RXN struct found: "+line);
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
				String[] leftHS = tokensReact[0].split(" \\+ ");
				String[] rightHS = tokensReact[1].split(" \\+ ");
				
				for(int i=0; i<leftHS.length ; i++) {
					leftHS[i] = this.cleanExtraCommas(leftHS[i]);
					rxn.addReactant(getNameFromMetab(leftHS[i]), getStoichFromMetab(leftHS[i]) );
				}
				for(int i=0; i<rightHS.length ; i++) {
					rightHS[i] = this.cleanExtraCommas(rightHS[i]);
					rxn.addProduct(getNameFromMetab(rightHS[i]), getStoichFromMetab(rightHS[i]));
				}
				
				for(BrendaEntryEnzyme e : currentEnzs) {
					e.addReaction(rxn);
				}				
				//currentEnz.addReaction(rxn);
			}
			else if(line.startsWith("ST\t") && organismFound) {
				List<Integer> orgIndexes = this.findSpecieNumberInLineBeg(line, currentEnzsIndex);
				if(orgIndexes.size() == 0) {
					// none of the current organisms are being mentioned in this NSP line
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
				// Parsing source tissue
				// ST	#1,2,5,7,8,11,27,30,32,43,44,51,56,62,63,68# liver (#5# isoenzyme A2,and B2 <48>; #27# isoenzyme AA-ADH and BB-ADH most abundant in <95>;,#7# isozyme ADH1C*2 <115>),<1,2,5,10,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,39,40,41,42,44,45,46,48,49,51,52,54,55,59,60,86,92,93,95,98,101,110,115,116,141>
				String[] specTokens = line.split("\t"); //pos[1] contains the row of species numbers
//				String speciesNum = specTokens[1].substring(1, specTokens[1].indexOf("#", 2));
//				String[] specNum = speciesNum.split(",");
//				boolean foundOrgNum = false;
//				int indexOfCorrespondingOrg=0;
//				for(String num : specNum) {
//					for(int i=0; i<currentEnzsIndex.size();i++)
//						if(num.equals(currentEnzsIndex.get(i))) {
//							foundOrgNum = true;
//							indexOfCorrespondingOrg=i;
//						}
////					if(num.equals(currentOrgNum)) {
////						foundOrgNum = true;
////						break;
////					}
//				}
//				if(!foundOrgNum) {
//					previousLine = line;
//					line = bfile.readLine();
//					continue;
//				}
				String[] restOfLine = specTokens[1].split("\\s");
				String tissue = "";
				for(int i=1;i<restOfLine.length;i++) {
					// tissues can have more than 1 word, like 'blood serum'
					if(restOfLine[i].charAt(0) == '(' || restOfLine[i].charAt(0) == '<')
						break;
					if(i > 1)
						tissue += " ";
					tissue += restOfLine[i];
				}
				if(tissue.length() > 0 && !tissue.equalsIgnoreCase("more")) {
					for(int i : orgIndexes) {
						currentEnzs.get(i).addSourceTissue(tissue);
					}
				}
//					currentEnzs.get(indexOfCorrespondingOrg).addSourceTissue(tissue);
					//currentEnz.addSourceTissue(tissue);
				
				// Add citations (we can use the same code for most types of lines)
				if(line.lastIndexOf("<") > 0 && (line.lastIndexOf("<") < line.lastIndexOf(">")) ) {
					String[] citationNums =  line.substring(line.lastIndexOf("<")+1, line.lastIndexOf(">")-1).split(",");
					for(int i : orgIndexes ) {
						BrendaEntryEnzyme e = currentEnzs.get(i);
						for(String num : citationNums) {
							// We citation existence for Enzyme in the enzyme class
							e.addCitationLink2lastAddedTissue(num);
						}
					}
				}				
				
			}
			else if(line.startsWith("RF\t")) {
				Matcher refs = referenceNumbers.matcher(line);
				// RF\t<(\\d+)>\\s(.+)\\{Pubmed:(\\d*)\\}
				for(BrendaEntryEnzyme e : currentEnzs) {
					if( refs.find() && refs.groupCount() == 3 && e.citationKeyExists(refs.group(1)) ) {
						String citNum = refs.group(1);
						String citDesc = refs.group(2);
						String citPubmed = refs.group(3);
						if(refs.group(3).length() > 0) {
						//	e.addCitationPubMedId(Integer.parseInt(citNum), Integer.parseInt(citPubmed));
							e.setCitationPubMedId(citNum, Integer.parseInt(citPubmed));
						}
						//e.addCitationDesc(Integer.parseInt(citNum), citDesc); //there is a null pointer exception error here
						e.setCitationTitle(citNum, citDesc);
					}					
				}
			}
			else if(line.startsWith("NSP\t")) {
	
				List<Integer> orgIndexes = this.findSpecieNumberInLineBeg(line, currentEnzsIndex);
				if(orgIndexes.size() == 0) {
					// none of the current organisms are being mentioned in this NSP line
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
				
				String react;
				int indexPar = line.indexOf("(#");
				if(indexPar < 0 )
					indexPar = line.length()*2;
				int indexOr = line.indexOf("|#");
				if(indexOr < 0)
					indexOr = line.length()*2;
				int indexCit = line.lastIndexOf("<");
				if(indexCit < 0)
					indexCit = line.length()*2;
				// Check here
				if( indexPar + indexOr + indexCit < line.length()*6 )
					react = line.substring(line.indexOf("#", 5)+1,Math.min(indexPar, Math.min(indexOr, indexCit)));
				else {
					rxnLog.write("NSP: No delimiter found for line\t"+line+"\n");
					// the delimiter that ends the reaction is not found
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
					
				Matcher reacPlusComma = plusComma.matcher(react);
				react = reacPlusComma.replaceAll(" + ");
				String[] tokensReact = react.split(" = ");
				if( tokensReact.length < 2 ) {
					rxnLog.write("No RXN struct found: "+line);
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
				String[] leftHS = tokensReact[0].split(" \\+ ");
				String[] rightHS = tokensReact[1].split(" \\+ ");
				Reaction rxn = new Reaction();
				boolean moreKeyword = false;
				for(int i=0; i<leftHS.length ; i++) {
					leftHS[i] = this.cleanExtraCommas(leftHS[i]);
					if(leftHS[i].equalsIgnoreCase("more")) {
						moreKeyword = true;
						break;
					}
					if(leftHS[i].equals("?"))
						continue;
					rxn.addReactant(getNameFromMetab(leftHS[i]), getStoichFromMetab(leftHS[i]) );
				}
				if(moreKeyword) {
					// We escape from these vague more fields
					line = bfile.readLine();
					continue;
				}
				for(int i=0; i<rightHS.length ; i++) {
					rightHS[i] = this.cleanExtraCommas(rightHS[i]);
					if(rightHS[i].equals("?"))
						continue;
					rxn.addProduct(getNameFromMetab(rightHS[i]), getStoichFromMetab(rightHS[i]));
				}		
				
				for( int i : orgIndexes ) {
					currentEnzs.get(i).addNatSubsProdRxn(rxn); // this should really iterate over all enzymes we might be working on
					// remember there is more than one organism entry with the same organism name in the PR lines sometimes					
				}

				// Add citations (we can use the same code for most types of lines)
				if(line.lastIndexOf("<") > 0 && (line.lastIndexOf("<") < line.lastIndexOf(">")) ) {
					String[] citationNums =  line.substring(line.lastIndexOf("<")+1, line.lastIndexOf(">")-1).split(",");
					for(int i : orgIndexes ) {
						BrendaEntryEnzyme e = currentEnzs.get(i);
						for(String num : citationNums) {
							// We citation existence for Enzyme in the enzyme class
							e.addCitationLink2lastAddedNSP(num);
						}
					}
				}
				
			}
			else if(line.startsWith("LO\t")) {
				List<Integer> orgIndexes = this.findSpecieNumberInLineBeg(line, currentEnzsIndex);
				if(orgIndexes.size() == 0) {
					// none of the current organisms are being mentioned in this NSP line
					previousLine = line;
					line = bfile.readLine();
					continue;
				}				
				
				String loc;
				int indexPar = line.indexOf("(#");
				if(indexPar < 0 )
					indexPar = line.length()*2;
				int indexOr = line.indexOf("|#");
				if(indexOr < 0)
					indexOr = line.length()*2;
				int indexCit = line.lastIndexOf("<");
				if(indexCit < 0)
					indexCit = line.length()*2;
				// Check here
				if( indexPar + indexOr + indexCit < line.length()*6 )
					loc = line.substring(line.indexOf("#", 4)+1,Math.min(indexPar, Math.min(indexOr, indexCit)));
				else {
					rxnLog.write("LO: No delimiter found for line\t"+line+"\n");
					// the delimiter that ends the reaction is not found
					previousLine = line;
					line = bfile.readLine();
					continue;
				}
				
				for( int i : orgIndexes ) {
					currentEnzs.get(i).addLocalization(loc); // this should really iterate over all enzymes we might be working on
					// remember there is more than one organism entry with the same organism name in the PR lines sometimes					
				}
				// Add citations (we can use the same code for most types of lines)
				if(line.lastIndexOf("<") > 0 && (line.lastIndexOf("<") < line.lastIndexOf(">")) ) {
					String[] citationNums =  line.substring(line.lastIndexOf("<")+1, line.lastIndexOf(">")-1).split(",");
					for(int i : orgIndexes ) {
						BrendaEntryEnzyme e = currentEnzs.get(i);
						for(String num : citationNums) {
							// We citation existence for Enzyme in the enzyme class
							e.addCitationLink2lastAddedLoc(num);
						}
					}
				}	
				
			}
			previousLine = line;
			line = bfile.readLine();
		}
		// We need to add the last set of Enzymes in buffer
		if(currentEnzs.size() > 0 && organismFound) {
			for(BrendaEntryEnzyme e : currentEnzs) {
				enzymes.add(e);
				prLog.write("Enzyme "+seenEnzymes+" added for line: "+currentIdLine+"\n");
			}
			//enzymes.add(currentEnz);
			
		}		
		System.out.println("Number of uniprot links:\t"+auxNumUniProtLinks);
		System.out.println("Number of embl links:\t"+auxNumEMBLLinks);
		System.out.println("Number of trembl links:\t"+auxNumTrEMBLLinks);
		
		prLog.close();
		commaReplace.close();
		System.out.println("Number of seen enzymes: "+seenEnzymes);
	}
	private List<Integer> findSpecieNumberInLineBeg(String line, ArrayList<String> currentEnzsIndex) {
		// returns the index of the correspondingOrg.
		String[] specTokens = line.split("\t"); //pos[1] contains the row of species numbers
		String speciesNum = specTokens[1].substring(1, specTokens[1].indexOf("#", 2));
		String[] specNum = speciesNum.split(",");
		List<Integer> indexOfCorrespondingOrgs = new ArrayList<Integer>();
		for(String num : specNum) {
			for(int i=0; i<currentEnzsIndex.size();i++)
				if(num.equals(currentEnzsIndex.get(i))) {
					indexOfCorrespondingOrgs.add(i);
				}
		}				
		return indexOfCorrespondingOrgs;
	}
	
	private Float getStoichFromMetab(String rxnSubToken) {
		String[] tokens = rxnSubToken.split(" ");
		if( tokens.length == 1 ) // meaning there is no number accompanying the metabolite
			return Float.valueOf(1);
		Float st;
		try {
			st = Float.valueOf(tokens[0]); 
		}
		catch (NumberFormatException n) {
			System.err.println(n);
			System.err.println("Number format exception produced by: "+rxnSubToken);
			return Float.valueOf(1);
		}
		return st;
	}
	private String getNameFromMetab(String rxnSubToken) {
		String[] tokens = rxnSubToken.split(" ");
		if( tokens.length == 1) // there is no number for stoich, stoich default = 1
			return tokens[0];
		Float st = getStoichFromMetab(rxnSubToken);
		if( tokens.length == 2 && st.floatValue() != 1) // two tokens and the first one is a number != 1
			return tokens[1];
		if( tokens.length >= 2 && st.floatValue() == 1) // eq or more than two tokens with default stoich 1 
			return rxnSubToken;
		else {
			String ans = tokens[1];
			for(int i=2; i<tokens.length ; i++) {
				ans += " "+tokens[i];
			}
			return ans;
		}
	}
	
	public int numberOfEnzymesParsed() {
		return enzymes.size();
	}
	public Set<String> getAllDistinctMetabolites() {
		Set<String> metabs = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for(BrendaEntryEnzyme e : enzymes) {
                    for(Reaction r : e.getReactions())
			for(String m : r.getMetabolites() ) {
				metabs.add(m);
			}
		}
		return metabs;
	}
	public Set<String> getAllDistinctMetabolitesFromNSP() {
		Set<String> ordMetabs = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for(BrendaEntryEnzyme e : enzymes) {
			for(Reaction nsp : e.getNaturalSubsProdRxns() ) {
				for(String metab : nsp.getMetabolites() ) {
					ordMetabs.add(metab);
				}
			}
		}
		return ordMetabs;
	}
	public Set<Citation> getAllDistinctCitations() {
		Set<Citation> cits = new TreeSet<Citation>();
		for(BrendaEntryEnzyme e : enzymes) {
			cits.addAll(e.getCitations());
		}
		return cits;
	}
	public Set<String> getAllDistinctTissues() {
		Set<String> tissues = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for(BrendaEntryEnzyme e : enzymes) {
			tissues.addAll(e.getTissues());
		}
		return tissues;
	}
	public ArrayList<String> getProteinLinks() {
		ArrayList<String> ans = new ArrayList<String>();
		for(BrendaEntryEnzyme e : enzymes) {
//			System.out.println("Current size:\t"+ans.size());
			ans.addAll(e.getProteinLinks());
//			System.out.println("Added:\t"+e.getProteinLinks().size());
//			System.out.println("New Size:\t"+ans.size());
			
		}
		return ans;
	}

	private String removeExtraCharacters(String s) throws IOException {
		s = s.trim();
		if(s.charAt(0) == ',') {
			s = s.substring(1);
		}
		if(s.charAt(s.length()-1) == ' ' || s.charAt(s.length()-1) == ',') {
			s = s.substring(0, s.length()-1);	
			commaReplace.write("FilterLastCharacter\t"+s+"\n");
		}
		return s;
	}
	
	private String cleanExtraCommas(String metabolite) throws IOException {
		// First case: stoichiometry and name joined with a comma
		// number,metabolite string
		Matcher commaStoichMat = stoichCommaPat.matcher(metabolite);
		Matcher validCommaMat = validCommaPat.matcher(metabolite);
		if(commaStoichMat.find() && !commaStoichMat.group(1).matches("\\d") ) {
			int indexComma = metabolite.indexOf(",");
			commaReplace.write("Filter1\t"+metabolite+"\t");
			metabolite = metabolite.substring(0, indexComma)+" "+metabolite.substring(indexComma+1);
			metabolite = this.removeExtraCharacters(metabolite);
			commaReplace.write(metabolite+"\n");
			return metabolite;
		}
		// Beware of 5alpha,4alpha, which is valid
		// like 5beta-cholestane-3alpha,7alpha,12alpha-triol
		// 6-[(1S,2R)-1,2-dihydroxy-3-triphosphooxypropyl]-7,8-dihydropterin
		// adenosine 3<94>,5<94>-bisphosphate
		// 3,3?,5?-triiodo-L-thyronine
		// 3-hydroxy-N6,N6,N6-trimethyl-L-lysine
		// (24R)-cholest-5-ene-3beta,24-diol
		// 	1-O,6-O
		else if(validCommaMat.find()) {
			// protect this cases
			commaReplace.write("PROTECT\t"+metabolite+"\n");
			metabolite = this.removeExtraCharacters(metabolite);
			return metabolite;
		}
		else if(metabolite.indexOf(",") > -1 ) {
			commaReplace.write("Filter2\t"+metabolite+"\t"); 
			String replaced = metabolite.replaceAll(",", " ");
			commaReplace.write(replaced+"\n");
			metabolite = this.removeExtraCharacters(replaced);
			return metabolite;
		}
		// Second case: between non numeric parts of the metabolite name
		// cases like 5-L-glutamyl amino,acid
		// 5-phospho-alpha-D-ribose 1-diphosphate
		// 5-phospho-alpha-D-ribose,1-diphosphate

		

		metabolite = this.removeExtraCharacters(metabolite);
		return metabolite;
	}
	public int getTaxID() {
		return taxid;
	}
	
	public Vector<BrendaEntryEnzyme> getEnzymes() {
		return this.enzymes; 
	}
	public String getOrganism() {
		return this.organism;
	}
	public String getBrendaVersion() {
		return this.version;
	}*/
}
