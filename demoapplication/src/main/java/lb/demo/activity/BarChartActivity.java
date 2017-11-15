package lb.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

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
                if (i < 3) {
                    continue;
                } else {
                    String str = array.get(i);
                    Hour24Bean hour24Bean = VOUtils.convertString2VO(str, Hour24Bean.class);
                    list.add(hour24Bean);
                }
            }

            LogUtils.lb("BarChartActivity --> list.size() = " + list.size());
            for (Hour24Bean bean : list) {
                if (bean.datatype == dataType) {
                    BarChartActivity.this.list.add(new BarChartBean(bean.date, "优", bean.avgvalue));
                }
            }
            LogUtils.lb("BarChartActivity --> BarChartActivity.this.list.size() = " + BarChartActivity.this.list.size());

        } else {
            LogUtils.lb("BarChartActivity --> value = \r\n" + null);
        }

        barChartAdapter = new BarChartAdapter(this, list, category);
        barchartListview.setAdapter(barChartAdapter);
    }

    public void back(View view) {
        onBackPressed();
        BarChartActivity.this.finish();
    }
}
