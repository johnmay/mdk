/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author pmoreno
 */
public class AEAtlasResponseParser {

    public static List<AEAtlasResultBean> parseResponse(String response) {
        XMLEventReader xmlEventReader;
        try {
            xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(new StringReader(response));
        } catch (XMLStreamException ex) {
            System.out.println("Error in opening XML stream");
            return null;
        }
        XMLEvent event;
        List<AEAtlasResultBean> results = new ArrayList<AEAtlasResultBean>();
        AEAtlasGeneBean currentGene=null;
        AEAtlasExpressionBean currentExpression=null;
        AEAtlasSlimExperiment currentExp=null;
        AEAtlasResultBean currentResult=null;

        try {
        while(xmlEventReader.hasNext()) {
            event = xmlEventReader.nextEvent();

            if(event.isStartElement()) {
                StartElement se = event.asStartElement();
                String name = se.getName().getLocalPart();
                if(name.equals("result")) {
                    currentResult = new AEAtlasResultBean();
                }
                else if(name.equals("gene")) {
                    currentGene = new AEAtlasGeneBean();
                } else if(name.equals("id") && currentGene!=null) {
                    event = xmlEventReader.nextEvent();
                    currentGene.setId(event.asCharacters().getData());
                } else if(name.equals("name") && currentGene!=null) {
                    event = xmlEventReader.nextEvent();
                    currentGene.setName(event.asCharacters().getData());
                } else if(name.equals("uniprotId") && currentGene!=null) {
                    event = xmlEventReader.nextEvent();
                    currentGene.getUniprotIDs().add(event.asCharacters().getData());
                }
                else if(name.equals("expression") && currentExp==null) {
                    currentExpression = new AEAtlasExpressionBean();
                } else if(name.equals("experiment")) {
                    currentExp = new AEAtlasSlimExperiment();
                } else if(name.equals("pvalue") && currentExp!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExp.setPvalue(Double.parseDouble(event.asCharacters().getData()));
                } else if(name.equals("expression") && currentExp!=null) {
                    // this is expression inside experiment
                    event = xmlEventReader.nextEvent();
                    currentExp.setUpExpression(event.asCharacters().getData().equals("UP"));
                } else if(name.equals("accession") && currentExp!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExp.setAccession(event.asCharacters().getData());
                } else if(name.equals("upExperiments") && currentExpression!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExpression.setUpExperiments(Integer.parseInt(event.asCharacters().getData()));
                } else if(name.equals("downExperiments") && currentExpression!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExpression.setDownExperiments(Integer.parseInt(event.asCharacters().getData()));
                } else if(name.equals("upPvalue") && currentExpression!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExpression.setUpPValue(Double.parseDouble(event.asCharacters().getData()));
                } else if(name.equals("downPvalue") && currentExpression!=null) {
                    event = xmlEventReader.nextEvent();
                    currentExpression.setDownPValue(Double.parseDouble(event.asCharacters().getData()));
                }
            } else if(event.isEndElement()) {
                EndElement ee = event.asEndElement();
                String name = ee.getName().getLocalPart();
                if(name.equals("result")) {
                    results.add(currentResult);
                    currentResult=null;
                } else if(name.equals("gene")) {
                    currentResult.setGene(currentGene);
                    currentGene=null;
                } else if(name.equals("experiment")) {
                    currentExpression.getExperiments().add(currentExp);
                    currentExp=null;
                } else if(name.equals("expression") && currentExp==null && currentExpression!=null) {
                    // this is the expression that encapsulates experiment
                    currentResult.getExpressions().add(currentExpression);
                    currentExpression=null;
                }
            }
        }
        } catch(XMLStreamException ex) {
            System.out.println("Error in XML parsing");
            return null;
        }

        return results;

    }


}
