<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="/GDP_WEB/index.jsp" method="POST">
            
            feature_wfs: <input type="text" name="feature_wfs" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            feature_wms: <input type="text" name="feature_wms" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            
            <!-- feature_ows is not currently being used but can be to set feature_wfs and feature_wms --> 
            feature_ows: <input type="text" name="feature_ows" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            
            coverage_wms: <input type="text" name="coverage_wms" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            coverage_opendap: <input type="text" name="coverage_opendap" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            coverage_wcs: <input type="text" name="coverage_wcs" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            
            item_id: <input type="text" name="item_id" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            
            redirect_url: <input type="text" name="redirect_url" size="90" value="https://my-beta.usgs.gov/catalogMaps/mapping/ows/4f4e4a49e4b07f02db623cbe" /><br />
            
            caller: <input type="text" name="caller" size="90" value="sciencebase" /><br />
            
            <input type="submit" name="submit" value="Go!">
        </form>
    </body>
</html>