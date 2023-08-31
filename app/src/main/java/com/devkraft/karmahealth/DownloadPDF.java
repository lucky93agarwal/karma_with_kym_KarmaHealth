package com.devkraft.karmahealth;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DownloadPDF extends Worker {
    public DownloadPDF(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String url = getInputData().getString("file_path");
        Log.i("checkurldownload","url s = "+url);
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
                .setMimeType("application/pdf")
                .setTitle("Dr Prescription")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        request.allowScanningByMediaScanner();
        DownloadManager downloadManager= (DownloadManager) getApplicationContext().getSystemService("download");
        long  lDownloadID = downloadManager.enqueue(request);
        return Result.success();
    }
}
