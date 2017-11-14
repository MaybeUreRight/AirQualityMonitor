package lb.demo.bean;

/**
 * Created by liubo on 2017/9/28.
 */

public class Hour24Bean extends BaseBean{
    public String time;
    public String date;
    public int datatype;
    public int datadevid;
    public float maxvalue;
    public float minvalue;
    public float avgvalue;

    @Override
    public String toString() {
        return "Hour24Bean{" +
                "time='" + time + '\'' +
                ", datatype=" + datatype +
                ", datadevid=" + datadevid +
                ", maxvalue=" + maxvalue +
                ", minvalue=" + minvalue +
                ", avgvalue=" + avgvalue +
                '}';
    }
}
