package com.vagnnermartins.adbelem.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 14/01/15.
 */
public class VideoDTO {

    private static final String PUBLISHEDAT = "publishedAt";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String VIDEOID = "videoId";
    private static final String THUMBNAILS = "thumbnails";
    private static final String HIGH = "high";
    private static final String ID = "id";

    private String publishedAt;
    private String title;
    private String description;
    private String videoId;
    private ThumbnailDTO thumbnail;

    public static List<VideoDTO> fromJsonToList(String json) throws JSONException {
        List<VideoDTO> result = new ArrayList<>();
        JSONObject object = new JSONObject(json);
        JSONArray array = object.getJSONArray("items");
        for (int i = 0; i < array.length(); i++) {
            if(!array.getJSONObject(i).getJSONObject(ID).isNull(VIDEOID)){
                JSONObject item = array.getJSONObject(i).getJSONObject("snippet");
                VideoDTO dto = new VideoDTO();
                dto.setDescription(item.getString(DESCRIPTION));
                dto.setPublishedAt(item.getString(PUBLISHEDAT));
                dto.setTitle(item.getString(TITLE));
                dto.setVideoId(array.getJSONObject(i).getJSONObject(ID).getString(VIDEOID));
                dto.setThumbnail(ThumbnailDTO.fromJsonToObject(item.getJSONObject(THUMBNAILS).getJSONObject(HIGH).toString()));
                result.add(dto);
            }
        }
        return result;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public ThumbnailDTO getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailDTO thumbnail) {
        this.thumbnail = thumbnail;
    }
}
