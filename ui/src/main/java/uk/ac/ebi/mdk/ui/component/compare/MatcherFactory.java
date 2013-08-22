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

package uk.ac.ebi.mdk.ui.component.compare;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;
import uk.ac.ebi.mdk.prototype.hash.seed.StereoSeed;
import uk.ac.ebi.mdk.tool.match.CrossReferenceMatcher;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.MetaboliteInChIMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John May
 */
public class MatcherFactory {

    private static final Logger LOGGER = Logger.getLogger(MatcherFactory.class);

    private List<MatcherDescription> methods = new ArrayList<MatcherDescription>();

    public static MatcherFactory getInstance() {
        return MatcherFactoryHolder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private MatcherFactory() {

        add(new MatcherDescription<Metabolite>("Cross-reference",
                                               "Defined two entities as the same if they have matching cross-references or they reference each other",
                                               new CrossReferenceMatcher<Metabolite>()));

        add(new MatcherDescription<Metabolite>("HashCode (exact)",
                                               "Uses the hash code of the chemical structure to determine match",
                                               new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                                ConnectedAtomSeed.class,
                                                                                                                BondOrderSumSeed.class,
                                                                                                                ChargeSeed.class,
                                                                                                                StereoSeed.class))));
        add(new MatcherDescription<Metabolite>("HashCode (allow charge difference)",
                                               "Uses the hash code with allowance for charge difference",
                                               new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                                ConnectedAtomSeed.class,
                                                                                                                BondOrderSumSeed.class,
                                                                                                                StereoSeed.class))));
        add(new MatcherDescription<Metabolite>("HashCode (allow stereo and charge difference)",
                                               "Uses the hash code with allowance for charge and stereo differences",
                                               new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                                ConnectedAtomSeed.class,
                                                                                                                BondOrderSumSeed.class))));
        add(new MatcherDescription<Metabolite>("InChi (exact)",
                                               "Uses the InChI line notation to match structure",
                                               new MetaboliteInChIMatcher(false)));
        add(new MatcherDescription<Metabolite>("InChi (connectivity)",
                                               "Uses the InChI line notation and matches only on the connectivity of the atoms",
                                               new MetaboliteInChIMatcher(false)));
        add(new MatcherDescription<Metabolite>("Name (exact)",
                                               "Does a direct string comparison on the name and abbreviation of the entities",
                                               new NameMatcher<Metabolite>(false, false)));
        add(new MatcherDescription<Metabolite>("Name (exact with synonyms)",
                                               "Does a direct string comparison on the name, abbreviation and synonyms of the entities",
                                               new NameMatcher<Metabolite>(false, true)));
        add(new MatcherDescription<Metabolite>("Name (normalised)",
                                               "Does a normalised string comparison on the name and abbreviation of the entities (e.g. removing punctuation)",
                                               new NameMatcher<Metabolite>(true, false)));
        add(new MatcherDescription<Metabolite>("Name (normalised with synonyms)",
                                               "Does a normalised string comparison on the name, abbreviation and synonyms of the entities (e.g. removing punctuation)",
                                               new NameMatcher<Metabolite>(true, true)));

    }

    public void add(MatcherDescription description) {
        this.methods.add(description);
    }

    public List<MatcherDescription> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    private static class MatcherFactoryHolder {
        private static MatcherFactory INSTANCE = new MatcherFactory();
    }
}
