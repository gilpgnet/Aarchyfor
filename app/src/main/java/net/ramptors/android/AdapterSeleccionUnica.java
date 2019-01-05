package net.ramptors.android;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdapterSeleccionUnica extends ArrayAdapter<InfoOpcion> {
  private final String name;
  private Spinner spinner;
  public AdapterSeleccionUnica(Context context, String name) {
    super(context, android.R.layout.simple_list_item_1);
    this.name = name;
  }
  public void append(FormData formData) {
    final int seleccion = spinner.getSelectedItemPosition();
    if (seleccion >= 0) {
      formData.append(name,  getItem(seleccion).value);
    }
  }
  public void adapta(Spinner spinner) {
    this.spinner = spinner;
    spinner.setAdapter(this);
  }
  public void setOpciones(InfoOpcion[] opciones) {
    clear();
    if (opciones != null) {
      addAll(opciones);
    }
    int seleccion = -1;
    for (int i = 0, longitud = opciones.length; i < longitud; i++) {
      final InfoOpcion fila = opciones[i];
      if (fila != null && fila.selected) {
        seleccion = i;
      }
    }
    spinner.setSelection(seleccion);
  }
  public InfoOpcion[] getOpciones() {
    final int seleccion = spinner.getSelectedItemPosition();
    final InfoOpcion[] opciones = new InfoOpcion[getCount()];
    for(int i = 0, length = opciones.length; i < length; i++){
      final InfoOpcion opcion = getItem(i);
      opciones[i] = opcion;
      opcion.selected = i == seleccion;
    }
    return opciones;
  }
}