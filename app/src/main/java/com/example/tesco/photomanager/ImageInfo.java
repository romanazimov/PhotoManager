package com.example.tesco.photomanager;


import java.io.Serializable;

public class ImageInfo implements Serializable {
    public String id;
    public String path = "";
    public String tagPlace = "";
    public String tagPerson = "";
    public String folderId = "";
    public String caption = "";
    public String captureTime = "";

    @Override
    public String toString() {
        return "ID: " + id + "Path: " + path + "Tag Place: " + tagPlace + "Tag Person: " + tagPerson + "Folder ID: " + folderId + "Caption: " + caption + "Capture Time" + captureTime;
    }
}
