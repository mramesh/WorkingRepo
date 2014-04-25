
package com.example.applicationmanager.vo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class DocInfo {

    private String fileName;

    private String fileType;

    private String path;

    public Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "application/pdf");
        return intent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
