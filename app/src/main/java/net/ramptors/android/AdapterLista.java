package net.ramptors.android;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.ramptors.android.InfoFila;
import static net.ramptors.android.Util.isNullOrEmpty;
import static net.ramptors.android.Util.texto;

public class AdapterLista extends ArrayAdapter<InfoFila> {
  public static final String IMAGEN = "imagen";
  public static final String TEXTO1 = "texto1";
  public static final String TEXTO2 = "texto2";
  @LayoutRes
  private final int layoutRes;
  public AdapterLista(Context context, @LayoutRes int layoutRes) {
    super(context, layoutRes);
    this.tipoDetalle = tipoDetalle;
    this.layoutRes = layoutRes;
  }
  public void setFilas(InfoFila[] filas) {
    clear();
    if (filas != null) {
      addAll(filas);
    }
  }
  @Override
  public View getView(int posicion, View convertView, ViewGroup parent) {
    final LayoutInflater inflater =
        LayoutInflater.from(parent.getContext());
    final View view = convertView != null ? convertView : inflater.inflate(layoutRes, parent, false);
    final ImageView imagen = view.findViewWithTag(IMAGEN);
    final TextView texto1 = view.findViewWithTag(TEXTO1);
    final TextView texto2 = view.findViewWithTag(TEXTO2);
    final InfoFila modelo = getItem(posicion);
    texto1.setText(texto(modelo.texto1));
    if (imagen != null) {
      imagen.setImageURI(isNullOrEmpty(imagen) ? null : modelo.imagen);
    }
    if (texto2 != null) {
      texto2.setText(texto(modelo.texto2));
    }
    return view;
  }
}