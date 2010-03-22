package gov.usgs.gdp.servlet;

import gov.usgs.gdp.helper.FileHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class AppInitializationServlet
 */
public class AppInitializationServlet extends HttpServlet {

    private static org.apache.log4j.Logger log = Logger.getLogger(AppInitializationServlet.class);
    private static final long serialVersionUID = 1L;
    private String tmpDir = "";
    private String seperator = "";
    private String applicationTempDir = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        Date start = new Date();
        super.init(config);
        log.info("GeoData Portal Server application initialization is starting.");

        initializeFileSystem();
        //initializeJMS();

        Date created = new Date();
        System.setProperty("tomcatStarted", Long.toString(created.getTime()));
        log.info("GeoData Portal Server application has started. Took " + (created.getTime() - start.getTime()) + " milliseconds to complete.");
    }

    public boolean initializeJMS() {
        Date start = new Date();
        log.info("JMS Initialization is starting.");

        // This is not yet implemented

        log.info("JMS Initialized. Took " + (new Date().getTime() - start.getTime()) + " milliseconds to complete.");
        return true;
    }

    /**
     * Creates directory structure for application run
     * @return true if all passed, false if errors were encountered
     */
    public boolean initializeFileSystem() {
        boolean success = true;
        Date start = new Date();
        log.info("File System Initialization is starting.");

        // Get the temp directory for the system
        this.seperator = FileHelper.getSeparator();
        this.tmpDir = FileHelper.getSystemTemp() + this.seperator + "GDP-APP-TEMP" + this.seperator;
        log.info("Current application directory is: " + this.tmpDir);

        // Cleanup code to remove anything from previous application run
        if (FileHelper.doesDirectoryOrFileExist(this.tmpDir)) {
            if (deleteApplicationTempDirs()) {
                log.info("Temporary files from application's previous run have been removed");
            } else {
                log.info("Application could not delete temp directories created on previous run.");
                success = false;
            }
        }

        // Now that the previous temp dirs are gone, create application temporary directory
        // This will be this.tmpDir + [new Date().toString()]
        this.applicationTempDir = generateApplicationTempDirName();
        boolean dirCreated = FileHelper.createDir(this.applicationTempDir);
        if (!dirCreated) {
            dirCreated = FileHelper.doesDirectoryOrFileExist(this.applicationTempDir);
        }

        System.setProperty("applicationTempDir", this.applicationTempDir);

        if (dirCreated) {
            log.info("Current application temp directory is: " + this.applicationTempDir);
        } else {
            log.info("ERROR: Could not create application temp directory: " + this.applicationTempDir);
            log.info("\tIf this directory is not created manually, there may be issues during application run");
        }

        // Create the work directory within the app temp dir
        // This will be used for writing output files before moving them to the upload area
        if (FileHelper.createDir(this.applicationTempDir + "work" + this.seperator)) {
            System.setProperty("applicationWorkDir", this.applicationTempDir + "work" + this.seperator);
            log.info("Current application work directory is: " + System.getProperty("applicationWorkDir"));
        } else {
            log.info("ERROR: Could not create application work directory: " + System.getProperty("applicationWorkDir"));
            log.info("\tIf this directory is not created manually, there may be issues during application run");
        }

        // Place example files in temporary directory
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL sampleFileLocation = cl.getResource("Sample_Files" + FileHelper.getSeparator());
            if (sampleFileLocation == null) {
                log.info("Sample files were not written to the application temp directory");
                log.info("\t These files will not be available for processing.");
                success = false;
            } else {
                try {
                    if (FileHelper.copyFileToFile(new File(sampleFileLocation.toURI()), this.applicationTempDir)) {
                        log.info("Example files saved to: " + this.applicationTempDir + "Sample_Files/");
                    } else {
                        log.info("Sample files were not written to the application temp directory \t These files will not be available for processing.");
                        success = false;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                    success = false;
                }
            }
        } catch (URISyntaxException e) {
            log.info("Unable to read from src/main/resources/Sample_Files");
            log.info("Sample files were not written to the application temp directory");
            log.error(e);
            return false;
        }
        log.info("File System Initialized. Took " + (new Date().getTime() - start.getTime()) + " milliseconds to complete.");
        return success;
    }

    public String generateApplicationTempDirName() {
        String result = "";

        Date currentDate = new Date();
        String currentMilliseconds = Long.toString(currentDate.getTime());
        result = this.tmpDir + currentMilliseconds + FileHelper.getSeparator();

        return result;
    }

    @Override
    public void destroy() {
        Long start = new Date().getTime();
        super.destroy();
        log.info("Application is ending.");
        boolean result = false;
        result = deleteApplicationTempDirs();
        if (result) {
            log.info("Application temp directory " + this.applicationTempDir + " successfully deleted.");
        } else {
            log.info("WARNING: Application temp directory " + this.applicationTempDir + " could not be deleted.");
            log.info("\t If this directory exists, you may want to delete it to free up space on your storage device.");
        }
        log.info("Application has ended. Took " + (new Date().getTime() - start) + " milliseconds.");
    }

    public boolean deleteApplicationTempDirs() {
        boolean result = false;
        try {
            String tempDir = FileHelper.getSystemTemp() + FileHelper.getSeparator();
            result = FileHelper.deleteDirRecursively(tempDir + "GDP-APP-TEMP" + FileHelper.getSeparator());
            if (result) {
                log.info("Temporary files from application's previous run have been removed");
            } else {
                log.info("Application could not delete temp directories created on previous run.");
            }
        } catch (IOException e) {
            log.info("Application did not find or could not delete temp directories created on previous run.");
        }
        return result;
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppInitializationServlet() {
        super();
    }

}
