package lb.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import lb.demo.R;
import lb.demo.adapter.BarChartAdapter;
import lb.demo.bean.BarChartBean;
import lb.demo.bean.Hour24Bean;
import lb.demo.location.IntentStr;
import lb.demo.manager.DataTypeManager;
import lb.demo.util.LogUtils;
import lb.demo.util.VOUtils;

/**
 * create by liubo at 2017年11月15日21:00:20
 *
 * @desc 需要向该界面传值：站点名称（StationName）；界面数据类型（24小时/30天）；数据种类（dataType）；源数据（value）
 */
public class BarChartActivity extends Activity {
    //TODO 遗留的工作是 柱状图的数据源（修改data）尚未初始化以及绑定

    //    private RelativeLayout bcTitleContainer;
    private TextView bcTitle;
    private TextView bcDesc;
    private BarChart bcBarchart;
    private ListView barchartListview;

    private String stationName;
    private int dataType;
    private String dataCategory;
    private String data;

    private boolean category;//true:24小时；false:30天

    private BarChartAdapter barChartAdapter;
    private List<BarChartBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        initView();
    }

    private void initView() {
        View rootView = findViewById(R.id.barchart_rootview);
//        bcTitleContainer = (RelativeLayout) rootView.findViewById(R.id.bc_title_container);
        bcTitle = (TextView) rootView.findViewById(R.id.bc_title);
        bcDesc = (TextView) rootView.findViewById(R.id.bc_desc);
        bcBarchart = (BarChart) rootView.findViewById(R.id.bc_barchart);
        barchartListview = (ListView) rootView.findViewById(R.id.barchart_listview);


        bcBarchart.getXAxis().setTextColor(Color.WHITE);
        bcBarchart.getAxisLeft().setTextColor(Color.WHITE);
        bcBarchart.getAxisRight().setTextColor(Color.WHITE);

        Intent intent = getIntent();
        dataType = intent.getIntExtra(IntentStr.DATA_TYPE, -1);
        stationName = intent.getStringExtra(IntentStr.S_NAME);
        dataCategory = intent.getStringExtra(IntentStr.DATA_CATEGORY);
        data = intent.getStringExtra(IntentStr.ORIGINAL_DATA);

        bcTitle.setText("" + stationName);
        if (!TextUtils.isEmpty(dataCategory)) {
            if (dataCategory.equals(IntentStr.DATA_CATEGORY_24HOUR)) {
//                temp += "24小时检测数据";
                bcDesc.setText("24小时" + DataTypeManager.getDescription(dataType));
                category = true;
            } else if (dataCategory.equals(IntentStr.DATA_CATEGORY_30DAY)) {
//                temp += "30天检测数据";
                bcDesc.setText("30天" + DataTypeManager.getDescription(dataType));
                category = false;
            }
        }


        View headView = View.inflate(this, R.layout.item_barchart, null);
        ((TextView) headView.findViewById(R.id.item_barchart_time)).setText("时间");
        TextView level = (TextView) headView.findViewById(R.id.item_barchart_level);
        level.setText("污染等级");
        level.setBackground(null);
        ((TextView) headView.findViewById(R.id.item_barchart_value)).setText("" + DataTypeManager.getDescription(dataType));
        barchartListview.addHeaderView(headView);

        list = new ArrayList<>();

        if (!TextUtils.isEmpty(data)) {
            LogUtils.lb("BarChartActivity --> value = \r\n" + data);

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

//            LogUtils.lb("BarChartActivity --> list.size() = " + list.size());
            for (Hour24Bean bean : list) {
                LogUtils.lb("bean.datatype = "+bean.datatype+"     dataType = "+dataType);
                if (bean.datatype == dataType) {
                    BarChartActivity.this.list.add(new BarChartBean(category ? bean.time : bean.date, "优", bean.avgvalue));
                }
            }
//            LogUtils.lb("BarChartActivity --> BarChartActivity.this.list.size() = " + BarChartActivity.this.list.size());

        } else {
//            LogUtils.lb("BarChartActivity --> value = \r\n" + null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("没有数据");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onBackPressed();
                }
            });
            builder.create().show();
        }

        barChartAdapter = new BarChartAdapter(this, list, category);
        barchartListview.setAdapter(barChartAdapter);

        showChart(list);
    }

    private void showChart(List<BarChartBean> list) {
        if (list == null || (list != null && list.size() == 0)) {
            return;
        } else {
            List<BarEntry> entries = new ArrayList<>();
            BarData barData = new BarData();
            for (int i = 0; i < list.size(); i++) {
                BarChartBean barChartBean = list.get(i);
                String time;
                if (category) {//24小时
                    String tempTime = barChartBean.time;
                    time = tempTime.split("  ")[1].split(":")[0];
                } else {//30天
                    String tempDate = barChartBean.time;
                    time = tempDate.split("-")[2];
                }
                entries.add(new BarEntry(Float.valueOf(time), (float) barChartBean.value));
            }
            if (entries.size() > 0) {
                BarDataSet dataSet = new BarDataSet(entries, "" + bcDesc.getText());
                dataSet.setValueTextColor(Color.WHITE);
//                dataSet.setColor(getResources().getColor(R.color.line_1));
                dataSet.setColors(new int[]{R.color.line_1, R.color.line_2
                        , R.color.line_3, R.color.line_4
                        , R.color.line_5, R.color.line_6
                        , R.color.line_7, R.color.line_8
                        , R.color.line_9, R.color.line_10
                        , R.color.line_11, R.color.line_12},BarChartActivity.this);
                barData.addDataSet(dataSet);
            }
            bcBarchart.setData(barData);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bcBarchart.invalidate();
                }
            });
        }
    }

    public void back(View view) {
        onBackPressed();
        BarChartActivity.this.finish();
    }
}
