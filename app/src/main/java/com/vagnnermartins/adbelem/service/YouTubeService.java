package com.vagnnermartins.adbelem.service;

import com.vagnnermartins.adbelem.constants.Environment;
import com.vagnnermartins.adbelem.dto.VideoDTO;
import com.vagnnermartins.adbelem.util.ServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vagnnermartins on 14/01/15.
 */
public class YouTubeService {

    public static List<VideoDTO> findVideos() throws Exception {
        Map<String, String> headers = new HashMap<>();
        return VideoDTO.fromJsonToList(ServiceUtil.requestGet(Environment.GET_YOUTUBE_VIDEOS, headers));
    }
}
