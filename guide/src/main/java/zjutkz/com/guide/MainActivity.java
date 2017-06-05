package zjutkz.com.guide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private TextView show;

    private Observable observable = new Observable() {
        @Override
        public void addUpdatable(@NonNull Updatable updatable) {
            updatable.update();
        }

        @Override
        public void removeUpdatable(@NonNull Updatable updatable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView)findViewById(R.id.show);
    }

    public void trigger(View view){
        observable.addUpdatable(this);
    }

    @Override
    public void update() {
        show.setText("update!!");
    }
}
