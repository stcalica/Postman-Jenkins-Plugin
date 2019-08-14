package io.jenkins.plugins.postman;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import hudson.AbortException;
import java.net.ProtocolException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class PostmanBuilder extends Builder implements SimpleBuildStep {
    private static final Logger LOGGER = Logger.getLogger(PostmanBuilder.class.getName());

    private ArrayList<String> collections;
    public String collectionId;
    public String filename;

    public PostmanUtils utils = new PostmanUtils();


    @DataBoundConstructor
    public PostmanBuilder(String collectionId) {
        this.collectionId = collectionId;
    }


    public String getCollectionId() {
        return this.collectionId;
    }

    public String getFilename(){
      return this.filename;
    }

    @DataBoundSetter
    public void setFilename(String filename){
      this.filename = filename;
    }

    @DataBoundSetter
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }


    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException, AbortException {

            String apiKey = utils.getApiKeyFromConfig();
            //check key

            listener.getLogger().println("Calling collection endpoint with id: "+ this.collectionId );
            String collection = utils.callEndpoint("https://api.getpostman.com/collections/"+this.collectionId, apiKey);

            if( !collection.isEmpty() ){
              listener.getLogger().println("Collection retrieved successfully\n");
              listener.getLogger().println(collection);
              listener.getLogger().println();
            } else {
                throw new AbortException("Collection was not retrieved.");
            }

            try {
              File file = new File(workspace+ "/"+this.getFilename());
              FileWriter fw = new FileWriter(file.getAbsoluteFile());
              BufferedWriter bw = new BufferedWriter(fw);
              bw.write(collection);
              bw.close();
              listener.getLogger().println("Wrote collection to file: "+this.getFilename());

            } catch(IOException E){
              listener.getLogger().println( E.toString() );
              throw new AbortException("Could not write collection to file");
            }
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public PostmanUtils utils = new PostmanUtils();

        public ArrayList<PostmanCollection>  getPostmanCollections(){

          ArrayList<PostmanCollection> collections = new ArrayList<PostmanCollection>();

          String apiKey = utils.getApiKeyFromConfig();
          String response = utils.callEndpoint("https://api.getpostman.com/collections/", apiKey);

          JsonParser jsonParser = new JsonParser();
          JsonObject jo = (JsonObject)jsonParser.parse(response.toString());
          JsonArray jsonArr = jo.getAsJsonArray("collections");
          LOGGER.log( Level.INFO, Integer.toString(jsonArr.size()));

          for( JsonElement j : jsonArr ){
            JsonObject obj = j.getAsJsonObject();
            Gson gson = new Gson();
            PostmanCollection collection = gson.fromJson( obj, PostmanCollection.class );
            collections.add(collection);
          }

          return collections;
        }

        public ListBoxModel doFillCollectionIdItems() {
        ListBoxModel items = new ListBoxModel();
        for (PostmanCollection collection : getPostmanCollections()) {
            items.add(collection.getName(), collection.getUid());
        }
        return items;
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Postman Collections";
        }

    }

}
