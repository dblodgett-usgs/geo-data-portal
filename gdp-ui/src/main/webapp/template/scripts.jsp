<%-- 
    Document   : scripts
    Created on : Jul 21, 2011, 1:21:27 PM
    Author     : Ivan Suftin <isuftin@usgs.gov>
--%>

<%@page import="java.util.Enumeration"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="java.io.File"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%!	protected DynamicReadOnlyProperties props = null;

	{
		try {
			File propsFile = new File(getClass().getClassLoader().getResource("application.properties").toURI());
			props = new DynamicReadOnlyProperties(propsFile);
			props = props.addJNDIContexts(new String[0]);
		} catch (Exception e) {
			LoggerFactory.getLogger("index.jsp").error("Could not find JNDI - Application will probably not function correctly");
		}
	}
	boolean development = Boolean.parseBoolean(props.getProperty("development", "false"));
	String versionCookieJquery = props.getProperty("version.cookie.jquery", "");
	String versionColorboxJquery = props.getProperty("version.colorbox.jquery", "");
%>
<%
	String method = request.getMethod();
%>

<jsp:include page="../js/log4javascript/log4javascript.jsp">
    <jsp:param name="relPath" value=""/>
</jsp:include>

<jsp:include page="../js/jquery/jquery.jsp">
    <jsp:param name="debug-qualifier" value="false"/>
</jsp:include>

<script type="text/javascript" src="webjars/jquery-cookie/<%=versionCookieJquery%>/jquery.cookie.js"></script>
<script type="text/javascript" src="webjars/jquery-colorbox/<%=versionColorboxJquery%>/jquery.colorbox<%=development ? "" : "-min"%>.js"></script>

<script type="text/javascript" src="js/xslt/jquery.xslt.js"></script>
<script type="text/javascript" src="js/xmlns/jquery.xmlns.js"></script>
<script type="text/javascript" src="js/objects/algorithm.js"></script>
<script type="text/javascript" src="js/constants.js"></script>
<jsp:include page="../js/openlayers/openlayers.jsp">
    <jsp:param name="debug-qualifier" value="false"/>
    <jsp:param name="include-deprecated" value="true"/>
</jsp:include>
<script type="text/javascript" src="js/jquery-ui/jquery-ui-1.8.23.custom.min.js"></script>
<script type="text/javascript" src="js/jgrowl/jquery.jgrowl_compressed.js"></script> <%-- http://plugins.jquery.com/project/jGrowl --%>
<script type="text/javascript" src="js/parseUri/parseUri.js"></script>
<script type="text/javascript" src="js/parsexml/jquery.xmldom-1.0.min.js"></script>
<script type="text/javascript" src="js/fileuploader/fileuploader.js"></script>
<script type="text/javascript" src="js/download/download.jQuery.js"></script>
<script type="text/javascript" src="js/jquery-url-parser/jquery.url.js"></script>
<script type="text/javascript" src="js/ogc/wps.js"></script>
<script type="text/javascript" src="js/ogc/wfs.js"></script>
<script type="text/javascript" src="js/ogc/csw.js"></script>
<script type="text/javascript" src="js/root.js"></script>
<script type="text/javascript" src="js/sciencebase.js"></script>
<script type="text/javascript" src="js/area_of_interest.js"></script>
<script type="text/javascript" src="js/dataset.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script type="text/javascript" src="js/tiptip/jquery.tipTip.js"></script>
<script type="text/javascript" src="js/sarissa/sarissa.js"></script>
<script type="text/javascript" src="js/sarissa/sarissa_ieemu_xpath.js"></script>
<script type="text/javascript" src="lib/scripts/cswclient.js"></script>
<script type="text/javascript" src="lib/scripts/gdp-cswclient-update.js"></script>
<script type="text/javascript">

	// http://stackoverflow.com/questions/37684/how-to-replace-plain-urls-with-links
	function replaceURLWithHTMLLinks(text) {
		var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
		if (text && !text.toLowerCase().contains('noreplace')) {
			return text.replace(exp, "<a href='$1' target='_blank'>$1</a>");
		} else {
			return text;
		}
	}

	var landingPage = '<%= props.getProperty("gdp.endpoint.landing", "/gdp-ui-landing/")%>';
	var incomingMethod = '<%=method%>';
	var incomingParams = {};
    <%
		Enumeration<String> paramNames = (Enumeration<String>) request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			String value = request.getParameter(key);
    %>
	incomingParams['<%=key%>'] = '<%=value%>';
    <%
		}
    %>



    <%-- Google Analytics --%> 
    <%-- http://internal.cida.usgs.gov/jira/browse/GDP-500 --%>
		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-34377683-1']);
		_gaq.push(['_gat._anonymizeIp']);
		_gaq.push(['_trackPageview']);

		(function() {
			var ga = document.createElement('script');
			ga.type = 'text/javascript';
			ga.async = true;
			ga.src = ('https:' === document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
			var s = document.getElementsByTagName('script')[0];
			s.parentNode.insertBefore(ga, s);
		})();


</script>