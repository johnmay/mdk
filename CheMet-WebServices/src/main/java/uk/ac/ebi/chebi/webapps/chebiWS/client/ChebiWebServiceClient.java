/*
Copyright (c) 2006 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.chebi.webapps.chebiWS.client;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServicePortType;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceService;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.OntologyDataItemList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.RelationshipType;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StructureSearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StructureType;


/**
 * This class is a helper class that allows you to create the
 * webservice connection to the ChEBI webservice and the codes
 * the respective methods for you.
 */
public class ChebiWebServiceClient {

   private ChebiWebServicePortType port;

   /**
    * Uses the default webservice url namely:
    * http://www.ebi.ac.uk/chebi/webservices/webservice?wsdl
    */
   public ChebiWebServiceClient () {
      port = new ChebiWebServiceService().getChebiWebServicePort();
   }

   /**
    * A helper implementation in case the URL changes.
    * @param url the url of the WSDL file
    * @param qname The webservice namespace.
    */
   public ChebiWebServiceClient (URL url, QName qname) {
      port = new ChebiWebServiceService(url, qname).getChebiWebServicePort();
   }

   /**
    * This method returns the complete ChEBI entity if the entity
    * including:
    * <ul>
    * <li>ChEBI ASCII name</li>
    * <li>ChEBI id</li>
    * <li>Definition if available</li>
    * <li>SMILES</li>
    * <li>InChI</li>
    * <li>Chemical Structure in MDL molfile format</li>
    * <li>Synonyms</li>
    * <li>IUPAC Names</li>
    * <li>Database Links</li>
    * <li>Registry Numbers</li>
    * <li>Ontology Parent relationships</li>
    * <li>Ontology Children relationships</li>
    * <li>Comments</li>
    * </ul>
    * @param chebiId the ChEBI identifier in the form of a number prefixed by 'CHEBI:'
    * @return The entity
    *
    * @throws ChebiWebServiceFault_Exception will be thrown if the input parameter is null or does
    * not have the correct identifier format.
    */
   public Entity getCompleteEntity(String chebiId) throws ChebiWebServiceFault_Exception {
      return port.getCompleteEntity(chebiId);
   }

   /**
    * This method allows you to retrieve just a list of names and ChEBI
    * identifiers based on the search parameters.
    *
    * @param searchString any searchstring including UTF-8 characters
    * @param searchCategory can be one of those specified by the enum
    * <code>SearchCategory</code>. If its null the default will be 'ALL'.
    * @return a list of lite entities
    *
    * @throws ChebiWebServiceFault_Exception
    *
    * @see uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory
    */
   public LiteEntityList getLiteEntity(String searchString, SearchCategory searchCategory, int maximumResults, StarsCategory stars) throws ChebiWebServiceFault_Exception {
      return port.getLiteEntity(searchString, searchCategory, maximumResults, stars);
   }

   /**
	* This method returns a list of complete ChEBI entities
    * including:
	*
    * <ul>
    * <li>ChEBI ASCII name</li>
    * <li>ChEBI id</li>
    * <li>Definition if available</li>
    * <li>SMILES</li>
    * <li>InChI</li>
    * <li>Chemical Structure in MDL molfile format</li>
    * <li>Synonyms</li>
    * <li>IUPAC Names</li>
    * <li>Database Links</li>
    * <li>Registry Numbers</li>
    * <li>Ontology Parent relationships</li>
    * <li>Ontology Children relationships</li>
    * <li>Comments</li>
    * </ul>
    *
    * @param list - A String list with ChEBI Ids, with or without prefix "CHEBI:"
	* @return list Entity objects
	* @throws ChebiWebServiceFault_Exception
	*/
   public List<Entity> getCompleteEntityByList (List<String> list) throws ChebiWebServiceFault_Exception {
	   return port.getCompleteEntityByList(list);
   }

   /**
    * Retrieves the ontology parental relationships for the entity identified
    * by its input ChEBI Id. The information returned for each entity are:
    * <ul>
    * <li>ChEBI identifier for the parent entity.</li>
    * <li>ChEBI ASCII name for the parent entity.</li>
    * <li>The relationship type e.g. 'is a'.</li>
    * <li>Whether the relationship type is cyclic.</li>
    * <li>The status of this relationship with CHECKED being annotated by a ChEBI annotator.</li>
    * </ul>
    * <br/>
    * Note if the list is empty then this entity is currently unclassified.
    * @param chebiId the ChEBI identifier in the form of a number prefixed by 'CHEBI:'
    * @return a list of ontology parents
    *
    * @throws ChebiWebServiceFault_Exception
    */
   public OntologyDataItemList getOntologyParents(String chebiId) throws ChebiWebServiceFault_Exception {
      return port.getOntologyParents(chebiId);
   }

   /**
    * Retrieves the ontology children relationships for the entity identified
    * by its input ChEBI Id. The information returned for each entity are:
    * <ul>
    * <li>ChEBI identifier for the child entity.</li>
    * <li>ChEBI ASCII name for the child entity.</li>
    * <li>The relationship type e.g. 'is a'.</li>
    * <li>Whether the relationship type is cyclic.</li>
    * <li>The status of this relationship with CHECKED being annotated by a ChEBI annotator.</li>
    * </ul>
    * <br/>
    * Note if the list is empty then there are not children for this entity.
    * @param chebiId  the ChEBI identifier in the form of a number prefixed by 'CHEBI:'
    * @return a list of ontology children
    *
    * @throws ChebiWebServiceFault_Exception
    */
   public OntologyDataItemList getOntologyChildren(String chebiId) throws ChebiWebServiceFault_Exception {
      return port.getOntologyChildren(chebiId);
   }

   /**
    * Method retrieves all the parent relationships with their
    * ChEBI id and ChEBI ASCII name in an <code>OntologyDataItemList</code>
    *
    * @param chebiId
    * @return the results - if nothing the list will be empty
    * @throws ChebiWebServiceFault_Exception
    */
   public LiteEntityList getAllOntologyChildrenInPath (String chebiId, RelationshipType relationshipType, boolean structureOnly) throws ChebiWebServiceFault_Exception {
	   return port.getAllOntologyChildrenInPath(chebiId, relationshipType, structureOnly);
   }

   /**
    * Retrieves the Compound based on a structure, of type MOL, CML or SMILES
    * You can also setup the category regarding Similarity, Substructure or Identical.
    * <br>Choose the maximum results and the tanimoto cutoff.
    *
	 * @param structure - String representing MOL, CML or SMILES
	 * @param type - MOLFILE, CML or SMILES
	 * @param structureSearchCategory - Substructure, Similarity or Identity
	 * @param totalResults - Maximum result returned.
	 * @param tanimotoCutoff - How similar the results will be to the structure been searched.
	 * @return LiteEntityList - List of Entity.
	 * @throws ChebiWebServiceFault_Exception
	 */
   public LiteEntityList getStructureSearch ( String structure, StructureType type, StructureSearchCategory structureSearchCategory, int totalResults, float tanimotoCutoff) throws ChebiWebServiceFault_Exception {
	   return port.getStructureSearch(structure, type, structureSearchCategory, totalResults, tanimotoCutoff);
   }

}
