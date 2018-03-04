package customdropdownselector.f0ris.com.customdropdownselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomSelector selector = findViewById(R.id.custom_selector);


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
