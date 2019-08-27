package cn.selinx.cloud.discovery.eureka.inet;

import java.net.*;
import java.util.Enumeration;

/**
 * 多网卡配置获取网卡问题
 *
 * @author JiePeng Chen
 * @since 2019/8/12 13:58
 */
public class InetUtilTest {

    public static void main(String[] args) {
        try {
            getIp();

            getLocalHostIp();

            boolean result = "VirtualBox Host-Only Ethernet Adapter".matches("Virtual.*");
            System.out.println("匹配结果:" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getIp() throws UnknownHostException, SocketException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("inetAddress.getHostAddress()" + inetAddress.getHostAddress());
        System.out.println("--------------------------------");

        // 读取所有的网卡信息
        int i = 0;
        for (Enumeration<NetworkInterface> nics = NetworkInterface
                .getNetworkInterfaces(); nics.hasMoreElements(); ) {
            NetworkInterface ifc = nics.nextElement();
            String hostAddress = "";
            String hostName = "";
            Enumeration<InetAddress> ids = ifc.getInetAddresses();
            if (ids.hasMoreElements() && ifc.isUp()) {
                InetAddress address = ids.nextElement();
                hostAddress = address.getHostAddress();
                hostName = address.getHostName();
            }
            System.out.println(i + "----" + ifc.isUp() + "---" + hostName + "---" + hostAddress + "---" + ifc.getDisplayName() + "");
            i = i + 1;
        }
        System.out.println("--------------------------------");

    }

    public static InetAddress getLocalHostIp() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            //System.out.println(netInterface.getName());
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    System.out.println("本机的ip=" + ip.getHostAddress());
                    break;
                }
            }
        }
        return ip;
    }

}
