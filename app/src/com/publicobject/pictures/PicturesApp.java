package com.publicobject.pictures;

import android.app.Application;
import retrofit.RestAdapter;
import retrofit.client.UrlConnectionClient;

public class PicturesApp extends Application {
  private static final String PICTURE_SERVER = "http://10.0.2.2:8910/";

  private PictureService pictureService;

  @Override public void onCreate() {
    super.onCreate();

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setClient(new UrlConnectionClient())
        .setServer(PICTURE_SERVER)
        .build();
    pictureService = restAdapter.create(PictureService.class);
  }

  public PictureService getPictureService() {
    return pictureService;
  }
}
