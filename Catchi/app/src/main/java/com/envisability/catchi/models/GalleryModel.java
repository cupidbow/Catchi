package com.envisability.catchi.models;

import android.util.Log;

import com.envisability.catchi.activities.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Тарас on 07.06.2017.
 */
public class GalleryModel {

    HashMap<String,GalleryDirectory> directories;
    public String selectedDirectory;

    ArrayList<String> selectedFiles;


    public void selectFile(String str)
    {
        if(selectedFiles==null)
            selectedFiles = new ArrayList<>();
        selectedFiles.add(str);
    }

    public void unselectFile(String str)
    {
        if(selectedFiles!=null)
        {
            for(String file:selectedFiles)
                if(str.equals(file))
                {
                    selectedFiles.remove(file);
                    return;
                }
        }
    }
    
    

    public int getSelectedIndex(String str)
    {
        if(selectedFiles!=null)
        {
            for(String file:selectedFiles)
                if(str.equals(file))
                {
                    return selectedFiles.indexOf(file);
                }
        }

        return -1;
    }

    public GalleryModel()
    {
        directories = new HashMap<>();
    }

    public void addFile(File file)
    {
        if(directories.get(file.getParent())!=null)
        {
            directories.get(file.getParent()).addFile(file);
        }
        else
        {
            directories.put(file.getParent(), new GalleryDirectory(file.getParentFile()));
            directories.get(file.getParent()).addFile(file);
        }
    }

    public void printAll()
    {
        for(GalleryDirectory directory:directories.values())
        {
            Log.d("gallery","directory " + directory.getName() + " ------------------------------------------");
            for(GalleryFile file: directory.getFiles())
            {
                Log.d("gallery", file.toString());
            }
        }
    }

    public void sort()
    {
    }

    public ArrayList<GalleryDirectory> getDirectories()
    {
        ArrayList<GalleryDirectory> directory = new ArrayList<>(this.directories.values());
        Collections.sort(directory );
        return directory ;
    }

    public static class GalleryDirectory implements Comparable<GalleryDirectory>
    {
        private String name;
        private long lastTimeModified;
        private Set<GalleryFile> files;

        public GalleryDirectory(File file)
        {
            name = file.getName();
            lastTimeModified = 0;
            files = new HashSet<>();
        }

        public void addFile(File file)
        {
            GalleryFile galleryFile = new GalleryFile(file);
            files.add(galleryFile);
            lastTimeModified = lastTimeModified > galleryFile.getLastTimeModified() ? lastTimeModified : galleryFile.getLastTimeModified();
        }

        public String getName() {
            return name;
        }

        public ArrayList<GalleryFile> getFiles() {
            ArrayList<GalleryFile> galleryFiles = new ArrayList<>(files);
            Collections.sort(galleryFiles);
            return galleryFiles;
        }

        @Override
        public int compareTo(GalleryDirectory galleryDirectory) {
            return Long.valueOf(galleryDirectory.lastTimeModified).compareTo(Long.valueOf(lastTimeModified));
        }
    }

    public static class GalleryFile implements Comparable<GalleryFile>
    {
        private String name;
        private String fullPath;
        private long lastTimeModified;

        public GalleryFile(File file)
        {
            name = file.getName();
            fullPath = file.getAbsolutePath();
            lastTimeModified = file.lastModified();
        }

        @Override
        public String toString() {
            return name + " " + lastTimeModified + " " + fullPath;
        }

        public long getLastTimeModified() {
            return lastTimeModified;
        }

        public String getFullPath() {
            return fullPath;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(GalleryFile galleryFile) {
            return Long.valueOf(galleryFile.lastTimeModified).compareTo(Long.valueOf(lastTimeModified));
        }

        public boolean isVideo()
        {
            return  fullPath.contains(".mkv")||  fullPath.contains(".MKV")
                    ||  fullPath.contains(".avi") ||  fullPath.contains(".AVI")
                    ||  fullPath.contains(".wmv") ||  fullPath.contains(".WMV")
                    ||  fullPath.contains(".mpeg") ||  fullPath.contains(".MPEG")
                    ||  fullPath.contains(".mpg")||  fullPath.contains(".MPG")
                    ||  fullPath.contains(".m4v") ||  fullPath.contains(".M4V")
                    ||  fullPath.contains(".3gp") ||  fullPath.contains(".3GP")
                    ||  fullPath.contains(".3g2") ||  fullPath.contains(".3G2")
                    ||  fullPath.contains(".mp4") ||  fullPath.contains(".mp4")
                    ||  fullPath.contains(".m4p")||  fullPath.contains(".m4p")
                    ||  fullPath.contains(".m2v") ||  fullPath.contains(".m2v");
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GalleryFile)
            {
                return getName().equalsIgnoreCase(((GalleryFile) obj).getName());
            }
            return super.equals(obj);
        }
/*
        @Override
        public int hashCode()
        {
            return getFullPath().length();
        }
        */
    }
}
