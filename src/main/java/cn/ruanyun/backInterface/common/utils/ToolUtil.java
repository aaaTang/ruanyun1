package cn.ruanyun.backInterface.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.*;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class ToolUtil extends ValidateUtil {

    public static final int SALT_LENGTH = 6;

    private static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    private static Joiner joiner = Joiner.on(",").skipNulls();

    public ToolUtil() {
    }

    /**
     * 分割字符串
     * @param str
     * @return
     */
    public  static List<String> splitterStr(String str) {
        return SPLITTER.splitToList(str);
    }

    /**
     * 用,号拼接字符串
     * @param ts
     * @return
     */
    public static String joinerList(List<String> ts) {
        return joiner.join(ts);
    }


    /**
     * 获取6位随机数
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    /**
     * list转化空值
     * @param objects
     * @param <T>
     */
    public static<T> List<T> setListToNul(List<T> objects){
        if (objects.size() == 0 || objects.isEmpty()){
            objects = null;
        }
        return objects;
    }
    /**
     * md5 加密
     * @param password
     * @param salt
     * @return
     */
    public static String md5Hex(String password, String salt) {
        return md5Hex(password + salt);
    }

    public static String md5Hex(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            StringBuilder md5StrBuff = new StringBuilder();

            for (byte b : bs) {
                if (Integer.toHexString(255 & b).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & b));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & b));
                }
            }

            return md5StrBuff.toString();
        } catch (Exception var5) {
            throw new RuntimeException();
        }
    }

    /**
     * 移除空白面板
     * @param value
     * @return
     */
    public static String removeWhiteSpace(String value) {
        return isEmpty(value) ? "" : value.replaceAll("\\s*", "");
    }

    /**
     *
     * @param seconds
     * @return
     */
    public static String getCreateTimeBefore(int seconds) {
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis - (long)(seconds * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取 异常信息
     * @param e
     * @return
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();

        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

        }

        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }

    public static String getIP() {
        try {
            StringBuilder IFCONFIG = new StringBuilder();
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        IFCONFIG.append(inetAddress.getHostAddress().toString() + "\n");
                    }
                }
            }

            return IFCONFIG.toString();
        } catch (SocketException var6) {
            var6.printStackTrace();

            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 空的字符串自动过滤掉
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true));
    }

    public static Boolean isWinOs() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }

    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**"
     * 转换为整形数
     * @param val
     * @return
     */
    public static Integer toInt(Object val) {
        if (val instanceof Double) {
            BigDecimal bigDecimal = new BigDecimal((Double)val);
            return bigDecimal.intValue();
        } else {
            return Integer.valueOf(val.toString());
        }
    }


    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static LocalDateTime formatTime(String time, String format) {

        DateTimeFormatter DATETIME19 = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time,DATETIME19);
    }
    /**
     * 判断是不是数字
     * @param obj
     * @return
     */
    public static boolean isNum(Object obj) {
        try {
            Integer.parseInt(obj.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static String getWebRootPath(String filePath) {
        try {
            String path = ToolUtil.class.getClassLoader().getResource("").toURI().getPath();
            path = path.replace("/WEB-INF/classes/", "");
            path = path.replace("/target/classes/", "");
            path = path.replace("file:/", "");
            return isEmpty(filePath) ? path : path + "/" + filePath;
        } catch (URISyntaxException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String getFileSuffix(String fileWholeName) {
        if (isEmpty(fileWholeName)) {
            return "none";
        } else {
            int lastIndexOf = fileWholeName.lastIndexOf(".");
            return fileWholeName.substring(lastIndexOf + 1);
        }
    }


    public static String repaceStr(String str){

        StringBuilder res = new StringBuilder();
        int len = str.length() - 1;
        for(int i = len; i >= 0; i--){
            if(str.charAt(i) == '/'||str.charAt(i) == 't')
                res.append("02%");
            else
                res.append(str.charAt(i));
        }
        return res.reverse().toString();
    }


    /**
     * 把时间根据时、分、秒转换为时间段
     * @param StrDate
     */
    public static String getTimes(String StrDate){
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date=df.parse(StrDate);
            long times = now.getTime()-date.getTime();
            long day  =  times/(24*60*60*1000);
            long hour = (times/(60*60*1000)-day*24);
            long min  = ((times/(60*1000))-day*24*60-hour*60);
            long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

            StringBuilder sb = new StringBuilder();
            //sb.append("发表于：");
            if(hour>0 ){
                sb.append(hour).append("小时前");
            } else if(min>0){
                sb.append(min).append("分钟前");
            } else{
                sb.append(sec).append("秒前");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTimes;
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception ignored){
        }
        return flag;
    }

    /**
     * 验证手机号码
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobileNumber);
        b = m.matches();
        return b;
    }

    /**
     * 将驼峰转下划线
     * @param param
     * @return
     */
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 去掉下划线并将下划线后的首字母转为大写
     * @param str
     * @return
     */
    public static String transformStr(String str){
        //去掉数据库字段的下划线
        if(str.contains("_")) {
            String[] names = str.split("_");
            String firstPart = names[0];
            String otherPart = "";
            for (int i = 1; i < names.length; i++) {
                String word = names[i].replaceFirst(names[i].substring(0, 1), names[i].substring(0, 1).toUpperCase());
                otherPart += word;
            }
            str = firstPart + otherPart;
        }
        return str;
    }

    /**
     * 转换为map
     * @param list
     * @return
     */
    public static List<Map<String,Object>> transformMap(List<Map<String,Object>> list){
        List<Map<String,Object>> resultMapList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            Map<String,Object> tempMap = new HashMap<>();
            for (String s : map.keySet()) {
                tempMap.put(transformStr(s),map.get(s));
            }
            resultMapList.add(tempMap);
        }
        return resultMapList;
    }

    public static String clearHtml(String content,int p) {
        if(null==content) return "";
        if(0==p) return "";

        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(content);
            content = m_script.replaceAll(""); //过滤script标签
            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(content);
            content = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(content);

            content = m_html.replaceAll(""); //过滤html标签
        }catch(Exception e) {
            return "";
        }

        if(content.length()>p){
            content = content.substring(0, p)+"...";
        }else{
            content = content + "...";
        }

        return content;
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String dateType(Object o) {
        return o instanceof Date ? DateUtil.formatDate((Date)o) : o.toString();
    }

    public static String currentTime() {
        return DateUtil.formatDateTime(new Date());
    }
}

