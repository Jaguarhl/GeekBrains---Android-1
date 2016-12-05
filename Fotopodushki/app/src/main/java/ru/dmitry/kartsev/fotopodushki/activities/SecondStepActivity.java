package ru.dmitry.kartsev.fotopodushki.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ru.dmitry.kartsev.fotopodushki.R;
import ru.dmitry.kartsev.fotopodushki.models.*;

/**
 * Created by Jag on 29.11.2016.
 */

public class SecondStepActivity extends AppCompatActivity {
    public static final int MIN_PHONE_NUM = 7;
    public final int MIN_NAME_SYMB = 3;

    private static Order order;
    private Spinner spinnerMaterial, spinnerSize, spinnerBrushes;
    private Button orderButton;
    private ImageView imgBrushes, imgSize, imgMaterial, imgName, imgEmail, imgPhone, imgComments;
    private EditText inpName, inpEmail, inpPhone, inpComments;
    private TextView price;
    private int lastMaterialSelected;
    private int lastSizeSelected;
    private int lastBrushesSelected;
    public int finalOrderPrice;
    private int materialPriceCoeff[];
    private int sizesPriceCoeff[];
    private int brushesPriceCoeff[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);

        initViews();
        initTextEditBehavior();
        initSpinnerViews();
        setButtonBehavior();

