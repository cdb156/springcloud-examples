package cn.selinx.boot.hello.api.impl;

import cn.selinx.boot.hello.api.ServiceRegistry;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册实现类
 *
 * @author JiePeng Chen
 * @since 2019/7/22 15:38
 */
@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    public static CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;

    public static final String REGISTRY_PATH = "/registry";

    public static final int SESSION_TIMEOUT = 5000;

    public ServiceRegistryImpl() {
    }

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            logger.debug("connected to zookeeper");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        try {

            // 创建根节点（持久节点）
            String registryPath = REGISTRY_PATH;
            if (zk.exists(registryPath, false) == null) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create registry node：{}", registryPath);
            }

            // 创建服务节点（持久节点）
            String servicePath = registryPath + "/" + serviceName;
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create server node：{}", servicePath);
            }
            // 创建地址节点（零时顺序节点）
            String addressPath = servicePath + "/address-";
            String addressNode = zk.create(addressPath, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create address node：{} => {}", addressNode, serviceAddress);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }
}
