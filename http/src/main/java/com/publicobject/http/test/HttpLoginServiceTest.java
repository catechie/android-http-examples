package com.publicobject.http.test;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class HttpLoginServiceTest {
  MockWebServer mockWebServer;

  @Before public void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.play();
  }

  @After public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test public void testSuccessfulLogin() throws Exception {
    mockWebServer.enqueue(new MockResponse().setBody("55378008"));
    URL baseUrl = mockWebServer.getUrl("/api/1.0/");

    LoginService loginService = new HttpLoginService(new OkHttpClient(), baseUrl);
    String token = loginService.login("jwilson@squareup.com", "pa55word");
    assertThat(token).isEqualTo("55378008");

    RecordedRequest request = mockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/api/1.0/login");
    assertThat(request.getUtf8Body()).isEqualTo("jwilson@squareup.com\npa55word");
  }

  @Test public void testUnsuccessfulLogin() throws Exception {
    mockWebServer.enqueue(new MockResponse().setResponseCode(401).setBody("bad password fool!"));
    URL baseUrl = mockWebServer.getUrl("/api/1.0/");

    LoginService loginService = new HttpLoginService(new OkHttpClient(), baseUrl);
    try {
      loginService.login("jwilson@squareup.com", "kittens");
      fail();
    } catch (IOException e) {
      assertThat(e).hasMessage("bad password fool!");
    }
  }
}
