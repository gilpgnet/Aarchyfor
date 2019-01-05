package net.ramptors.android;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import static net.ramptors.asincro.Util.agregaCookies;
import static net.ramptors.asincro.Util.getMensaje;
import static net.ramptors.asincro.Util.recibeJson;

public class PostTaskLoader<Respuesta> extends AsyncTaskLoader<Respuesta> {
  private final String tag = getClass().getName();
  private final Controlador controlador;
  private final String url;

  public PostTaskLoader(Controlador controlador, String url) {
    super(controlador);
    this.controlador = controlador;
    this.url = url;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }

  @Override
  public Respuesta loadInBackground() {
    FormData formData = null;
    try {
      formData = new FormData(url);
      controlador.llenaFormData(formData);
      return formData.recibe(tipo);
    } catch (Exception e) {
      final Respuesta respuesta = new Respuesta();
      respuesta.error = getMensaje(e);
      return respuesta;
    } finally {
      if (formData != null) {
        formData.desconecta();
      }
    }
  }
}