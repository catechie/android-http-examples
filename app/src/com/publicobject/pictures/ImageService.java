package com.publicobject.pictures;

import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ImageService {
  @GET("/{directory}")
  void listImages(@Path("directory") String directory, Callback<List<String>> imagesCallback);
}
