/**
 * MarshallFactory.java
 *
 * 2012.01.31
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
package uk.ac.ebi.io.core;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ebi.interfaces.io.marshal.AnnotatedEntityMarshaller;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.entities.*;
import uk.ac.ebi.interfaces.io.marshal.MarshallFactory;
import uk.ac.ebi.io.core.marshal.AbstractAnnotatedEntityMarshaller;
import uk.ac.ebi.io.core.marshal.AbstractEntityMarshaller;
import uk.ac.ebi.io.core.marshal.versions.*;
import uk.ac.ebi.io.core.marshal.versions.product.GeneProductMarshaller_0_8_5_3;
import uk.ac.ebi.io.core.marshal.versions.product.ProteinProductMarshaller_0_8_5_3;
import uk.ac.ebi.io.core.marshal.versions.product.RNAMarshaller_0_8_5_3;
import uk.ac.ebi.io.core.marshal.versions.product.RibosomalRNAMarshaller_0_8_5_3;
import uk.ac.ebi.io.core.marshal.versions.product.TransferRNAMarshaller_0_8_5_3;


/**
 *
 *          MarshallFactory 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Singleton description
 *
 */
public class MarshallFactoryImplementation implements MarshallFactory {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MarshallFactoryImplementation.class);

    private Version version;

    private AbstractEntityMarshaller[] entityMarshalls = new AbstractEntityMarshaller[]{
        new EntityMarshaller_0_8_5_0()};

    private AbstractAnnotatedEntityMarshaller[] annotatedEntityMarshalls = new AbstractAnnotatedEntityMarshaller[]{
        new AnnotatedEntityMarshaller_0_8_5_2(),
        new AnnotatedEntityMarshaller_0_8_5_0()};

    private AnnotatedEntityMarshaller[] metaboliteMarshalls = new AnnotatedEntityMarshaller[]{
        new MetaboliteMarshaller_0_8_5_0()};

    private AnnotatedEntityMarshaller[] reactionMarshalls = new AnnotatedEntityMarshaller[]{
        new ReactionMarshaller_0_8_5_1(),
        new ReactionMarshaller_0_8_5_0()
    };

    private AnnotatedEntityMarshaller[] productMarshalls = new AnnotatedEntityMarshaller[]{
        new GeneProductMarshaller_0_8_5_3()
    };

    private AnnotatedEntityMarshaller[] proteinMarshalls = new AnnotatedEntityMarshaller[]{
        new ProteinProductMarshaller_0_8_5_3()
    };

    private AnnotatedEntityMarshaller[] rnaMarhsalls = new AnnotatedEntityMarshaller[]{
        new RNAMarshaller_0_8_5_3()
    };

    private AnnotatedEntityMarshaller[] rrnaMarshalls = new AnnotatedEntityMarshaller[]{
        new RibosomalRNAMarshaller_0_8_5_3()
    };

    private AnnotatedEntityMarshaller[] trnaMarshalls = new AnnotatedEntityMarshaller[]{
        new TransferRNAMarshaller_0_8_5_3()
    };

    private EntityFactory factory;

    private Map<Class, MarshallAccessor> map = new HashMap<Class, MarshallAccessor>();


    public MarshallFactoryImplementation(Version version, EntityFactory factory) {
        this.version = version;
        this.factory = factory;

        map.put(RibosomalRNA.class, new MarshallAccessor() {

            @Override
            public EntityMarshaller getMarhsaller() {
                return getRRNAMarhsaller();
            }
        });
        map.put(TransferRNA.class, new MarshallAccessor() {

            @Override
            public EntityMarshaller getMarhsaller() {
                return getTRNAMarhsaller();
            }
        });
        map.put(ProteinProduct.class, new MarshallAccessor() {

            @Override
            public EntityMarshaller getMarhsaller() {
                return getProteinMarhsaller();
            }
        });
        map.put(Metabolite.class, new MarshallAccessor() {

            @Override
            public EntityMarshaller getMarhsaller() {
                return getMetaboliteMarshaller();
            }
        });
        map.put(MetabolicReaction.class, new MarshallAccessor() {

            @Override
            public EntityMarshaller getMarhsaller() {
                return getReactionMarshaller();
            }
        });

    }


    public Version getVersion() {
        return version;
    }


    private abstract class MarshallAccessor {

        public abstract EntityMarshaller getMarhsaller();
    }


    public EntityMarshaller getMarhsaller(Class<? extends Entity> c) {
        return map.get(c).getMarhsaller();
    }


    private EntityMarshaller getEntityMarshal(EntityMarshaller superclass) {
        for (AbstractEntityMarshaller entityMarshal : entityMarshalls) {
            if (version.getIndex() >= entityMarshal.getVersion().getIndex()) {
                AbstractEntityMarshaller copy = (AbstractEntityMarshaller) entityMarshal.newInstance();
                copy.setParent(superclass);
                copy.setMarshallFactory(this);
                copy.setEntityFactory(factory);
                return copy;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    private AnnotatedEntityMarshaller getAnnotatedEntityMarshal(AnnotatedEntityMarshaller superclass) {
        for (AbstractAnnotatedEntityMarshaller entityMarshal : annotatedEntityMarshalls) {
            if (version.getIndex() >= entityMarshal.getVersion().getIndex()) {
                AbstractAnnotatedEntityMarshaller copy = (AbstractAnnotatedEntityMarshaller) entityMarshal.newInstance();
                copy.setParent(superclass);
                copy.setMarshallFactory(this);
                copy.setEntityFactory(factory);
                return copy;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public EntityMarshaller getMetaboliteMarshaller() {
        for (AnnotatedEntityMarshaller metaboliteMarshall : metaboliteMarshalls) {
            if (version.getIndex() >= metaboliteMarshall.getVersion().getIndex()) {
                EntityMarshaller marshaller = getEntityMarshal(getAnnotatedEntityMarshal(metaboliteMarshall));
                marshaller.setEntityFactory(factory);
                marshaller.setMarshallFactory(this);
                return marshaller;

            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public EntityMarshaller getReactionMarshaller() {
        LOGGER.debug("Finding reaction version");
        for (AnnotatedEntityMarshaller rxnMarshaller : reactionMarshalls) {
            LOGGER.debug(version + " >= " + rxnMarshaller.getVersion() + "?");
            if (version.getIndex() >= rxnMarshaller.getVersion().getIndex()) {
                EntityMarshaller marshaller = getEntityMarshal(getAnnotatedEntityMarshal(rxnMarshaller));
                marshaller.setEntityFactory(factory);
                marshaller.setMarshallFactory(this);

                return marshaller;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public AnnotatedEntityMarshaller getGeneProductMarhsaller(AnnotatedEntityMarshaller superclass) {
        for (AnnotatedEntityMarshaller productMarhsall : productMarshalls) {
            if (version.getIndex() >= productMarhsall.getVersion().getIndex()) {
                AbstractAnnotatedEntityMarshaller copy = (AbstractAnnotatedEntityMarshaller) productMarhsall.newInstance();
                copy.setParent(superclass);
                copy.setMarshallFactory(this);
                copy.setEntityFactory(factory);
                return copy;

            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public AnnotatedEntityMarshaller getRNAMarhsaller(AnnotatedEntityMarshaller superclass) {
        for (AnnotatedEntityMarshaller rnaMarshall : rnaMarhsalls) {
            if (version.getIndex() >= rnaMarshall.getVersion().getIndex()) {
                AbstractAnnotatedEntityMarshaller copy = (AbstractAnnotatedEntityMarshaller) rnaMarshall.newInstance();
                copy.setParent(superclass);
                copy.setMarshallFactory(this);
                copy.setEntityFactory(factory);
                return copy;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public EntityMarshaller getRRNAMarhsaller() {
        for (AnnotatedEntityMarshaller rrnaMarshall : rrnaMarshalls) {
            if (version.getIndex() >= rrnaMarshall.getVersion().getIndex()) {
                EntityMarshaller marshaller = getEntityMarshal(getAnnotatedEntityMarshal(getGeneProductMarhsaller(getRNAMarhsaller(rrnaMarshall))));
                marshaller.setEntityFactory(factory);
                marshaller.setMarshallFactory(this);

                return marshaller;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public EntityMarshaller getTRNAMarhsaller() {
        for (AnnotatedEntityMarshaller trnaMarhsall : trnaMarshalls) {
            if (version.getIndex() >= trnaMarhsall.getVersion().getIndex()) {
                EntityMarshaller marshaller = getEntityMarshal(getAnnotatedEntityMarshal(getGeneProductMarhsaller(getRNAMarhsaller(trnaMarhsall))));
                marshaller.setEntityFactory(factory);
                marshaller.setMarshallFactory(this);

                return marshaller;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }


    public EntityMarshaller getProteinMarhsaller() {
        for (AnnotatedEntityMarshaller proteinMarshaller : proteinMarshalls) {
            if (version.getIndex() >= proteinMarshaller.getVersion().getIndex()) {
                EntityMarshaller marshaller = getEntityMarshal(getAnnotatedEntityMarshal(getGeneProductMarhsaller(proteinMarshaller)));
                marshaller.setEntityFactory(factory);
                marshaller.setMarshallFactory(this);

                return marshaller;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }
}
