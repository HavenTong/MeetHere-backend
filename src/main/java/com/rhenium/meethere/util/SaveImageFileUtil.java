package com.rhenium.meethere.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/26 16:44
 */
public class SaveImageFileUtil {
    public static String saveImage(MultipartFile file) {
        String pathName = "/data/images/";
        String fName = file.getOriginalFilename();
        pathName += fName;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathName);
            fos.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String imageUrl = "http://152.136.173.30/images/" + fName;
        return imageUrl;
    }
}
