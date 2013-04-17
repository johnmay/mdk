/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.domain.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.DefaultLoader;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name KEGGCrossReference - 2011.10.03 <br>
 * Class metaInfo
 */
@Context(Metabolite.class)
@Brief("KEGG Compound Cross-reference")
@Description("A cross-reference that specifically links to the KEGG Compound database")
public class KEGGCrossReference<O extends Observation> extends CrossReference<KEGGCompoundIdentifier, O> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCrossReference.class);

    private static MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            KEGGCrossReference.class);


    public KEGGCrossReference() {
    }


    public KEGGCrossReference(KEGGCompoundIdentifier identifier) {
        super(identifier);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public KEGGCrossReference newInstance() {
        return new KEGGCrossReference();
    }
}
