package com.myproject.use;

import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.myproject.utils.PageUtil;

/**
 * WCM Use class to check whether a given link is an internal link
 */
public class CTAUse extends WCMUsePojo {
  
  private String buttonLinkUrl = StringUtils.EMPTY;
  private static final Logger LOG = LoggerFactory.getLogger(CTAUse.class);

  /*
   * If it is internal link, append .html
   */
  @Override
  public void activate() {
    buttonLinkUrl = getProperties().get("buttonLinkUrl", String.class);

    if (StringUtils.isNotBlank(buttonLinkUrl)) {
      try {
        LOG.trace("Button Link Url : {0}", buttonLinkUrl);
        buttonLinkUrl = PageUtil.getValidLink(buttonLinkUrl, getRequest());
      } catch (URISyntaxException urlException) {
        LOG.error("Button Link Url :{0}", buttonLinkUrl, urlException);
      }
    }
  }

  public String getButtonLinkUrl() {
    return buttonLinkUrl;
  }
}
