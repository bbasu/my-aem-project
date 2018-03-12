package com.myproject.use;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.myproject.service.GoogleMapServiceConfig;

/** Use class for Google Map Component. */
public class GoogleMapUse extends WCMUsePojo {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleMapUse.class);

    private String googleApiKey;

    @Override
    public void activate() throws Exception {

        GoogleMapServiceConfig service =
                getSlingScriptHelper().getService(GoogleMapServiceConfig.class);
        if (null != service) {
            googleApiKey = service.getGoogleApiKey();
            LOG.trace("Map Google API Key : {}", googleApiKey);
        }
    }

    /**
     * Getter for Google API Key
     *
     * @return Google API Key
     */
    public String getGoogleApiKey() {
        return googleApiKey;
    }
}
