#import "FluoceanPlugin.h"
#if __has_include(<fluocean/fluocean-Swift.h>)
#import <fluocean/fluocean-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "fluocean-Swift.h"
#endif

@implementation FluoceanPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFluoceanPlugin registerWithRegistrar:registrar];
}
@end
