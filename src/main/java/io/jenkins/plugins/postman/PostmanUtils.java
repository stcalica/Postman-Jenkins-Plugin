package io.jenkins.plugins.postman;

import javax.servlet.ServletException;
import java.io.IOException;
import hudson.AbortException;
import jenkins.tasks.SimpleBuildStep;
import jenkins.model.GlobalConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ProtocolException;

public class PostmanUtils {
  private static final Logger LOGGER = Logger.getLogger(PostmanBuilder.class.getName());


  public String getApiKeyFromConfig() {
    PostmanConfiguration config = GlobalConfiguration.all().get(PostmanConfiguration.class);
    String key  = config.getApiKey();
    if( key.isEmpty() ){
      return key;
    } else {
      LOGGER.log(Level.INFO, "Retrieved Postman API key from global Global Configuration");
      return key;
    }
  }

  public String callEndpoint(String url, String apiKey){

    try {
        URL endpoint = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) endpoint.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Api-Key", apiKey);

        int responseCode = con.getResponseCode();
        if(responseCode != 200){
          LOGGER.log(Level.SEVERE, "Unsuccessful call to Collections Endpoint: " + url);
          LOGGER.log(Level.SEVERE, "BAD RESPONSE CODE: " + Integer.toString(responseCode) );
        }

        BufferedReader in = new BufferedReader(
                  new InputStreamReader(con.getInputStream()));

        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        return response.toString();

      } catch (Exception E){
        LOGGER.log(Level.SEVERE, " Unsuccessful call to Collections Endpoint: " + url);
        return E.toString();
      }
    }

}
