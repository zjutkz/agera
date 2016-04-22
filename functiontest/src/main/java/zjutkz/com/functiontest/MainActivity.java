package zjutkz.com.functiontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.Functions;
import com.google.android.agera.Predicate;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable{

    private TextView show;

    private Supplier<String> supplier = new Supplier<String>() {
        @NonNull
        @Override
        public String get() {
            return "url";
        }
    };

    private Function<String,List<Integer>> strToList = new Function<String, List<Integer>>() {
        @NonNull
        @Override
        public List<Integer> apply(@NonNull String input) {
            List<Integer> data = new ArrayList<>();
            for(int i = 0;i < 10;i++){
                data.add(i);
            }
            return data;
        }
    };

    private Predicate<Integer> filter = new Predicate<Integer>() {
        @Override
        public boolean apply(@NonNull Integer value) {
            return value > 5;
        }
    };

    private Function<Integer,String> intToStr = new Function<Integer, String>() {
        @NonNull
        @Override
        public String apply(@NonNull Integer input) {
            return String.valueOf(input);
        }
    };

    private Function<List<String>, Integer> getSize = new Function<List<String>, Integer>() {
        @NonNull
        @Override
        public Integer apply(@NonNull List<String> input) {
            return input.size();
        }
    };

    Function<String,Integer> finalFunc = Functions.functionFrom(String.class)
            .unpack(strToList)
            .filter(filter)
            .map(intToStr)
            .thenApply(getSize);

    private Repository<String> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView)findViewById(R.id.show);
    }

    public void function(View view){
        repository = Repositories.repositoryWithInitialValue("default")
                .observe()
                .onUpdatesPerLoop()
                .getFrom(supplier)
                .transform(finalFunc)
                .thenTransform(new Function<Integer, String>() {
                    @NonNull
                    @Override
                    public String apply(@NonNull Integer input) {
                        return String.valueOf(input);
                    }
                })
                .compile();

        repository.addUpdatable(this);
    }

    @Override
    public void update() {
        show.setText(repository.get());
    }
}
