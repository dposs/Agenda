package br.com.alura.agenda.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import br.com.alura.agenda.R;

public class Image {

    public static void crop(Context context, ImageView referenceView, Uri source, Uri destination) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle(context.getResources().getString(R.string.app_name));
        options.setShowCropGrid(false);

        float height = referenceView.getMeasuredHeight();
        float width = referenceView.getMeasuredWidth();

        UCrop.of(source, destination)
                .withOptions(options)
                .withAspectRatio(width, height)
                .withMaxResultSize(500,500)
                .start((Activity) context);
    }
}
