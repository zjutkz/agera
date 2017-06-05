package zjutkz.com.lazyloadtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private TextView trigger;

    private Repository<String> repository;

    private Supplier<String> strSupplier = new Supplier<String>() {
        @NonNull
        @Override
        public String get() {
            return "value";
        }
    };

    private Function<String,String> transform = new Function<String, String>() {
        @NonNull
        @Override
        public String apply(@NonNull String input) {
            return "new " + input;
        }
    };

    private Supplier<Integer> integerSupplier = new Supplier<Integer>() {
        @NonNull
        @Override
        public Integer get() {
            return 100;
        }
    };

    private Merger<String,Integer,String> merger = new Merger<String, Integer, String>() {
        @NonNull
        @Override
        public String merge(@NonNull String s, @NonNull Integer integer) {
            return s + " plus " + String.valueOf(integer);
        }
    };

    private Updatable updatable = new Updatable() {
        @Override
        public void update() {
            Log.d("TAG", repository.get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trigger = (TextView)findViewById(R.id.trigger);
    }

    public void load(View view){
        repository = Repositories.repositoryWithInitialValue("default")
                .observe()
                .onUpdatesPerLoop()
                .getFrom(strSupplier)
                .transform(transform)
                .thenMergeIn(integerSupplier,merger)
                .compile();

        repository.addUpdatable(updatable);
    }

    //This goLazy means data processing flow will suspend,and when you call repository.get(),it will be resumed.
    public void lazy_load(View view){
        repository = Repositories.repositoryWithInitialValue("default")
                .observe()
                .onUpdatesPerLoop()
                .goLazy()
                .thenGetFrom(strSupplier)
                .compile();

        repository.addUpdatable(this);
    }

    public void clear(View view){
        trigger.setText("wait for trigger...");

        repository.removeUpdatable(this);
    }

    @Override
    public void update() {
        trigger.setText(repository.get());
    }
}
