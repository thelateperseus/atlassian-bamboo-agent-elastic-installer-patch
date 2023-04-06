package com.atlassian.aws.ec2;

import com.amazonaws.util.IOUtils;
import com.atlassian.aws.utils.CompressionUtils;
import com.atlassian.aws.utils.JsonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

public final class Ec2UtilsThin {

  static final String API_BASE_URL = "http://169.254.169.254/latest/";

  private static final String TOKEN_URL = API_BASE_URL + "api/token";
  private static final String USER_DATA_URL = API_BASE_URL + "user-data";

  @NotNull
  public static EC2Instance getLocalEC2Instance() throws IOException {
    return new LocalEC2InstanceImpl();
  }

  public static <T> T getUserData(final Class<T> aClass) throws IOException {
    final URL tokenUrl = new URL(TOKEN_URL);
    final HttpURLConnection tokenConnection = (HttpURLConnection) tokenUrl.openConnection();
    tokenConnection.setRequestMethod("PUT");
    tokenConnection.setRequestProperty("X-aws-ec2-metadata-token-ttl-seconds", "21600");
    String token;
    try (InputStream in = tokenConnection.getInputStream()) {
      token = IOUtils.toString(in);
    }

    final URL userDataUrl = new URL(USER_DATA_URL);
    final HttpURLConnection userDataConnection = (HttpURLConnection) userDataUrl.openConnection();
    userDataConnection.setRequestProperty("X-aws-ec2-metadata-token", token);
    byte[] userData;
    try (InputStream in = userDataConnection.getInputStream()) {
      userData = IOUtils.toByteArray(in);
    }

    try {
      return JsonUtils.fromJson(
          new String(CompressionUtils.decompress(userData), StandardCharsets.UTF_8),
          aClass);
    } catch (IOException e) {
      return JsonUtils.fromJson(new String(userData, StandardCharsets.UTF_8), aClass);
    }
  }
}
