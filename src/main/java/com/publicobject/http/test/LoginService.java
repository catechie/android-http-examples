package com.publicobject.http.test;

import java.io.IOException;

public interface LoginService {
  /**
   * Attempts to authenticate with the login service. Returns a session token
   * if login is successful. Throws if login fails.
   */
  String login(String username, String password) throws IOException;
}
