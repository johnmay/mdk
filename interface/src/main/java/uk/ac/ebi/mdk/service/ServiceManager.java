/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.service;


import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

/**
 * ServiceManager - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ServiceManager {

    /**
     * Determine whether a particular service is available in the manager. This method will
     * determine not only if the service is pressent in the manager but also whether the service
     * is available (via. {@see QueryService#startup()). <br/>
     * <p/>
     * <b>Example:</b>
     * <pre>
     * manager.hasService(ChEBIIdentifier.class,
     *                    CrossReferenceService.class)
     * </pre>
     *
     * @param identifierClass class of the identifier you want the service for
     * @param serviceClass    class of the
     * @param <S>             extends {@see QueryService}
     * @param <I>             extends {@see Identifier}
     *
     * @return
     */
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<? extends I> identifierClass,
                                                                                Class<S> serviceClass);

    public <S extends QueryService<I>, I extends Identifier> boolean hasService(I identifier,
                                                                                Class<S> serviceClass);

    /**
     * Access a service for a given identifier and service type. This method
     * is used to access a required service. Note that if multiple services
     * match the requirement then the first encountered will be returned. It
     * is therefore important to have a different service manager for each
     * service type (i.e. lucene, database and webservice).
     * <p/>
     * <b>Example:</b><br/>
     * <pre>{@code
     * // get the required service manager
     * ServiceManager manager = ...;
     * <p/>
     * ChEBIIdentifier identifier = new ChEBIIdentifier("CHEBI:15422");
     * <p/>
     * // get the cross-reference service for a given identifier
     * if(manager.hasService(identifier.getClass(), CrossReferenceService.class)){
     * <p/>
     *     CrossReferenceService service = manager.getService(identifier.getClass(),
     *                                                        CrossReferenceService.class);
     * <p/>
     *     // use the service
     *     Collection crossreferences = service.getCrossReferences(identifier);
     * }}
     * </pre>
     *
     * @param identifierClass class of the identifier you want the service for
     * @param serviceClass    class of the
     * @param <S>             extends {@see QueryService}
     * @param <I>             extends {@see Identifier}
     *
     * @return instance of the service that matches the criteria (if no service
     *         matches and invalid parameter exception is thrown)
     */
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<? extends I> identifierClass,
                                                                          Class<S> serviceClass);

    public <S extends QueryService<I>, I extends Identifier> S getService(I identifier,
                                                                          Class<S> serviceClass);

}
