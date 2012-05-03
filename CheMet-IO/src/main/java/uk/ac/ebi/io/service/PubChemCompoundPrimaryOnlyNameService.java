/**
 * ChEBINameService.java
 *
 * 2011.10.26
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.ChemicalNameQueryService;
import uk.ac.ebi.io.remote.PubChemComp2ParentCompound;

/**
 *          PubChemCompoundPrimaryOnlyNameService - 2012.01.22 <br>
 *          Equivalent to PubChemCompoundNameService, but the {@link #fuzzySearchForName(java.lang.String) },
 *          {@link #searchForName(java.lang.String) } and {@link #searchForNameExcludeSynonyms(java.lang.String) }
 *          results are filtered to deliver only the parent compound identifiers of each identifier in the result.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class PubChemCompoundPrimaryOnlyNameService
        extends PubChemCompoundQueryService
            implements ChemicalNameQueryService<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundPrimaryOnlyNameService.class);
    
    private final PubChemCompoundNameService nameService = PubChemCompoundNameService.getInstance();
    private final PubChemComp2ParentCompoundResolverService resolverService = PubChemComp2ParentCompoundResolverService.getInstance();
    
    private static class PubChemCompoundPrimaryOnlyNameServiceHolder {
        private static final PubChemCompoundPrimaryOnlyNameService INSTANCE = new PubChemCompoundPrimaryOnlyNameService();
    }
    
    public static PubChemCompoundPrimaryOnlyNameService getInstance() {
        return PubChemCompoundPrimaryOnlyNameServiceHolder.INSTANCE;
    }

    private PubChemCompoundPrimaryOnlyNameService() {
        super(new PubChemComp2ParentCompound());
    }
    
    public String getIUPACName(PubChemCompoundIdentifier identifier) {
        return nameService.getIUPACName(identifier);
    }
    
    private Collection<PubChemCompoundIdentifier> getOnlyPrimaryUniqueIdentifiers(Collection<PubChemCompoundIdentifier> idents) {
        Set<PubChemCompoundIdentifier> toRet = new HashSet<PubChemCompoundIdentifier>();
        for (PubChemCompoundIdentifier ident : idents) {
            toRet.add(resolverService.getPrimaryID(ident));
        }
        return toRet;
    }

    public Collection<PubChemCompoundIdentifier> fuzzySearchForName(String name) {
        return getOnlyPrimaryUniqueIdentifiers(nameService.fuzzySearchForName(name));
    }

    public Collection<PubChemCompoundIdentifier> searchForName(String name) {
        return getOnlyPrimaryUniqueIdentifiers(nameService.searchForName(name));
    }

    public Collection<PubChemCompoundIdentifier> searchForNameExcludeSynonyms(String name) {
        return getOnlyPrimaryUniqueIdentifiers(nameService.searchForNameExcludeSynonyms(name));
    }

    public Collection<String> getNames(PubChemCompoundIdentifier identifier) {
        return nameService.getNames(identifier);
    }

    public String getPreferredName(PubChemCompoundIdentifier identifier) {
        return nameService.getPreferredName(identifier);
    }

    public Collection<String> getSynonyms(PubChemCompoundIdentifier identifier) {
        return nameService.getSynonyms(identifier);
    }

}
