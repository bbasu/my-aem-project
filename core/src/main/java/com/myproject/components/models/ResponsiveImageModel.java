package com.myproject.components.models;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myproject.utils.Constants;
import com.myproject.utils.PageUtil;

/**
 * Implementation for image component sling modal
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = ResponsiveImageModel.class,
        resourceType = ResponsiveImageModel.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME, extensions = Constants.EXPORTER_EXTENSION)
public class ResponsiveImageModel {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsiveImageModel.class);
    protected static final String RESOURCE_TYPE = "my-project/components/content/image";
    protected static final String ADAPTIVE_LG = "adaptiveLG";
    protected static final String ADAPTIVE_MD = "adaptiveMD";
    protected static final String ADAPTIVE_XS = "adaptiveXS";
    private boolean adaptiveImagesAuthored = false;

    @Self
    SlingHttpServletRequest request;
    
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String targeturl;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String windowSelection;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String resImagePath;

    private String adaptiveXS;
    private String adaptiveMD;
    private String adaptiveLG;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    private void initModel() {
        LOG.debug("Inside init of ResponsiveImageModel class");
        readProperties();
    }

    private void readProperties() {

        resImagePath = Text.escapePath(resImagePath);
        final Map<String, String> adaptiveImages = PageUtil.getMultipleAuthoredImages(request.getResource());
        if(!adaptiveImages.isEmpty()) {
            adaptiveImagesAuthored = true;
            if(null != adaptiveImages.get(ADAPTIVE_LG)) {
                adaptiveLG = Text.escapePath(adaptiveImages.get(ADAPTIVE_LG));
            }
            if(null != adaptiveImages.get(ADAPTIVE_MD)) {
                adaptiveMD = Text.escapePath(adaptiveImages.get(ADAPTIVE_MD));
            }
            if(null != adaptiveImages.get(ADAPTIVE_XS)) {
                adaptiveXS = Text.escapePath(adaptiveImages.get(ADAPTIVE_XS));
            }
        }
    }

    public String getTargeturl() {
        return targeturl;
    }

    public String getResImagePath() {
        return resImagePath;
    }

    public String getAdaptiveXS() {
        return adaptiveXS;
    }

    public String getAdaptiveMD() {
        return adaptiveMD;
    }

    public String getAdaptiveLG() {
        return adaptiveLG;
    }

    public boolean isAdaptiveImagesAuthored() {
        return adaptiveImagesAuthored;
    }
}
