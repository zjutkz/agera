package zjutkz.com.delayregistrationtest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

/**
 * Created by kangzhe on 16/4/23.
 */
public class MyTextView extends TextView implements Updatable{

    private Repository<String> repository;

    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRepo(Repository<String> repo){
        repository = repo;
    }

    @Override
    public void update() {
        setText(repository.get());
    }
}
