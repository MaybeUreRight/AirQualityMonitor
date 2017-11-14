package lb.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lb.demo.R;
import lb.demo.bean.AllLastDataBean;
import lb.demo.bean.BaseBean;
import lb.demo.bean.Hour24Bean;
import lb.demo.bean.OtherBean;
import lb.demo.listener.OnScrollChangedListenerImp;
import lb.demo.manager.DataTypeManager;
import lb.demo.util.LogUtils;
import lb.demo.view.InterceptScrollContainer;
import lb.demo.view.MyHScrollView;

/**
 * Created by liubo on 2017/9/12.
 */

public class ListDataAdapter extends BaseAdapter {
    private Context mContext;
    private List<AllLastDataBean> mList;
    private View view;

    public ListDataAdapter(Context mContext, List<AllLastDataBean> mList, View view) {
        this.mContext = mContext;
        this.mList = mList;
        this.view = view;
    }

    public void setList(List<AllLastDataBean> mDataList) {
        if (mDataList != null) {
            this.mList.clear();
            this.mList.addAll(mDataList);
        } else {
            this.mList.clear();
            this.mList.addAll(new ArrayList<AllLastDataBean>());
        }
//        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AllLastDataBean getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        AllLastDataBean data = mList.get(i);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.item_fragment_list_station_name = convertView.findViewById(R.id.item_fragment_list_station_name);
            viewHolder.scroollContainter = convertView.findViewById(R.id.scroollContainter);
            viewHolder.horizontalScrollView1 = convertView.findViewById(R.id.horizontalScrollView1);
            viewHolder.item_fragment_list_datatype_1_name = convertView.findViewById(R.id.item_fragment_list_datatype_1_name);
            viewHolder.item_fragment_list_datatype_2_name = convertView.findViewById(R.id.item_fragment_list_datatype_2_name);
            viewHolder.item_fragment_list_datatype_3_name = convertView.findViewById(R.id.item_fragment_list_datatype_3_name);
            viewHolder.item_fragment_list_datatype_4_name = convertView.findViewById(R.id.item_fragment_list_datatype_4_name);
            viewHolder.item_fragment_list_datatype_5_name = convertView.findViewById(R.id.item_fragment_list_datatype_5_name);
            viewHolder.item_fragment_list_datatype_6_name = convertView.findViewById(R.id.item_fragment_list_datatype_6_name);
            viewHolder.item_fragment_list_datatype_7_name = convertView.findViewById(R.id.item_fragment_list_datatype_7_name);
            viewHolder.item_fragment_list_datatype_8_name = convertView.findViewById(R.id.item_fragment_list_datatype_8_name);
            viewHolder.item_fragment_list_datatype_9_name = convertView.findViewById(R.id.item_fragment_list_datatype_9_name);
            viewHolder.item_fragment_list_datatype_10_name = convertView.findViewById(R.id.item_fragment_list_datatype_10_name);
            viewHolder.item_fragment_list_datatype_11_name = convertView.findViewById(R.id.item_fragment_list_datatype_11_name);
            viewHolder.item_fragment_list_datatype_12_name = convertView.findViewById(R.id.item_fragment_list_datatype_12_name);

            viewHolder.item_fragment_list_datatype_1_value = convertView.findViewById(R.id.item_fragment_list_datatype_1_value);
            viewHolder.item_fragment_list_datatype_2_value = convertView.findViewById(R.id.item_fragment_list_datatype_2_value);
            viewHolder.item_fragment_list_datatype_3_value = convertView.findViewById(R.id.item_fragment_list_datatype_3_value);
            viewHolder.item_fragment_list_datatype_4_value = convertView.findViewById(R.id.item_fragment_list_datatype_4_value);
            viewHolder.item_fragment_list_datatype_5_value = convertView.findViewById(R.id.item_fragment_list_datatype_5_value);
            viewHolder.item_fragment_list_datatype_6_value = convertView.findViewById(R.id.item_fragment_list_datatype_6_value);
            viewHolder.item_fragment_list_datatype_7_value = convertView.findViewById(R.id.item_fragment_list_datatype_7_value);
            viewHolder.item_fragment_list_datatype_8_value = convertView.findViewById(R.id.item_fragment_list_datatype_8_value);
            viewHolder.item_fragment_list_datatype_9_value = convertView.findViewById(R.id.item_fragment_list_datatype_9_value);
            viewHolder.item_fragment_list_datatype_10_value = convertView.findViewById(R.id.item_fragment_list_datatype_10_value);
            viewHolder.item_fragment_list_datatype_11_value = convertView.findViewById(R.id.item_fragment_list_datatype_11_value);
            viewHolder.item_fragment_list_datatype_12_value = convertView.findViewById(R.id.item_fragment_list_datatype_12_value);

            setViewGone(viewHolder);

            MyHScrollView mMyHScrollView = view.findViewById(R.id.horizontalScrollView1);

            mMyHScrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(viewHolder.horizontalScrollView1));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item_fragment_list_station_name.setText("" + data.stationName);
        if (data.mList != null) {
            for (int j = 0; j < data.mList.size(); j++) {
                BaseBean bean = data.mList.get(j);
                int datatype;
                float avgvalue;
                if (bean instanceof Hour24Bean) {
                    datatype = ((Hour24Bean) bean).datatype;
                    avgvalue = ((Hour24Bean) bean).avgvalue;
                } else if (bean instanceof OtherBean) {
                    datatype = ((OtherBean) bean).dataType;
                    avgvalue = ((OtherBean) bean).avgValue;
                } else {
                    datatype = -1;
                    avgvalue = 0.0f;
                }
                showData(viewHolder, datatype, avgvalue);
            }
        } else {
            setViewGone(viewHolder);
        }

