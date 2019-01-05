package net.ramptors.aarchyfor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ListView;
import java.io.FileNotFoundException;
import net.ramptors.android.AdapterSeleccionMultiple;
import net.ramptors.android.AdapterSeleccionUnica;
import net.ramptors.android.Controlador;
import net.ramptors.android.FormData;
import net.ramptors.android.InfoOpcion;
import net.ramptors.android.GetRespuesta;
import net.ramptors.android.PostForma;
import net.ramptors.android.Respuesta;
import net.ramptors.android.Util;
import static net.ramptors.aarchyfor.CtrlUsuarios.URL_SERVICIOS;
import static net.ramptors.android.Util.getBitmap;
import static net.ramptors.android.Util.SELECCIONA_IMAGEN;
import static net.ramptors.android.Util.texto;

public class CtrlUsuarioNuevo extends Controlador
    implements GetRespuesta.RecibeRespuesta<RespuestaUsuario>, PostForma.Publicado<Respuesta> {
  private static final GetRespuesta<RespuestaUsuario> buscaForaneas = new GetRespuesta<RespuestaUsuario>();
  private static final PostForma<Respuesta> postForma = new PostForma<Respuesta>();
  private static RespuestaUsuario respuesta;
  private static Intent data;
  private EditText cue;
  private ImageView avatar;
  private EditText match;
  private EditText nombre;
  private Spinner pasatiempo;
  private ListView roles;
  private AdapterSeleccionUnica adapterPasatiempo;
  private AdapterSeleccionMultiple adapterRoles;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    configuraRegreso();
    setContentView(R.layout.form_usuario_nuevo);
    cue = findViewById(R.id.cue);
    avatar = findViewById(R.id.avatar);
    match = findViewById(R.id.match);
    nombre = findViewById(R.id.nombre);
    pasatiempo = findViewById(R.id.pasatiempo);
    roles = findViewById(R.id.roles);
    adapterPasatiempo = new AdapterSeleccionUnica(this, "pasatiempo");
    adapterRoles = new AdapterSeleccionMultiple(this, "roles[]");
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
    case R.id.action_imagen:
      seleccionaImagen();
      return true;
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
  protected void llenaFormData(FormData formData) throws Exception {
    formData.append("cue", cue.getText().toString().trim());
    if (data != null) {
      formData.append("avatar", "avatar", getContentResolver().openInputStream(data.getData()));
    }
    formData.append("match", match.getText().toString().trim());
    formData.append("nombre", nombre.getText().toString().trim());
    adapterPasatiempo.append(formData);
    adapterRoles.append(formData);
  }
  @Override
  public void regresa() {
    respuesta = null;
    super.regresa();
  }
  @Override
  public void publicado(Respuesta respuesta) {
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

  private void seleccionaImagen() {
    final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    // intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, SELECCIONA_IMAGEN);
  }

  @Override
  public void onActivityResult(final int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == SELECCIONA_IMAGEN && data != null) {
      CtrlUsuarioNuevo.data = data;
      try {
        final Bitmap bitmap = getBitmap(this, data);
        if (bitmap != null) {
          avatar.setImageBitmap(bitmap);
        }
      } catch (FileNotFoundException e) {
        muestraError("Error recuperando imagen.", e);
      }
    }
  }
}