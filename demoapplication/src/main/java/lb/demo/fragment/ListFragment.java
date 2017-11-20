package lb.demo.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lb.demo.DemoApplication;
import lb.demo.R;
import lb.demo.activity.StationDetailActivity;
import lb.demo.adapter.ListDataAdapter;
import lb.demo.bean.AllLastDataBean;
import lb.demo.bean.BaseBean;
import lb.demo.bean.Hour24Bean;
import lb.demo.bean.OtherBean;
import lb.demo.listener.ListViewAndHeadViewTouchLinstener;
import lb.demo.manager.HttpManager;
import lb.demo.util.LogUtils;
import lb.demo.util.TimeUtil;
import lb.demo.util.VOUtils;
import lb.demo.view.CustomDatePicker;
import lb.demo.view.CustomProgressDialog;


/**
 *
 */
public class ListFragment extends Fragment implements View.OnClickListener {
    private final String PRE = "更新时间：";
    private final String SUFFIX = " 00:00:00";

    //一周的毫秒值
    private final long INTERVAL = 1l * 7 * 24 * 60 * 60 * 1000;

    private final int MESSAGE_CURRENT = 101;
    private final int MESSAGE_DAY = 102;
    private final int MESSAGE_WEEK = 103;
    private final int MESSAGE_MONTH = 104;
    private final int MESSAGE_YEAR = 105;
    private final int UPDATE_CURRENT_DATA = 106;

    private Context mContext;

    private TextView list_item_1;
    private TextView list_item_2;
    private TextView list_item_3;
    private TextView list_item_4;
    private TextView list_item_5;
    private TextView list_update_time;
    private TextView list_end_time;//年报数据显示界面，需要添加结束日期
    private Button list_query;
    private View mHeadView;

    private ListView list_listview;
    private ListDataAdapter mAdapter;
    private List<AllLastDataBean> mCurrentDataList;
    private Map<Integer, List<AllLastDataBean>> map;

    private String stationCode;
    private String stationName;

    private int currentPosition;
    private String dateTime, endDate;
    private String currentTime;
    private String lastSelectedDay, lastSelectedWeek, lastSelectedMonth, lastSelectedYear, currentYear;
    private String lastSelectedYearEnd, lastSelectedYearStart;


    private CustomDatePicker customDatePicker1;
    private CustomDatePicker endTimeDatePicker;
    private CustomProgressDialog customProgressDialog;

    private SimpleDateFormat sdf;
    private long lastGetCurrentDataTime;
    private boolean current;

    public ListFragment() {
    }

    private Handler handler;