        return convertView;
    }

    private void showData(ViewHolder viewHolder, int datatype, float avgvalue) {
        switch (datatype) {
            case 1:
                viewHolder.item_fragment_list_datatype_1_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_1_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_1_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_1_value.setVisibility(View.VISIBLE);
                break;
            case 3:
                viewHolder.item_fragment_list_datatype_2_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_2_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_2_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_2_value.setVisibility(View.VISIBLE);
                break;
            case 4:
                viewHolder.item_fragment_list_datatype_3_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_3_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_3_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_3_value.setVisibility(View.VISIBLE);
                break;
            case 5:
                viewHolder.item_fragment_list_datatype_4_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_4_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_4_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_4_value.setVisibility(View.VISIBLE);
                break;
            case 6:
                viewHolder.item_fragment_list_datatype_5_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_5_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_5_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_5_value.setVisibility(View.VISIBLE);
                break;
            case 7:
                viewHolder.item_fragment_list_datatype_6_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_6_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_6_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_6_value.setVisibility(View.VISIBLE);
                break;
            case 9:
                viewHolder.item_fragment_list_datatype_7_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_7_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_7_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_7_value.setVisibility(View.VISIBLE);
                break;
            case 10:
                viewHolder.item_fragment_list_datatype_8_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_8_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_8_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_8_value.setVisibility(View.VISIBLE);
                break;
            case 11:
                viewHolder.item_fragment_list_datatype_9_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_9_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_9_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_9_value.setVisibility(View.VISIBLE);
                break;
            case 12:
                viewHolder.item_fragment_list_datatype_10_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_10_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_10_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_10_value.setVisibility(View.VISIBLE);
                break;
            case 13:
                viewHolder.item_fragment_list_datatype_11_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_11_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_11_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_11_value.setVisibility(View.VISIBLE);
                break;
            case 14:
                viewHolder.item_fragment_list_datatype_12_name.setText("" + DataTypeManager.getDescription(datatype));
                viewHolder.item_fragment_list_datatype_12_value.setText("" + avgvalue);
                viewHolder.item_fragment_list_datatype_12_name.setVisibility(View.VISIBLE);
                viewHolder.item_fragment_list_datatype_12_value.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setViewGone(ViewHolder viewHolder) {
        viewHolder.item_fragment_list_datatype_1_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_2_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_3_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_4_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_5_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_6_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_7_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_8_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_9_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_10_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_11_name.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_12_name.setVisibility(View.GONE);

        viewHolder.item_fragment_list_datatype_1_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_2_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_3_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_4_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_5_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_6_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_7_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_8_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_9_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_10_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_11_value.setVisibility(View.GONE);
        viewHolder.item_fragment_list_datatype_12_value.setVisibility(View.GONE);
    }

    class ViewHolder {
        public TextView item_fragment_list_station_name;
        public InterceptScrollContainer scroollContainter;
        public MyHScrollView horizontalScrollView1;
        public TextView item_fragment_list_datatype_1_name;
        public TextView item_fragment_list_datatype_2_name;
        public TextView item_fragment_list_datatype_3_name;
        public TextView item_fragment_list_datatype_4_name;
        public TextView item_fragment_list_datatype_5_name;
        public TextView item_fragment_list_datatype_6_name;
        public TextView item_fragment_list_datatype_7_name;
        public TextView item_fragment_list_datatype_8_name;
        public TextView item_fragment_list_datatype_9_name;
        public TextView item_fragment_list_datatype_10_name;
        public TextView item_fragment_list_datatype_11_name;
        public TextView item_fragment_list_datatype_12_name;

        public TextView item_fragment_list_datatype_1_value;
        public TextView item_fragment_list_datatype_2_value;
        public TextView item_fragment_list_datatype_3_value;
        public TextView item_fragment_list_datatype_4_value;
        public TextView item_fragment_list_datatype_5_value;
        public TextView item_fragment_list_datatype_6_value;
        public TextView item_fragment_list_datatype_7_value;
        public TextView item_fragment_list_datatype_8_value;
        public TextView item_fragment_list_datatype_9_value;
        public TextView item_fragment_list_datatype_10_value;
        public TextView item_fragment_list_datatype_11_value;
        public TextView item_fragment_list_datatype_12_value;
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
