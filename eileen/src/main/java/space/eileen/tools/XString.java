package space.eileen.tools;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eileen on 2017/7/3.
 */

public class XString {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static java.text.SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static java.text.SimpleDateFormat miniFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    /**
     * 查看是否是手机号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNO(String phoneNum) {
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = pattern.matcher(phoneNum.trim().toString());
        return matcher.matches();
    }

    /**
     * 从long值转换到日期格式
     *
     * @param dateLong
     * @return
     */
    public static String toDateForLong(long dateLong) {
        return dateFormat.format(new Date(dateLong));
    }

    /**
     * 从long值转换到日期加分钟格式
     *
     * @param dateLong
     * @return
     */
    public static String toMiniForLong(long dateLong) {
        return miniFormat.format(new Date(dateLong));
    }

    /**
     * 将数值转换为分钟、秒、小时响应的字符串
     *
     * @param num
     * @return
     */
    public static String convertText(int num) {
        return (num > 9 ? num + "" : "0" + num);
    }

    /**
     * 判断是否为将来时间
     *
     * @param timeString
     * @return
     * @throws ParseException
     */
    public static boolean isFutureTime(String timeString) throws ParseException {
        Date date = timeFormat.parse(timeString);
        return isFutureTime(date);
    }

    /**
     * 判断是否为将来时间
     *
     * @param date
     * @return
     */
    public static boolean isFutureTime(Date date) {
        Date date1 = new Date();
        if (date.getTime() > date1.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static String convertToMoney(int money) {
        if (money >= 10000) {
            double num = (double) money / (double) 10000;
            BigDecimal bigDecimal = new BigDecimal(num);
            double num2 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            return num2 + "万";
        }
        return money + "";
    }

    public static double convertToKM(double countJourney) {
        double km = countJourney / (double) 1000;
        BigDecimal bigDecimal = new BigDecimal(km);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String convertToTime(int value) {
        int a = value / 60;
        int b = value / (60 * 24);
        if (a == 0) return value + "分钟";
        if (b == 0) return a + "小时" + value % 60 + "分钟";
        if (b > 0)
            return b + "天" + (value % (60 * 24)) / 60 + "小时" + (value % (60 * 24)) % 60 + "分钟";
        return null;
    }


    public static int calcMines(String fromDate,String toDate){
        long from = 0  , to=0;
        try {
            from = miniFormat.parse(fromDate).getTime();
            to = miniFormat.parse(toDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int minutes = (int) ((to - from)/(1000 * 60));
        return minutes;
    }
}
