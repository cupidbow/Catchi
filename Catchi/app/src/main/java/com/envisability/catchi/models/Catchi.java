package com.envisability.catchi.models;

/**
 * Created by Тарас on 08.06.2017.
 */
public class Catchi {

    private String avatarURL;
    private String name;
    private boolean isOnline;
    private boolean selected;

    public Catchi(String name, String avatarURL, boolean isOnline)
    {
        this.isOnline = isOnline;
        this.name = name;
        this.avatarURL = avatarURL;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public boolean isOnline()
    {
        return isOnline;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select()
    {
        selected = true;
    }

    public void unselect()
    {
        selected = false;
    }

}
