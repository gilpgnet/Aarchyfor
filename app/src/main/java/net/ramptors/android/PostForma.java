package net.ramptors.android;

import static net.ramptors.android.Util.EXECUTOR;
import static net.ramptors.android.Util.isNullOrEmpty;

public class PostForma<R extends Respuesta> {
  public interface Publicado<T extends Respuesta> {
    void publicado(T respuesta);
  }
  private enum Etapa {
    NINGUNO, PROCESANDO, MOSTRANDO
  }
  private Etapa etapa = Etapa.NINGUNO;
  private R resultado;
  private Exception error;
  private Controlador controlador;
  private Class<R> tipo;
  private Publicado<R> publicado;

  public synchronized void setControlador(Controlador controlador) {
    this.controlador = controlador;
    this.publicado = null;
  }

  public synchronized void post(Controlador controlador, String url,  Class<R> tipo, Publicado<R> publicado) {
    this.controlador = controlador;
    this.tipo = tipo;
    this.publicado = publicado;
    if (etapa == Etapa.NINGUNO) {
      etapa = Etapa.PROCESANDO;
      envia(url);
    }
  }
  public synchronized void continua(Controlador controlador, Publicado<R> publicado) {
    this.controlador = controlador;
    this.publicado = publicado;
    switch (etapa) {
    case NINGUNO:
    case PROCESANDO:
      break;
    case MOSTRANDO:
      if (resultado != null) {
        publicado(resultado);
      } else if (error != null) {
        muestra(error);
      }
    }
  }

  private void envia(final String url) {
    resultado = null;
    error = null;
    EXECUTOR.execute(new Runnable() {
      @Override
      public void run() {
        FormData formData = null;
        try {
          formData = new FormData(url);
          controlador.llenaFormData(formData);
          final R respuesta = formData.recibe(tipo);
          if (isNullOrEmpty(respuesta.error)) {
            publicado(respuesta);
          } else {
            throw new Exception(respuesta.error);
          }
        } catch (Exception e) {
          muestra(e);
        } finally {
          if (formData != null) {
            formData.desconecta();
          }
        }
      }
    });
  }

  private synchronized void publicado(final R resultado) {
    this.resultado = resultado;
    etapa = Etapa.MOSTRANDO;
    if (controlador != null && publicado != null) {
      controlador.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          publicado.publicado(resultado);
        }
      });
      etapa = Etapa.NINGUNO;
    }
  }

  private synchronized void muestra(final Exception error) {
    this.error = error;
    etapa = Etapa.MOSTRANDO;
    if (controlador != null) {
      controlador.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          controlador.muestraError("Error publicando.", error);
        }
      });
      etapa = Etapa.NINGUNO;
    }
  }
}