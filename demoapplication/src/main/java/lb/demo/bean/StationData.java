package lb.demo.bean;


/**
 * Created by liubo on 2017/9/11.
 */

public class StationData{
    public int id;
    public String s_code;
    public String s_name;
    public String s_desc;
    public String s_buildDate;
    public String s_manager;
    public String s_phone;
    public String s_longitude;
    public String s_latitude;
    public String s_regionalcode;
    public String enabled;

    @Override
    public String toString() {
        return "StationData{" + "\r\n" +
                "id=" + id + "\r\n" +
                ", s_code=" + s_code +
                ", s_name='" + s_name + '\'' + "\r\n" +
                ", s_desc='" + s_desc + '\'' + "\r\n" +
                ", s_buildDate='" + s_buildDate + '\'' + "\r\n" +
                ", s_manager='" + s_manager + '\'' + "\r\n" +
                ", s_phone='" + s_phone + '\'' + "\r\n" +
                ", s_longitude='" + s_longitude + '\'' + "\r\n" +
                ", s_latitude='" + s_latitude + '\'' + "\r\n" +
                ", s_regionalcode='" + s_regionalcode + '\'' + "\r\n" +
                ", enabled='" + enabled + '\'' + "\r\n" +
                '}';
    }
}
