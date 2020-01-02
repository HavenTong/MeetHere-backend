package com.rhenium.meethere.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/26 16:44
 */
public class SaveImageFileUtil {
    public static String saveImage(String imgStr) {
        String random = String.valueOf(imgStr.hashCode());
        int length = 20 < random.length() ? 20 : random.length();
        String filename = String.valueOf(imgStr.hashCode()).substring(1, length);
        String path = "/data/images/" + filename;
        String url = "http://152.136.173.30/images/" + filename;

        String partSeparator = ",";
        if (imgStr.contains(partSeparator)) {
            String encodedImg = imgStr.split(partSeparator)[1];

            String fileType = imgStr.split(";")[0].split("/")[1];

            url = url + "." + fileType;
            path = path + "." + fileType;
            byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(path);
            try {
                Files.write(destinationFile, decodedImg);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return url;
    }
}
