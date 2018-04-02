package br.com.alura.agenda.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;

import br.com.alura.agenda.BuildConfig;
import br.com.alura.agenda.activity.helper.AlunoActivityHelper;
import br.com.alura.agenda.R;
import br.com.alura.agenda.activity.util.RequestCode;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

public class AlunoActivity extends AppCompatActivity {

    private AlunoActivityHelper helper;
    private AlunoDAO alunoDAO;
    private File picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        helper = new AlunoActivityHelper(this);
        alunoDAO = new AlunoDAO(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if (aluno != null) {
            helper.setAluno(aluno);
        }

        final Toolbar toolbar = findViewById(R.id.aluno_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Remove Toolbar Title when expanded
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.aluno_collapsing_toolbar);
        final AppBarLayout appBarLayout = findViewById(R.id.aluno_app_bar);


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

        FloatingActionButton fabCamera = findViewById(R.id.aluno_fab_camera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String picturePath = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                picture = new File(picturePath);

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

            case R.id.menu_aluno_ok:
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

                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    options.setToolbarTitle(getResources().getString(R.string.app_name));
                    options.setShowCropGrid(false);

                    ImageView imageView = findViewById(R.id.aluno_foto);
                    float height = imageView.getMeasuredHeight();
                    float width = imageView.getMeasuredWidth();

                    UCrop.of(Uri.fromFile(picture), Uri.fromFile(picture))
                        .withOptions(options)
                        .withAspectRatio(width, height)
                        .withMaxResultSize(500,500)
                        .start(AlunoActivity.this);

                    break;

                case UCrop.REQUEST_CROP:
                    Uri uri = UCrop.getOutput(data);

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

                    helper.setImage(uri);

                    break;
            }
        }
    }
}
