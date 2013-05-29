package com.publicobject.http;

import com.google.mockwebserver.Dispatcher;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileServer extends Dispatcher {
  private final File base;

  public FileServer(File base) {
    this.base = base.getAbsoluteFile();
  }

  @Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
    try {
      File requested = new File(base, request.getPath()).getAbsoluteFile();
      boolean upload = request.getMethod().equals("POST");

      if (!requested.getPath().startsWith(base.getPath())) {
        return new MockResponse().setResponseCode(404);
      }

      if (upload) {
        if (requested.isDirectory()) {
          return new MockResponse().setResponseCode(400);
        }
        return saveFile(request.getBody(), requested);
      }

      if (!requested.exists()) {
        return new MockResponse().setResponseCode(404);
      }

      return requested.isDirectory() ? serveDirectory(requested) : serveFile(requested);
    } catch (Exception e) {
      return new MockResponse().setBody(e.toString()).setResponseCode(500);
    }
  }

  private MockResponse saveFile(byte[] data, File requested) throws IOException {
    OutputStream out = new FileOutputStream(requested);
    out.write(data);
    out.close();
    return new MockResponse().setResponseCode(200);
  }

  private MockResponse serveDirectory(File directory) throws IOException {
    StringBuilder result = new StringBuilder();
    String[] files = directory.list();
    if (files != null) {
      for (String file : files) {
        result.append(file).append("\n");
      }
    }
    return new MockResponse()
        .addHeader("Content-Type: text/plain")
        .setBody(result.toString());
  }

  private MockResponse serveFile(File file) throws IOException {
    return new MockResponse()
        .setBody(new FileInputStream(file), file.length())
        .addHeader("Content-Type", contentType(file))
        .addHeader("Cache-Control", "max-age=3600");
  }

  private String contentType(File file) {
    String path = file.getPath();
    return path.endsWith(".png") ? "image/png"
        : path.endsWith(".gif") ? "image/gif"
        : path.endsWith(".jpg") ? "image/jpeg"
        : path.endsWith(".jpeg") ? "image/jpeg"
        : "application/octet-stream";
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) throw new IllegalArgumentException("Usage HttpService <directory>");

    MockWebServer server = new MockWebServer();
    server.setDispatcher(new FileServer(new File(args[0])));
    server.play(8910);
  }
}
