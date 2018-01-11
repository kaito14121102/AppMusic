package com.anime.rezero.appmusic;

/**
 * Created by zing on 1/10/2018.
 */

public class Song {
    private String Title;
    private int File;
    private int Hinh;

    public Song(String title, int file, int hinh) {
        Title = title;
        File = file;
        Hinh = hinh;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getFile() {
        return File;
    }

    public void setFile(int file) {
        File = file;
    }

    public int getHinh() {
        return Hinh;
    }

    public void setHinh(int hinh) {
        Hinh = hinh;
    }
}
