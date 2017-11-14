package lb.demo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.activity.ChartActivity;
import lb.demo.bean.DayData;
import lb.demo.manager.DataTypeManager;
import lb.demo.util.LogUtils;

/**
 * Created by liubo on 2017/9/12.
 */

public class ChartAdapter extends BaseAdapter {
    private Context mContext;
    private String dataTime;
    private LineChart lineChart;
    private LineData lineData;

    private String[] categoriers;

    public ChartAdapter(Context mContext, LineChart chart, LineData lineData) {
        this.mContext = mContext;
        this.lineChart = chart;
        this.lineData = lineData;

        Date date = new Date();
        dataTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (DemoApplication.debug) {
            dataTime = "2017-08-19";
        }
        LogUtils.lb("dataTime = " + dataTime);

        categoriers = new String[]{"风速"
//                , "风向",
                , "温度", "压强", "湿度"
                , "PM10", "PM2.5"
//                , "一氧化氮"
                , "二氧化氮", "氮氧化物", "臭氧", "一氧化碳", "二氧化硫", "氯化氢"
        };
    }

    @Override
    public int getCount() {
        return categoriers.length;
    }

    @Override
    public String getItem(int i) {
        return categoriers[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        String data = categoriers[i];
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chart, null);
            viewHolder = new ViewHolder();
            viewHolder.item_chart_checkbox = convertView.findViewById(R.id.item_chart_checkbox);
            viewHolder.item_chart_textview = convertView.findViewById(R.id.item_chart_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_chart_textview.setText("" + data);
        viewHolder.item_chart_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ILineDataSet dataSetByIndex = lineData.getDataSetByIndex(i);
                if (dataSetByIndex != null) {
                    if (b) {
                        dataSetByIndex.setVisible(true);
                    } else {
                        dataSetByIndex.setVisible(false);
                    }
                }

                ((ChartActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineChart.invalidate();
                    }
                });
            }
        });

        return convertView;
    }

    class ViewHolder {
        public CheckBox item_chart_checkbox;
        public TextView item_chart_textview;
    }

    /**
     * 局部刷新
     *
     * @param mListView
     * @param position
     * @param arr
     */
    public void updateSingleRow(AbsListView mListView, int position, String[] arr) {
        if (mListView != null) {
            //获取第一个显示的item
            int visiblePosition = mListView.getFirstVisiblePosition();
            //计算出当前选中的position和第一个的差，也就是当前在屏幕中的item位置
            int offset = position - visiblePosition;
            int lenth = mListView.getChildCount();
            // 只有在可见区域才更新,因为不在可见区域得不到Tag,会出现空指针,所以这是必须有的一个步骤
            if ((offset < 0) || (offset >= lenth)) {
                return;
            }
            View convertView = mListView.getChildAt(offset);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            //以下是处理需要处理的控件方法。。。。。
        }
    }
}
