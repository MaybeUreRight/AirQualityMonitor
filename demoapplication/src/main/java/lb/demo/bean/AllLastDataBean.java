package lb.demo.bean;

import java.util.List;

/**
 * Created by liubo on 2017/9/29.
 */

public class AllLastDataBean{
    public String stationCode;
    public String stationName;
    public String dateTime;
    public List<BaseBean> mList;

    public AllLastDataBean(String stationName, List<BaseBean> mList) {
        this.stationName = stationName;
        this.mList = mList;
    }

    public AllLastDataBean(String stationCode, String stationName, String dateTime, List<BaseBean> mList) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.dateTime = dateTime;
        this.mList = mList;
    }
}
