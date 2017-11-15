package lb.demo.bean;

/**
 * Created by liubo on 2017/11/15.
 */

public class BarChartBean {
    public String time;
    public String level;
    public double value;

    public BarChartBean(String time, String level, double value) {
        this.time = time;
        this.level = level;
        this.value = value;
    }

    public BarChartBean() {
    }
}
