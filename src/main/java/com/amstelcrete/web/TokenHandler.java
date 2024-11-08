package com.amstelcrete.web;

import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;
import com.google.recaptchaenterprise.v1.RiskAnalysis.ClassificationReason;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assess the reCaptcha token from the HTML page.
 */
public class TokenHandler {
  private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);
  
  private static final String PROJECT_ID = "ealse-amstelcret-1729883311394";
  private static final String RECAPTCHA_KEY = "6LdJNmwqAAAAAL8TsoUd1B_vZW27FovrXCSA0RcQ";
  
  private static final float TRESHOLD = 0.89f;

  /**
   * Assess whether the token indicates a human interaction.
   *
   * @param token - generated in the HTML page.
   * @param recaptchaAction - action code; must be the same as in the HTML page
   * @return true when passed the assessment
   * @throws IOException 
   */
  public boolean performAssessment(String token, String recaptchaAction)
      throws IOException {
    // Create the reCAPTCHA client.
    try (RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create()) {

      // Set the properties of the event to be tracked.
      Event event = Event.newBuilder().setSiteKey(RECAPTCHA_KEY).setToken(token).build();

      // Build the assessment request.
      CreateAssessmentRequest createAssessmentRequest =
          CreateAssessmentRequest.newBuilder()
              .setParent(ProjectName.of(PROJECT_ID).toString())
              .setAssessment(Assessment.newBuilder().setEvent(event).build())
              .build();

      Assessment response = client.createAssessment(createAssessmentRequest);

      // Check if the token is valid.
      if (!response.getTokenProperties().getValid()) {
        String errorMessage = "The CreateAssessment call failed because the token was: "
            + response.getTokenProperties().getInvalidReason().name();
        log.error(errorMessage);
        return false;
      }

      // Check if the expected action was executed.
      if (!response.getTokenProperties().getAction().equals(recaptchaAction)) {
        log.error(
            "The action attribute in reCAPTCHA tag is: {}",
                 response.getTokenProperties().getAction());
        log.error(
            "The action attribute in the reCAPTCHA tag "
                + "does not match the action '{}' you are expecting to score", recaptchaAction);
        return false;
      }

      // Get the risk score and the reason(s).
      // For more information on interpreting the assessment, see:
      // https://cloud.google.com/recaptcha-enterprise/docs/interpret-assessment
      for (ClassificationReason reason : response.getRiskAnalysis().getReasonsList()) {
        log.error("reason: {}", reason);
      }

      float recaptchaScore = response.getRiskAnalysis().getScore();
      log.info("The reCAPTCHA score is: {}", recaptchaScore);

      // Get the assessment name (id). Use this to annotate the assessment.
      /*
      String assessmentName = response.getName();
      log.debug("Assessment name: {}", assessmentName.substring(assessmentName.lastIndexOf("/") + 1));
      */
      return recaptchaScore > TRESHOLD;
    }
  }
}
