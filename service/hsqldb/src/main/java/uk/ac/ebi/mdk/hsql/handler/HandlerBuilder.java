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

package uk.ac.ebi.mdk.hsql.handler;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.tool.AutomaticCompartmentResolver;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.service.query.name.PreferredNameAccess;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

/**
 * A builder of participant handlers. Provides simplified creation of a {@link
 * ParticipantHandler} to create {@link MetabolicParticipant}s.
 *
 * @author John May
 */
public class HandlerBuilder {

    private ServiceManager services = DefaultServiceManager.getInstance();
    private CompartmentResolver resolver = new AutomaticCompartmentResolver();
    private final Class<? extends Identifier> type;
    private Function<Metabolite, Metabolite> annotator = Functions.identity();
    private MoleculeCache<Metabolite> cache = MoleculeCache.empty();

    /**
     * Start building a handler for the given id.
     *
     * @param type an identifier type
     */
    public HandlerBuilder(Class<? extends Identifier> type) {
        this.type = type;
    }

    /**
     * Specify the compartment resolver.
     *
     * @param resolver the compartment resolver.
     * @return self-reference
     */
    public HandlerBuilder withResolver(CompartmentResolver resolver) {
        this.resolver = resolver;
        return this;
    }

    /**
     * Use the metabolite cache to index compounds and cache any created
     * metabolites. When the compound is encountered again it uses the instance
     * which was registered with the key.
     *
     * @param cache molecule cache
     * @return self-reference
     */
    public HandlerBuilder withCache(MoleculeCache<Metabolite> cache) {
        this.cache = cache;
        return this;
    }

    /**
     * Indicate that compound names should be resolved.
     *
     * @return self-reference
     * @see NameAnnotator
     */
    @SuppressWarnings("unchecked")
    public HandlerBuilder withNameResolution() {
        if (services.hasService(type, PreferredNameAccess.class)) {
            return with(new NameAnnotator(services.getService(type, PreferredNameAccess.class)));
        }
        return this;
    }

    /**
     * Indicate that compound structures should be resolved.
     *
     * @return self-reference
     * @see StructureAnnotator
     */
    @SuppressWarnings("unchecked")
    public HandlerBuilder withStructureResolution() {
        if (services.hasService(type, StructureService.class)) {
            return with(new StructureAnnotator(services.getService(type, StructureService.class)));
        }
        return this;
    }

    /**
     * Use the specified function to annotate the metabolite during
     * construction.
     *
     * @param annotator function to annotate a metabolite
     * @return self-reference
     */
    public HandlerBuilder with(Function<Metabolite, Metabolite> annotator) {
        this.annotator = Functions.compose(this.annotator, annotator);
        return this;
    }

    /**
     * Build the handler.
     *
     * @return handler instance
     * @see uk.ac.ebi.mdk.service.query.RawReactionAccess
     */
    public ParticipantHandler<MetabolicParticipant> build() {
        IdentifierFactory ids = DefaultIdentifierFactory.getInstance();
        EntityFactory entities = DefaultEntityFactory.getInstance();
        return new MetabolicParticipantHandler(entities,
                                               ids.ofClass(type),
                                               resolver,
                                               annotator,
                                               cache);
    }

    public static ParticipantHandler<MetabolicParticipant> automatic(Identifier id) {
        return new HandlerBuilder(id.getClass())
                .withNameResolution()
                .withStructureResolution()
                .build();
    }

}
