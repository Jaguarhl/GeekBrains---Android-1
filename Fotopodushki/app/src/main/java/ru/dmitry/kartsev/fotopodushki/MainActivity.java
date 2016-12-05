package ru.dmitry.kartsev.fotopodushki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.dmitry.kartsev.fotopodushki.activities.FirstStepActivity;
import ru.dmitry.kartsev.fotopodushki.models.Order;

public class MainActivity extends AppCompatActivity {
    private Button createPillow;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setButtonBehavior();
    }

    private void initViews() {
        createPillow = (Button) findViewById(R.id.btnOrderPillow);
    }

    private void setButtonBehavior() {
        createPillow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = new Order(getApplicationContext());
                FirstStepActivity.openView(MainActivity.this, order);
            }
        });
    }

}
