package zjutkz.com.terminaltest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

public class MainActivity extends AppCompatActivity implements Updatable{

    private TextView trigger;

    private Repository<Integer> repository;

    private Repository<Result<Integer>> safeRepository;

    private Receiver<Throwable> errorReceiver = new Receiver<Throwable>() {
        @Override
        public void accept(@NonNull Throwable value) {
            trigger.setText(value.toString());
        }
    };

    private Receiver<Integer> successReceiver = new Receiver<Integer>() {
        @Override
        public void accept(@NonNull Integer value) {
            trigger.setText(String.valueOf(value));
        }
    };

    private Supplier<Integer> strSupplier = new Supplier<Integer>() {
        @NonNull
        @Override
        public Integer get() {
            return 1/0;
        }
    };

    private Supplier<Result<Integer>> saveStrSupplier = new Supplier<Result<Integer>>() {
        @NonNull
        @Override
        public Result<Integer> get() {
            try{
                return Result.success(1/ 0);
            }catch (ArithmeticException e){
                return Result.failure(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trigger = (TextView)findViewById(R.id.trigger);
    }

    public void load(View view){
        repository = Repositories.repositoryWithInitialValue(0)
                .observe()
                .onUpdatesPerLoop()
                .thenGetFrom(strSupplier)
                .compile();

        repository.addUpdatable(this);
    }

    public void safe_load(View view){
        safeRepository = Repositories.repositoryWithInitialValue(Result.<Integer>absent())
                .observe()
                .onUpdatesPerLoop()
                .attemptGetFrom(saveStrSupplier).orEnd(new Function<Throwable, Result<Integer>>() {
                    @NonNull
                    @Override
                    public Result<Integer> apply(@NonNull Throwable input) {
                        return Result.success(2222);
                    }
                })
                .thenTransform(new Function<Integer, Result<Integer>>() {
                    @NonNull
                    @Override
                    public Result<Integer> apply(@NonNull Integer input) {
                        return Result.absentIfNull(input);
                    }
                })
                .compile();

        safeRepository.addUpdatable(this);
    }

    public void clear(View view){
        trigger.setText("wait for trigger...");
        if(repository != null){
            repository.removeUpdatable(this);
            return;
        }

        safeRepository.removeUpdatable(this);
    }

    @Override
    public void update() {
        if(repository != null){
            trigger.setText(String.valueOf(repository.get()));
            return;
        }

        safeRepository.get()
                .ifFailedSendTo(errorReceiver)
                .ifSucceededSendTo(successReceiver);
    }
}
