package com.example.queuedemo.role;

import com.example.queuedemo.server.PubRequest;
import com.example.queuedemo.server.PullRequest;
import com.example.queuedemo.transport.TLVData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class Slave extends AbstractRole{
    public Slave(String systemName, String groupName, String zkAddr) {
        super(systemName, groupName, zkAddr);
        this.roleName = "slave";
    }

    @Override
    public void start() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, groupNodePath, true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String masterNodePath = groupNodePath + "/master";

                if(event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED)) {
                    if(register()){
                        System.out.println("slave started!");
                        if(client.checkExists().forPath(masterNodePath) == null){
                            active();
                        }
                    }else{
                        deactive();
                        System.out.println("slave failed to start because register ZooKeeper failed!");
                        System.exit(2);
                    }
                    return;
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_LOST)){
                    deactive();
                    return;
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    String childPath = event.getData().getPath();
                    System.out.println("child removed: " + childPath);

                    if(masterNodePath.equals(childPath)){
                        active();
                    }
                    return;
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    String childPath = event.getData().getPath();
                    System.out.println("child added: " + childPath);

                    if(masterNodePath.equals(childPath)){
                        deactive();
                    }
                    return;
                }
            }
        });
        pathChildrenCache.start();
        client.start();

    }

    @Override
    public void active() {
        roleActived = true;
        System.out.println(roleName + " is active");
    }

    @Override
    public void deactive() {
        roleActived = false;
        System.out.println(roleName + " is inactive");
    }

    @Override
    public boolean isActive() {
        return roleActived;
    }

    @Override
    public TLVData pub(PubRequest request) {
        System.out.println("got one pub request, timestamp: " + System.currentTimeMillis());

        String message = "sorry, I'm slave!";
        TLVData TLVData = new TLVData((byte) '2', message.length(), message);
        return TLVData;
    }

    @Override
    public TLVData pull(PullRequest request) {
        System.out.println("got one pull request, timestamp: " + System.currentTimeMillis());

        if(isActive()) {
            String message = "hello, world!(from slave)";
            TLVData TLVData = new TLVData((byte) '4', message.length(), message);
            return TLVData;
        }else{
            String message = "sorry, I'm inactive!(slave)";
            TLVData TLVData = new TLVData((byte) '4', message.length(), message);
            return TLVData;
        }
    }
}
