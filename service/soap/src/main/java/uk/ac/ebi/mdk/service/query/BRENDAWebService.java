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
package uk.ac.ebi.mdk.service.query;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * SOAP WebService access for BRENDA. This module uses the BRENDA SOAP Web Service Version 2:
 *
 * http://www.brenda-enzymes.org/soap2/index.php
 *
 * The WSDL file is not WS-I compliant, and as such it can't be used to generate code through the maven-axis plugin.
 * Instead this modules makes direct calls through axis client. The SOAP can be retrieved from:
 *
 * http://www.brenda-enzymes.org/soap2/brenda_server.php?wsdl
 *
 * To implement new methods, the {@link #getNewCall} method needs to be called with the BRENDA Web Service method as argument,
 * as it is done in {@link #getLigandStructureID}. The arguments to the call are given when the call.invoke method is
 * used, adding the arguments as an Object[] array. See the first URL for details on the different available methods.
 *
 *
 * @author pmoreno
 */
public class BRENDAWebService {

    private final Service service;
    private static final String DOMAIN = "http://soapinterop.org/";
    private static final Logger LOGGER = Logger.getLogger(BRENDAWebService.class);

    public BRENDAWebService() {
        service = new Service();
    }

    /**
     * This method should be called for each method of the web service that needs to be exposed.
     *
     * @param methodName The name of the web service method according to BRENDA documentation.
     * @return the call to be executed.
     * @throws ServiceException
     * @throws MalformedURLException
     */
    private Call getNewCall(String methodName) throws ServiceException, MalformedURLException {
        Call call = (Call) service.createCall();
        String endpoint = "http://www.brenda-enzymes.org/soap2/brenda_server.php";
        call.setTargetEndpointAddress( new java.net.URL(endpoint) );
        call.setOperationName(new QName(DOMAIN, methodName));
        return call;
    }

    /**
     * Given a BRENDA ligand (chemical entity) name, it produces the BRENDA Ligand ID (group id) if the name is found in BRENDA.
     * @param ligandName
     * @return the ligand id (group id) or null if there are any errors.
     */
    public String getLigandStructureID(String ligandName) {
        String resultString = null;
        try {
        String methodName = "getLigandStructureIdByCompoundName";
        Call call = getNewCall(methodName);
        resultString = (String) call.invoke( new Object[] {ligandName} );
        } catch (RemoteException e) {
            LOGGER.debug("RemoteException occurred while invoking getLigandStructureIdByCompoundName", e);
        } catch (MalformedURLException e) {
            LOGGER.debug("MalformedURLException occurred while invoking getLigandStructureIdByCompoundName", e);
        } catch (ServiceException e) {
            LOGGER.debug("ServiceException occurred while invoking getLigandStructureIdByCompoundName", e);
        }
        return resultString;
    }


}
