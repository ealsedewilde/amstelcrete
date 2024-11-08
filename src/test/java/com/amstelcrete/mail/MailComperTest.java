package com.amstelcrete.mail;

import org.junit.jupiter.api.Test;

class MailComperTest {
  
  @Test
  void composeMailTest() {
    MailRecord result = MailComposer.composeMail("www.amstelcrete.com", "klant@bedrijf.nl", "<bla&>");
    System.out.println(result);
  }

}
