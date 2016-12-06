package ru.dmitry.kartsev.fotopodushki.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.dmitry.kartsev.fotopodushki.MainActivity;
import ru.dmitry.kartsev.fotopodushki.R;
import ru.dmitry.kartsev.fotopodushki.models.Order;

/**
 * Created by Jag on 06.12.2016.
 */

public class FinalActivity extends AppCompatActivity {
    TextView finalMessage;
    Button close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sent);

        initViews();
        setButtonBehavior();        
    }

    private void setButtonBehavior() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.openView(FinalActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViews() {
        finalMessage = (TextView) findViewById(R.id.textMessage);
        finalMessage.setText(getResources().getString(R.string.order_done));
        close = (Button) findViewById(R.id.btnOK);
    }

    public static void openView(Context context) {
        Intent intent = new Intent(context, FinalActivity.class);
        context.startActivity(intent);
    }
}
