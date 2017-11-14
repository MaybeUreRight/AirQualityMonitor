package lb.demo.manager;

/**
 * Created by liubo on 2017/9/27.
 */

public class DataTypeManager {
    public static String getDescription(int dataType) {
        String desc;

        switch (dataType) {
            case 1:
                desc = "风速";
                break;
            case 2:
                desc = "风向";
                break;
            case 3:
                desc = "温度";
                break;
            case 4:
                desc = "压强";
                break;
            case 5:
                desc = "湿度";
                break;
            case 6:
//                desc = "颗粒物pm10";
                desc = "PM10";
                break;
            case 7:
//                desc = "颗粒物pm2.5";
                desc = "PM2.5";
                break;
            case 8:
                desc = "一氧化氮";
                break;
            case 9:
                desc = "二氧化氮";
                break;
            case 10:
                desc = "氮氧化物";
                break;
            case 11:
                desc = "臭氧";
                break;
            case 12:
                desc = "一氧化碳";
                break;
            case 13:
                desc = "二氧化碳";
                break;
            case 14:
                desc = "氯化氢";
                break;
            default:
                desc = "";
                break;
        }
        return desc;


    }
}
