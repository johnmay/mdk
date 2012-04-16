package uk.ac.ebi.metabolomes.webservices;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


import javax.xml.namespace.QName;
import java.util.ArrayList;

import java.net.*;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;

public class SabioRKWebServiceConnection {

    /**
     * @param args
     */
    private Service service;
    private Call call;

    private void init() {
        service = new Service();
        try {
            call = ( Call ) service.createCall();
        } catch ( ServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit( 1 );
        }
        String endpoint = "http://sabio.villa-bosch.de/sabiork.jws";
        try {
            call.setTargetEndpointAddress( new URL( endpoint ) );
        } catch ( MalformedURLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit( 1 );
        } //set the endpoint as the URL of the SABIO-RK Web service
    }

    public ArrayList<String> callMethod( String methodName , String argument ) {
        call.setOperationName( new QName( "getPathwayNames" ) ); //set the operation name to the passed method name
        String[] Result = null;
        try {
            Result = ( String[] ) call.invoke( new Object[]{ argument } );
        } catch ( RemoteException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } //invoke the call, typecasting the return value as an array of strings
        ArrayList<String> res = new ArrayList<String>();
        for ( String s : Result ) {
            res.add( s );
        }
        return res;
    }

    public SabioRKWebServiceConnection() {
        this.init();
    }

    public static void main( String[] args ) throws Exception {

        String methodName = args[0]; //first argument passed is the method name
        String argument = args[1]; //second will be the argument for the web service method
        SabioRKWebServiceConnection srk = new SabioRKWebServiceConnection();
        System.out.println( StringUtils.join(
                srk.callMethod( methodName , argument ) , "\t" ) );


    } //end main

    public ArrayList<String> getChEBIIDFromSabioID( String sabioID ) throws Exception {
        ArrayList<String> res = this.callMethod( "getChEBIID" , sabioID );
        return res;
    }
}
