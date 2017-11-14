package lb.demo.manager;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.transform.Templates;

import lb.demo.location.ConstantValues;
import lb.demo.util.LogUtils;

/**
 * Created by liubo on 2017/9/11.
 */

public class HttpManager {
    //以后再补

    /**
     * 获取站点列表
     *
     * @return
     */
    public static String getStationList() {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_STATION_LIST;
        String post = post(url, params);
        LogUtils.lb("getStationList = \r\n" + post);
        return post;
    }

    /**
     * 通过站点值获取某站点的信息
     *
     * @param stationCode 站点值
     * @return 站点信息
     */
    public static String getStationInfoByCode(String stationCode) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_STATION_INFO_BY_CODE + ConstantValues.AND + "stationCode=" + stationCode;
        String post = post(url, params);
        LogUtils.lb("getStationInfoByCode = \r\n" + post);
        return post;
    }

    /**
     * 获取设备的最新数据
     *
     * @param devid    设备ID
     * @param dataType 数据类型
     * @param dateTime 时间，格式是2017-12-11
     * @return
     */
    public static String get24HourDataByDevIDAndDataType(String devid, String dataType, String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_24HOUR_DATA_BY_DEVID_AND_DATATYPE
                + ConstantValues.AND + "devid=" + devid
                + ConstantValues.AND + "dataType=" + dataType
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);
        LogUtils.lb("get24HourDataByDevIDAndDataType = \r\n" + post);
        return post;
    }

    /**
     * 获取24小时检测数据
     *
     * @param stationCode
     * @param dateTime
     * @return
     */
    public static String get24HourDataByStationCode(String stationCode, String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_24HOUR_DATA_BY_STATION_CODE
                + ConstantValues.AND + "stationCode=" + stationCode
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);
        if (!TextUtils.isEmpty(post)) {
            String temp = post.substring(post.indexOf("dateTime") + 24);
            if (!TextUtils.isEmpty(temp)) {
                post = "[" + temp;
            } else {
                post = null;
            }
        }
        LogUtils.lb("get24HourDataByStationCode = \r\n" + post);
        return post;
    }

    /**
     * 获取某一天的设备数据
     *
     * @param devid    设备ID
     * @param dataType 数据类型
     * @param dateTime 日期，格式是2017-11-11
     * @return
     */
    public static String getDayDataByDevIDAndDataType(String devid, String dataType, String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_DAEDATA_BY_DEVID_DATATYPE
                + ConstantValues.AND + "devid=" + devid
                + ConstantValues.AND + "dataType=" + dataType
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);
        LogUtils.lb("getDayDataByDevIDAndDataType = \r\n" + post);
        return post;
    }

    /**
     * @param stationCode
     * @param dateTime
     * @return [{"datatype":"1","maxvalue":"2.40","minvalue":"0.00","avgvalue":"0.46"},
     * {"datatype":"2","maxvalue":"358.00","minvalue":"0.00","avgvalue":"206.31"},
     * {"datatype":"6","maxvalue":"80.00","minvalue":"38.00","avgvalue":"60.38"},
     * {"datatype":"7","maxvalue":"70.00","minvalue":"28.00","avgvalue":"46.55"},
     * {"datatype":"8","maxvalue":"2.37","minvalue":"0.00","avgvalue":"0.39"},
     * {"datatype":"9","maxvalue":"37.00","minvalue":"12.10","avgvalue":"20.28"},
     * {"datatype":"10","maxvalue":"36.90","minvalue":"12.19","avgvalue":"19.98"},
     * {"datatype":"11","maxvalue":"541.56","minvalue":"0.19","avgvalue":"41.47"},
     * {"datatype":"12","maxvalue":"3.85","minvalue":"1.00","avgvalue":"2.40"},
     * {"datatype":"13","maxvalue":"2.63","minvalue":"0.01","avgvalue":"1.09"}]
     */
    public static String getDayDataBystationCode(String stationCode, String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_DAYDATA_BY_STATIONCODE
                + ConstantValues.AND + "stationCode=" + stationCode
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);

