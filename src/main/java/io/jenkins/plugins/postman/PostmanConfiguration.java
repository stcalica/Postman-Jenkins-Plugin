package io.jenkins.plugins.postman;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * Example of Jenkins global configuration.
 */
@Extension
public class PostmanConfiguration extends GlobalConfiguration {

    /** @return the singleton instance */
    public static PostmanConfiguration get() {
        return GlobalConfiguration.all().get(PostmanConfiguration.class);
    }

    private String apiKey;

    public PostmanConfiguration() {
        // When Jenkins is restarted, load any saved configuration from disk.
        load();
    }

    /** @return the currently configured label, if any */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Together with {@link #getApiKey}, binds to entry in {@code config.jelly}.
     * @param apiKey the new value of this field
     */
    @DataBoundSetter
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        save();
    }

    public FormValidation doCheckLabel(@QueryParameter String value) {
        if (StringUtils.isEmpty(value)) {
            return FormValidation.warning("Please add an API key.");
        }
        return FormValidation.ok();
    }

}