    private void notifyAdapter(Message msg) {
        List<AllLastDataBean> tempList = (List<AllLastDataBean>) msg.obj;
        switch (msg.what) {
            case MESSAGE_CURRENT:
                map.put(1, tempList);
                if (current) {
                    mCurrentDataList.addAll(tempList);
                    mAdapter.notifyDataSetInvalidated();
                }
                break;
            case MESSAGE_DAY:
                map.put(2, tempList);
                mCurrentDataList.addAll(tempList);
                mAdapter.notifyDataSetInvalidated();
                break;
            case MESSAGE_WEEK:
                map.put(3, tempList);
                mCurrentDataList.addAll(tempList);
                mAdapter.notifyDataSetInvalidated();
                break;
            case MESSAGE_MONTH:
                map.put(4, tempList);
                mCurrentDataList.addAll(tempList);
                mAdapter.notifyDataSetInvalidated();
                break;
            case MESSAGE_YEAR:
                map.put(5, tempList);
                mCurrentDataList.addAll(tempList);
                mAdapter.notifyDataSetInvalidated();
                break;
            case UPDATE_CURRENT_DATA:
                getCurrentData(1);
                handler.sendEmptyMessageDelayed(UPDATE_CURRENT_DATA, 10 * 60 * 1000);
                break;
        }
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        long interval = System.currentTimeMillis() - lastGetCurrentDataTime;
        if (interval > 5 * 60 * 1000) {
            getCurrentData(1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        LogUtils.lb("OnCreateView");
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (mContext == null) {
            mContext = getActivity();
        }
        stationCode = ((StationDetailActivity) getActivity()).s_code;
        stationName = ((StationDetailActivity) getActivity()).s_name;

        list_item_1 = view.findViewById(R.id.list_item_1);
        list_item_2 = view.findViewById(R.id.list_item_2);
        list_item_3 = view.findViewById(R.id.list_item_3);
        list_item_4 = view.findViewById(R.id.list_item_4);
        list_item_5 = view.findViewById(R.id.list_item_5);
        list_update_time = view.findViewById(R.id.list_update_time);
        list_end_time = view.findViewById(R.id.list_end_time);
        list_query = view.findViewById(R.id.list_query);
        list_listview = view.findViewById(R.id.list_listview);
        mHeadView = view.findViewById(R.id.list_head);

        list_listview.addHeaderView(View.inflate(mContext, R.layout.head, null));

        map = new HashMap<>();
        mCurrentDataList = new ArrayList<>();
        mAdapter = new ListDataAdapter(mContext, mCurrentDataList, mHeadView);
        list_listview.setAdapter(mAdapter);

        customProgressDialog = CustomProgressDialog.createDialog(mContext);
        customProgressDialog.setMessage("正在加载");
        customProgressDialog.setCancelable(true);

        list_update_time.setOnClickListener(this);
        list_end_time.setOnClickListener(this);
        list_item_1.setOnClickListener(this);
        list_item_2.setOnClickListener(this);
        list_item_3.setOnClickListener(this);
        list_item_4.setOnClickListener(this);
        list_item_5.setOnClickListener(this);
        list_query.setOnClickListener(this);

        mHeadView.setFocusable(true);
        mHeadView.setClickable(true);
        mHeadView.setBackgroundColor(Color.parseColor("#b2d235"));
        mHeadView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener(mHeadView));
        list_listview.setOnTouchListener(new ListViewAndHeadViewTouchLinstener(mHeadView));

        //初始化时间数据
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = simpleDateFormat.format(date);
        currentTime = dateTime.split(" ")[0];

        if (DemoApplication.debug) {
            dateTime = "2017-08-19" + SUFFIX;
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        endDate = sdf.format(new Date());

        String startDate = sdf.format(date.getTime() - INTERVAL);
//        LogUtils.lb("startDate = " + startDate);
//        LogUtils.lb("endDate = " + endDate);

        list_update_time.setText(PRE + currentTime);

        lastSelectedDay = endDate;
        lastSelectedWeek = endDate.substring(0, 10);
        lastSelectedMonth = endDate.substring(0, 7);
        lastSelectedYear = endDate.substring(0, 4) + "-01-01";
        lastSelectedYearEnd = endDate.substring(0, 10);
        currentYear = endDate.substring(0, 4);

        currentPosition = 1;
        current = true;
        getCurrentData(currentPosition);

        initDatePicker();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                notifyAdapter(msg);
            }
        };

        handler.sendEmptyMessageDelayed(UPDATE_CURRENT_DATA, 10 * 60 * 1000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ssd_back:
                getActivity().onBackPressed();
                break;
            case R.id.list_update_time:
                if (currentPosition == 1) {
                    return;
                } else {
                    // 日期格式为yyyy-MM-dd
                    String temp = list_update_time.getText().toString().replace(PRE, "");
                    if (currentPosition == 4) {
                        temp = temp + "-01";
                    } else if (currentPosition == 5) {
                        temp = temp.replace(LIST_ITEM_5_START_TIME_PRE, "");
                    }
                    customDatePicker1.show(temp);
                }
                break;
            case R.id.list_end_time:
                endTimeDatePicker.show(lastSelectedYearEnd);
                break;
            case R.id.list_item_1:
                current = true;
                if (currentPosition == 1) {
                    return;
                } else {
                    list_end_time.setVisibility(View.GONE);
                    list_query.setVisibility(View.GONE);

                    customDatePicker1.showSpecialDay(true);
                    customDatePicker1.showSpecialMonth(true);
                    list_update_time.setText(PRE + currentTime);

                    changeItemBackground(1);
                    List<AllLastDataBean> tempList1 = map.get(currentPosition);
                    if (tempList1 != null) {
                        mCurrentDataList.clear();
                        mCurrentDataList.addAll(tempList1);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        getCurrentData(currentPosition);
                        handler.removeMessages(MESSAGE_DAY);
                        handler.removeMessages(MESSAGE_WEEK);
                        handler.removeMessages(MESSAGE_MONTH);
                        handler.removeMessages(MESSAGE_YEAR);
                    }
                }
                break;
            case R.id.list_item_2:
                current = false;
                if (currentPosition == 2) {
                    return;
                } else {
                    list_end_time.setVisibility(View.GONE);
                    list_query.setVisibility(View.GONE);

                    customDatePicker1.showSpecialDay(true);
                    customDatePicker1.showSpecialMonth(true);
                    list_update_time.setText(PRE + lastSelectedDay);

                    changeItemBackground(2);
                    List<AllLastDataBean> tempList2 = map.get(currentPosition);
                    if (tempList2 != null) {
                        mCurrentDataList.clear();
                        mCurrentDataList.addAll(tempList2);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        getOtherData(currentPosition, lastSelectedDay + SUFFIX, null, null);
                        handler.removeMessages(MESSAGE_CURRENT);
                        handler.removeMessages(MESSAGE_WEEK);
                        handler.removeMessages(MESSAGE_MONTH);
                        handler.removeMessages(MESSAGE_YEAR);
                    }
                }
                break;
            case R.id.list_item_3:
                current = false;
                if (currentPosition == 3) {
                    return;
                } else {
                    list_end_time.setVisibility(View.GONE);
                    list_query.setVisibility(View.GONE);

                    customDatePicker1.showSpecialDay(true);
                    customDatePicker1.showSpecialMonth(true);
                    list_update_time.setText(PRE + lastSelectedWeek);

                    changeItemBackground(3);
                    List<AllLastDataBean> tempList3 = map.get(currentPosition);
                    if (tempList3 != null) {
                        mCurrentDataList.clear();
                        mCurrentDataList.addAll(tempList3);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        String startDate = ListFragment.this.sdf.format(TimeUtil.getLongFromString(lastSelectedWeek + SUFFIX) - INTERVAL);
                        getOtherData(currentPosition, null, startDate, lastSelectedWeek + SUFFIX);
                        handler.removeMessages(MESSAGE_DAY);
                        handler.removeMessages(MESSAGE_CURRENT);
                        handler.removeMessages(MESSAGE_MONTH);
                        handler.removeMessages(MESSAGE_YEAR);
                    }
                }
                break;
            case R.id.list_item_4:
                current = false;
                if (currentPosition == 4) {
                    return;
                } else {
                    list_end_time.setVisibility(View.GONE);
                    list_query.setVisibility(View.GONE);
                    //只显示年月
                    customDatePicker1.showSpecialDay(false);
                    customDatePicker1.showSpecialMonth(true);
                    list_update_time.setText(PRE + lastSelectedMonth);

                    changeItemBackground(4);
                    List<AllLastDataBean> tempList4 = map.get(currentPosition);
                    if (tempList4 != null) {
                        mCurrentDataList.clear();
                        mCurrentDataList.addAll(tempList4);
                    } else {
                        mAdapter.notifyDataSetChanged();
                        getOtherData(currentPosition, lastSelectedMonth + "-01" + SUFFIX, null, null);
                        handler.removeMessages(MESSAGE_DAY);
                        handler.removeMessages(MESSAGE_WEEK);
                        handler.removeMessages(MESSAGE_CURRENT);
                        handler.removeMessages(MESSAGE_YEAR);
                    }
                }
                break;
            case R.id.list_item_5:
                current = false;
                if (currentPosition == 5) {
                    return;
                } else {
                    list_end_time.setText(LIST_ITEM_5_END_TIME_PRE + lastSelectedYearEnd);
                    list_end_time.setVisibility(View.VISIBLE);
                    list_query.setVisibility(View.VISIBLE);

                    //时间只显示年份
                    customDatePicker1.showSpecialDay(true);
                    customDatePicker1.showSpecialMonth(true);
//                    customDatePicker1.showSpecificTime(false);

                    endTimeDatePicker.showSpecialDay(true);
                    endTimeDatePicker.showSpecialMonth(true);

                    list_update_time.setText(LIST_ITEM_5_START_TIME_PRE + lastSelectedYear);

                    changeItemBackground(5);
                    List<AllLastDataBean> tempList5 = map.get(currentPosition);
                    if (tempList5 != null) {
                        mCurrentDataList.clear();
                        mCurrentDataList.addAll(tempList5);
                        mAdapter.notifyDataSetChanged();
                    } else {
//                        getOtherData(currentPosition, lastSelectedYear + "-01-01" + SUFFIX, null, null);
//                        String startDate = lastSelectedYear + "-01-01" + SUFFIX;
                        String startDate = lastSelectedYear + SUFFIX;
//                        String endDate = "";
//                        if (currentYear.equals(lastSelectedYear)) {
//                            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                        } else {
//                            endDate = lastSelectedYear + "-12-31 23:59:59";
//                        }
                        String endDate = lastSelectedYearEnd + SUFFIX;
                        getOtherData(currentPosition, null, startDate, endDate);
                        handler.removeMessages(MESSAGE_DAY);
                        handler.removeMessages(MESSAGE_WEEK);
                        handler.removeMessages(MESSAGE_CURRENT);
                        handler.removeMessages(MESSAGE_MONTH);
                    }
                }
                break;
            case R.id.list_query:
                String startDate = lastSelectedYear + SUFFIX;
                String endDate = lastSelectedYearEnd + SUFFIX;

                Long startLong = TimeUtil.getLongFromString(startDate);
                Long endLong = TimeUtil.getLongFromString(endDate);
                if (endLong <= startLong) {
                    Toast.makeText(mContext, "请确认开始时间和结束时间", Toast.LENGTH_SHORT).show();
                } else {
                    getOtherData(currentPosition, null, startDate, endDate);
                }
                break;
            default:
                break;
        }
    }