//        LogUtils.lb("getDayDataBystationCode = \r\n" + post);

        if (!TextUtils.isEmpty(post)) {
            String temp = post.substring(post.indexOf("dateTime") + 24);
            if (!TextUtils.isEmpty(temp)) {
                post = "[" + temp;
            } else {
                post = null;
            }
        }
        LogUtils.lb("getDayDataBystationCode = \r\n" + post);
        return post;
    }

    /**
     * 获取指定时间段的检测数据，比如获取30天检测数据
     *
     * @param stationCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getSomeDayDataByStationCode(String stationCode, String startDate, String endDate) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_SOMEDAYDATA_BY_STATIONCODE
                + ConstantValues.AND + "stationCode=" + stationCode

                + ConstantValues.AND + "startDate=" + startDate
                + ConstantValues.AND + "endDate=" + endDate;
        String post = post(url, params);
        LogUtils.lb("getSomeDayDataByStationCode = \r\n" + post);
        return post;
    }

    /**
     * 获取所有站点的实时数据
     *
     * @return [{"stationData":[{"stationCode":"00001"},
     * {"dateTime":"2017-07-17"},
     * {"datatype":"1","datadevid":"1","maxvalue":"0.30","minvalue":"0.00","avgvalue":"0.14"},
     * {"datatype":"2","datadevid":"1","maxvalue":"354.00","minvalue":"4.00","avgvalue":"179.27"},
     * {"datatype":"6","datadevid":"2","maxvalue":"22.00","minvalue":"22.00","avgvalue":"22.00"},
     * {"datatype":"7","datadevid":"3","maxvalue":"18.00","minvalue":"18.00","avgvalue":"18.00"},
     * {"datatype":"8","datadevid":"4","maxvalue":"3.06","minvalue":"0.01","avgvalue":"0.97"},
     * {"datatype":"9","datadevid":"4","maxvalue":"50.12","minvalue":"27.82","avgvalue":"35.89"},
     * {"datatype":"10","datadevid":"4","maxvalue":"47.91","minvalue":"27.69","avgvalue":"34.92"},
     * {"datatype":"11","datadevid":"6","maxvalue":"13.64","minvalue":"4.52","avgvalue":"8.35"},
     * {"datatype":"12","datadevid":"7","maxvalue":"1.75","minvalue":"1.49","avgvalue":"1.63"},
     * {"datatype":"13","datadevid":"5","maxvalue":"2.59","minvalue":"1.47","avgvalue":"1.96"}]},
     * {"stationData":[{"stationCode":"00002"},{"dateTime":"2017-07-17"}]}]
     */
    public static String getAllLastData() {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_ALL_LAST_DATA;
        String post = post(url, params);
        LogUtils.lb("getAllLastData = \r\n" + post);
        return post;
    }

    /**
     * 获取日报
     *
     * @param dateTime
     * @return [{"stationData":[{"stationCode":"00001"},
     * {"dataType":"1","datadevid":"1","avgValue":"0.46"},
     * {"dataType":"2","datadevid":"1","avgValue":"206.31"},
     * {"dataType":"6","datadevid":"2","avgValue":"60.38"},
     * {"dataType":"7","datadevid":"3","avgValue":"46.55"},
     * {"dataType":"8","datadevid":"4","avgValue":"0.39"},
     * {"dataType":"9","datadevid":"4","avgValue":"20.28"},
     * {"dataType":"10","datadevid":"4","avgValue":"19.98"},
     * {"dataType":"11","datadevid":"6","avgValue":"41.47"},
     * {"dataType":"12","datadevid":"7","avgValue":"2.40"},
     * {"dataType":"13","datadevid":"5","avgValue":"1.09"}]},
     * {"stationData":[{"stationCode":"00002"}]}]
     */
    public static String getDayCumuAvgData(String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_DAY_CUMU_AVG_DATA
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);
        LogUtils.lb("getDayCumuAvgData = \r\n" + post);
        return post;
    }

    /**
     * 获取月报
     *
     * @param dateTime
     * @return [{"stationData":[{"stationCode":"00001"},
     * {"dataType":"1","datadevid":"1","avgValue":"0.41"},
     * {"dataType":"2","datadevid":"1","avgValue":"188.07"},
     * {"dataType":"6","datadevid":"2","avgValue":"49.99"},
     * {"dataType":"7","datadevid":"3","avgValue":"32.18"},
     * {"dataType":"8","datadevid":"4","avgValue":"7.48"},
     * {"dataType":"9","datadevid":"4","avgValue":"48.28"},
     * {"dataType":"10","datadevid":"4","avgValue":"40.92"},
     * {"dataType":"11","datadevid":"6","avgValue":"47.97"},
     * {"dataType":"12","datadevid":"7","avgValue":"2.91"},
     * {"dataType":"13","datadevid":"5","avgValue":"2.51"}]},
     * {"stationData":[{"stationCode":"00002"}]}]
     */
    public static String getMonthCumuAvgData(String dateTime) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_MONTH_CUMU_AVG_DATA
                + ConstantValues.AND + "dateTime=" + dateTime;
        String post = post(url, params);
        LogUtils.lb("getMonthCumuAvgData = \r\n" + post);
        return post;
    }

    /**
     * 获取周报
     *
     * @param startDate
     * @param endDate
     * @return 同日报/月报返回的数据
     */
    public static String getCumuAvgData(String startDate, String endDate) {
        String url = ConstantValues.BASE_URL;
        String params = ConstantValues.GET_CUMU_AVG_DATA
                + ConstantValues.AND + "startDate=" + startDate
                + ConstantValues.AND + "endDate=" + endDate;
        String post = post(url, params);
        LogUtils.lb("getCumuAvgData = \r\n" + post);
        return post;
    }


    /**
     * POST请求数据
     *
     * @param url    地址
     * @param params 参数
     * @return
     */
    public static String post(String url, String params) {
        LogUtils.lb("url = \r\n" + url + "\r\nparams = \r\n" + params);
        String responseBody = null;
        try {
            URL postUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) postUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.connect();

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(params);
            outputStream.flush();
            outputStream.close();

            int respCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == respCode) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String inputLine = null;
                StringBuffer response = new StringBuffer();
                while (null != (inputLine = bufferedReader.readLine())) {
                    response.append(inputLine);
                }

                bufferedReader.close();
                responseBody = response.toString();
            } else {
                LogUtils.lb("respCode = " + respCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

}
