package com.publicobject.http.post;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApacheHttpClientExample {
  final HttpClient httpClient = new DefaultHttpClient();

  public int post(String url, String body) throws IOException {
    HttpPost post = new HttpPost(url);
    post.setEntity(new StringEntity(body, ContentType.TEXT_PLAIN));
    HttpResponse response = httpClient.execute(post);
    return response.getStatusLine().getStatusCode();
  }

  public static void main(String[] args) throws IOException {
    String message = "It’s Friday, Friday\n"
        + "Gotta get down on Friday\n"
        + "Everybody’s lookin’ forward to the weekend, weekend\n"
        + "Friday, Friday\n"
        + "Gettin’ down on Friday\n"
        + "Everybody’s lookin’ forward to the weekend";
    String url = "http://localhost:8910/friday.txt";
    new ApacheHttpClientExample().post(url, message);
  }
}
