#import "GroupLinkPlugin.h"

@import GroupLink;

@implementation GroupLinkPlugin

- (void)init:(CDVInvokedUrlCommand*)command{
    
    CDVPluginResult* pluginResult = nil;
    
    NSString *token = [command.arguments objectAtIndex:0];
    
    if (token != nil && [token length] > 0) {
        
        [GroupLinkSDK startWithToken:token];

        [GroupLinkSDK startBluetooth];

        [GroupLinkSDK startLocation];

        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString: @"Invalid token"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getUserId:(CDVInvokedUrlCommand*)command{

    NSString *userId = [GroupLinkSDK getUserID];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: userId];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getUIBackgroundModes:(CDVInvokedUrlCommand*)command{

    NSArray *backgroundModes = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"UIBackgroundModes"];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray: backgroundModes];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
