package com.publicobject.pictures;

import android.app.Application;
import android.util.Log;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpLoader;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import retrofit.RestAdapter;

public class PicturesApp extends Application {
  private static final String PICTURE_SERVER = "http://10.0.2.2:8910/";
  public static final int CACHE_SIZE = 10 * 1024 * 1024;

  private PictureService pictureService;
  private Picasso picasso;
  private OkHttpClient okHttpClient;

  @Override public void onCreate() {
    super.onCreate();

    // Set up Retrofit.
    // This uses 10.0.2.2 which is how the emulator can address the host operating system.
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setServer(PICTURE_SERVER)
        .build();
    pictureService = restAdapter.create(PictureService.class);

    okHttpClient = new OkHttpClient();
    try {
      okHttpClient.setResponseCache(
          new HttpResponseCache(new File(getCacheDir(), "http-cache"), CACHE_SIZE));
    } catch (IOException e) {
      Log.w("PicturesApp", "Failed to create a response cache: ", e);
    }

    picasso = new Picasso.Builder(this)
        .loader(new OkHttpLoader(okHttpClient))
        .build();
    picasso.setDebugging(true);
  }

  public PictureService getPictureService() {
    return pictureService;
  }

  public String fileToUrl(String fileName) {
    return PICTURE_SERVER + fileName;
  }

  public Picasso getPicasso() {
    return picasso;
  }
}
