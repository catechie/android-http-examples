package com.publicobject.http.get;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApacheHttpClientExample {
  final HttpClient httpClient = new DefaultHttpClient();

  public void get(String url) throws IOException {
    HttpResponse response = httpClient.execute(new HttpGet(url));
    printResponse(response.getEntity().getContent());
  }

  private void printResponse(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    for (String line; (line = reader.readLine()) != null; ) {
      System.out.println(line);
    }
  }

  public static void main(String[] args) throws IOException {
    String url = "http://localhost:8910/rfc2616.txt";
    new ApacheHttpClientExample().get(url);
  }
}
