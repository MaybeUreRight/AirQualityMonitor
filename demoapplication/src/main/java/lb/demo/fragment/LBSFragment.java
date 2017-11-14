package lb.demo.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.activity.ChartActivity;
import lb.demo.activity.StationDetailActivity;
import lb.demo.adapter.StationDetailAdapter;
import lb.demo.bean.DayData;
import lb.demo.location.IntentStr;
import lb.demo.manager.HttpManager;
import lb.demo.util.VOUtils;


/**
 *
 */
public class LBSFragment extends Fragment implements View.OnClickListener {

private Context mContext;

    public LBSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
//        LogUtils.lb("onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lbs, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ssd_back:
                getActivity().onBackPressed();
                break;
        }

    }
}
