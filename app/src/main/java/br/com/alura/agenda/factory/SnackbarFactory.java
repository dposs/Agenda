package br.com.alura.agenda.factory;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import br.com.alura.agenda.R;

public class SnackbarFactory {

    public static Snackbar create(Context context, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        return snackbar;
    }
}
