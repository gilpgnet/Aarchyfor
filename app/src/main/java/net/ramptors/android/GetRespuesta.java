package net.ramptors.android;

import java.net.HttpURLConnection;
import java.net.URL;
import static net.ramptors.android.Util.agregaCookies;
import static net.ramptors.android.Util.EXECUTOR;
import static net.ramptors.android.Util.isNullOrEmpty;
import static net.ramptors.android.Util.recibeJson;

public class GetRespuesta<R extends Respuesta> {
  public interface RecibeRespuesta<T extends Respuesta> {
    void recibe(T respuesta);
  }
  private enum Etapa {
    INICIA, CONSULTANDO, MOSTRANDO
  }
  private Etapa etapa = Etapa.INICIA;
  private R resultado;
  private Exception error;
  private Controlador controlador;
  private Class<R> tipo;
  private RecibeRespuesta<R> recibeRespuesta;

  public synchronized void setControlador(Controlador controlador) {
    this.controlador = controlador;
    recibeRespuesta = null;
  }

  public synchronized void get(Controlador controlador, String url, Class<R> tipo, RecibeRespuesta<R> recibeRespuesta) {
    this.controlador = controlador;
    this.tipo = tipo;
    this.recibeRespuesta = recibeRespuesta;
    switch (etapa) {
    case INICIA:
      etapa = Etapa.CONSULTANDO;
      descarga(url);
      break;
    case CONSULTANDO:
      break;
    case MOSTRANDO:
      if (resultado != null) {
        recibe(resultado);
      } else if (error != null) {
        muestra(error);
      }
    }

  }

  private void descarga(final String url) {
    resultado = null;
    error = null;
    EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        HttpURLConnection c = null;
        try {
          c = (HttpURLConnection) new URL(url).openConnection();
            c.setUseCaches(false);
            agregaCookies(c);
            final R respuesta = recibeJson(tipo, c);
            if (isNullOrEmpty(respuesta.error)) {
              recibe(respuesta);
            } else {
              throw new Exception(respuesta.error);
            }
        } catch (Exception e) {
          muestra(e);
        } finally {
          if (c != null) {
            c.disconnect();
          }
        }
      }
    });
  }

  private synchronized void recibe(final R resultado) {
    this.resultado = resultado;
    etapa = Etapa.MOSTRANDO;
    if (controlador != null && recibeRespuesta != null) {
      controlador.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          recibeRespuesta.recibe(resultado);
        }
      });
      etapa = Etapa.INICIA;
    }
  }

  private synchronized void muestra(final Exception error) {
    this.error = error;
    etapa = Etapa.MOSTRANDO;
    if (controlador != null) {
      controlador.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          controlador.muestraError("Error consultando.", error);
        }
      });
      etapa = Etapa.INICIA;
    }
  }
}