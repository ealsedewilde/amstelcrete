package com.amstelcrete.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to hold credentials.
 */
public final class Credentials {
  private static final Logger log = LoggerFactory.getLogger(Credentials.class);
  
  private static final Properties props;
  
  static {
    props = new Properties();
     try (InputStream is = Credentials.class.getResourceAsStream("/confidential/credentials.properies")) {
       props.load(is);
     } catch (IOException e) {
       log.error("Could not load 'credentials.properties'", e);
       throw new ExceptionInInitializerError("Could not load 'credentials.properties'");
    }
  }

  private Credentials() {
  }
  
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

}
