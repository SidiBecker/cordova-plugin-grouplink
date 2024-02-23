#import <Cordova/CDVPlugin.h>

@interface GroupLinkPlugin : CDVPlugin

- (void)init:(CDVInvokedUrlCommand*)command;

- (void)getUserId:(CDVInvokedUrlCommand*)command;

@end