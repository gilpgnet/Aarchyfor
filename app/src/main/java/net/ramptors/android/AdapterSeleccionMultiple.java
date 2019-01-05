package net.ramptors.android;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdapterSeleccionMultiple extends ArrayAdapter<InfoOpcion> {
  private final String name;
  private ListView lista;
  public AdapterSeleccionMultiple(Context context, String name) {
    super(context, android.R.layout.simple_list_item_multiple_choice);
    this.name = name;
  }
  public void append(FormData formData) {
    final SparseBooleanArray seleccion = lista.getCheckedItemPositions();
    for(int i = 0, count = getCount(); i < count; i++){
        if(seleccion.get(i)) {
          formData.append(name, getItem(i).value);
        }
    }
  }
  public void adapta(ListView lista) {
    this.lista = lista;
    lista.setAdapter(this);
    lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
  }
  public void setOpciones(InfoOpcion[] opciones) {
    clear();
    if (opciones != null) {
      addAll(opciones);
    }
    for (int i = 0, longitud = opciones.length; i < longitud; i++) {
      final InfoOpcion fila = opciones[i];
      lista.setItemChecked(i, fila != null && fila.selected);
    }
  }
  public InfoOpcion[] getOpciones() {
    final InfoOpcion[] opciones = new InfoOpcion[getCount()];
    final SparseBooleanArray seleccion = lista.getCheckedItemPositions();
    for(int i = 0, length = opciones.length; i < length; i++){
      final InfoOpcion opcion = getItem(i);
      opciones[i] = opcion;
      opcion.selected = seleccion.get(i);
    }
    return opciones;
  }
}