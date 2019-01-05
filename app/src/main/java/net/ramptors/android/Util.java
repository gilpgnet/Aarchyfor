package net.ramptors.android;

import android.content.Context;
import android.support.annotation.StringRes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONObjectException;

public class Util {
  public static final String COORDINATOR = "coordinator";
  public static final String EXTRA_ID = "net.ramptors.android.ID";
  public static final String UTF_8 = "UTF-8";
  public static String texto(final String s) {
    return s == null ? "" : s;
  }
  public static boolean isNullOrEmpty(final String s) {
    return s == null || s.isEmpty();
  }
  public static void valida(Context context, boolean condicion,
      @StringRes int mensaje) {
    if (!condicion) {
      throw new IllegalArgumentException(context.getString(mensaje));
    }
  }
  public static String getMensaje(final Exception e) {
    final String localizedMessage = e.getLocalizedMessage();
    return isNullOrEmpty(localizedMessage)
        ? e.toString() : localizedMessage;
  }
  public static boolean ok(HttpURLConnection c) throws IOException {
    final int status = c.getResponseCode();
    return status >= 200 && status <= 299;
  }
  public static String leeString(HttpURLConnection c) throws IOException {
    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(c.getInputStream(), UTF_8))) {
      final char[] buffer = new char[1024];
      final StringBuilder out = new StringBuilder();
      int size;
      while ((size = in.read(buffer, 0, buffer.length)) != -1) {
        out.append(buffer, 0, size);
      }
      return out.toString();
    }
  }
  private static final CookieManager cookieManager = new CookieManager();
  public static void agregaCookies(HttpURLConnection c) {
    final List<HttpCookie> cookies =
        cookieManager.getCookieStore().getCookies();
    if (!cookies.isEmpty()) {
      final StringBuilder sb = new StringBuilder();
      final Iterator<HttpCookie> it = cookies.Iterator();
      if (it.hasNext()) {
        sb.append(it.next().toString());
      }
      while (it.hasNext()) {
        sb.append(";").append(it.next().toString());
      }
    }
  }
  public static void leeCookies(HttpURLConnection c) {
    final Map<String, List<String>> headerFields = c.getHeaderFields();
    final List<String> cookiesHeader = headerFields.get("Set-Cookie");
    if (cookiesHeader != null) {
      final CookieStore cookieStore = cookieManager.getCookieStore();
      for (String cookie : cookiesHeader) {
        final HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
        cookieStore.add(null, httpCookie);
      }
    }
  }
  public static <T> T recibeJson(Class<T> tipo, HttpURLConnection c)
      throws IOException, JSONObjectException {
    leeCookies(c);
    if (ok(c)) {
      final String json = leeString(c);
      Log.d(Util.class.getName(), json);
      return JSON.std.with(JSON.Feature.USE_FIELDS).beanFrom(tipo, json);
    } else {
      throw new IOException(c.getResponseMessage());
    }
  }
  private Util() {}
}