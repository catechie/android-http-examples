package com.publicobject.pictures;

import android.app.Activity;
import android.os.Bundle;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PicturesActivity extends Activity {
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ImageService imageService = getApp().getImageService();
    imageService.listImages("", new Callback<List<String>>() {
      @Override public void success(List<String> images, Response response) {
        System.out.println("Pictures: " + images);
      }
      @Override public void failure(RetrofitError retrofitError) {
        System.out.println("Pictures failure: " + retrofitError);
      }
    });
  }

  private PicturesApp getApp() {
    return (PicturesApp) getApplication();
  }
}
