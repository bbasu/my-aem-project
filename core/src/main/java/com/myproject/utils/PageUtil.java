package com.myproject.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/** Page related utilities. */
public class PageUtil {
    private static final String SUFFIX_HTML = ".html";

    /** Static class should not be initialized. */
    private PageUtil() {
        // prevent outside initialization
        throw new IllegalArgumentException("Cannot instantiate " + this.getClass().getName());
    }

    /**
     * Formats the url appending .html if it's internal page link. Excludes DAM Urls. Check if the
     * content is present
     *
     * @param targetUrlPath the target url path
     * @param request
     * @return String
     * @throws URISyntaxException For malformed URIs.
     */
    public static String getValidLink(String targetUrlPath, SlingHttpServletRequest request) throws URISyntaxException {
        URI testUrl = new URI(targetUrlPath);
        if (!testUrl.isAbsolute() && targetUrlPath.startsWith("/content")
                && !targetUrlPath.contains(DamConstants.MOUNTPOINT_ASSETS)) {
            if (targetUrlPath.endsWith("/")) {
                targetUrlPath = targetUrlPath.substring(0, targetUrlPath.length() - 1);
            }
            targetUrlPath = request.getResourceResolver().map(request, targetUrlPath);
            if (StringUtils.isNotBlank(targetUrlPath) && !targetUrlPath.endsWith(SUFFIX_HTML)) {
                targetUrlPath = targetUrlPath.concat(SUFFIX_HTML);
            }
        }
        return targetUrlPath;
    }

    /**
     * Get the path until the language page based on the current page path
     *
     * @param currentPage
     *          - the current page path of the resource
     * @return - returns the language as page object
     */
    public static Page getLanguagePage(final Page currentPage) {
        Page languagePage = null;
        String pagePathUntilLanguage = null;
        PageManager pageManager = currentPage.getPageManager();

        final String lang = currentPage.getLanguage(false).getLanguage();
        String langPgIdentifier = "/" + lang + "/";
        final String currPgPath = currentPage.getPath();
        if (currPgPath.contains(langPgIdentifier)) {
            String langPgParentPath = currPgPath.substring(0, currPgPath.indexOf(langPgIdentifier));
            pagePathUntilLanguage = langPgParentPath.concat(langPgIdentifier);
        }

        languagePage = pageManager.getPage(pagePathUntilLanguage);
        return languagePage;
    }
    
    /**
     * Method will read the images for the component where multiple images has been authored dialog
     *
     * @param imageResource
     *            Image resource.
     * @return Map contains field name as key and value as image path
     */
    public static Map<String, String> getMultipleAuthoredImages(final Resource imageResource) {
        final Map<String, String> imageList = new HashMap<>();
        String fileName;
        if (imageResource != null && imageResource.hasChildren()) {
            final Iterator<Resource> imageIterator = imageResource.getChildren().iterator();

            while (imageIterator.hasNext()) {
                final Resource image = imageIterator.next();
                final ValueMap imageFileReference = image.adaptTo(ValueMap.class);
                if (imageFileReference == null) {
                    continue;
                }

                if (imageFileReference.containsKey(Constants.IMAGES_FILE_REFERENCE)) {
                    fileName = imageFileReference.get(Constants.IMAGES_FILE_REFERENCE).toString();
                    imageList.put(image.getName(), fileName);
                }
            }
        }
        return imageList;
    }
}
