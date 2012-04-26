package uk.ac.ebi.mdk.service;


import uk.ac.ebi.interfaces.identifiers.Identifier;
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
     * is available (via. {@see QueryService#isAvailable()). <br/>
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
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<I> identifierClass,
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
     *
     * ChEBIIdentifier identifier = new ChEBIIdentifier("CHEBI:15422");
     *
     * // get the cross-reference service for a given identifier
     * if(manager.hasService(identifier.getClass(), CrossReferenceService.class)){
     *
     *     CrossReferenceService service = manager.getService(identifier.getClass(),
     *                                                        CrossReferenceService.class);
     *
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
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<I> identifierClass,
                                                                          Class<S> serviceClass);

}
