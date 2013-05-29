package com.publicobject.http.post;

import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class OkHttpExample {
  final OkHttpClient okHttpClient = new OkHttpClient();

  public int post(String url, String body) throws IOException {
    HttpURLConnection connection = okHttpClient.open(new URL(url));
    connection.setRequestMethod("POST");
    postRequest(body, connection.getOutputStream());
    return connection.getResponseCode();
  }

  private void postRequest(String body, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, "UTF-8");
    writer.write(body);
    writer.close();
  }

  public static void main(String[] args) throws IOException {
    String message = "It’s Friday, Friday\n"
        + "Gotta get down on Friday\n"
        + "Everybody’s lookin’ forward to the weekend, weekend\n"
        + "Friday, Friday\n"
        + "Gettin’ down on Friday\n"
        + "Everybody’s lookin’ forward to the weekend";
    String url = "http://localhost:8910/friday.txt";
    new OkHttpExample().post(url, message);
  }
}
