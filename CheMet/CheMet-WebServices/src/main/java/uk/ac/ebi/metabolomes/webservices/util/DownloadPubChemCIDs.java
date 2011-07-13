package uk.ac.ebi.metabolomes.webservices.util;

//Axis-generated PUG SOAP classes
import gov.nih.nlm.ncbi.pubchem.*;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.FileOutputStream;

public class DownloadPubChemCIDs {

    public static void main (String[] argv) throws Exception {

	PUGLocator locator = new PUGLocator();
	PUGSoap soap = locator.getPUGSoap();

	int[] cids = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        String listKey = soap.inputList(cids, PCIDType.eID_CID);
	System.out.println("ListKey = " + listKey);
        System.out.println("number of compounds = "
            + soap.getListItemsCount(listKey));

        // Initialize the download; request SDF with gzip compression
	String downloadKey = soap.download(listKey,
            FormatType.eFormat_SDF, CompressType.eCompress_GZip, false);
	System.out.println("DownloadKey = " + downloadKey);

        // Wait for the download to be prepared
	StatusType status;
	while ((status = soap.getOperationStatus(downloadKey))
                    == StatusType.eStatus_Running ||
               status == StatusType.eStatus_Queued)
        {
            System.out.println("Waiting for download to finish...");
	    Thread.sleep(10000);
	}

        // On success, get the download URL, save to local file
        if (status == StatusType.eStatus_Success) {
            URL url = new URL(soap.getDownloadUrl(downloadKey));
            System.out.println("Success! Download URL = " + url.toString());

            // get input stream from URL
            URLConnection fetch = url.openConnection();
            InputStream input = fetch.getInputStream();

            // open local file based on the URL file name
            String filename = "E:/Users/Thiessen/Downloads"
                    + url.getFile().substring(url.getFile().lastIndexOf('/'));
            FileOutputStream output = new FileOutputStream(filename);
            System.out.println("Writing data to " + filename);

            // buffered read/write
            byte[] buffer = new byte[10000];
            int n;
            while ((n = input.read(buffer)) > 0)
                output.write(buffer, 0, n);
        } else {
            System.out.println("Error: "
                + soap.getStatusMessage(downloadKey));
        }
    }
}

// $Id: DownloadPubChemCIDs.java 151339 2009-02-03 19:39:12Z thiessen $

