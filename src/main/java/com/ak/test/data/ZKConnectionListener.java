package com.ak.test.data;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by internet on 10/2/2016.
 */
@Component
public class ZKConnectionListener {

    @Autowired
    private Data data;

    private CuratorFramework client;

    @PostConstruct
    public void init() {
        try {
            client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            client.getData().usingWatcher(new DSWatcher()).forPath("/dataservice");

        }catch(Exception e){
            throw new RuntimeException("Aborting bootstrap. Cann't get watch on zk", e);
        }
    }


    public class DSWatcher implements Watcher {

        public void process(WatchedEvent event) {
            switch(event.getState()) {
                case Disconnected:
                    data.fail();
                    break;
                case SyncConnected:
                    System.out.println("Connected");
                    data.reloadData();
                    break;

            }
        }

    }
}
