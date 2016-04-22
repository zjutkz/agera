package zjutkz.com.extensiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private Button observableBtn;
    private TextView show;

    private ClickObservable clickObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        observableBtn = (Button)findViewById(R.id.observable_btn);
        show = (TextView)findViewById(R.id.show);

        clickObservable = new ClickObservable();
        clickObservable.addUpdatable(this);
        observableBtn.setOnClickListener(clickObservable);
    }

    @Override
    public void update() {
        show.setText("update!!");
    }

    public static class ClickObservable extends BaseObservable implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dispatchUpdate();
        }
    }
}
