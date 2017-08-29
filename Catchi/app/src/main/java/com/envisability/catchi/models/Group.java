package com.envisability.catchi.models;

/**
 * Created by Тарас on 08.06.2017.
 */
public class Group {
    private String name;
    private String imageUrl;
    private String lastMessage;
    private long time;

    public Group(String name, String imageUrl, String lastMessage, long time)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTime() {
        return time;
    }
}
