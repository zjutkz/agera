package zjutkz.com.broadcasttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.agera.Updatable;
import com.google.android.agera.content.ContentObservables;

/**
 * Created by kangzhe on 16/4/23.
 */
public class MyFragment2 extends Fragment implements Updatable{

    private ContentObservables.BroadcastObservable observable;

    private TextView tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observable = (ContentObservables.BroadcastObservable) ContentObservables.broadcastObservable(this.getActivity(),TestActivity.ACTION);
        observable.addUpdatable(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);

        tv = (TextView)view.findViewById(R.id.fragment_tv);
        return view;
    }

    @Override
    public void update() {
        tv.setText("update!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        observable.removeUpdatable(this);
    }
}
