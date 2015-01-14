package com.vagnnermartins.adbelem.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vagnnermartins on 14/01/15.
 */
public class ThumbnailDTO {

    private static final String URL = "url";

    private String url;

    public static ThumbnailDTO fromJsonToObject(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        ThumbnailDTO result = new ThumbnailDTO();
        result.setUrl(object.getString(URL));
        return result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
