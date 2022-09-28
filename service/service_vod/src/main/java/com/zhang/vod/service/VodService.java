package com.zhang.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VodService {
    String uploadVideoAly(MultipartFile file);

    void removeMoreAlyVideo(List videoIdList);
}