    private final String LIST_ITEM_5_START_TIME_PRE = "开始日期：";
    private final String LIST_ITEM_5_END_TIME_PRE = "结束日期：";

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());

        endTimeDatePicker = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                //年报界面，选择结束日期
                lastSelectedYearEnd = time.split(" ")[0];
                list_end_time.setText(LIST_ITEM_5_END_TIME_PRE + lastSelectedYearEnd);
            }
        }, "2010-01-01 00:00", now);
        endTimeDatePicker.showSpecificTime(false); // 不显示时和分
        endTimeDatePicker.setIsLoop(false); // 不允许循环滚动


        customDatePicker1 = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String selectedTime = time.split(" ")[0];
                //日周月报界面
                dateTime = endDate = selectedTime;

                switch (currentPosition) {
                    case 2:
                        lastSelectedDay = time.split(" ")[0];
                        list_update_time.setText(PRE + lastSelectedDay);
                        getOtherData(2, lastSelectedDay + SUFFIX, null, null);
                        break;
                    case 3:
                        lastSelectedWeek = time.split(" ")[0];
                        list_update_time.setText(PRE + lastSelectedWeek);
                        String startDate = ListFragment.this.sdf.format(TimeUtil.getLongFromString(lastSelectedWeek + SUFFIX) - INTERVAL) + SUFFIX;
                        LogUtils.lb("startDate = " + startDate);
                        getOtherData(3, null, startDate, lastSelectedWeek + SUFFIX);
                        break;
                    case 4:
                        lastSelectedMonth = time.split(" ")[0].substring(0, 7);
                        list_update_time.setText(PRE + lastSelectedMonth);
                        getOtherData(4, endDate, null, null);
                        break;
                    case 5:
                        lastSelectedYear = time.split(" ")[0];
                        list_update_time.setText(LIST_ITEM_5_START_TIME_PRE + lastSelectedYear);
//
//                            String startDateTemp = lastSelectedYear + "-01-01 00:00:00";
//
//                            String endDate = "";
//                            if (currentYear.equals(lastSelectedYear)) {
//                                endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                            } else {
//                                endDate = lastSelectedYear + "-12-31 23:59:59";
//                            }
//                            getOtherData(3, null, startDateTemp, endDate);
                        break;
                    default:
                        break;
                }
