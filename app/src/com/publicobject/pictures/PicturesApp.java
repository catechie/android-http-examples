package com.publicobject.pictures;

import android.app.Application;
import retrofit.RestAdapter;
import retrofit.client.UrlConnectionClient;

public class PicturesApp extends Application {
  private static final String IMAGE_SERVER = "http://10.0.2.2:8910/";

  private ImageService imageService;

  @Override public void onCreate() {
    super.onCreate();

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setClient(new UrlConnectionClient())
        .setServer(IMAGE_SERVER)
        .build();
    imageService = restAdapter.create(ImageService.class);
  }

  public ImageService getImageService() {
    return imageService;
  }
}
