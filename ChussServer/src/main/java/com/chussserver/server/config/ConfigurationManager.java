package com.chussserver.server.config;

import com.chussserver.server.util.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager myConfiguratonManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (myConfiguratonManager==null)
            myConfiguratonManager = new ConfigurationManager();
        return myConfiguratonManager;
    }

    /**
     * Used to load a configuration file by the provided path
     */
    public void loadConfigurationFile(String filepath)  {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filepath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer sb = new StringBuffer();
        int i;
        try {
            while ( ( i = fileReader.read()) != -1) {
                sb.append((char)i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }
        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File", e);
        }
        try {
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File, internal", e);
        }
    }

    /**
     * Returns the current loaded configuration
     */

    public Configuration getCurrentConfiguration() {
        if( myCurrentConfiguration == null) {
            throw new HttpConfigurationException("Noo Current Configuration Set.");
        }
        return myCurrentConfiguration;
    }
}
