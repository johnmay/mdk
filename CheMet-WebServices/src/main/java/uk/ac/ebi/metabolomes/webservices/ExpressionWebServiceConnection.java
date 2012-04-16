/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices;

import uk.ac.ebi.metabolomes.experimental.EvidenceList;

/**
 *
 * @author pmoreno
 */
public abstract class ExpressionWebServiceConnection {

    public abstract EvidenceList getExpEvidencesFor(String id);
}
