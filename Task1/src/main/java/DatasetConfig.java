package main.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vraj on 11/21/15.
 */
public class DatasetConfig {

    public static final String DATASET_CONFIG = "src/main/java/dataset_config.properties";

    private String homeDir;
    private String indexDir;
    private String businessJSON;
    private String reviewJSON;
    private String tipJSON;

    public void loadProperties() {
        Properties prop = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream(DATASET_CONFIG);

            prop.load(input);

            homeDir = prop.getProperty("home_dir");
            indexDir = homeDir+prop.getProperty("index_dir");
            businessJSON = homeDir+prop.getProperty("business");
            reviewJSON = homeDir+prop.getProperty("review");
            tipJSON = homeDir+prop.getProperty("tip");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public String getBusinessJSON() {
        return businessJSON;
    }

    public void setBusinessJSON(String businessJSON) {
        this.businessJSON = businessJSON;
    }

    public String getReviewJSON() {
        return reviewJSON;
    }

    public String getTipJSON() {
        return tipJSON;
    }

    public void setTipJSON(String tipJSON) {
        this.tipJSON = tipJSON;
    }

    public void setReviewJSON(String reviewJSON) {
        this.reviewJSON = reviewJSON;
    }


}
