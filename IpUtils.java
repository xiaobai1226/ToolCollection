import java.util.regex.Pattern;

/**
 * IP工具类
 *
 * @author Felix Pai          
 * @date 2019/01/15  16:49
 */
public class IpUtils {
    /**
     * 功能：判断IPv4地址的正则表达式：
     */
    private static final Pattern IPV4_REGEX = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    /**
     * /功能：判断标准IPv6地址的正则表达式
     */
    private static final Pattern IPV6_STD_REGEX = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    /**
     * 功能：判断一般情况压缩的IPv6正则表达式
     */
    private static final Pattern IPV6_COMPRESS_REGEX = Pattern.compile(
            "^((?:[0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4})*)?)::((?:([0-9A-Fa-f]{1,4}:)*[0-9A-Fa-f]{1,4})?)$");

    /**
     * 功能：抽取特殊的边界压缩情况
     * <p>
     * 由于IPv6压缩规则是必须要大于等于2个全0块才能压缩
     * 不合法压缩 ： fe80:0000:8030:49ec:1fc6:57fa:ab52:fe69
     * fe80::8030:49ec:1fc6:57fa:ab52:fe69
     * 该不合法压缩地址直接压缩了处于第二个块的单独的一个全0块，
     * 上述不合法地址不能通过一般情况的压缩正则表达式IPV6_COMPRESS_REGEX判断出其不合法
     * 所以定义了如下专用于判断边界特殊压缩的正则表达式
     * (边界特殊压缩：开头或末尾为两个全0块，该压缩由于处于边界，且只压缩了2个全0块，不会导致':'数量变少)
     */
    private static final Pattern IPV6_COMPRESS_REGEX_BORDER = Pattern.compile(
            "^(::(?:[0-9A-Fa-f]{1,4})(?::[0-9A-Fa-f]{1,4}){5})|((?:[0-9A-Fa-f]{1,4})(?::[0-9A-Fa-f]{1,4}){5}::)$");

    /**
     * 将整数形式的ip地址转换为字符串形式
     *
     * @param ipLong 数字形式的ip地址
     * @return 字符串形式的ip地址
     */
    public static String longToIp(Long ipLong) {
        StringBuffer ip = new StringBuffer();
        for (int i = 3; i >= 0; i--) {
            ip.insert(0, (ipLong & 0xff));
            if (i != 0) {
                ip.insert(0, ".");
            }
            ipLong = ipLong >> 8;
        }

        return ip.toString();
    }

    /**
     * 判断是否为合法IPv4地址
     *
     * @param input ip地址
     * @return 是否为IPV4地址
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_REGEX.matcher(input).matches();
    }

    /**
     * 判断是否为合法IPv6地址
     *
     * @param input ip地址
     * @return 是否为IPV6地址
     */
    public static boolean isIPv6Address(final String input) {
        int num = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ':') {
                num++;
            }
        }
        if (num > 7) {
            return false;
        }
        if (IPV6_STD_REGEX.matcher(input).matches()) {
            return true;
        }
        if (num == 7) {
            return IPV6_COMPRESS_REGEX_BORDER.matcher(input).matches();
        } else {
            return IPV6_COMPRESS_REGEX.matcher(input).matches();
        }
    }
}
