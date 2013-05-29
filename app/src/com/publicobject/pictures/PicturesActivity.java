package com.publicobject.pictures;

import android.app.Activity;
import android.os.Bundle;

public class PicturesActivity extends Activity {
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ImageService imageService = getApp().getImageService();
    System.out.println(imageService.listImages(""));
  }

  private PicturesApp getApp() {
    return (PicturesApp) getApplication();
  }
}
