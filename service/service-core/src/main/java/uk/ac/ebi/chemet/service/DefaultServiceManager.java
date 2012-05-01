package uk.ac.ebi.chemet.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * @author John May
 */
public class DefaultServiceManager implements ServiceManager {

    private final ServiceLoader<QueryService> serviceLoader = ServiceLoader.load(QueryService.class);

    private final Multimap<ServiceKey, QueryService> services = TreeMultimap.create(new Comparator<ServiceKey>() {
                                                                                        @Override
                                                                                        public int compare(ServiceKey o1, ServiceKey o2) {
                                                                                            int diff =  o1.identifier.compareTo(o2.identifier);
                                                                                            if(diff != 0){
                                                                                                return diff;
                                                                                            }
                                                                                            return o1.service.compareTo(o2.service);
                                                                                        }
                                                                                    }, new Comparator<QueryService>() {
                                                                                        @Override
                                                                                        public int compare(QueryService o1, QueryService o2) {
                                                                                            return o1.getServiceType().getPriority().compareTo(o2.getServiceType().getPriority());
                                                                                        }
                                                                                    }
    );
    private final Multimap<Class<?>, Class<? extends QueryService>> interfaceMap = HashMultimap.create();

    private static class DefaultServiceManagerHolder {
        private static DefaultServiceManager INSTANCE = new DefaultServiceManager();
    }

    public static ServiceManager getInstance() {
        return DefaultServiceManagerHolder.INSTANCE;
    }

    private DefaultServiceManager() {

        // load services using the JDK's Service Provider Interface
        for (QueryService service : serviceLoader) {

            Class identifierClass = service.getIdentifier().getClass();

            for (Class<? extends QueryService> serviceClass : getImplementingInterfaces(service.getClass())) {
                ServiceKey key = new ServiceKey(identifierClass, serviceClass);
                services.put(key, service);
            }
        }

    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(I identifier, Class<S> serviceClass) {
        return hasService((Class<? extends I>) identifier.getClass(), serviceClass);
    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<? extends I> identifierClass, Class<S> serviceClass) {
        ServiceKey key = new ServiceKey(identifierClass, serviceClass);
        return services.containsKey(key);
    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> S getService(I identifier, Class<S> serviceClass) {
        return getService((Class<? extends I>) identifier.getClass(), serviceClass);
    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<? extends I> identifierClass, Class<S> serviceClass) {

        ServiceKey key = new ServiceKey(identifierClass, serviceClass);

        Collection<QueryService> implementations = (Collection<QueryService>) services.get(key);

        // sort into desired order
        for (QueryService service : implementations) {
            if (service.startup()) {
                return (S) service;
            }
        }

        throw new InvalidParameterException("No services available!");

    }

    // Note: does not account for inherited identifiers
    private class ServiceKey {

        private final String identifier;
        private final String service;

        public ServiceKey(Class<? extends Identifier> identifier,
                          Class<? extends QueryService> service) {
            this.identifier = identifier.getName();
            this.service = service.getName();
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (!(o instanceof ServiceKey)) return false;

            ServiceKey that = (ServiceKey) o;

            if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;
            if (service != null ? !service.equals(that.service) : that.service != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = identifier != null ? identifier.hashCode() : 0;
            result = 31 * result + (service != null ? service.hashCode() : 0);
            return result;
        }
    }

    /**
     * Class traversal algorithm adapted from:
     * <a href="http://today.java.net/pub/a/today/2008/08/21/complex-table-cell-rendering.html">Article</a>
     *
     * @param c
     *
     * @return
     */
    private Collection<Class<? extends QueryService>> getImplementingInterfaces(Class<?> c) {

        if (interfaceMap.containsKey(c)) {
            return interfaceMap.get(c);
        }

        Queue<Class<?>> queue = new LinkedList<Class<?>>();
        Set<Class<?>> visited = new HashSet<Class<?>>();
        Set<Class<? extends QueryService>> implementations = new HashSet<Class<? extends QueryService>>();

        queue.add(c);
        visited.add(c);

        while (!queue.isEmpty()) {
            Class<?> curClass = queue.remove();

            // get the super types to visit.
            List<Class<?>> supers = new LinkedList<Class<?>>();
            for (Class<?> itrfce : curClass.getInterfaces()) {
                supers.add(itrfce);
            }
            Class<?> superClass = curClass.getSuperclass(); // this would be null for interfaces.
            if (superClass != null) {
                supers.add(superClass);
            }

            for (Class<?> ifs : supers) {
                if (QueryService.class.isAssignableFrom(ifs)
                        && ifs.isInterface()
                        && !QueryService.class.equals(ifs)) {
                    implementations.add((Class<? extends QueryService>) ifs);
                }
                implementations.addAll(getImplementingInterfaces(ifs));
            }

        }

        interfaceMap.putAll(c, implementations);

        return implementations;
    }

}
