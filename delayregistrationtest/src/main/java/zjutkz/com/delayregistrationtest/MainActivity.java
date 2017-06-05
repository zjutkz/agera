package zjutkz.com.delayregistrationtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Reservoir;
import com.google.android.agera.Reservoirs;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private static final String UPDATE = "update!!";

    private TextView tv1;
    private MyTextView tv2;

    private Repository<String> repository;

    private Reservoir<String> provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (MyTextView)findViewById(R.id.tv2);

        provider = Reservoirs.reservoir();

        repository = Repositories.repositoryWithInitialValue("default")
                .observe(provider)
                .onUpdatesPerLoop()
                .goLazy()
                .thenAttemptGetFrom(provider).orSkip()
                .compile();

        tv2.setRepo(repository);
    }

    public void trigger(View view){
        repository.addUpdatable(this);

        provider.accept(UPDATE);
    }

    public void delay_register(View view){
        repository.addUpdatable(tv2);

        provider.accept(UPDATE);
    }

    @Override
    public void update() {
        tv1.setText(repository.get());
    }
}
