package com.envisability.catchi.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.envisability.catchi.models.GalleryModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Тарас on 06.06.2017.
 */
public class ImageFileUtil {
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap compress(Bitmap bmp)
    {
        float scale;
        if(bmp.getWidth()*1.0f/bmp.getHeight()> 1024.0f/720)
            scale = 1024.0f/bmp.getWidth();
        else
            scale = 720.0f/bmp.getHeight();
        Bitmap result = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*scale),(int)(bmp.getHeight()*scale),false);
        bmp.recycle();
        return result;

    }

    public static File saveCompressed(Context context, Bitmap bitmap, String fileName, int maxSize)
    {
        ByteArrayOutputStream stream = null;
        int streamSize = maxSize+10;
        int quality = 100;
        while (streamSize > maxSize) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stream = new ByteArrayOutputStream();
            quality *= 0.9;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            streamSize = stream.toByteArray().length;
        }
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getFilesDir();
        File mypath = new File(directory, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(mypath);
            outputStream.write(stream.toByteArray());
            outputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return mypath;
    }


    public static GalleryModel getAllImages(Context context)
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        GalleryModel galleryWrapper = new GalleryModel();

        String[] directories = null;
        if (u != null)
        {
            c = context.getContentResolver()
                    .query(u,projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }
        HashSet<String> checked = new HashSet<>();
        for(int i=0;i<dirList.size();i++)
        {

            File imageDir = new File(directories[i]);
            ArrayList<File> imageList = new ArrayList<>(Arrays.asList(imageDir.listFiles())) ;
            if(imageList == null)
                continue;
            for (int k=0;k<imageList.size();k++) {

                File imagePath = imageList.get(k);

                if(imagePath.getName().startsWith(".") || checked.contains(imagePath.getAbsolutePath()))
                    continue;
                checked.add(imagePath.getAbsolutePath());
                try {

                    if(imagePath.isDirectory())
                    {
                        if(!imagePath.getName().contains("."))
                            imageList.addAll(Arrays.asList(imagePath.listFiles())) ;


                    }

                    else if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")

                            || imagePath.getName().contains(".mkv")|| imagePath.getName().contains(".MKV")
                            || imagePath.getName().contains(".avi") || imagePath.getName().contains(".AVI")
                            || imagePath.getName().contains(".wmv") || imagePath.getName().contains(".WMV")
                            || imagePath.getName().contains(".mpeg") || imagePath.getName().contains(".MPEG")
                            || imagePath.getName().contains(".mpg")|| imagePath.getName().contains(".MPG")
                            || imagePath.getName().contains(".m4v") || imagePath.getName().contains(".M4V")
                            || imagePath.getName().contains(".3gp") || imagePath.getName().contains(".3GP")
                            || imagePath.getName().contains(".3g2") || imagePath.getName().contains(".3G2")
                            || imagePath.getName().contains(".mp4") || imagePath.getName().contains(".mp4")
                            || imagePath.getName().contains(".m4p")|| imagePath.getName().contains(".m4p")
                            || imagePath.getName().contains(".m2v") || imagePath.getName().contains(".m2v")

                            )
                    {

                        galleryWrapper.addFile(imagePath);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return galleryWrapper;


    }
}
