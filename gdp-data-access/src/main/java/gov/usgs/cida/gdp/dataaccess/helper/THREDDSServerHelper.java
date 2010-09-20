package gov.usgs.cida.gdp.dataaccess.helper;

import gov.usgs.cida.gdp.dataaccess.bean.DataTypeCollection;
import gov.usgs.cida.gdp.utilities.bean.Time;
import gov.usgs.cida.gdp.utilities.bean.XmlResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import org.slf4j.LoggerFactory;

import thredds.catalog.*;
import ucar.nc2.VariableSimpleIF;
import ucar.nc2.ft.FeatureDataset;
import ucar.nc2.ft.FeatureDatasetFactoryManager;

public class THREDDSServerHelper {

    static org.slf4j.Logger log = LoggerFactory.getLogger(THREDDSServerHelper.class);

    /**
     * Tests whether or not a THREDDS server is reachable
     *
     * @param host
     * @param port
     * @param timeout - milliseconds
     * @return
     * @throws IOException
     */
    public static boolean isServerReachable(final String host, final int port, final int timeout) throws IOException {
        boolean result = false;

        Socket testSocket = new Socket();
        InetSocketAddress address = new InetSocketAddress(host, port);
        testSocket.connect(address, timeout);
        result = testSocket.isConnected();
        if (result) {
            testSocket.close();
        }

        return result;
    }

    public static Time getTimeBean(String datasetUrl, String gridSelection) throws IOException, ParseException {
        Formatter errorLog = new Formatter();
        FeatureDataset featureDataset = null;
        featureDataset =
                FeatureDatasetFactoryManager.open(null, datasetUrl, null, errorLog);

        if (featureDataset != null) {
            try {
                List<String> dateRange = NetCDFUtility.getDateRange(datasetUrl, gridSelection);
        		if (dateRange.isEmpty()) {
        			boolean hasTimeCoord = NetCDFUtility.hasTimeCoordinate(datasetUrl);
        			if (hasTimeCoord) { // This occurs when there is no date range in the file but has time coords
        				// We want the user to pick dates but don't have a range to give 
        				dateRange.add("1800-01-01 00:00:00Z");
        				dateRange.add("2100-12-31 00:00:00Z");
        			}
        		}
        		
        		Time timeBean = new Time(dateRange);
        		
                if (timeBean != null /*&& !timeBean.getTime().isEmpty()*/) {
                    return timeBean;
                }
                
            } finally {
                featureDataset.close();
            }
        }
        return null;
    }
    
    /**
     * Returns a list of dataset handles from the specified server.
     * 
     * @param hostname
     * @param port
     * @param uri
     * @param serviceType
     * @return
     */
    public static List<InvAccess> getDatasetHandlesFromServer(
            String hostname, int port, String uri, ServiceType serviceType) {
        try {
            InvCatalog catalog = getCatalogFromServer(hostname, port, uri);
            return NetCDFUtility.getDatasetHandles(catalog, serviceType);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static InvCatalog getCatalogFromServer(String hostname, int port, String uri) throws IOException {
        String ThreddsURL = "http://" + hostname + ":" + port + uri;
        URI catalogURI = URI.create(ThreddsURL);
        InvCatalogFactory factory = new InvCatalogFactory("default", true);
        InvCatalog catalog = factory.readXML(catalogURI);

        StringBuilder buff = new StringBuilder();
        if (!catalog.check(buff)) {
            throw new IOException(buff.toString());
        }

        return catalog;
    }

    /**
     * Returns a List<XmlBean> which is actually a List<DataSetBean>
     * 
     * @param hostname
     * @param port
     * @param uri
     * @return
     * @throws IllegalArgumentException
     */
    /*public static List<XmlBean> getDatasetListFromServer(String hostname,
    int port, String uri) throws IllegalArgumentException {

    if (hostname == null || "".equals(hostname)) throw new IllegalArgumentException("Hostname invalid or null");
    if (uri == null || "".equals(uri)) throw new IllegalArgumentException("URI invalid or null");
    if (port == 0) port = 80;

    List<XmlBean> result = new ArrayList<XmlBean>();
    List<InvAccess> invAccessList = THREDDSServerHelper.getDatasetHandlesFromServer(hostname, port, uri);

    if (invAccessList == null) return result;
    for (InvAccess invAccess : invAccessList) {

    InvDataset ds = invAccess.getDataset();
    List<InvPropertyBean> = ds.getP
    DataSetBean dsb = new DataSetBean(ds);
    result.add(dsb);
    }


    return result;
    }*/
    public static List<XmlResponse> getGridBeanListFromServer(String datasetUrl) throws IllegalArgumentException, IOException {

        if (datasetUrl == null || "".equals(datasetUrl)) {
            throw new IllegalArgumentException("DataSet URL invalid or null");
        }

        List<XmlResponse> result = new ArrayList<XmlResponse>();
        List<VariableSimpleIF> variables = NetCDFUtility.getDataVariableNames(datasetUrl);
        String type = NetCDFUtility.getDatasetType(datasetUrl);
        DataTypeCollection dtcb = new DataTypeCollection(type, variables.toArray(new VariableSimpleIF[0]));
        result.add(dtcb);
        return result;

    }


}
