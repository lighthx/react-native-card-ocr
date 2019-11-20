//
//  JYBDNewFormeScaningV.h
//  Pods
//
//  Created by tianxiuping on 2018/7/9.
//

#import <UIKit/UIKit.h>

typedef enum : NSUInteger {
    JYBD_BankScanType,
    JYBD_IDScanBackType,
    JYBD_IDScanFrontType
} JYBDScaningType;

@interface JYBDNewFormeScaningV : UIView

@property (nonatomic, assign) JYBDScaningType scanType;

@property (nonatomic,copy)void (^turnOnOrOffClick)(BOOL isSelectState);
@end
