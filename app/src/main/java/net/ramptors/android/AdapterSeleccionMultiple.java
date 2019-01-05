package net.ramptors.android;

import android.content.Context;
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
    SparseBooleanArray sparseBooleanArray = lista.getCheckedItemPositions();
    for(int i = 0, count = lista.getCount(); i < count; i++){
        if(sparseBooleanArray.get(i)) {
          formData.append(name, lista.getItemAtPosition(i).value);
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
  }
}