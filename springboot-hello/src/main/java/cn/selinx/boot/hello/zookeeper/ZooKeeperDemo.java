package cn.selinx.boot.hello.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper 连接Demo
 *
 * @author JiePeng Chen
 * @since 2019/7/22 10:56
 */
public class ZooKeeperDemo {

    public static final String CONNECTION_STRING = "127.0.0.1:2181";
    public static final int SESSION_TIMEOUT = 5000;

    /**
     * 倒计数器
     */
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    // 打开阀门
                    System.out.println("异步连接ZK成功");
                    latch.countDown();
                }
            }
        });
        // 关闭阀门
        latch.await();

        // 获取ZooKeeper客户端对象
        System.out.println("zk客户端信息：" + zk);

//        create(zk);
//        delete(zk);
//        setData(zk);
//        getData(zk);
//        getChildren(zk);
        exists(zk);

    }

    /**
     * create:创建节点名称（同步和异步）
     */
    public static void create(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 同步创建节点数据
        String name = zk.create("/foo", "boot".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("同步创建节点/foo=" + name);


        // 异步创建节点
        zk.create("/foo/demo", "demo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int i, String s, Object o, String s1) {
                System.out.println("结束异步节点/foo/demo=" + s1);
            }
        }, null);

        System.out.println("异步创建节点开始");

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * delete:删除节点名称（同步和异步）
     */
    public static void delete(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 同步删除节点,先删除底层节点，当节点不存在KeeperException$NoNodeException
        zk.delete("/foo/demo", -1);
        zk.delete("/foo", -1);

        // 异步删除
        zk.delete("/foo", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println(rc == 0);
            }
        }, null);

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);
    }


    /**
     * setData : 更新修改节点数据(同步和异步)
     *
     * @param zk
     */
    public static void setData(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 同步修改节点,更新最新的版本-1
        Stat stat = zk.setData("/foo", "fooHi3".getBytes(), -1);
        System.out.println(stat != null);

        zk.setData("/foo/demo", "fooHiDemo3".getBytes(), -1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                System.out.println(stat != null);
            }
        }, null);

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * getData : 更新修改节点数据(同步和异步)
     *
     * @param zk
     */
    public static void getData(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 同步查询数据
        byte[] bytes = zk.getData("/foo", null, null);
        String name = new String(bytes);
        System.out.println("查询结果/foo=" + name);

        // 异步查询数据
        zk.getData("/foo/demo", null, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                String demoName = new String(data);
                System.out.println("异步查询结果/foo/demo=" + demoName);
            }
        }, null);

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * getChildren : 列出子节点数据(同步和异步)
     *
     * @param zk
     */
    public static void getChildren(ZooKeeper zk) throws KeeperException, InterruptedException {

        // 同步方式获取子节点
        List<String> children = zk.getChildren("/", null);
        children.forEach(System.out::println);

        // 异步获取所有子节点
        zk.getChildren("/foo", null, new AsyncCallback.ChildrenCallback() {

            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                children.forEach(System.out::println);
            }
        }, null);

        // 异步获取所有子节点
        zk.getChildren("/foo", null, new AsyncCallback.Children2Callback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                for (String childName : children) {
                    System.out.println("子节点：" + childName);
                }

            }
        }, null);

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);

    }

    /**
     * exists : 子节点是否存在(同步和异步)
     *
     * @param zk
     */
    public static void exists(ZooKeeper zk) throws KeeperException, InterruptedException {

        // 同步方式获取子节点
        Stat stat = zk.exists("/food", null);
        if (stat != null) {
            System.out.println("同步获取：node exists");
        } else {
            System.out.println("同步获取：node does not exists");
        }

        // 异步调用
        zk.exists("/foo", null, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                if (stat != null) {
                    System.out.println("异步获取：node exists");
                } else {
                    System.out.println("异步获取：node does not exists");
                }
            }
        }, null);

        // 确保异步回调方法被执行
        Thread.sleep(Long.MAX_VALUE);

    }

}
