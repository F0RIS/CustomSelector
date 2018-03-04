package customdropdownselector.f0ris.com.customdropdownselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomSelector selector = findViewById(R.id.custom_selector);
        selector.setTitle("Click");

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("Item" + String.valueOf(i + 1));
        }

        selector.setValues(list);
        selector.setActionListener(new CustomSelector.ItemClickAction() {
            @Override
            public void onAction(Object object, int position) {
                Toast.makeText(MainActivity.this, "Selected: " + object + " pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            CustomSelector selector = findViewById(R.id.custom_selector);
            if (selector != null) {
                selector.dispatchTouchEvent(event);
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
