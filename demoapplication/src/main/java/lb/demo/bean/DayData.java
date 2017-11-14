package lb.demo.bean;

import android.os.Parcelable;

import lb.demo.manager.DataTypeManager;

/**
 * Created by liubo on 2017/9/27.
 */

public class DayData {
    public int datatype;
    public double maxvalue;
    public double minvalue;
    public double avgvalue;

    @Override
    public String toString() {
        return DataTypeManager.getDescription(datatype) + " ： " + avgvalue;
    }
}
