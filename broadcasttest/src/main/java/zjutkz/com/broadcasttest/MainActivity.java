package zjutkz.com.broadcasttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Updatable;
import com.google.android.agera.content.ContentObservables;

public class MainActivity extends AppCompatActivity implements Updatable{

    private static final String ACTION = "action";

    private TextView trigger;

    private ContentObservables.BroadcastObservable observable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trigger = (TextView)findViewById(R.id.trigger);

        observable = (ContentObservables.BroadcastObservable) ContentObservables.broadcastObservable(this,ACTION);
        observable.addUpdatable(this);
    }


    public void send(View view){
        Intent intent = new Intent();
        intent.setAction(ACTION);
        sendBroadcast(intent);
    }

    @Override
    public void update() {
        trigger.setText("update!!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        observable.removeUpdatable(this);
    }
}
