# react-native-card-ocr
在react-native@0.59.10下做的封装,理论向下兼容；
## Getting started

`$ npm install https://github.com/lighthx/react-native-card-ocr.git --save`
或者
`$ npm yarn add https://github.com/lighthx/react-native-card-ocr.git `

### 配置

`$ react-native link react-native-card-ocr`

### 额外配置


#### iOS

1. 在 TARGETS->Build Settings->Library Search Path 添加 $(SRCROOT)/../node_modules/react-native-card-ocr/ios ，改为recursive,注意！注意！注意！
2. 将 项目/node_modules/react-native-card-ocr/ios/JYBD_IDCardRecognition/Resources 整个文件夹添加到你的项目中；
3. 在 info.plist下面加入相机权限   
```
   <key>NSCameraUsageDescription</key>
   <string>相机权限文字描述</string>
```
4. 在TARGETS和PROJECT 两处中的Build Setting 下找到 Enable Bitcode 将其设置为NO
5. 在TARGETS和PROJECT 两处中Build Setting  搜索 ENABLE_TESTABILITY 改为NO
6. 在TARGETS和PROJECT 两处中Build Setting  搜索 Dead Code Stripping Yes
#### Android

1. 打开 你的项目/android/app/src/main/AndroidManifest.xml 在<manifest ...>添加相机权限
```
<uses-permission android:name="android.permission.CAMERA" />
```
 

## Usage 使用方法
```javascript
import {bankCardOcr,idCardOcr} from 'react-native-card-ocr';
```

```
const bankCardInfo:bankCardInfo=await bankCardOcr()
```

#### BankCardInfo 属性
| Prop                    | Type    |  Description
| ----------------------- |:-------:| -------
| bankNumber              | string  | 
| bankName                | string  |  Only IOS

### idCardOcr仅IOS可用

```
const idCardInfo:IdCardInfo=await bankCardOcr()
```

#### idCardInfo 属性
| Prop                    | Type    |  Description
| ----------------------- |:-------:| -------
| image                   | string  |  图片
| idCard                  | string  |  卡号
| name                    | string  |  姓名
| gender                  | string  |  性别
| nation                  | string  |  民族
| address                 | string  |  地址
| issue                   | string  |  签发机关
| valid                   | string  |  有效期
| type                    | string  |  正面|反面

## 注意
1. 无法在模拟器下调用方法！！！SDK缺少相应X86文件，模拟器下不会crash。
2. IOS封装的[tiantianios/JYBDAVCapture](https://github.com/tiantianios/JYBDAVCapture)
3. Android封装的[duaiyun1314/BankCardScanner](https://github.com/duaiyun1314/BankCardScanner)


## TODO
- [x] 银行卡识别支持Android&IOS
- [x] 支持Typescript
- [x] 支持Promise
- [ ] 身份证识别支持Android

