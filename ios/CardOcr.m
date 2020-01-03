#import "CardOcr.h"

@implementation CardOcr 

RCT_EXPORT_MODULE()


RCT_REMAP_METHOD(idCardOcr,
                 type:(NSString *)type
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
 

    JYBDIDCardVC *AVCaptureVC = [[JYBDIDCardVC alloc] init];
    if([type isEqual:@"BACK"]){
        AVCaptureVC.scanType=JYBD_IDScanBackType;
    }else{
        AVCaptureVC.scanType=JYBD_IDScanFrontType;
        
    }
    AVCaptureVC.finish = ^(JYBDCardIDInfo *info, UIImage *image)
    {
        // Create path.
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *filePath = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Image.png"];
        
        // Save image.
        [UIImagePNGRepresentation(image) writeToFile:filePath atomically:YES];
        resolve(@{
                  @"image":filePath,
                  @"idCard":info.num?info.num:@"",
                  @"name":info.name?info.name:@"",
                  @"gender":info.gender?info.gender:@"",
                  @"nation":info.nation?info.nation:@"",
                  @"address":info.address?info.address:@"",
                  @"issue":info.issue?info.issue:@"",
                  @"valid":info.valid?info.valid:@"",
                  @"type":info.num!=nil?@"正面":@"反面"
                  });
    };
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *controller = (UINavigationController*)[[[UIApplication sharedApplication] keyWindow] rootViewController];
        [controller presentViewController:AVCaptureVC animated:YES completion:nil];
    });
}

RCT_REMAP_METHOD(bankCardOcr,
                 resolver:(RCTPromiseResolveBlock)resolver
                 rejecter:(RCTPromiseRejectBlock)rejecter)
{

    JYBDBankCardVC *vc = [[JYBDBankCardVC alloc]init];
    vc.finish = ^(JYBDBankCardInfo *info, UIImage *image) {
        // Create path.
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *filePath = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"Image.png"];
        
        // Save image.
        [UIImagePNGRepresentation(image) writeToFile:filePath atomically:YES];
        resolver(@{
                   @"bankNumber":info.bankNumber,
                   @"bankName":info.bankName,
                   });
        
    };
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *controller = (UINavigationController*)[[[UIApplication sharedApplication] keyWindow] rootViewController];
        [controller presentViewController:vc animated:YES completion:nil];
    });
}

@end
