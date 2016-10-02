package com.ak.test.data;

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.curator.framework.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.ZonedDateTime;

/**
 * Created by internet on 10/2/2016.
 */

@Component
public class DataChangeNotifier {

    @Autowired
    DataChangeListener listener;

    private String zkPath;

    private CuratorFramework framework;

    @PostConstruct
    private void init() {
        //create the zookeeper connection here
        createZookeeperConnection();
    }

    public void notifyChange() {
        // update zookeeper node
        updateZookeeperNode();
    }

    private void createZookeeperConnection() {
        try {
            String zookeeperConnectionString = "localhost:2181";
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

            framework = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
            framework.start();
            zkPath = framework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/dataservice/child", new byte[0]);
            System.out.println("o="+zkPath);
        }
        catch(Exception e ) {
            e.printStackTrace();

            throw new RuntimeException("Unable to start because we cannot connect too zookeeper", e);
        }

    }

    private void updateZookeeperNode() {
        try {
            framework.setData().forPath(zkPath, (System.nanoTime()+"").getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void close() {
        framework.close();
    }
}
