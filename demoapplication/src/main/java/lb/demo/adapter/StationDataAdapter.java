package lb.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.activity.StationDetailActivity;
import lb.demo.bean.StationData;
import lb.demo.location.IntentStr;
import lb.demo.util.LogUtils;
import lb.demo.view.UPMarqueeView;

/**
 * Created by liubo on 2017/9/12.
 */

public class StationDataAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<StationData> mList;
    private String dataTime;

    public StationDataAdapter(Context mContext, ArrayList<StationData> mList) {
        this.mContext = mContext;
        this.mList = mList;

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
    public StationData getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final StationData data = mList.get(i);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stationdata, null);
            viewHolder = new ViewHolder();
            viewHolder.item_stationdata_iv = convertView.findViewById(R.id.item_stationdata_iv);
            viewHolder.item_stationdata_tv = convertView.findViewById(R.id.item_stationdata_tv);
//            viewHolder.ftv = convertView.findViewById(R.id.ftv);
            viewHolder.upview1 = convertView.findViewById(R.id.upview1);
            convertView.setTag(viewHolder);

//            viewHolder.ftv.setTimeout(2, TimeUnit.SECONDS);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_stationdata_tv.setText("" + data.s_name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, StationDetailActivity.class)
                        .putExtra(IntentStr.S_CODE, data.s_code)
                        .putExtra(IntentStr.S_NAME, data.s_name)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                );
            }
        });

        return convertView;
    }

    class ViewHolder {
        public ImageView item_stationdata_iv;
        public TextView item_stationdata_tv;
//        public FadingTextView ftv;
        public UPMarqueeView upview1;
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
//            viewHolder.ftv.setTexts(arr);

            List<String> strList = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                strList.add(arr[i]);
            }

            List<View> viewList = new ArrayList<>();
            setView(strList,viewList);
            viewHolder.upview1.setViews(viewList);
        }
    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private void setView(List<String> data,List<View> views) {
        for (int i = 0; i < data.size(); i = i + 2) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
            //初始化布局的控件
            TextView tv1 =  moreView.findViewById(R.id.tv1);
            TextView tv2 =  moreView.findViewById(R.id.tv2);
            //进行对控件赋值
            tv1.setText(data.get(i).toString().replace("\"",""));
            if (data.size() > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                tv2.setText(data.get(i + 1).toString().replace("\"",""));
            }

            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }
}
