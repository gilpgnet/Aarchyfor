package net.ramptors.aarchyfor;

import net.ramptors.android.Controlador;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import net.ramptors.android.AdapterLista;
import net.ramptors.android.GetRespuesta;
import net.ramptors.android.RespuestaFilas;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import net.ramptors.android.Util;
import static net.ramptors.android.Util.EXTRA_ID;
import static net.ramptors.android.Util.isNullOrEmpty;

public class CtrlUsuarios extends Controlador
   implements OnItemClickListener, GetRespuesta.RecibeRespuesta<RespuestaFilas> {
  public static final String URL_SERVICIOS = "https://archyfor.000webhostapp.com/servicios";
  private static final GetRespuesta<RespuestaFilas> consultaFilas = new GetRespuesta<RespuestaFilas>();
  private final AdapterLista adapter = new AdapterLista(this, R.layout.fila);
  private ListView lista;
  private TextView vacio;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.form_maestra);
    lista = findViewById(R.id.lista);
    vacio = findViewById(R.id.vacio);
    lista.setAdapter(adapter);
    consultaFilas.get(this, URL_SERVICIOS + "usuarios_consulta.php", RespuestaFilas.class, this);
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
        consultaFilas.get(this, URL_SERVICIOS + "usuarios_consulta.php", RespuestaFilas.class, this);
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
  public void recibe(RespuestaFilas respuesta) {
    adapter.setFilas(respuesta.lista);
    Util.setVisible(lista, respuesta.lista.length > 0);
    Util.setVisible(vacio, respuesta.lista.length == 0);
  }
  @Override
  protected void onDestroy() {
    consultaFilas.setControlador(null);
    super.onDestroy();
  }
}