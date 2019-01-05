package net.ramptors.android;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import static net.ramptors.asincro.Util.agregaCookies;
import static net.ramptors.asincro.Util.getMensaje;
import static net.ramptors.asincro.Util.recibeJson;

public class GetTaskLoader<R extends Respuesta> extends AsyncTaskLoader<R> {
  private final String tag = getClass().getName();
  private final R respuestaError;
  private final String url;

  public GetTaskLoader(Context context,R respuestaError, String url) {
    super(context);
    this.respuestaError = respuestaError;
    this.url = url;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }

  @Override
  public R loadInBackground() {
    HttpURLConnection c = null;
    try {
      c = (HttpURLConnection) new URL(url).openConnection();
      c.setUseCaches(false);
      agregaCookies(c);
      return recibeJson(tipo, c);
    } catch (Exception e) {
      Log.e(tag, "Error en la conexión.", e);
      respuestaError.error = getMensaje(e);
      return respuesta;
    } finally {
      if (c != null) {
        c.disconnect();
      }
    }
  }
}