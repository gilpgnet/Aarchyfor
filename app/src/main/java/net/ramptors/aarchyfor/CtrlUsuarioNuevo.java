package net.ramptors.aarchyfor;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ListView;
import net.ramptors.aususeg.databinding.FormUsuarioNuevoBinding;
import android.support.v4.app.LoaderManager;
import net.ramptors.android.Controlador;
import net.ramptors.android.FormData;
import net.ramptors.android.Util;
import static net.ramptors.aarchyfor.CtrlUsuarios.URL_SERVICIO;
import static net.ramptors.android.Util.texto;

public class CtrlUsuarioNuevo extends Controlador
    implements LoaderManager.LoaderCallbacks<Respuesta> {
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
    cue = view.findViewWithId(R.id.cue);
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
          return new GetTaskLoader(this, new RespuestaUsuario(), URL_SERVICIO + "usuarios_busca.php");
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
    getMenuInflater().inflate(R.menu.menu_nuevo, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_guarda:
        getSupportLoaderManager().restartLoader(1, null, this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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
  public Loader<Respuesta> onCreateLoader(int id, Bundle args) {
    return new PostTaskLoader(this, URL_SERVICIO + "usuarios_agrega.php");
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