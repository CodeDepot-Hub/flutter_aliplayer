#import "FlutterAliplayerPlugin.h"
#import "AliPlayerFactory.h"
#import "FlutterAliDownloaderPlugin.h"
#import "FlutterAliMediaLoader.h"
#import "FlutterAliPlayerGlobalSettings.h"

/**
 *  业务标识
 *  TODO: 发布版本前,请修改版本并删除此提示
 */
static NSString *const kExtraDataFlutter =@"{\"platform\":\"flutter\",\"flutter-version\":\"7.2.0\"}";

@implementation FlutterAliplayerPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    //声明业务场景
    [AliPlayerGlobalSettings setOption:SET_EXTRA_DATA value:kExtraDataFlutter];
    AliPlayerFactory* factory =
    [[AliPlayerFactory alloc] initWithMessenger:registrar.messenger];
    [registrar registerViewFactory:factory withId:@"plugins.flutter_aliplayer"];
   
    [FlutterAliDownloaderPlugin registerWithRegistrar:registrar];
    [FlutterAliPlayerGlobalSettings registerWithRegistrar:registrar];
    [FlutterAliMediaLoader registerWithRegistrar:registrar];
}


- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

@end
