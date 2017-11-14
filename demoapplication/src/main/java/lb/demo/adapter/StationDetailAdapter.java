package lb.demo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.bean.DayData;
import lb.demo.manager.DataTypeManager;
import lb.demo.util.LogUtils;

/**
 * Created by liubo on 2017/9/12.
 */

public class StationDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<DayData> mList;
    private String dataTime;
    private final int SHOW_CONTENT = 102;
    private Handler mHandler;

    public StationDetailAdapter(Context mContext, List<DayData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mHandler = new Handler();

        Date date = new Date();
        dataTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (DemoApplication.debug) {
            dataTime = "2017-08-19";
        }
        LogUtils.lb("dataTime = " + dataTime);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DayData getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final DayData data = mList.get(i);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_station_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.item_stationdata_name = convertView.findViewById(R.id.item_stationdata_name);
            viewHolder.item_stationdata_value = convertView.findViewById(R.id.item_stationdata_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_stationdata_name.setText("" + DataTypeManager.getDescription(data.datatype));
        viewHolder.item_stationdata_value.setText("" + data.avgvalue);

        return convertView;
    }

    class ViewHolder {
        public TextView item_stationdata_name;
        public TextView item_stationdata_value;
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
