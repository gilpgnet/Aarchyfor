package net.ramptors.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static net.ramptors.android.Util.COORDINATOR;
import static net.ramptors.android.Util.getMensaje;

public abstract class Controlador extends AppCompatActivity {
  protected final String tag = getClass().getName();
  private CoordinatorLayout coordinator;
  protected CoordinatorLayout getCoordinator() {
    if (coordinator == null) {
      final ViewGroup content = findViewById(android.R.id.content);
      final ViewGroup vista = (ViewGroup) content.getChildAt(0);
      coordinator = vista.findViewWithTag(COORDINATOR);
    }
    return coordinator;
  }
  protected void configuraRegreso() {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
  protected void regresa() {
    NavUtils.navigateUpFromSameTask(this);
  }
  protected void llenaFormData(FormData formData) throws Exception {}
  protected void muestraError(@StringRes int errorRes, Exception e) {
    muestraError(getString(errorRes), e);
  }
  public void muestraError(String error, Exception e) {
    if (e != null) {
      Log.w(tag, error, e);
      muestraMensaje(getMensaje(e));
    }
  }
  protected void muestraMensaje(@StringRes int mensajeRes) {
    muestraMensaje(getString(mensajeRes));
  }
  protected void muestraMensaje(String mensaje) {
    final CoordinatorLayout coordinator = getCoordinator();
    if (coordinator != null) {
      Snackbar.make(coordinator, mensaje, LENGTH_LONG).show();
    }
  }
}