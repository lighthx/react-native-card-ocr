# react-native-card-ocr
在react-native@0.6+；
## Getting started

`$ npm install https://github.com/lighthx/react-native-card-ocr.git --save`
或者
`$ npm yarn add https://github.com/lighthx/react-native-card-ocr.git `

### 配置
`在ios文件夹下面执行 pod install`

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

### idCardOcr

```
const idCardInfo:IdCardInfo=await bankCardOcr("FRONT"||"BACK")
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
1. 无法在模拟器下调用方法！无法在模拟器下调用方法！无法在模拟器下调用方法！SDK缺少相应X86文件。
2. IOS封装的[tiantianios/JYBDAVCapture](https://github.com/tiantianios/JYBDAVCapture)
3. Android封装的[duaiyun1314/BankCardScanner](https://github.com/duaiyun1314/BankCardScanner)


## TODO
- [x] 银行卡识别支持Android&IOS
- [x] 支持Typescript
- [x] 支持Promise
- [x] 身份证识别支持Android

