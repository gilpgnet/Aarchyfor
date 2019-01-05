package net.ramptors.android;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.fasterxml.jackson.jr.ob.JSONObjectException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import static net.ramptors.android.Util.agregaCookies;
import static net.ramptors.android.Util.isNullOrEmpty;
import static net.ramptors.android.Util.recibeJson;
import static net.ramptors.android.Util.UTF_8;

public class FormData {
  private static final String SALTO_DE_LINEA = "\r\n";
  /** Boundary único  basado en el timestamp. */
  private final String boundary =
      "=========" + System.currentTimeMillis() + "=========";
  private final HttpURLConnection c;
  private final PrintWriter out;
  private final OutputStream os;
  public FormData(String url) throws IOException {
    c = (HttpURLConnection) new URL(url).openConnection();
    c.setUseCaches(false);
    c.setDoOutput(true); // Envía datos al servidor.
    c.setDoInput(true);
    agregaCookies(c);
    c.setRequestProperty("Content-Type",
        "multipart/form-data; boundary=" + boundary);
    os = c.getOutputStream();
    out = new PrintWriter(new OutputStreamWriter(os, UTF_8), true);
  }
  /** Agrega un form field a la solicitud
   * @param nombre nombre del campo.
   * @param valor valor del campo. */
  public void append(String nombre, String valor) {
    if (!isNullOrEmpty(valor)) {
      out.append("--").append(boundary).append(SALTO_DE_LINEA)
          .append("Content-Disposition: form-data; name=\"").append(nombre)
          .append("\"").append(SALTO_DE_LINEA)
          .append("Content-Type: text/plain; charset=").append(UTF_8)
          .append(SALTO_DE_LINEA)
          .append(SALTO_DE_LINEA)
          .append(valor).append(SALTO_DE_LINEA)
          .flush();
    }
  }
  /** Agrega a archivo de subida a la solicitud.
   * @param nombre atributo name en <input type="file" name="..." />
   * @param fileName nombre del archivo.
   * @param is archivo a subir.
   * @throws IOException si algo sale mal. */
  public void append(String nombre, String fileName, InputStream is)
      throws IOException {
    if (!isNullOrEmpty(nombre) && !isNullOrEmpty(nombre) && is != null) {
      try {
        out.append("--").append(boundary).append(SALTO_DE_LINEA)
            .append("Content-Disposition: form-data; name=\"")
            .append(nombre)
            .append("\"; filename=\"").append(fileName).append("\"")
            .append(SALTO_DE_LINEA)
            .append("Content-Type: ").append("application/octet-stream")
            .append(SALTO_DE_LINEA)
            .append("Content-Transfer-Encoding: binary")
            .append(SALTO_DE_LINEA)
            .append(SALTO_DE_LINEA)
            .flush();
        enviaInputStream(is);
        os.flush();
        out.append(SALTO_DE_LINEA);
      } finally {
        out.flush();
      }
    }
  }
  /** Completa la solicitud y devuelve la respuesta del servidor.
   * @return un ModeloFormConocidos si el status es correcto; en otro caso,
   * lanza una excepción. */
  public <T> T recibe(Class<T> tipo)
      throws IOException, JSONObjectException {
    out.append("--").append(boundary).append("--").append(SALTO_DE_LINEA)
        .close();
    return recibeJson(tipo, c);
  }
  public void desconecta() {
    c.disconnect();
  }
  private void enviaInputStream(final InputStream is) throws IOException {
    try (InputStream i = is){
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    }
  }
}