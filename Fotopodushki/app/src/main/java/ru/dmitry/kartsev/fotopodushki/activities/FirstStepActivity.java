package ru.dmitry.kartsev.fotopodushki.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import it.sephiroth.android.library.picasso.Picasso;
import ru.dmitry.kartsev.fotopodushki.R;
import ru.dmitry.kartsev.fotopodushki.models.Order;

/**
 * Created by Jag on 29.11.2016.
 */

public class FirstStepActivity extends AppCompatActivity {
    public final String STATE_VALUE_IMG_A = "ru.dmitry.kartsev.fotopodushki.imgDecodableStringA";
    public final String STATE_VALUE_IMG_B = "ru.dmitry.kartsev.fotopodushki.imgDecodableStringB";
    public final String STATE_VALUE_READY = "ru.dmitry.kartsev.fotopodushki.firstStepReady";
    public int MIN_SIZE = 1000;
    boolean ready = false;
    private int RESULT_LOAD_IMG_A = 1;
    private int RESULT_LOAD_IMG_B = 2;
    private static Order order;
    String imgDecodableStringA, imgDecodableStringB;
    private Button nextStepBtn, addPhotoA, addPhotoB;
    private ImageView imagePreviewSideA, imagePreviewSideB, imagePhotoA, imagePhotoB;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);

        initViews();
        setButtonBehavior();

        if (savedInstanceState != null) {
            imgDecodableStringA = savedInstanceState.getString(STATE_VALUE_IMG_A);
            imgDecodableStringB = savedInstanceState.getString(STATE_VALUE_IMG_B);
            ready = savedInstanceState.getBoolean(STATE_VALUE_READY);
            setMiniaturePicture(imagePreviewSideA, imgDecodableStringA);
            setMiniaturePicture(imagePreviewSideB, imgDecodableStringB);
            updateView();
        } else {
            imgDecodableStringB = "";
            imgDecodableStringB = "";
        }
    }

    private void initViews() {
        nextStepBtn = (Button) findViewById(R.id.next_step);
        addPhotoA = (Button) findViewById(R.id.add_foto_1);
        addPhotoB = (Button) findViewById(R.id.add_foto_2);
        imagePreviewSideA = (ImageView) findViewById(R.id.imagePreviewSideA);
        imagePreviewSideB = (ImageView) findViewById(R.id.imagePreviewSideB);
        imagePhotoA = (ImageView) findViewById(R.id.imagePhotoA);
        imagePhotoB = (ImageView) findViewById(R.id.imagePhotoB);
    }

    private void setButtonBehavior() {
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ready) {
                    order.setFilePathA(imgDecodableStringA);
                    order.setFilePathB(imgDecodableStringB);
                    try {
                        SecondStepActivity.openView(FirstStepActivity.this, order);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.err_select_photo, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        addPhotoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG_A);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addPhotoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG_B);
            }
        });
    }

    public static void openView(Context context, Order ord) {
        order = ord;
        Intent intent = new Intent(context, FirstStepActivity.class);
        context.startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if ((requestCode == RESULT_LOAD_IMG_A || requestCode == RESULT_LOAD_IMG_B) && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                cursor.moveToFirst();

                Bitmap bitmap;
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                ImageView currentImageView;
                String temporaryString;

                if (requestCode == RESULT_LOAD_IMG_A) {
                    imgDecodableStringA = temporaryString = cursor.getString(columnIndex);
                    currentImageView = imagePreviewSideA;
                } else {
                    imgDecodableStringB = temporaryString = cursor.getString(columnIndex);
                    currentImageView = imagePreviewSideB;
                }
                bitmap = BitmapFactory
                        .decodeFile(temporaryString);
                cursor.close();
                if (bitmap.getHeight() >= MIN_SIZE && bitmap.getWidth() >= MIN_SIZE) {
                    setMiniaturePicture(currentImageView, temporaryString);
                    ready = true;
                } else {
                    Toast.makeText(this, R.string.err_photo_size,
                            Toast.LENGTH_LONG).show();
                    if (requestCode == RESULT_LOAD_IMG_A) {
                        imgDecodableStringA = "";
                        if (imgDecodableStringB == "") {
                            ready = false;
                        }
                    } else {
                        imgDecodableStringB = "";
                        if (imgDecodableStringA == "") {
                            ready = false;
                        }
                    }
                    currentImageView.setImageResource(R.drawable.question);
                }

            } else {
                Toast.makeText(this, R.string.err_no_photo_selected,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.err_photo_unknown, Toast.LENGTH_LONG)
                    .show();
        }
        updateView();
    }

    private void setMiniaturePicture(ImageView currentImageView, String temporaryString) {
        if((temporaryString.length() > 0) && (currentImageView != null)) {
            try {
                Picasso.with(this.getBaseContext())
                        .load("file:///" + temporaryString)
                        .resize(currentImageView.getMeasuredWidth(), currentImageView.getMeasuredHeight())
                        .centerCrop()
                        .into(currentImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            currentImageView.setImageResource(R.drawable.question);
        }
    }

    private void updateView() {
        if (imgDecodableStringA != "") {
            imagePhotoA.setImageResource(R.drawable.checked);
        } else {
            imagePhotoA.setImageResource(R.drawable.unchecked);
        }

        if (imgDecodableStringB != "") {
            imagePhotoB.setImageResource(R.drawable.checked);
        } else {
            imagePhotoB.setImageResource(R.drawable.unchecked);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_VALUE_IMG_A, imgDecodableStringA);
        outState.putString(STATE_VALUE_IMG_B, imgDecodableStringB);
        outState.putBoolean(STATE_VALUE_READY, ready);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        imgDecodableStringA = savedInstanceState.getString(STATE_VALUE_IMG_A);
        imgDecodableStringB = savedInstanceState.getString(STATE_VALUE_IMG_B);
        ready = savedInstanceState.getBoolean(STATE_VALUE_READY);
        if(imgDecodableStringA != "") {
            setMiniaturePicture(imagePreviewSideA, imgDecodableStringA);
        }
        if(imgDecodableStringB != "") {
            setMiniaturePicture(imagePreviewSideB, imgDecodableStringB);
        }
        updateView();
    }
}
