package com.ciwei.consul;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
/**
 * @author liuqi
 * @date 2019/07/24 21:31
 * @desc 获取本地地址
 */
public class LocalAddr {

    private final static Logger logger = LoggerFactory.getLogger(LocalAddr.class);

    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static String getLocalIP()  {
        try {
            if (isWindowsOS()) {
                return InetAddress.getLocalHost().getHostAddress();
            } else {
                return getLinuxLocalIp();
            }
        } catch (UnknownHostException e) {
            logger.error("获取服务应用IP异常{}", e);
            throw new RuntimeException(e);
        }
    }

    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            //获取到所有本地网卡信息，然后你可以从网卡信息上面获取到网卡的IP地址
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface networkInterface = en.nextElement();
                //去掉环回，虚拟，关闭
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    String ipaddress = inetAddress.getHostAddress();
                    logger.info("getLinuxLocalIp() 获取中 ipaddress:" + ipaddress);
                    if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                        ip = ipaddress;
                        break;
                    }
//                        if (inetAddress.isSiteLocalAddress()) {
//                            // 如果是site-local地址，就是它了
//                            ip = inetAddress.getHostAddress();
//                            break;
//                        }else{
//                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
//                                ip = ipaddress;
//                            }
//                        }
                }
            }
        } catch (SocketException ex) {
            logger.error("获取Linux服务IP异常{}", ex);
            throw new RuntimeException(ex);
        }
        if("".equals(ip)){
            logger.error("获取不到Linux服务IP");
            throw new RuntimeException();
        }
        return ip;
    }
}
