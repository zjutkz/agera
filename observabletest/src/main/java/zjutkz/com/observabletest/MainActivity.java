package zjutkz.com.observabletest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private TextView trigger;

    private MyTextView customTrigger;

    private Repository<String> repository;

    private int index = 1;

    private int count = 0;


    private Repository<String> observable = Repositories.repositoryWithInitialValue("a")
            .observe()
            .onUpdatesPerLoop()
            .thenGetFrom(new Supplier<String>() {
                @NonNull
                @Override
                public String get() {
                    return "a";
                }
            })
            .compile();

    private Updatable updatable = new Updatable() {
        @Override
        public void update() {
            Log.d("aaa", "update: " + observable.get());
        }
    };
    private Supplier<Integer> supplier = new Supplier<Integer>() {
        @NonNull
        @Override
        public Integer get() {
            return 1;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trigger = (TextView)findViewById(R.id.tv);
        customTrigger = (MyTextView)findViewById(R.id.my_tv);

        //observable.addUpdatable(updatable);
        /*observable = Repositories.repositoryWithInitialValue("a")
                .observe()
                .onUpdatesPerLoop()
                .thenGetFrom(new Supplier<String>() {
                    @NonNull
                    @Override
                    public String get() {
                        return "b";
                    }
                })
                .compile();

        Updatable updatable = new Updatable() {
            @Override
            public void update() {
                Log.d("aaa", "update: ");
            }
        };

        observable.addUpdatable(updatable);*/
    }

    public void observable(View view){
        repository = Repositories.repositoryWithInitialValue("default")
                .observe(observable)
                //.onUpdatesPer(800)
                .onUpdatesPerLoop()
                .goLazy()
                .getFrom(supplier)
                .transform(new Function<Integer, Integer>() {

                    @NonNull
                    @Override
                    public Integer apply(@NonNull Integer input) {
                        return input + 1;
                    }
                })
                .thenTransform(new Function<Integer, String>() {
                    @NonNull
                    @Override
                    public String apply(@NonNull Integer input) {
                        return String.valueOf(input);
                    }
                })
                .compile();

        customTrigger.setRepository(repository);
    }

    public void register(View view){
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                repository.addUpdatable(MainActivity.this);
                Looper.loop();
            }
        }).start();*/

        repository.addUpdatable(MainActivity.this);
        /*customTrigger.postDelayed(new Runnable() {
            @Override
            public void run() {
                repository.removeUpdatable(MainActivity.this);
            }
        },200);*/
        /*customTrigger.postDelayed(new Runnable() {
            @Override
            public void run() {
                //repository.removeUpdatable(MainActivity.this);
                repository.addUpdatable(customTrigger);
            }
        }, 1000);*/

        /*observable.addUpdatable(this);
        customTrigger.postDelayed(new Runnable() {
            @Override
            public void run() {
                observable.addUpdatable(customTrigger);
            }
        },1000);*/
    }

    public void clear(View view){
        repository.removeUpdatable(MainActivity.this);
        //observable.addUpdatable();
    }

    @Override
    public void update() {
        trigger.setText(repository.get());
        repository.removeUpdatable(MainActivity.this);
        //trigger.setText("update!!" + index++);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //repository.removeUpdatable(this);
        //observable.removeUpdatable(this);
    }
}
