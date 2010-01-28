package gov.usgs.gdp.servlet;

import gov.usgs.gdp.bean.AckBean;
import gov.usgs.gdp.bean.AttributeBean;
import gov.usgs.gdp.bean.ErrorBean;
import gov.usgs.gdp.bean.FilesBean;
import gov.usgs.gdp.bean.ShapeFileSetBean;
import gov.usgs.gdp.bean.XmlReplyBean;
import gov.usgs.gdp.helper.CookieHelper;
import gov.usgs.gdp.helper.FileHelper;
import gov.usgs.gdp.helper.THREDDSServerHelper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import thredds.catalog.InvAccess;

/**
 * Servlet implementation class THREDDSServlet
 */
public class THREDDSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(THREDDSServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public THREDDSServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("command");
		XmlReplyBean xmlReply = null;
		
		if ("getgrid".equals(command)) {
			log.debug("User has chosen to get datasets from server");
			
			// Grab what we need to work with for this request
			String hostname = request.getParameter("hostname");
			String portString = request.getParameter("port");
			if (portString == null || "".equals(portString)) portString = "80";
			int port = 80;
			if (hostname == null || "".equals(hostname)) {
				xmlReply = new XmlReplyBean(AckBean.ACK_FAIL, new ErrorBean(ErrorBean.ERR_MISSING_PARAM));
				RouterServlet.sendXml(xmlReply, response);
				return;
			}
			
			try {
				if (portString != null && !"".equals(portString) && !"null".equals(portString)) {
					port = Integer.parseInt(portString);
				}
			} catch (NumberFormatException e) {
				xmlReply = new XmlReplyBean(AckBean.ACK_FAIL, new ErrorBean("Port provided is not a number"));
				RouterServlet.sendXml(xmlReply, response);
				return;
			}

			List<InvAccess> invAccessList = THREDDSServerHelper.getInvAccessListFromServer(hostname, port);
			if (invAccessList == null || invAccessList.isEmpty()) {
				xmlReply = new XmlReplyBean(AckBean.ACK_FAIL, new ErrorBean(ErrorBean.ERR_MISSING_DATASET));
				RouterServlet.sendXml(xmlReply, response);
				return;
			}
			for ()
			attributeBean.setFilesetName(shapefile);
			xmlReply = new XmlReplyBean(AckBean.ACK_OK, attributeBean);
			RouterServlet.sendXml(xmlReply, response);
			return;
			
		}
		
		if ("getgrid".equals(command)) {
			log.debug("User has chosen to list shapefile attributes");
			
			// Grab what we need to work with for this request
			String hostname = request.getParameter("hostname");
			String portString = request.getParameter("port");
			if (portString == null || "".equals(portString)) portString = "80";
			int port = 80;
			if (hostname == null || "".equals(hostname)) {
				xmlReply = new XmlReplyBean(AckBean.ACK_FAIL, new ErrorBean(ErrorBean.ERR_MISSING_PARAM));
				RouterServlet.sendXml(xmlReply, response);
				return;
			}
			
			try {
				if (portString != null && !"".equals(portString) && !"null".equals(portString)) {
					port = Integer.parseInt(portString);
				}
			} catch (NumberFormatException e) {
				xmlReply = new XmlReplyBean(AckBean.ACK_FAIL, new ErrorBean("Port provided is not a number"));
				RouterServlet.sendXml(xmlReply, response);
				return;
			}

			/*AttributeBean attributeBean = new AttributeBean(attributeList);
			attributeBean.setFilesetName(shapefile);
			xmlReply = new XmlReplyBean(AckBean.ACK_OK, attributeBean);
			RouterServlet.sendXml(xmlReply, response);
			return;*/
			
		}
	}

}
