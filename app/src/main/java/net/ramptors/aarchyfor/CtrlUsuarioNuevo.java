package net.ramptors.aarchyfor;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ListView;
import net.ramptors.android.AdapterSeleccionMultiple;
import net.ramptors.android.AdapterSeleccionUnica;
import net.ramptors.android.Controlador;
import net.ramptors.android.FormData;
import net.ramptors.android.GetRespuesta;
import net.ramptors.android.PostForma;
import net.ramptors.android.Respuesta;
import net.ramptors.android.Util;
import static net.ramptors.aarchyfor.CtrlUsuarios.URL_SERVICIOS;
import static net.ramptors.android.Util.texto;

public class CtrlUsuarioNuevo extends Controlador
    implements GetRespuesta.RecibeRespuesta<RespuestaUsuario>,
    PostForma.Publicado<Respuesta> {
  private static final GetRespuesta<RespuestaUsuario> buscaForaneas = new GetRespuesta<RespuestaUsuario>();
  private static final PostForma<Respuesta> postForma = new PostForma<Respuesta>();
  private static RespuestaUsuario respuesta;
  private EditText cue;
  private EditText match;
  private EditText nombre;
  private Spinner pasatiempo;
  private ListView roles;
  private final AdapterSeleccionUnica adapterPasatiempo = new AdapterSeleccionUnica(this, "pasatiempo");
  private final AdapterSeleccionMultiple adapterRoles = new AdapterSeleccionMultiple(this, "roles[]");
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    configuraRegreso();
    setContentView(R.layout.form_usuario_nuevo);
    cue = findViewById(R.id.cue);
    match = findViewById(R.id.match);
    nombre = findViewById(R.id.nombre);
    pasatiempo = findViewById(R.id.pasatiempo);
    roles = findViewById(R.id.roles);
    adapterPasatiempo.adapta(pasatiempo);
    adapterRoles.adapta(roles);
    if (respuesta == null) {
      buscaForaneas.get(this, URL_SERVICIOS + "usuarios_busca.php", RespuestaUsuario.class, this);
    } else {
      adapterPasatiempo.setOpciones(respuesta.pasatiempos);
      adapterRoles.setOpciones(respuesta.roles);
    }
    postForma.continua(this, this);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_nuevo, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_guarda:
        postForma.post(this, URL_SERVICIOS + "usuarios_agrega.php", Respuesta.class, this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  @Override
  public void recibe(RespuestaUsuario respuesta) {
    CtrlUsuarioNuevo.respuesta = respuesta;
    adapterPasatiempo.setOpciones(respuesta.pasatiempos);
    adapterRoles.setOpciones(respuesta.roles);
  }
  @Override
  protected void llenaFormData(FormData formData) {
    formData.append("cue", cue.getText().toString().trim());
    formData.append("match", match.getText().toString().trim());
    formData.append("nombre", nombre.getText().toString().trim());
    adapterPasatiempo.append(formData);
    adapterRoles.append(formData);
  }
  @Override
  public void publicado(Respuesta respuesta)  {
    regresa();
  }
  @Override
  protected void onDestroy() {
    postForma.setControlador(null);
    if (respuesta == null) {
      buscaForaneas.setControlador(null);
    } else {
      respuesta.pasatiempos = adapterPasatiempo.getOpciones();
      respuesta.roles = adapterRoles.getOpciones();
    }
    super.onDestroy();
  }
}