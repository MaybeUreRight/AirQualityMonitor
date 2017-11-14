package lb.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.adapter.StationDataAdapter;
import lb.demo.bean.DayData;
import lb.demo.bean.StationData;
import lb.demo.location.ConstantValues;
import lb.demo.manager.DataTypeManager;
import lb.demo.manager.HttpManager;
import lb.demo.util.LogUtils;
import lb.demo.util.VOUtils;

public class ShowStationListActivity extends Activity {
    private GridView gridView;
    private StationDataAdapter mAdapter;
    private ArrayList<StationData> mList;
    private long firstTime = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        gridView = findViewById(R.id.main_gv);
        mList = new ArrayList<>();
        mAdapter = new StationDataAdapter(this, mList);
        gridView.setAdapter(mAdapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (year == 2017 && month == 10) {
            getData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowStationListActivity.this);
            builder.setTitle("提示");
            builder.setMessage("请先支付费用");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShowStationListActivity.this.finish();
                        }
                    }, 800);
                }
            });
            builder.create().show();
        }

    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String getStationListString = HttpManager.getStationList();

                ArrayList<String> array = VOUtils.getJsonToArray(getStationListString);
                for (String str : array) {
                    StationData stationData = VOUtils.convertString2VO(str, StationData.class);
                    LogUtils.lb("name = " + stationData.s_name);
                    mList.add(stationData);
                    DemoApplication.stationMap.put(stationData.s_code, stationData.s_name);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });


                String dataTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                if (DemoApplication.debug) {
                    dataTime = "2017-08-19";
                }

                for (int i = 0; i < mList.size(); i++) {
                    StationData stationData = mList.get(i);
//                    String dayDataBystationCode = HttpManager.getDayDataBystationCode(stationData.s_code, dataTime);
                    String getRealTimeData = HttpManager.getRealTimeData(stationData.s_code);
                    if (TextUtils.isEmpty(getRealTimeData) || getRealTimeData.length() <= 1) {
                        return;
                    }

                    ArrayList<String> jsonToArray = VOUtils.getJsonToArray(getRealTimeData);
                    ArrayList<DayData> list = new ArrayList<>();
                    for (int k = 0; k < jsonToArray.size(); k++) {
                        if (k == 0 || k == 1) {
                            continue;
                        } else {
                            String str = jsonToArray.get(k);
                            DayData data = VOUtils.convertString2VO(str, DayData.class);
                            list.add(data);
                        }
                    }
                    LogUtils.lb("list.size() = " + list.size());

                    ArrayList<String> titleList = new ArrayList<>();
//                    String temp = "";
                    for (int j = 0; j < list.size(); j++) {
                        DayData dat = list.get(j);
//                        if (j != 0 && j % 2 == 0) {
//                            temp = temp + "\n" + DataTypeManager.getDescription(dat.datatype) + " : " + dat.avgvalue;
//                            titleList.add(temp);
//                            temp = "";
//                        } else {
//                            temp = DataTypeManager.getDescription(dat.datatype) + " : " + dat.avgvalue;
//                        }
                        titleList.add(DataTypeManager.getDescription(dat.datatype) + " : " + dat.avgvalue);
                    }
                    LogUtils.lb("titleList = " + VOUtils.convertVO2String(titleList));

                    final String[] arr = VOUtils.convertVO2String(titleList).replace("[", "").replace("]", "").split(",");
                    final int m = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.updateSingleRow(gridView, m, arr);
                        }
                    });
                }

            }
        }).start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(ShowStationListActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
