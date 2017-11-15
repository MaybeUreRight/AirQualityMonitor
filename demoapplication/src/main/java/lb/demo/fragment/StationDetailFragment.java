package lb.demo.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import lb.demo.activity.LineChartActivity;
import lb.demo.activity.StationDetailActivity;
import lb.demo.adapter.StationDetailAdapter;
import lb.demo.bean.DayData;
import lb.demo.location.IntentStr;
import lb.demo.manager.HttpManager;
import lb.demo.util.VOUtils;


/**
 *
 */
public class StationDetailFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private ImageView ssdBack;
    private TextView ssdStationName;
    private TextView ssdUpdateTime;
    private GridView ssdGridview;
    private TextView ssd_24hour;
    private TextView ssd_30day;

    private List<DayData> mList;
    private StationDetailAdapter mAdapter;

    private String stationCode;
    private String stationName;

    public StationDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_station_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (mContext == null) {
            mContext = getActivity();
        }
        stationCode = ((StationDetailActivity) getActivity()).s_code;
        stationName = ((StationDetailActivity) getActivity()).s_name;

        ssdBack = view.findViewById(R.id.ssd_back);
        ssdStationName = view.findViewById(R.id.ssd_station_name);
        ssdUpdateTime = view.findViewById(R.id.ssd_update_time);
        ssdGridview = view.findViewById(R.id.ssd_gridview);
        ssd_24hour = view.findViewById(R.id.ssd_24hour);
        ssd_30day = view.findViewById(R.id.ssd_30day);

        ssdBack.setOnClickListener(this);
        ssd_24hour.setOnClickListener(this);
        ssd_30day.setOnClickListener(this);

        String stationName = ((StationDetailActivity) getActivity()).s_name;
        if (!TextUtils.isEmpty(stationName)) {
            ssdStationName.setText(stationName);
        }


        mList = new ArrayList<>();
        mAdapter = new StationDetailAdapter(mContext, mList);
        ssdGridview.setAdapter(mAdapter);


        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = simpleDateFormat.format(date);
        if (DemoApplication.debug) {
            dateTime = "2017-08-19";
        }
        ssdUpdateTime.setText(ssdUpdateTime.getText() + dateTime);
//        getDayDataBystationCode(stationCode, dateTime.split(" ")[0]);
        getRealTimeData(stationCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ssd_back:
                getActivity().onBackPressed();
                break;
            case R.id.ssd_24hour:
                startActivity(new Intent(mContext, LineChartActivity.class)
                        .putExtra(IntentStr.S_CODE, stationCode)
                        .putExtra(IntentStr.S_NAME, stationName)
                        .putExtra(IntentStr.DATA_CATEGORY, IntentStr.DATA_CATEGORY_24HOUR)
                );
                break;
            case R.id.ssd_30day:
                startActivity(new Intent(mContext, LineChartActivity.class)
                        .putExtra(IntentStr.S_CODE, stationCode)
                        .putExtra(IntentStr.S_NAME, stationName)
                        .putExtra(IntentStr.DATA_CATEGORY, IntentStr.DATA_CATEGORY_30DAY)
                );
                break;
        }
    }

    private void getRealTimeData(final String stationCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = HttpManager.getRealTimeData(stationCode);
                if (mList.size() > 0) {
                    mList.clear();
                }
                ArrayList<String> array = VOUtils.getJsonToArray(data);
                for (int i = 0; i < array.size(); i++) {
                    if (i == 0 || i == 1) {
                        continue;
                    } else {
                        String str = array.get(i);
                        DayData dat = VOUtils.convertString2VO(str, DayData.class);
                        mList.add(dat);
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    private void getDayDataBystationCode(final String stationCode, final String dateTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = HttpManager.getDayDataBystationCode(stationCode, dateTime);
                if (mList.size() > 0) {
                    mList.clear();
                }
                ArrayList<String> array = VOUtils.getJsonToArray(data);
                for (String str : array) {
                    DayData dat = VOUtils.convertString2VO(str, DayData.class);
                    mList.add(dat);
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
}
