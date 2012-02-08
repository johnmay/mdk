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

import uk.ac.ebi.interfaces.io.marshal.AnnotatedEntityMarshaller;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.io.core.marshal.AbstractAnnotatedEntityMarshaller;
import uk.ac.ebi.io.core.marshal.AbstractEntityMarshaller;
import uk.ac.ebi.io.core.marshal.versions.*;


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
public class MarshallFactory {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MarshallFactory.class);

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

    private EntityFactory factory;


    public MarshallFactory(Version version, EntityFactory factory) {
        this.version = version;
        this.factory = factory;
    }


    public Version getVersion() {
        return version;
    }


    private EntityMarshaller getEntityMarshal(EntityMarshaller superclass) {
        for (AbstractEntityMarshaller entityMarshal : entityMarshalls) {
            if (version.getIndex() >= entityMarshal.getVersion().getIndex()) {
                AbstractEntityMarshaller copy = (AbstractEntityMarshaller) entityMarshal.newInstance();
                copy.setSuperclassMarshal(superclass);
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
                copy.setSuperclassMarshal(superclass);
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
                return marshaller;
            }
        }
        throw new UnsupportedOperationException("No marshall available, version is too old");
    }
}
