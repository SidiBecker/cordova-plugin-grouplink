#import <Cordova/CDV.h>

@interface GroupLinkPlugin : CDVPlugin

- (void)init:(CDVInvokedUrlCommand*)command;

- (void)getUserId:(CDVInvokedUrlCommand*)command;

- (void)getUIBackgroundModes:(CDVInvokedUrlCommand*)command;

@end