//                } else {
//                    //年报界面，选择结束日期
//                    lastSelectedYearEnd = time.split(" ")[0];
//                    list_end_time.setText(LIST_ITEM_5_END_TIME_PRE + lastSelectedYearEnd);
//                }

            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    /**
     * 改变选择后实时/日报/周报/月报的背景
     *
     * @param position 1，2，3，4
     */
    private void changeItemBackground(int position) {
        currentPosition = position;
        list_item_1.setBackgroundResource(R.drawable.list_item_left_unselected_bg);
        list_item_2.setBackgroundResource(R.color.list_item_unselected_bg);
        list_item_3.setBackgroundResource(R.color.list_item_unselected_bg);
        list_item_4.setBackgroundResource(R.color.list_item_unselected_bg);
        list_item_5.setBackgroundResource(R.drawable.list_item_right_unselected_bg);
        switch (position) {
            case 1:
                list_item_1.setBackgroundResource(R.drawable.list_item_left_selected_bg);
                break;
            case 2:
                list_item_2.setBackgroundResource(R.color.list_item_selected_bg);
                break;
            case 3:
                list_item_3.setBackgroundResource(R.color.list_item_selected_bg);
                break;
            case 4:
                list_item_4.setBackgroundResource(R.color.list_item_selected_bg);
                break;
            case 5:
                list_item_5.setBackgroundResource(R.drawable.list_item_right_selected_bg);
                break;
            default:
                break;
        }
    }

    /**
     * 获取实时数据
     */
    private void getCurrentData(final int position) {
        lastGetCurrentDataTime = System.currentTimeMillis();
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (customProgressDialog != null && !customProgressDialog.isShowing()) {
                        customProgressDialog.show();
                    }
                    if (current) {
                        mCurrentDataList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = HttpManager.getAllLastData();
                if (!TextUtils.isEmpty(data)) {
                    ArrayList<String> jsonToArray = VOUtils.getJsonToArray(data);
                    List<AllLastDataBean> tempList = new ArrayList<>();
                    for (String str : jsonToArray) {
                        int index = str.indexOf("dateTime") + "dateTime".length() + 23 + 1;
                        String temp1 = str.substring(0, index).replace("{\"stationData\":[", "").replace("},{", ",");
                        LogUtils.lb("temp1 = \r\n" + temp1);
                        AllLastDataBean dataBean = VOUtils.convertString2VO(temp1, AllLastDataBean.class);
                        List<BaseBean> list = new ArrayList<>();
                        if (str.contains("datatype")) {
                            String temp2 = "[" + str.substring(index + 1, str.length() - 2) + "]";
                            LogUtils.lb("temp2 = \r\n" + temp2);
                            ArrayList<String> toArray = VOUtils.getJsonToArray(temp2);
                            for (String s : toArray) {
                                Hour24Bean hour24Bean = VOUtils.convertString2VO(s, Hour24Bean.class);
                                list.add(hour24Bean);
                            }
                            dataBean.mList = list;
                        }
                        String stationCode = dataBean.stationCode;
                        if (DemoApplication.stationMap != null && DemoApplication.stationMap.get(stationCode) != null) {
                            dataBean.stationName = DemoApplication.stationMap.get(stationCode);
                        }
                        tempList.add(dataBean);

                    }
                    Message message = handler.obtainMessage();
                    message.obj = tempList;
                    message.what = MESSAGE_CURRENT;
                    handler.sendMessageDelayed(message, 200);

                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    /**
     * 获取日周月年报检测数据
     *
     * @param position  2 3 4 5
     * @param dateTime  获取日报需要的日期（yyyy-MM-dd HH:mm:ss）
     * @param startDate 获取周报需要的开始日期
     * @param endDate   获取周报需要的结束日期
     */
    private void getOtherData(final int position, final String dateTime, final String startDate, final String endDate) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCurrentDataList.clear();
                    mAdapter.notifyDataSetChanged();
                    if (customProgressDialog != null && !customProgressDialog.isShowing()) {
                        customProgressDialog.show();
                    }
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data;
                switch (position) {
                    case 2:
                        data = HttpManager.getDayCumuAvgData(dateTime);
                        dealData(data, position, 109, 110);
                        break;
                    case 3:
                        data = HttpManager.getCumuAvgData(startDate, endDate);
                        dealData(data, position, 100, 101);
                        break;
                    case 4:
                        data = HttpManager.getMonthCumuAvgData(dateTime);
                        dealData(data, position, 107, 108);
                        break;
                    case 5:
                        data = HttpManager.getCumuAvgData(startDate, endDate);
                        dealData(data, position, 109, 110);
                        break;
                    default:
                        data = "";
                        break;
                }

            }
        }).start();
    }

    private void dealData(String data, int position, int temp1EndPosition, int temp2StartPosition) {
        if (!TextUtils.isEmpty(data)) {
            ArrayList<String> jsonToArray = VOUtils.getJsonToArray(data);
            List<AllLastDataBean> tempList = new ArrayList<>();
            for (String str : jsonToArray) {
                LogUtils.lb("str = \r\n" + str);
                int index = str.indexOf("dataType");
                if (index > 0) {
//                            String temp1 = str.substring(16, index - 3).replace("},{", ",");
                    String temp1 = str.substring(16, temp1EndPosition).replace("},{", ",");
                    LogUtils.lb("temp1 = \r\n" + temp1);

                    AllLastDataBean dataBean = VOUtils.convertString2VO(temp1, AllLastDataBean.class);
                    List<BaseBean> list = new ArrayList<>();
//                            String temp2 = "[" + str.substring(index - 2).replace("}]}", "}]");
                    String temp2 = "[" + str.substring(temp2StartPosition).replace("}]}", "}]");
                    LogUtils.lb("temp2 = \r\n" + temp2);
                    ArrayList<String> toArray = VOUtils.getJsonToArray(temp2);
                    for (String s : toArray) {
                        OtherBean otherBean = VOUtils.convertString2VO(s, OtherBean.class);
                        list.add(otherBean);
                    }
                    dataBean.mList = list;
                    String stationCode = dataBean.stationCode;
                    if (DemoApplication.stationMap != null && DemoApplication.stationMap.get(stationCode) != null) {
                        dataBean.stationName = DemoApplication.stationMap.get(stationCode);
                    }
                    tempList.add(dataBean);
                } else {
                    String temp1 = str.substring(16).replace("},{", ",").replace("}]}", "}");
                    AllLastDataBean dataBean = VOUtils.convertString2VO(temp1, AllLastDataBean.class);
                    String stationCode = dataBean.stationCode;
                    if (DemoApplication.stationMap != null && DemoApplication.stationMap.get(stationCode) != null) {
                        dataBean.stationName = DemoApplication.stationMap.get(stationCode);
                    }
                    tempList.add(dataBean);
                }
            }
            Message message = handler.obtainMessage();
            message.obj = tempList;
//                    message.what = MESSAGE_CURRENT;
            switch (position) {
                case 2:
                    message.what = MESSAGE_DAY;
                    break;
                case 3:
                    message.what = MESSAGE_WEEK;
                    break;
                case 4:
                    message.what = MESSAGE_MONTH;
                    break;
                case 5:
                    message.what = MESSAGE_YEAR;
                    break;
            }
            handler.sendMessageDelayed(message, 200);

        } else {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
