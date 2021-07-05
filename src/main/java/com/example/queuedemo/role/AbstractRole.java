package com.example.queuedemo.role;

import com.google.common.primitives.Longs;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public abstract class AbstractRole implements Role {
    protected String roleName;
    protected volatile boolean roleActived = false;

    protected final String systemName;
    protected final String groupName;
    protected final String zkAddr;
    protected final String groupNodePath;

    protected final CuratorFramework client;

    AbstractRole(String systemName, String groupName, String zkAddr){
        this.systemName = systemName;
        this.groupName = groupName;

        this.zkAddr = zkAddr;
        this.groupNodePath = "/org/example/" + systemName + "/" + groupName;

        client = CuratorFrameworkFactory.builder()
                .connectString(zkAddr).sessionTimeoutMs(5000).connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
    }

    boolean register() {
        if(zkAddr == null || zkAddr.isEmpty()){
            throw new RuntimeException("zkAddr is null or empty");
        }

        String nodePath = groupNodePath + "/" + roleName;

        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .forPath(nodePath, Longs.toByteArray(System.currentTimeMillis()));

            System.out.println("succeeded to register to ZooKeeper as " + roleName);

            return true;
        } catch (Exception e) {
            System.out.println("failed to register to ZooKeeper: " + zkAddr);
            System.out.println("error message: " + e.getMessage());
            return false;
        }
    }
}
