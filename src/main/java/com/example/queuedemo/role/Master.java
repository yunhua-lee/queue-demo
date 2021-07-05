package com.example.queuedemo.role;

import com.example.queuedemo.server.PubRequest;
import com.example.queuedemo.server.PullRequest;
import com.example.queuedemo.transport.TLVData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Desctiption
 * @Author wallace
 * @Date 2021/6/4
 */
public class Master extends AbstractRole{

    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    public Master(String systemName, String groupName, String zkAddr) {
        super(systemName, groupName, zkAddr);
        this.roleName = "master";
    }

    @Override
    public void start() throws Exception {
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if(connectionState.equals(ConnectionState.CONNECTED)
                        || connectionState.equals(ConnectionState.RECONNECTED)){
                    if(register()){
                        active();
                        System.out.println("master started!");
                    }else{
                        deactive();
                        System.out.println("master failed to start because register ZooKeeper failed!");
                        System.exit(2); //just exit and wait for operator fixing, no need to retry
                    }
                    return;
                }else if(connectionState.equals(ConnectionState.LOST)
                        || connectionState.equals(ConnectionState.SUSPENDED)){
                    deactive();
                    System.out.println("master is deactived!");
                }
            }
        });

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
    public boolean isActive(){
        return roleActived;
    }

    @Override
    public TLVData pub(PubRequest request) {
        System.out.println("got one pub request, timestamp: " + System.currentTimeMillis());

        if(isActive()) {
            String message = "got it";
            TLVData TLVData = new TLVData((byte) '2', message.length(), message);
            return TLVData;
        }else{
            String message = "sorry, I'm inactive!";
            TLVData TLVData = new TLVData((byte) '2', message.length(), message);
            return TLVData;
        }
    }

    @Override
    public TLVData pull(PullRequest request) {
        System.out.println("got one pull request, timestamp: " + System.currentTimeMillis());

        if(isActive()) {
            String message = "hello, world!(from master)";
            TLVData TLVData = new TLVData((byte) '4', message.length(), message);
            return TLVData;
        }else{
            String message = "sorry, I'm inactive!";
            TLVData TLVData = new TLVData((byte) '4', message.length(), message);
            return TLVData;
        }
    }
}
