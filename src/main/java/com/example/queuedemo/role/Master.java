package com.example.queuedemo.role;

import com.example.queuedemo.transport.PubRequest;
import com.example.queuedemo.transport.PullRequest;
import com.example.queuedemo.transport.Response;
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
	public Response pub(PubRequest request) {
		if(isActive()) {
			String message = "got it";
			Response response = new Response((byte) '2', message.length(), message);
			return response;
		}else{
			String message = "sorry, I'm inactive!";
			Response response = new Response((byte) '2', message.length(), message);
			return response;
		}
	}

	@Override
	public Response pull(PullRequest request) {
		if(isActive()) {
			String message = "hello, world!(from master)";
			Response response = new Response((byte) '4', message.length(), message);
			return response;
		}else{
			String message = "sorry, I'm inactive!";
			Response response = new Response((byte) '4', message.length(), message);
			return response;
		}
	}
}
