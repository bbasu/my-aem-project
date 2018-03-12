package com.myproject.service;

/** Map Service interface to read Google API Key from OSGI configuration */
public interface GoogleMapServiceConfig {

  /** @return google api key */
  public String getGoogleApiKey();
}
