package com.devkraft.karmahealth.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
public class ApiService {
    private static final int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    private static final int MAX_PARALLEL_THREAD_POOL_SIZE = 10;

    private static final int MAX_CACHE_SIZE = 10 * 1024 * 1024; //10 MB
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int IMAGE_CACHE_QUALITY = 90;

    public static final String TAG = ApiService.class.getSimpleName();

    private static ApiService instance;

    private Context context;
    private RequestQueue mRequestQueue;
    private RequestQueue mImageRequestQueue;
    private final ImageLoader mImageLoader;

    public static ApiService get() {
        if(instance == null) {
            throw new RuntimeException("Call ApiService.init() first");
        }

        return instance;
    }

    public static ApiService init(Context context) {
        instance = new ApiService(context);
        return instance;
    }

    private ApiService(Context context) {
        this.context = context;

        mRequestQueue = prepareSerialRequestQueue();
        mRequestQueue.start();

        mImageRequestQueue = prepareParallelRequestQueue();
        mImageRequestQueue.start();

        mImageLoader = new ImageLoader(mImageRequestQueue,
                new DiskLruImageCache(context, this.getClass().getSimpleName(),
                        DISK_CACHE_SIZE, Bitmap.CompressFormat.JPEG, IMAGE_CACHE_QUALITY));
    }

    private RequestQueue prepareParallelRequestQueue() {
        Cache cache = new DiskBasedCache(context.getCacheDir(), MAX_CACHE_SIZE);
        Network network = getNetwork();
        return new RequestQueue(cache, network, MAX_PARALLEL_THREAD_POOL_SIZE);
    }

    private RequestQueue prepareSerialRequestQueue() {
        Cache cache = new DiskBasedCache(context.getCacheDir(), MAX_CACHE_SIZE);
        Network network = getNetwork();
        return new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
    }

    private Network getNetwork() {
        HttpStack stack;
        String userAgent = "volley/0";
        if(Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
            throw new RuntimeException("SDK version <=9 is not supported");
        }
        return new BasicNetwork(stack);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(ApiService.TAG);
        //mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, AppUtils.getSocketFactory(context)));
        mRequestQueue.add(request);
    }

    public <T> void addToRequestQueue(String tag, Request<T> request) {
        request.setTag(tag);
        mRequestQueue.add(request);
    }

    public void cancel(String tag) {
        mRequestQueue.cancelAll(tag);
    }

    public void cancelAPICalls(){
        cancel(TAG);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void cancelApiCalls() {
        cancel(TAG);
    }
}

