package com.amstelcrete.web;

import com.amstelcrete.mail.MailComposer;
import com.amstelcrete.mail.MailRecord;
import com.amstelcrete.mail.MailService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the input from the HTML form.
 * Servlet implementation class AmstelCreteServlet
 */
// @WebServlet("/contact")
public class AmstelCreteServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(AmstelCreteServlet.class);
      
  private static final long serialVersionUID = 1L;
  private static final String ACTION = "info_request";

  private final TokenHandler tokenHandler = new TokenHandler();
  private final MailService mailService = new MailService();

  /**
   * GET method is not supported by this Servlet.
   * Instead an error page is presented.
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/get.html");
    dispatcher.forward(request, response);
  }

  /**
   * Handle the HTML form and respond with Post-Redirect-Get (PRG).
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    if (processRequest(request)) {
      session.setAttribute("messageType", "showInfo");
      session.setAttribute("messageKey", "infoMessage");
    } else {
      session.setAttribute("messageType", "showError");
      session.setAttribute("messageKey", "errorMessage");
    }

    response.sendRedirect("/amstelcrete", true);
  }

  /**
   * Validate the request input and, when valid,  process the input.
   * 
   * @param request
   * @return true only when processing was successful
   */
  private boolean processRequest(HttpServletRequest request) {
    String token = request.getParameter("captchatoken");
    try {
      if (tokenHandler.performAssessment(token, ACTION)) {
        String emailAddress = request.getParameter("email");
        String inputMessage = request.getParameter("inputMessage");
        if (validInput(emailAddress, inputMessage)) {
          String website = request.getServerName();
          MailRecord mailRecord = MailComposer.composeMail(website, emailAddress, inputMessage);
          mailService.sendMail(mailRecord);
          return true;
        }
      }
    } catch (IOException e) {
      log.error("Error contacting recaptcha", e);
    }
    return false;
  }

  /**
   * Validat the HTML form input.
   * @param emailAddress
   * @param inputMessage
   * @return
   */
  private boolean validInput(String emailAddress, String inputMessage) {
    EmailValidator validator = EmailValidator.getInstance();
    return validator.isValid(emailAddress) && inputMessage.length() >= 80
        && inputMessage.length() <= 800;
  }

}
