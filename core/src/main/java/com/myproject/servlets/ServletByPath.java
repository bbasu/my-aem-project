package com.myproject.servlets;
 
import java.io.IOException;

import javax.jcr.Repository;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * The {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations we use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(paths = "/bin/my-project/custom/servlet-path")
public class ServletByPath extends SlingAllMethodsServlet {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServletByPath.class);
    
    @Reference
    private Repository repository;
    
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
        LOG.debug("Inside doGet");
        
        resp.setContentType("application/json");
        String keys[] = repository.getDescriptorKeys();
        JSONObject jsonObject = new JSONObject();    
        for(int i=0;i<keys.length;i++){
            try {
                jsonObject.put(keys[i], repository.getDescriptor(keys[i]));
            } catch (JSONException e) {
                LOG.error(e.getMessage());
            }
        }
        resp.getWriter().println(jsonObject.toString());
         
    }
}