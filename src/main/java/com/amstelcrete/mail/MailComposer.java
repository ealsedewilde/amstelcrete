package com.amstelcrete.mail;

import com.amstelcrete.error.GenericException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to produce a {@link MailRecord} with the relevant e-mail data.
 */
public final class MailComposer {

  private static final Logger log = LoggerFactory.getLogger(MailComposer.class);

  private static final String TEMPLATE_NAME = "mail.ftlh";

  private static final Configuration config;

  /**
   * Set up the Freemarker configuration.
   */
  static {
    config = new Configuration(Configuration.VERSION_2_3_33);
    config.setClassForTemplateLoading(MailComposer.class, "/");
    config.setDefaultEncoding("UTF-8");
    config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    config.setLogTemplateExceptions(false);
    config.setWrapUncheckedExceptions(true);
  }

  private MailComposer() {}

  /**
   * Produce a {@link MailRecord} with the relevant e-mail data.
   *
   * @param website - where the user made the request 
   * @param emailAddress - from the user
   * @param request - the request made by the user
   * @return  a {@link MailRecord} with the relevant e-mail data.
   */
  public static MailRecord composeMail(String website, String emailAddress, String request) {
    Map<String, Object> parms = new HashMap<>();
    parms.put("website", website);
    parms.put("emailAddress", emailAddress);
    parms.put("request", StringEscapeUtils.escapeHtml4(request));
    ResourceBundle bundle;
    if (website.indexOf(".nl") > -1) {
      bundle = ResourceBundle.getBundle("bundle", new Locale("nl", "NL"));
    } else {
      bundle = ResourceBundle.getBundle("bundle", Locale.US);
    }
    String websiteHeader = bundle.getString("websiteHeader");
    parms.put("websiteHeader", websiteHeader);
    parms.put("emailAddressLabel", bundle.getString("emailAddressLabel"));
    parms.put("requestLabel", bundle.getString("requestLabel"));
    
    StringBuilder mailSubject = new StringBuilder();
    mailSubject.append(websiteHeader).append("").append(website);

    try {
      Template temp = config.getTemplate(TEMPLATE_NAME);
      Writer sw = new StringWriter();
      temp.process(parms, sw);
      return new MailRecord(mailSubject.toString(), sw.toString());
    } catch (IOException | TemplateException e) {
      log.error("Failed to compose mail", e);
      throw new GenericException(e);
    }
  }

}
