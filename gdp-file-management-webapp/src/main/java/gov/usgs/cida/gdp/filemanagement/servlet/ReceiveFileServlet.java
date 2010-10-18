package gov.usgs.cida.gdp.filemanagement.servlet;

import gov.usgs.cida.gdp.utilities.FileHelper;
import gov.usgs.cida.gdp.utilities.XmlUtils;
import gov.usgs.cida.gdp.utilities.bean.Acknowledgement;
import gov.usgs.cida.gdp.utilities.bean.Error;
import gov.usgs.cida.gdp.utilities.bean.UserDirectory;
import gov.usgs.cida.gdp.utilities.bean.XmlReply;
import java.io.File;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ReceiveFileServlet
 * @author isuftin
 *
 */
public class ReceiveFileServlet extends HttpServlet {

    private static final long serialVersionUID = 6766229674722132238L;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ReceiveFileServlet.class);
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long start = Long.valueOf(new Date().getTime());

        XmlReply xmlOutput = null;
        String command = request.getParameter("command");

        if (ServletFileUpload.isMultipartContent(request)) {
            // "gdp.shapefile.temp.path" should be set in the tomcat startup script or setenv.sh as JAVA_OPTS="-Dgdp.shapefile.temp.path=/wherever/you/want/this/file/placed"
            String tempLocation = ("".equals(System.getProperty("gdp.shapefile.temp.path")) || System.getProperty("shapefile.temp.path") == null)? System.getProperty("java.io.tmpdir") : System.getProperty("gdp.io.tmpdir");
            String applicationUserspaceDir = tempLocation +  File.separator + "GDP" + File.separator +  UUID.randomUUID();
            
            String userDirectory = "";

            try {
                userDirectory = uploadFiles(request, applicationUserspaceDir);
            } catch (Exception e) {
                xmlOutput = new XmlReply(Acknowledgement.ACK_FAIL, new Error(Error.ERR_FILE_UPLOAD, e));
                XmlUtils.sendXml(xmlOutput, start, response);
                return;
            }

            if ("".equals(userDirectory)) { // User directory could not be created
                xmlOutput = new XmlReply(Acknowledgement.ACK_FAIL, new Error(Error.ERR_USER_DIR_CREATE));
                XmlUtils.sendXml(xmlOutput, start, response);
                return;
            }

            // Work directly into another webservice to list the files available
            // including the file the user just uploaded
            log.debug("Files successfully uploaded.");
            RequestDispatcher rd = request.getRequestDispatcher("/FileSelectionServlet?command=listfiles&userdirectory=" + userDirectory);
            rd.forward(request, response);
            return;
        }
    }

    /**
     * Save the uploaded files to a specified directory
     * @param request
     * @param directory
     * @return
     * @throws Exception 
     */
    private String uploadFiles(HttpServletRequest request, String applicationTempDir) throws Exception {
        log.debug("User uploading file(s).");

        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Constructs an instance of this class which
        // uses the supplied factory to create FileItem instances.
        ServletFileUpload upload = new ServletFileUpload(factory);

        Cookie[] cookies = request.getCookies();
        String userDirectory = "";
        for (int cookieIndex = 0; cookieIndex < cookies.length; cookieIndex++) {
            if ("gdp-user-directory".equals(cookies[cookieIndex].getName().toLowerCase())) {
                userDirectory = cookies[cookieIndex].getValue();
            }
        }

        Object interimItems = upload.parseRequest(request);
        @SuppressWarnings("unchecked")
        List<FileItem> items = (List<FileItem>) interimItems;

        // Save the file(s) to the user directory
        if (FileHelper.saveFileItems(applicationTempDir + userDirectory, items)) {
            return userDirectory;
        }
        return "";
    }
}
