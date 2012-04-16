package uk.ac.ebi.metabolomes.webservices;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class BrendaHTTPQuery {

	/**
	 * @param args
	 */
	private final String baseUrl = "http://www.brenda-enzymes.org/";
	private final String query = "php/check_inchi.php4?InchiKey=";
	private final String option = "&getLigands=true";
	private URL url;
	private ArrayList<String> brendaMolNames;
	private SAXParserFactory factory;
	private SAXParser parser;
	private BrendaQueryResults queryHandler;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		BrendaHTTPQuery b = new BrendaHTTPQuery();
		for(String arg : args) {
			System.out.println("For InChI Key: "+arg);
			for(String r : b.makeInChIKey2NameQuery(arg)) {
				System.out.println("Metabolite name: "+r);
			}
		}
	}
	
	private void init() {
		brendaMolNames = new ArrayList<String>();
		factory = SAXParserFactory.newInstance();
		try {
			parser = factory.newSAXParser();
			queryHandler = new BrendaQueryResults();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BrendaHTTPQuery() throws IOException {
		this.init();
	}
	
	public ArrayList<String> makeInChIKey2NameQuery(String inchiKey) throws IOException {
		url = new URL(baseUrl+query+inchiKey+option);
		queryHandler = new BrendaQueryResults();
		try {
                        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
                        String line = bf.readLine();
                        String xmlRes = "";
                        while(line!=null) {
                            xmlRes += line;
                            line = bf.readLine();
                        }
                        //xmlRes = xmlRes.replaceAll("[", "\\[");
                        xmlRes = xmlRes.replaceAll("]", "__CLOSE_BRACKET__");
                        ByteArrayInputStream xmlByteBuf = new ByteArrayInputStream(xmlRes.getBytes());
			parser.parse(xmlByteBuf, (DefaultHandler) queryHandler );
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> lines = new ArrayList<String>();
                for(String xmlLine : queryHandler.getCompoundNames()) {
                    lines.add(xmlLine.replaceAll("__CLOSE_BRACKET__", "]"));
                }
                return lines;
	}

}