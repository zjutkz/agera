package zjutkz.com.broadcasttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by kangzhe on 16/4/23.
 */
public class TestActivity extends AppCompatActivity{

    public static final String ACTION = "action";

    private MyFragment1 myFragment1 = new MyFragment1();

    private MyFragment2 myFragment2 = new MyFragment2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container1,myFragment1);
        transaction.add(R.id.container2,myFragment2);
        transaction.commit();
    }

    public void trigger(View view){
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("data","update!!");
        sendBroadcast(intent);
    }
}
