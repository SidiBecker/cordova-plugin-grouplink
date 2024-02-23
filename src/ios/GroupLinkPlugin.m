#import "GroupLinkPlugin.h"
#import <Cordova/CDVPlugin.h>

@import GroupLink;

@implementation GroupLinkPlugin


- (void)echo:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)init:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSObject *params = [command.arguments objectAtIndex:0];
    NSString *token = [params valueForKey:@"token"];

    if (token != nil && [token length] > 0) {

        // Override point for customization after application launch.
        [GroupLinkSDK startWithToken:token];

        [GroupLinkSDK startBluetooth];

        [GroupLinkSDK startLocation];

        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString: @"Invalid token"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
