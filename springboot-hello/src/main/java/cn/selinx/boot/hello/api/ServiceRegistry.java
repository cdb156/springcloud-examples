package cn.selinx.boot.hello.api;

/**
 * @author JiePeng Chen
 * @since 2019/7/22 15:37
 */
public interface ServiceRegistry {

    /**
     * 注册服务信息
     * @param serviceName 服务名称
     * @param serviceAddress 服务地址
     */
    void register(String serviceName,String serviceAddress);
}
