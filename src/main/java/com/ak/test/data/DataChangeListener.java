package com.ak.test.data;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by internet on 10/2/2016.
 */

@Component
public class DataChangeListener {

    @Autowired
   private Data data;

    private PathChildrenCache cache;

    private CuratorFramework client;

    private DSPathChildrenCacheListener listener;


    @PostConstruct
    private void init() {
        try {
            client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            cache = new PathChildrenCache(client, "/dataservice", true);
            cache.start();
            listener = new DSPathChildrenCacheListener();
            cache.getListenable().addListener(listener);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyDataSources() {
        data.reloadData();
    }







    @PreDestroy
    private void close() {
        try {
            cache.close();
            client.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    class DSPathChildrenCacheListener implements PathChildrenCacheListener {
        public void childEvent(CuratorFramework framework, PathChildrenCacheEvent event) {
            switch(event.getType()) {
                case CHILD_ADDED:{
                    System.out.println("Child added. Nothing to do "+event.getData().getPath());
                    break;
                }
                case CHILD_UPDATED: {
                    System.out.println("We have an update. reloading data");
                    notifyDataSources();
                    break;
                }
                case CHILD_REMOVED:{
                    System.out.println("Child removed. Nothing to do "+event.getData().getPath());
                    break;
                }
            }
        }
    }
}
