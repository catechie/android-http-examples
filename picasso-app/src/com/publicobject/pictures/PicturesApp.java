package com.publicobject.pictures;

import android.app.Application;
import com.squareup.picasso.Loader;
import com.squareup.picasso.Picasso;
import retrofit.RestAdapter;

public class PicturesApp extends Application {
  private static final String PICTURE_SERVER = "http://10.0.2.2:8910/";

  private PictureService pictureService;
  private Picasso picasso;

  @Override public void onCreate() {
    super.onCreate();

    // Set up Retrofit.
    // This uses 10.0.2.2 which is how the emulator can address the host operating system.
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setServer(PICTURE_SERVER)
        .build();
    pictureService = restAdapter.create(PictureService.class);

    picasso = new Picasso.Builder(this)
        .build();
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
