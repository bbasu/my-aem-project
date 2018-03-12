package com.myproject.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(
        resourceTypes = "my-project/components/structure/servlet",
        selectors = "data",
        extensions = "html",
        methods = "GET",
        metatype =true
        )
public class ServletByResourceType extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ServletByResourceType.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("Inside doGet");

        ResourceResolver resourceResolver = req.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        try {

            String contentPath = req.getParameter("contentPath");
            Page rootPage = pageManager.getPage(contentPath);
            
            List<String>pageList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("current-server-time", getCurrentTime());
            Iterator<Page> rootPageIterator = rootPage.listChildren();
            while(rootPageIterator.hasNext()) {
                Page childPage = rootPageIterator.next();
                jsonObject.put(childPage.getName(), childPage.getPath());
            }
            
            String responseJSONString = jsonObject.toString();

            if(StringUtils.isNotBlank(responseJSONString)){
                resp.setStatus(SlingHttpServletResponse.SC_OK);                 
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write(responseJSONString);
                writer.close();
            } else {
                resp.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);                  
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
            }
        } catch (JSONException e) {
            LOG.error(e.getMessage());
        }
    }

    private String getCurrentTime() {
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}