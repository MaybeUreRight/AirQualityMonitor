package lb.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lb.demo.R;
import lb.demo.adapter.LineChartAdapter;
import lb.demo.bean.Hour24Bean;
import lb.demo.location.IntentStr;
import lb.demo.manager.HttpManager;
import lb.demo.myinterface.Jump2BarChartActivityInterface;
import lb.demo.util.LogUtils;
import lb.demo.util.VOUtils;
import lb.demo.view.CustomProgressDialog;

/**
 * 折线图界面（24小时/30天监测数据的展示）
 * Created by liubo on 2017/9/28.
 */

public class LineChartActivity extends Activity implements View.OnClickListener, Jump2BarChartActivityInterface {

    private ImageView chartBack;
    private TextView chartStationName;
    private TextView chartUpdateTime;
    private LineChart chart;
    private GridView chartGridview;
    private LineChartAdapter mAdapter;

    private String stationCode;
    private String stationName;
    private String dataCategory;

    private String currentTime;
    private String monthBeforeTime;

    private LineData lineData;
    private CustomProgressDialog customProgressDialog;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        init();
    }

    private void init() {
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = format.format(currentDate);
//        monthBeforeTime = format.format(new Date(currentDate.getYear(), currentDate.getMonth() - 1, currentDate.getDate()));

        long temp2 = currentDate.getTime() - 1L * 30 * 24 * 60 * 60 * 1000;
        monthBeforeTime = format.format(new Date(temp2));

        LogUtils.lb("currentTime = " + currentTime + "\r\nmonthBeforeTime = " + monthBeforeTime);

        Intent intent = getIntent();
        stationCode = intent.getStringExtra(IntentStr.S_CODE);
        stationName = intent.getStringExtra(IntentStr.S_NAME);
        dataCategory = intent.getStringExtra(IntentStr.DATA_CATEGORY);

        customProgressDialog = CustomProgressDialog.createDialog(this);
        customProgressDialog.setMessage("正在加载");
        customProgressDialog.setCancelable(true);

        String temp = "";
        if (!TextUtils.isEmpty(dataCategory)) {
            if (dataCategory.equals(IntentStr.DATA_CATEGORY_24HOUR)) {
                temp += "24小时检测数据";
                get24HourData(currentTime);
            } else if (dataCategory.equals(IntentStr.DATA_CATEGORY_30DAY)) {
                temp += "30天检测数据";
                get30DayData(monthBeforeTime, currentTime);
            } else {

            }
        } else {
            //
        }

        chartBack = findViewById(R.id.chart_back);
        chartStationName = findViewById(R.id.chart_station_name);
        chartUpdateTime = findViewById(R.id.chart_update_time);
        chart = findViewById(R.id.chart);
        chartGridview = findViewById(R.id.chart_gridview);

        chartStationName.setText(stationName + temp);

        lineData = new LineData();
        mAdapter = new LineChartAdapter(this, chart, lineData);
        chartGridview.setAdapter(mAdapter);

        chartBack.setOnClickListener(this);
    }

    private void get30DayData(final String startDate, final String endDate) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = HttpManager.getSomeDayDataByStationCode(stationCode, startDate, endDate);
                if (!TextUtils.isEmpty(data)) {
                    List<Hour24Bean> list = new ArrayList<>();
                    ArrayList<String> array = VOUtils.getJsonToArray(data);
                    for (int i = 0; i < array.size(); i++) {
                        if (i == 0 || i == 1 || i == 2) {
                            continue;
                        } else {
                            String str = array.get(i);
                            Hour24Bean hour24Bean = VOUtils.convertString2VO(str, Hour24Bean.class);
                            list.add(hour24Bean);
                        }
                    }
                    showChart(list, true);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                            }
                            Toast.makeText(LineChartActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取24小时检测数据
     */
    private void get24HourData(final String currentTime) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = HttpManager.get24HourDataByStationCode(stationCode, currentTime);
//                data = HttpManager.get24HourDataByStationCode(stationCode, "2017-11-14 00:00:00");
                if (!TextUtils.isEmpty(data)) {
                    List<Hour24Bean> list = new ArrayList<>();
                    ArrayList<String> array = VOUtils.getJsonToArray(data);
                    for (int i = 0; i < array.size(); i++) {
                        if (i < 2) {
                            continue;
                        } else {
                            String str = array.get(i);
                            Hour24Bean hour24Bean = VOUtils.convertString2VO(str, Hour24Bean.class);
                            list.add(hour24Bean);
                        }
                    }
                    showChart(list, false);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                            }
                            Toast.makeText(LineChartActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 将数据显示到折线图中
     *
     * @param list
     * @param flag true:30天；false:24小时
     */
    private void showChart(List<Hour24Bean> list, boolean flag) {
        List<Entry> entries1 = new ArrayList<>();
//                    List<Entry> entries2= new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();
        List<Entry> entries5 = new ArrayList<>();
        List<Entry> entries6 = new ArrayList<>();
        List<Entry> entries7 = new ArrayList<>();
//                    List<Entry> entries8 = new ArrayList<>();
        List<Entry> entries9 = new ArrayList<>();
        List<Entry> entries10 = new ArrayList<>();
        List<Entry> entries11 = new ArrayList<>();
        List<Entry> entries12 = new ArrayList<>();
        List<Entry> entries13 = new ArrayList<>();
        List<Entry> entries14 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Hour24Bean hour24Bean = list.get(i);
            String time;
            if (!flag) {
                time = hour24Bean.time.substring(12, 14);
            } else {
//                time = hour24Bean.date.substring(8);
                time = "" + i;
            }
            switch (hour24Bean.datatype) {
                case 1:
                    entries1.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
//              case 2:
//                  entries2.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
//              break;
                case 3:
                    entries3.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 4:
                    entries4.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 5:
                    entries5.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 6:
                    entries6.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 7:
                    entries7.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
//              case 8:
//                  entries8.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
//              break;
                case 9:
                    entries9.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 10:
                    entries10.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 11:
                    entries11.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 12:
                    entries12.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 13:
                    entries13.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                case 14:
                    entries14.add(new Entry(Integer.valueOf(time), hour24Bean.avgvalue));
                    break;
                default:
                    break;
            }

        }
        if (entries1.size() > 0) {
            LineDataSet dataSet1 = new LineDataSet(entries1, "风速"); // add entries to dataset
            dataSet1.setColor(getResources().getColor(R.color.line_1));
            lineData.addDataSet(dataSet1);
        }
        //LineDataSet dataSet2 = new LineDataSet(entries2, "风向"); // add entries to dataset
        if (entries3.size() > 0) {
            LineDataSet dataSet3 = new LineDataSet(entries3, "温度"); // add entries to dataset
            dataSet3.setColor(getResources().getColor(R.color.line_2));
            lineData.addDataSet(dataSet3);

        }
        if (entries4.size() > 0) {
            LineDataSet dataSet4 = new LineDataSet(entries4, "压强"); // add entries to dataset
            dataSet4.setColor(getResources().getColor(R.color.line_3));
            lineData.addDataSet(dataSet4);

        }
        if (entries5.size() > 0) {
            LineDataSet dataSet5 = new LineDataSet(entries5, "湿度"); // add entries to dataset
            dataSet5.setColor(getResources().getColor(R.color.line_4));
            lineData.addDataSet(dataSet5);
        }
        if (entries6.size() > 0) {
            LineDataSet dataSet6 = new LineDataSet(entries6, "PM10"); // add entries to dataset
            dataSet6.setColor(getResources().getColor(R.color.line_5));
            lineData.addDataSet(dataSet6);
        }
        if (entries7.size() > 0) {
            LineDataSet dataSet7 = new LineDataSet(entries7, "PM2.5"); // add entries to dataset
            dataSet7.setColor(getResources().getColor(R.color.line_6));
            lineData.addDataSet(dataSet7);

        }
        //LineDataSet dataSet8 = new LineDataSet(entries8, "一氧化氮"); // add entries to dataset

        if (entries9.size() > 0) {
            LineDataSet dataSet9 = new LineDataSet(entries9, "二氧化氮"); // add entries to dataset
            dataSet9.setColor(getResources().getColor(R.color.line_7));
            lineData.addDataSet(dataSet9);

        }
        if (entries10.size() > 0) {
            LineDataSet dataSet10 = new LineDataSet(entries10, "氮氧化物"); // add entries to dataset
            dataSet10.setColor(getResources().getColor(R.color.line_8));
            lineData.addDataSet(dataSet10);

        }
        if (entries11.size() > 0) {
            LineDataSet dataSet11 = new LineDataSet(entries11, "臭氧"); // add entries to dataset
            dataSet11.setColor(getResources().getColor(R.color.line_9));
            lineData.addDataSet(dataSet11);

        }
        if (entries12.size() > 0) {
            LineDataSet dataSet12 = new LineDataSet(entries12, "一氧化碳"); // add entries to dataset
            dataSet12.setColor(getResources().getColor(R.color.line_10));
            lineData.addDataSet(dataSet12);

        }
        if (entries13.size() > 0) {
            LineDataSet dataSet13 = new LineDataSet(entries13, "二氧化硫"); // add entries to dataset
            dataSet13.setColor(getResources().getColor(R.color.line_11));
            lineData.addDataSet(dataSet13);
        }
        if (entries14.size() > 0) {
            LineDataSet dataSet14 = new LineDataSet(entries14, "氯化氢"); // add entries to dataset
            dataSet14.setColor(getResources().getColor(R.color.line_12));
            lineData.addDataSet(dataSet14);

        }
        chart.setData(lineData);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chart.invalidate(); // refresh

                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_back:
                onBackPressed();
                break;
        }

    }

    /**
     * 将字符串格式的时间转为毫秒值
     *
     * @param dateTime 字符串时间（格式：2015-12-31 23:59:53）
     * @return
     */
    public static Long getLongFromString(String dateTime) {
        if (TextUtils.isEmpty(dateTime)) {
            return null;
        } else {
            try {
                /**
                 * 将字符串数据转化为毫秒数
                 */
                StringBuffer buffer = new StringBuffer();
                char[] charArray = dateTime.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    if (Character.isDigit(charArray[i])) {
                        buffer.append(charArray[i]);
                    }
                }
                dateTime = buffer.toString();
                buffer = null;
                Calendar c = Calendar.getInstance();
                c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(dateTime));
                return c.getTimeInMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void Jump2BarChartActivity(int index) {
        startActivity(new Intent(this, BarChartActivity.class)
                .putExtra(IntentStr.DATA_CATEGORY, dataCategory)
                .putExtra(IntentStr.DATA_TYPE, index)
                .putExtra(IntentStr.S_NAME, stationName)
                .putExtra(IntentStr.ORIGINAL_DATA, data)
        );

    }
}
