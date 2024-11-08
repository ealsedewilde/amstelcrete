package com.amstelcrete.mail;

import org.junit.jupiter.api.Test;

class MailServiceTest {
  
  static {
    System.setProperty("LOGS", "logs");
  }
  
  private final MailService sut = new MailService();
  
  private String content = """
          <!DOCTYPE html>
          <div style='background-color: rgba(128, 119, 86, 0.2)'>
          <h2 style='color: rgb(128, 119, 86); background-color: black; padding: 10px'>Request from website ....</h2>
          <table style='width: 100%'>
            <tr><td style='padding: 10px'>
              <h3>E-mail address:<h3
            </td></tr>
            <tr><td style='-left: 10px'>
            <div style='border:1px solid black;padding-left: 10px'>
            e@a,com
            </div>
            </td></tr>
            
            <tr><td><hr></td></tr>
            
            <tr><td style='padding: 10px'><h3>Request:<h3></td></tr>
            <tr><td style='padding-left: 10px'>
            <div style='border:1px solid black; padding: 10px'>
            &lt;&amp;&gt;<br>
            Hier komt een mooi verhaal.<br>  
            Bij wijze van spreken.<br> 
            <br>                  
            Toch?    
            </div>
            </td></tr>
            <tr><td><hr></td></tr>
          </table>
          </div>
          """;
  
  @Test
  void sendMailTest() {
    MailRecord mailRecord = new MailRecord("Test mail", content);
    sut.sendMail(mailRecord);
  }

}
