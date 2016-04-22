package zjutkz.com.observabletest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

/**
 * Created by kangzhe on 16/4/20.
 */
public class MyTextView extends TextView implements Updatable{

    private Repository<String> repository;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRepository(Repository<String> repository){
        this.repository = repository;
    }

    @Override
    public void update() {
        if(repository != null){
            setText("custom " + repository.get());
            return;
        }

        setText("custom update!!");
    }
}
