package com.alibaba.fplayer.flutter_aliplayer;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliyun.loader.MediaLoader;
import com.aliyun.player.bean.ErrorInfo;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import android.util.Log;
public class FlutterAliMediaLoader implements FlutterPlugin, MethodChannel.MethodCallHandler, EventChannel.StreamHandler {

    private Context mContext;
    private MethodChannel mMethodChannel;
    private EventChannel.EventSink mEventSink;
    private EventChannel mEventChannel;
    private final MediaLoader mMediaLoader;

    public FlutterAliMediaLoader(Context context, FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        this.mContext = context;
        mMediaLoader = MediaLoader.getInstance();
        this.mMethodChannel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "plugins.flutter_aliplayer_media_loader");
        mMethodChannel.setMethodCallHandler(this);

        mEventChannel = new EventChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_aliplayer_media_loader_event");
        mEventChannel.setStreamHandler(this);
        mMediaLoader.setOnLoadStatusListener(new MediaLoader.OnLoadStatusListener() {
            @Override
            public void onError(String url, int code, String msg) {
                if (mEventSink != null) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("method", "onError");
                    resultMap.put("url", url);
                    resultMap.put("code", String.valueOf(code));
                    resultMap.put("msg", msg);
                    mEventSink.success(resultMap);
                } else {
                    Log.w("FlutterAliMediaLoader", "EventSink is null, onError ignored. url=" + url);
                }
            }

            @Override
            public void onCompleted(String url) {
                if (mEventSink != null) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("method", "onCompleted");
                    resultMap.put("url", url);
                    mEventSink.success(resultMap);
                } else {
                    Log.w("FlutterAliMediaLoader", "EventSink is null, onCompleted ignored. url=" + url);
                }
            }

            @Override
            public void onCanceled(String url) {
                if (mEventSink != null) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("method", "onCanceled");
                    resultMap.put("url", url);
                    mEventSink.success(resultMap);
                } else {
                    Log.w("FlutterAliMediaLoader", "EventSink is null, onCanceled ignored. url=" + url);
                }
            }

            @Override
            public void onErrorV2(String url, ErrorInfo errorInfo) {
                if (mEventSink != null) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("method", "onErrorV2");
                    resultMap.put("url", url);
                    resultMap.put("code", errorInfo.getCode().name());
                    resultMap.put("msg", errorInfo.getMsg());
                    mEventSink.success(resultMap);
                } else {
                    Log.w("FlutterAliMediaLoader", "EventSink is null, onErrorV2 ignored. url=" + url);
                }
            }
        });
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "load":
                Map<String, String> loadMap = call.arguments();
                if (null != loadMap) {
                    String url = loadMap.get("url");
                    String duration = loadMap.get("duration");
                    if (loadMap.containsKey("bandWidth")) {
                        int bandWidth = Integer.parseInt(loadMap.get("bandWidth"));
                        mMediaLoader.load(url, Long.parseLong(duration), bandWidth);
                    } else {
                        mMediaLoader.load(url, Long.parseLong(duration));
                    }
                }
                break;
            case "resume":
                String resumeUrl = call.arguments();
                mMediaLoader.resume(resumeUrl);
                break;
            case "pause":
                String pauseUrl = call.arguments();
                mMediaLoader.pause(pauseUrl);
                break;
            case "cancel":
                String cancelUrl = call.arguments();
                mMediaLoader.cancel(cancelUrl);
                break;
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        this.mEventSink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        mEventSink = null;
    }
}