package com.myproject.service.impl;

import java.util.Dictionary;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import com.myproject.service.GoogleMapServiceConfig;

/** Map Service Implementation to read Google API Key from OSGI configuration */
@Component(
        metatype = true,
        label = "CCR - Components - Google Map Configuration",
        description = "Configuration details for Google map Integration"
        )
@Service
public class GoogleMapServiceConfigImpl implements GoogleMapServiceConfig {

    @Property(
            name = "map.google.api.key",
            value = "12345",
            label = "Google Map API Key",
            description = "Provide the Google API Key (https://console.developers.google.com). For Lat/Lng - Map JavaScript API. For Address - Embed Map API"
            )
    public static final String GOOGLE_API_KEY = "map.google.api.key";

    private String googleApiKey;

    @Activate
    @Modified
    protected void activate(ComponentContext componentContext) {
        @SuppressWarnings("unchecked")
        Dictionary<String, String> properties = componentContext.getProperties();

        googleApiKey = PropertiesUtil.toString(properties.get(GOOGLE_API_KEY), StringUtils.EMPTY);
    }

    @Override
    public String getGoogleApiKey() {
        return googleApiKey;
    }
}
