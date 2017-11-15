package lb.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lb.demo.R;
import lb.demo.bean.BarChartBean;

/**
 * Created by liubo on 2017/11/15.
 */

public class BarChartAdapter extends BaseAdapter {
    private Context mContext;
    private List<BarChartBean> list;
    private boolean category;

    public BarChartAdapter(Context mContext, List<BarChartBean> list, boolean category) {
        this.mContext = mContext;
        this.list = list;
        this.category = category;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BarChartBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BarChartBean barChartBean = list.get(position);
        BarChartAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_barchart, null);
            viewHolder = new BarChartAdapter.ViewHolder();
            viewHolder.item_barchart_time = convertView.findViewById(R.id.item_barchart_time);
            viewHolder.item_barchart_level = convertView.findViewById(R.id.item_barchart_level);
            viewHolder.item_barchart_value = convertView.findViewById(R.id.item_barchart_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BarChartAdapter.ViewHolder) convertView.getTag();
        }
        if (category) {
            //24小时
            String[] time = barChartBean.time.split(" ");
            viewHolder.item_barchart_time.setText(time[0] + "\r\n" + time[1]);
        } else {
            //30天
            viewHolder.item_barchart_time.setText(barChartBean.time);
        }
        viewHolder.item_barchart_level.setText("" + barChartBean.level);
        viewHolder.item_barchart_value.setText("" + barChartBean.value);

        return convertView;
    }

    class ViewHolder {
        public TextView item_barchart_time;
        public TextView item_barchart_level;
        public TextView item_barchart_value;
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
            BarChartAdapter.ViewHolder viewHolder = (BarChartAdapter.ViewHolder) convertView.getTag();
            //以下是处理需要处理的控件方法。。。。。
        }
    }
}
