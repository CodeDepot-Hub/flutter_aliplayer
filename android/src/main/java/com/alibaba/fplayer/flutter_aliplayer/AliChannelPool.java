package com.alibaba.fplayer.flutter_aliplayer;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BasicMessageChannel;

public class AliChannelPool {


    private final Map<String, BasicMessageChannel<String>> channels;


    private AliChannelPool() {
        channels = new HashMap<>();
    }

    private static volatile AliChannelPool instance;

    public static synchronized AliChannelPool getInstance() {
        if (null == instance) {
            instance = new AliChannelPool();
        }
        return instance;
    }

    public boolean addChannel(String key, BasicMessageChannel<String> channel) {
        if (!channels.containsKey(key)) {
            channels.put(key, channel);
            return true;
        }
        return false;
    }

    public boolean removeChannel(String key) {
        if (channels.containsKey(key)) {
            BasicMessageChannel<String> channel = channelForKey(key);
            if (null != channel) {
                channel = null;
            }
            channels.remove(key);
            return true;
        }
        return false;
    }

    public boolean containChannel(String key) {
        return channels.containsKey(key);
    }


    public BasicMessageChannel<String> channelForKey(String key) {
        return channels.get(key);
    }

    public void clear() {
        for (Map.Entry<String, BasicMessageChannel<String>> entry : channels.entrySet()) {
            // 在清空 Map 之前，调用每个资源的 destroy 方法
            BasicMessageChannel<String> channel = entry.getValue();
            if (null != channel) {
                channel = null;
            }
        }
        channels.clear();
    }

}
