package com.publicobject.pictures;

import android.app.Application;
import retrofit.RestAdapter;

public class PicturesApp extends Application {
  private static final String PICTURE_SERVER = "http://10.0.2.2:8910/";

  private PictureService pictureService;

  @Override public void onCreate() {
    super.onCreate();

    // Set up Retrofit.
    // This uses 10.0.2.2 which is how the emulator can address the host operating system.
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setServer(PICTURE_SERVER)
        .build();
    pictureService = restAdapter.create(PictureService.class);
  }

  public PictureService getPictureService() {
    return pictureService;
  }
}
