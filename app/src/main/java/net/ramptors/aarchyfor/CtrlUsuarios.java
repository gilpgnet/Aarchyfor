package net.ramptors.aarchyfor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import net.ramptors.android.Controlador;
import net.ramptors.android.GetTaskLoader;
import net.ramptors.android.RespuestaFilas;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.LoaderManager;
import static net.ramptors.android.Util.abre;
import static net.ramptors.android.Util.EXTRA_ID;
import static net.ramptors.android.Util.isNullOrEmpty;

public class CtrlUsuarios extends Controlador
    implements OnItemClickListener, LoaderManager.LoaderCallbacks<RespuestaFilas> {
  public static final String URL_SERVICIOS = "https://archyfor.000webhostapp.com/servicios";
  private final AdapterLista adapter = new AdapterLista(this, R.layout.fila);
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.form_maestra);
    getSupportLoaderManager().initLoader(0, null, this);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_listado, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_agrega:
        startActivity(new Intent(this, CtrlUsuarioNuevo.class));
        return true;
      case R.id.action_actualiza:
        getSupportLoaderManager().restartLoader(0, null, this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
      }
  }
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    final Intent intent = new Intent(this, CtrlUsuario.class);
    intent.putExtra(EXTRA_ID, id);
    startActivity(intent);
  }
  @Override
  public Loader<RespuestaFilas> onCreateLoader(int id, Bundle args) {
    return new GetTaskLoader(this, new RespuestaFilas(), URL_SERVICIO + "usuarios_consulta.php");
  }
  @Override
  public void onLoadFinished(Loader<RespuestaFilas> loader, RespuestaFilas data) {
    try {
      if (data == null) {
        throw new Exception(getString(R.string.error_procesando_respuesta));
      } else if (!isNullOrEmpty(data.error)) {
        throw new Exception(data.error);
      } else {
        adapter.setFilas(data.lista);
      }      
    } catch (Exception e) {
      muestraError(this, tag, "Error procesando respuesta.", e);
    }
  }
  @Override
  public void onLoaderReset(Loader<RespuestaFilas> loader) {
  }
}