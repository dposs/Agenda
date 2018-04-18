package br.com.alura.agenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import br.com.alura.agenda.BuildConfig;
import br.com.alura.agenda.R;
import br.com.alura.agenda.activity.helper.AlunoActivityHelper;
import br.com.alura.agenda.activity.util.RequestCode;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.util.Image;

public class AlunoActivity extends AppCompatActivity {

    private AlunoActivityHelper helper;
    private AlunoDAO alunoDAO;
    private File picture;

    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        helper = new AlunoActivityHelper(this);
        alunoDAO = new AlunoDAO(this);

        String picturePath = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
        picture = new File(picturePath);

        ivPicture = findViewById(R.id.aluno_foto);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if (aluno != null) {
            helper.setAluno(aluno);
        }

        final CoordinatorLayout layout = findViewById(R.id.aluno_layout);
        final AppBarLayout appBarLayout = findViewById(R.id.aluno_app_bar);
        final Toolbar toolbar = findViewById(R.id.aluno_toolbar);
        final FloatingActionButton fabCamera = findViewById(R.id.aluno_fab_camera);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float range = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

                if (Math.abs(verticalOffset) + range >= appBarLayout.getTotalScrollRange()) {
                    fabCamera.hide(true);
                } else {
                    fabCamera.show(true);
                }
            }
        });

        /* Remove Toolbar Title when expanded
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.aluno_collapsing_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            String title = collapsingToolbarLayout.getTitle().toString();

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });*/

        final TextInputEditText tvTelefone = findViewById(R.id.aluno_telefone);
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "([00]) [00000]-[0000]",
                true,
                tvTelefone,
                null,
                null
        );

        tvTelefone.addTextChangedListener(listener);
        tvTelefone.setOnFocusChangeListener(listener);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentImageCapture.putExtra("return-data", false);
                intentImageCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(AlunoActivity.this, BuildConfig.APPLICATION_ID + ".provider", picture));

                startActivityForResult(intentImageCapture, RequestCode.TAKE_PICTURE_ALUNO);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aluno, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.menu_aluno_salvar:
                Aluno aluno = helper.getAluno();

                if (aluno.getId() == null) {
                    alunoDAO.insert(aluno);
                } else {
                    alunoDAO.update(aluno);
                }

                alunoDAO.close();

                setResult(Activity.RESULT_OK, new Intent().putExtra("aluno", aluno));
                finish();

                break;

            case R.id.menu_aluno_foto:
                Intent intentImagePick = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentImagePick, RequestCode.PICK_PICTURE_ALUNO);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RequestCode.TAKE_PICTURE_ALUNO:

                    /* Rotate the Image

                    bitmap = BitmapFactory.decodeFile(picture.getPath());

                    try {

                        ExifInterface exif = new ExifInterface(picture.getPath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(bitmap, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(bitmap, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(bitmap, 270);
                                break;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */

                    Image.crop(this, ivPicture, Uri.fromFile(picture), Uri.fromFile(picture));
                    break;

                case UCrop.REQUEST_CROP:
                    Uri uriCropedPicture = UCrop.getOutput(data);

                    /* Reduce Picture Size

                    Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());

                    int imageWidth = bitmap.getWidth();
                    int imageHeight = bitmap.getHeight();

                    int newHeight = (imageHeight * 500) / imageWidth;

                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, newHeight, false);

                    try {
                        OutputStream streamPicture = new FileOutputStream(picture);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, streamPicture);
                        streamPicture.flush();
                        streamPicture.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    helper.setImage(uriCropedPicture);

                    break;

                case RequestCode.PICK_PICTURE_ALUNO:

                    if (data != null && data.getData() != null) {
                        Uri pickedImage = data.getData();
                        Image.crop(this, ivPicture, pickedImage, Uri.fromFile(picture));
                    }

                    break;
            }
        }
    }
}