        finalOrderPrice = getBaseContext().getResources().getInteger(R.integer.start_price);
        calculatePrice();
    }

    private void initTextEditBehavior() {
        inpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateViews();
            }
        });

        inpPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateViews();
            }
        });

        inpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateViews();
            }
        });

        inpComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateViews();
            }
        });
    }

    private void updateViews() {
        if (validateName()) {
            imgName.setImageResource(R.drawable.checked);
        } else {
            imgName.setImageResource(R.drawable.unchecked);
        }
        if (validateEmail()) {
            imgEmail.setImageResource(R.drawable.checked);
        } else {
            imgEmail.setImageResource(R.drawable.unchecked);
        }
        if (validatePhone()) {
            imgPhone.setImageResource(R.drawable.checked);
        } else {
            imgPhone.setImageResource((R.drawable.unchecked));
        }
        if (validateComments()) {
            imgComments.setImageResource(R.drawable.checked);
        } else {
            imgComments.setImageResource(R.drawable.unchecked);
        }
    }

    private boolean validate() {
        if (!validateName()) {
            showToastMessage(R.string.err_name_required);
            return false;
        }
        if(!validatePhone()) {
            showToastMessage(R.string.err_phone_required);
            return false;
        }
        if(!validateEmail()) {
            showToastMessage(R.string.err_email_required);
            return false;
        }
        return true;
    }

    private boolean validateName() {
        if (inpName.getText().length() < MIN_NAME_SYMB) { return false; }
        return true;
    }

    private boolean validateEmail() {
        if ((inpEmail.getText().length() < MIN_NAME_SYMB) || (!isEmailValid(inpEmail.getText()))) { return false; }
        return true;
    }

    private boolean validatePhone() {
        if ((inpPhone.getText().length() < MIN_PHONE_NUM) || (!isValidPhoneNumber(inpPhone.getText()))) { return false; }
        return true;
    }

    private boolean validateComments() {
        if (inpComments.getText().length() > 0) {
            return true;
        }
        return false;
    }

    private void calculatePrice() {
        price.setText(String.valueOf(finalOrderPrice) + " " + getBaseContext().getResources().getString(R.string.currency));
    }

    private void setButtonBehavior() {
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    setOrderFields();

                    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    emailIntent .setType("vnd.android.cursor.dir/email");
                    String to[] = {getResources().getString(R.string.email_destination)};
                    emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
                    // the attachment
                    ArrayList<Uri> uris = new ArrayList<Uri>();
                    if(order.isFilePathA()) {
                        File filelocationA = new File(order.getFilePathA());
                        Uri path = Uri.fromFile(filelocationA);
                        uris.add(path);
                    }
                    if(order.isFilePathB()) {
                        File filelocationB = new File(order.getFilePathB());
                        Uri path = Uri.fromFile(filelocationB);
                        uris.add(path);
                    }
                    emailIntent .putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                    emailIntent .putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject) + " " + order.getClientName());
                    emailIntent .putExtra(Intent.EXTRA_TEXT, order.getMail());
                    startActivity(Intent.createChooser(emailIntent , getResources().getString(R.string.email_sending)));
                }
            }
        });
    }

    private void setOrderFields() {
        order.setClientName(inpName.getText().toString());
        order.setClientPhone(inpPhone.getText().toString());
        order.setClientEmail(inpEmail.getText().toString());
        order.setClientComments(inpComments.getText().toString());
        order.setPillowMaterial(spinnerSize.getSelectedItem().toString());
        order.setPillowSize(spinnerSize.getSelectedItem().toString());
        order.setPillowBrushes(spinnerBrushes.getSelectedItem().toString());
        order.setOrderPrice(price.getText().toString());
    }

    private void initViews() {
        spinnerMaterial = (Spinner) findViewById(R.id.chooseMaterial);
        ArrayAdapter<CharSequence> adapterMaterial = ArrayAdapter.createFromResource(this, R.array.material,
                android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(adapterMaterial);
        spinnerSize = (Spinner) findViewById(R.id.chooseSize);
        ArrayAdapter<CharSequence> adapterSizes = ArrayAdapter.createFromResource(this, R.array.sizes,
                android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSizes);
        spinnerBrushes = (Spinner) findViewById(R.id.chooseBrushes);
        ArrayAdapter<CharSequence> adapterBrushes = ArrayAdapter.createFromResource(this, R.array.brushes,
                android.R.layout.simple_spinner_dropdown_item);
        spinnerBrushes.setAdapter(adapterBrushes);
        orderButton = (Button) findViewById(R.id.btnFinalOrder);
        imgBrushes = (ImageView) findViewById(R.id.imageBrushes);
        imgBrushes.setImageResource(R.drawable.checked);
        imgSize = (ImageView) findViewById(R.id.imageSize);
        imgSize.setImageResource(R.drawable.checked);
        imgMaterial = (ImageView) findViewById(R.id.imageMaterial);
        imgMaterial.setImageResource(R.drawable.checked);
        imgName = (ImageView) findViewById(R.id.imageName);
        imgEmail = (ImageView) findViewById(R.id.imageEmail);
        imgPhone = (ImageView) findViewById(R.id.imagePhone);
        imgComments = (ImageView) findViewById(R.id.imageComments);
        inpName = (EditText) findViewById(R.id.inputName);
        inpEmail = (EditText) findViewById(R.id.inputEmail);
        inpPhone = (EditText) findViewById(R.id.inputPhone);
        inpComments = (EditText) findViewById(R.id.inputComments);
        price = (TextView) findViewById(R.id.textPrice);
        lastMaterialSelected = lastSizeSelected = lastBrushesSelected = 0;
        materialPriceCoeff = getResources().getIntArray(R.array.material_price);
        sizesPriceCoeff = getResources().getIntArray(R.array.sizes_price);
        brushesPriceCoeff = getResources().getIntArray(R.array.brushes_price);
    }

    private void initSpinnerViews() {
        spinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != lastMaterialSelected) {
                    finalOrderPrice -= materialPriceCoeff[lastMaterialSelected];
                    lastMaterialSelected = position;
                    finalOrderPrice += materialPriceCoeff[position];
                    calculatePrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != lastSizeSelected) {
                    finalOrderPrice -= sizesPriceCoeff[lastSizeSelected];
                    lastSizeSelected = position;
                    finalOrderPrice += sizesPriceCoeff[position];
                    calculatePrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBrushes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != lastBrushesSelected) {
                    finalOrderPrice -= brushesPriceCoeff[lastBrushesSelected];
                    lastBrushesSelected = position;
                    finalOrderPrice += brushesPriceCoeff[position];
                    calculatePrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static void openView(Context context, Order ord) {
        order = ord;
        Intent intent = new Intent(context, SecondStepActivity.class);
        context.startActivity(intent);
    }

    private void showToastMessage(int msg) {
        Toast message = Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_LONG);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }
}
