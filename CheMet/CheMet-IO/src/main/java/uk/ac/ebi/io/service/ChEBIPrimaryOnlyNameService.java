/**
 * ChEBIPrimaryOnlyNameService.java
 *
 * 2012.01.22
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
package uk.ac.ebi.io.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.services.ChemicalNameQueryService;
import uk.ac.ebi.io.remote.ChEBINames;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 * @name    ChEBIPrimaryOnlyNameService
 * @date    2012.01.22
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Equivalent to ChEBINameService, but in the case of fuzzy name search and search name, it will collapse all the ids
 *          obtained by ChEBINameService to retrieve only the Primary ChEBI IDs.
 *
 */
public class ChEBIPrimaryOnlyNameService extends ChEBIQueryService
        implements ChemicalNameQueryService<ChEBIIdentifier> {
    
    private static final Logger LOGGER = Logger.getLogger(ChEBIPrimaryOnlyNameService.class);
    
    private final ChEBINameService nameService = ChEBINameService.getInstance();
    private final ChEBISecondary2PrimaryIDService sec2PrimService = ChEBISecondary2PrimaryIDService.getInstance();

    private ChEBIPrimaryOnlyNameService() {
        super(new ChEBINames());
    }
    
    public static ChEBIPrimaryOnlyNameService getInstance() {
        return ChEBIPrimaryOnlyNameServiceHolder.INSTANCE;
    }
    
    private static class ChEBIPrimaryOnlyNameServiceHolder {

        private static final ChEBIPrimaryOnlyNameService INSTANCE = new ChEBIPrimaryOnlyNameService();
    }
    
    public String getIUPACName(ChEBIIdentifier identifier) {
        return nameService.getIUPACName(identifier);
    }

    public Collection<ChEBIIdentifier> fuzzySearchForName(String name) {
        return getOnlyPrimaryUniqueIdentifiers(nameService.fuzzySearchForName(name));
    }

    public Collection<ChEBIIdentifier> searchForName(String name) {
        return getOnlyPrimaryUniqueIdentifiers(prioritizeNameOverSynonymResults(nameService.searchForName(name),name));
    }
    
    /**
     * When multiple results are retrieved, this method checks whether some results where obtained by comparing against
     * synonys compared to names. If this is the case, only the direct name results are kept.
     * @param searchForName
     * @return 
     */
    private Collection<ChEBIIdentifier> prioritizeNameOverSynonymResults(Collection<ChEBIIdentifier> searchForName, String name) {
        Collection<ChEBIIdentifier> selectedByName = new ArrayList<ChEBIIdentifier>();
        for (ChEBIIdentifier chEBIIdentifier : searchForName) {
            String prefName = nameService.getPreferredName(chEBIIdentifier);
            if(prefName!=null && prefName.equalsIgnoreCase(name))
                selectedByName.add(chEBIIdentifier);
        }
        if(selectedByName.size()>0)
            return selectedByName;
        else
            return searchForName;
    }
    
    private Collection<ChEBIIdentifier> getOnlyPrimaryUniqueIdentifiers(Collection<ChEBIIdentifier> idents) {
        Set<ChEBIIdentifier> toRet = new HashSet<ChEBIIdentifier>();
        for (ChEBIIdentifier ident : idents) {
            toRet.add(sec2PrimService.getPrimaryChEBIID(ident));
        }
        return toRet;
    }

    public Collection<String> getNames(ChEBIIdentifier identifier) {
        return nameService.getNames(identifier);
    }

    public String getPreferredName(ChEBIIdentifier identifier) {
        return nameService.getPreferredName(identifier);
    }

    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {
        return nameService.getSynonyms(identifier);
    }
}
