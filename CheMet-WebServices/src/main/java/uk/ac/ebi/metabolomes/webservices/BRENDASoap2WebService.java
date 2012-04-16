/**
 * BRENDASoap2WebService.java
 *
 * 2012.01.21
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
package uk.ac.ebi.metabolomes.webservices;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;

/**
 * @name    BRENDASoap2WebService
 * @date    2012.01.21
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class BRENDASoap2WebService {

    private static final Logger LOGGER = Logger.getLogger(BRENDASoap2WebService.class);
    private final String ENDPOINT = "http://www.brenda-enzymes.org/soap2/brenda_server.php";
    private final Service service = new Service();

    public String getLigandIdentifier(String ligandName) throws WebServiceException {
        String operationName = "getLigandStructureIdByCompoundName";
        try {
            Call call = getCallForService(operationName);
            return (String) call.invoke(new Object[]{ligandName});
        } catch (MalformedURLException e) {
            LOGGER.error("Failed due to malformed url: ", e);
            throw new WebServiceException(e.getMessage());
        } catch (RemoteException e) {
            LOGGER.error("Failed due to remote exception: ", e);
            throw new WebServiceException(e.getMessage());
        } catch (ServiceException e) {
            LOGGER.error("Failed due to Service exception: ", e);
            throw new WebServiceException(e.getMessage());
        }
    }

    private Call getCallForService(String operationName) throws ServiceException, MalformedURLException {
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(new java.net.URL(ENDPOINT));
        call.setOperationName(new QName("http://soapinterop.org/", operationName));
        return call;
    }
}
