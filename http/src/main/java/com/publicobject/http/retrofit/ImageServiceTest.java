package com.publicobject.http.retrofit;

import org.junit.Test;
import retrofit.RestAdapter;
import retrofit.client.UrlConnectionClient;

public class ImageServiceTest {
  @Test public void test() {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setClient(new UrlConnectionClient())
        .setServer("http://localhost:8910/")
        .build();
    ImageService imageService = restAdapter.create(ImageService.class);
    System.out.println(imageService.listImages(""));
  }
}
