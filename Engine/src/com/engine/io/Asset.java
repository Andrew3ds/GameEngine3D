package com.engine.io;

import com.engine.misc.DialogWindow;

import java.io.File;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Asset {
    public static String home = "./res/";

    private File file;

    public Asset(String location) {
        if(location.contains("..")) {
            try {
                throw new IllegalAccessException("Cannot leave sandboxed directory");
            } catch (IllegalAccessException e) {
                DialogWindow.errorDialog(e);
            }
        }

        file = new File(home.concat(location));
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public File asFile() {
        return file;
    }

    public long fileSize() {
        return file.length();
    }

    public boolean writable() {
        return file.canWrite();
    }

    @Override
    public String toString() {
        return file.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
