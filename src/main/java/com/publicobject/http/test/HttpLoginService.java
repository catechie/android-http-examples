package com.publicobject.http.test;

import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpLoginService implements LoginService {
  final OkHttpClient okHttpClient;
  final URL baseUrl;

  public HttpLoginService(OkHttpClient okHttpClient, URL baseUrl) {
    this.okHttpClient = okHttpClient;
    this.baseUrl = baseUrl;
  }

  @Override public String login(String username, String password) throws IOException {
    URL loginUrl = new URL(baseUrl, "login");
    String requestBody = makeRequestBody(username, password);
    HttpURLConnection connection = okHttpClient.open(loginUrl);
    connection.setRequestMethod("POST");
    String responseBody = doHttp(requestBody, connection);
    return responseBody;
  }

  private String makeRequestBody(String username, String password) {
    return username + "\n" + password; // Should use JSON or form encoding!
  }

  private String doHttp(String body, HttpURLConnection connection) throws IOException {
    // Post a request
    Writer writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
    writer.write(body);
    writer.close();

    int responseCode = connection.getResponseCode();

    // Handle failure.
    if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
      String failure = streamToString(connection.getErrorStream());
      throw new IOException(failure);
    }

    // Success.
    return streamToString(connection.getInputStream());
  }

  private String streamToString(InputStream in) throws IOException {
    Reader reader = new InputStreamReader(in, "UTF-8");
    char[] buffer = new char[1024];
    StringWriter stringWriter = new StringWriter();
    for (int count; (count = reader.read(buffer)) != -1; ) {
      stringWriter.write(buffer, 0, count);
    }
    return stringWriter.toString();
  }
}
