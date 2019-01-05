package net.ramptors.aarchyfor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ListView;
import net.ramptors.android.Controlador;
import net.ramptors.android.FormData;
import static net.ramptors.android.Util.texto;
import static net.ramptors.android.Util.EXTRA_ID;

public class CtrlUsuario extends Controlador
    implements DialogoConfirmaEliminar.EliminarConfirmado,
    LoaderManager.LoaderCallbacks<Respuesta>  {
  private String extra_id;
  private static RespuestaUsuario respuesta;
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
    setContentView(R.layout.form_usuario);
    extra_id = getIntent().getStringExtra(EXTRA_ID);
    setTitle(extra_id);
    match = view.findViewWithId(R.id.match);
    nombre = view.findViewWithId(R.id.nombre);
    pasatiempo = view.findViewWithId(R.id.pasatiempo);
    roles = view.findViewWithId(R.id.roles);
    adapterPasatiempo.adapta(pasatiempo);
    adapterRoles.adapta(roles);
    if (respuesta != null) {
      adapterPasatiempo.setOpciones(data.pasatiempos);
      adapterRoles.setOpciones(data.roles);
    } else {
      getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<RespuestaUsuario>() {
        @Override
        public Loader<RespuestaUsuario> onCreateLoader(int id, Bundle args) {
          return new GetTaskLoader(this, new RespuestaUsuario(), Uri.parse(URL_SERVICIO + "usuarios_busca.php").buildUpon()
          .appendQueryParameter("cue", extra_id).build().toString());
        }
        @Override
        public void onLoadFinished(Loader<RespuestaUsuario> loader, RespuestaUsuario data) {
          try {
            if (data == null) {
              throw new Exception(getString(R.string.error_procesando_respuesta));
            } else if (!isNullOrEmpty(data.error)) {
              throw new Exception(data.error);
            } else {
              respuesta = data;
              nombre.setText(texto(data.modelo.nombre));
              adapterPasatiempo.setOpciones(data.pasatiempos);
              adapterRoles.setOpciones(data.roles);
            }      
          } catch (Exception e) {
            muestraError(this, tag, "Error procesando respuesta.", e);
          }
        }
        @Override
        public void onLoaderReset(Loader<RespuestaUsuario> loader) {
        }  
       });
    }
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_detalle, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
       regresa();
       return true;
      case R.id.action_guarda:
        getSupportLoaderManager().restartLoader(1, null, this);
        return true;
      case R.id.action_elimina:
        DialogoConfirmaEliminar.confirma(getSupportFragmentManager())));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  @Override
  protected void llenaFormData(FormData formData) {
    formData.append("cue", extra_id);
    formData.append("match", match.getText().toString().trim());
    formData.append("nombre", campos.nombre.getText().toString().trim());
    adapterPasatiempo.append(formData);
    adapterRoles.append(formData);
  }
  @Override
  public void eliminarConfirmado() {
    if (extra_id != null) {
      getSupportLoaderManager().restartLoader(2, null, this);
    }
  }
  @Override
  public Loader<Respuesta> onCreateLoader(int id, Bundle args) {
    final String url = id == 1 ? "usuarios_modifica.php": "usuarios_elimina.php";
    return new PostTaskLoader(this, URL_SERVICIO + url);
  }
  @Override
  public void onLoadFinished(Loader<Respuesta> loader, RespuestaFilas data) {
    try {
      if (!isNullOrEmpty(data.error)) {
        throw new Exception(data.error);
      } else {
        regresa();
      }      
    } catch (Exception e) {
      muestraError(this, tag, "Error procesando respuesta.", e);
    }
  }
  @Override
  public void onLoaderReset(Loader<Respuesta> loader) {
  }
}