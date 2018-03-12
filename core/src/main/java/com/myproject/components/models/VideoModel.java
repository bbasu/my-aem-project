package com.myproject.components.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myproject.utils.Constants;

/**
 * Implementation for Video component Sling model
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = VideoModel.class,
        resourceType = VideoModel.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME, extensions = Constants.EXPORTER_EXTENSION)
public class VideoModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoModel.class);
    protected static final String RESOURCE_TYPE = "my-project/components/content/video";
    
    protected static final String YOUTUBE_REGEX_PATTERN = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
    protected static final String VIMEO_REGEX_PATTERN = "((http://|https://){0,1}vimeo.com/(m/){0,1}){0,1}(\\d+)[^0-9]*";
    protected static final String YOUTUBE_EMBED_LINK_PREFIX = "https://www.youtube.com/embed/";
    protected static final String VIMEO_EMBED_LINK_PREFIX = "https://player.vimeo.com/video/";

    private String videoLink;
    private boolean isLinkValid = true;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String videoSource;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String damUrl;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String youtubeUrl;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = StringUtils.EMPTY)
    private String vimeoUrl;

    @PostConstruct
    private void initModel() {
        LOGGER.info("Inside init of VideoModel class");
        getDetails();
    }

    private void getDetails() {
        if(Constants.YOUTUBE.equalsIgnoreCase(videoSource) && StringUtils.isNotBlank(youtubeUrl)) {
            String id = extractIDfromURL(YOUTUBE_REGEX_PATTERN, youtubeUrl, false);
            if(StringUtils.isNotBlank(id)) {
                videoLink =  YOUTUBE_EMBED_LINK_PREFIX + id;
            } else {
                isLinkValid = false;
            }
        }

        if(Constants.VIMEO.equalsIgnoreCase(videoSource) && StringUtils.isNotBlank(vimeoUrl)) {
            String id = extractIDfromURL(VIMEO_REGEX_PATTERN, vimeoUrl, true);
            if(StringUtils.isNotBlank(id)) {
                videoLink =  VIMEO_EMBED_LINK_PREFIX + id;
            } else {
                isLinkValid = false;
            }
        }
    }

    private String extractIDfromURL(String pattern, String url, boolean isVimeo) {
        
        String id = StringUtils.EMPTY;
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if(matcher.find()){
            id = isVimeo ? matcher.group(4) : matcher.group();
        }
        return id;
    }

    public String getVideoLink() {
        return videoLink;
    }
    
    public boolean getIsLinkValid() {
        return isLinkValid;
    }
}