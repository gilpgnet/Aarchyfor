package net.ramptors.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdapterSeleccionUnica extends ArrayAdapter<InfoOpcion>
    implements OnItemSelectedListener {
  private final String name;
  private String value;
  private Spinner spinner;
  public AdapterSeleccionUnica(Context context, String name) {
    super(context, android.R.layout.simple_list_item_1);
    this.name = name;
  }
  public void append(FormData formData) {
    formData.append(name, value);
  }
  public void adapta(Spinner spinner) {
    this.spinner = spinner;
    spinner.setAdapter(this);
    spinner.setOnItemSelectedListener(this);
  }
  public void setOpciones(InfoOpcion[] opciones) {
    clear();
    if (opciones != null) {
      addAll(opciones);
    }
    int seleccion = -1;
    for (int i = 0, longitud = this.opciones.size(); i < longitud; i++) {
      final InfoOpcion fila = this.opciones.get(i);
      if (fila != null && fila.selected) {
        seleccion = i;
      }
    }
    notifyDataSetChanged();
    spinner.setSelection(seleccion);
  }
  @Override
  public void onItemSelected(AdapterView<?> parent, View sel, int pos,
      long id) {
    final InfoOpcion item = (InfoOpcion) spinner.getSelectedItem();
    value = item == null ? null : item.value;
  }
  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    value = null;
  }
}