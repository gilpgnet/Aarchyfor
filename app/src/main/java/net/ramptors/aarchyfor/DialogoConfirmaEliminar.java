package net.ramptors.aarchyfor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v4.app.FragmentManager;
import static android.support.v7.app.AlertDialog.Builder;

public class DialogoConfirmaEliminar extends AppCompatDialogFragment {
  public interface EliminarConfirmado {
    void eliminarConfirmado();
  }
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Builder builder = new Builder(requireActivity());
    builder.setTitle(R.string.confirma_eliminar)
        .setMessage(R.string.perderas_datos)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            final EliminarConfirmado eliminarConfirmado = (EliminarConfirmado) requireActivity();
            eliminarConfirmado.eliminarConfirmado();
          }
        })
        .setNegativeButton(android.R.string.no, null);
    return builder.create();
  }
  public static void confirma(FragmentManager fragmentManager) {
    final DialogoConfirmaEliminar dialogo = new DialogoConfirmaEliminar();
    dialogo.show(fragmentManager, "confirma");
  }
}