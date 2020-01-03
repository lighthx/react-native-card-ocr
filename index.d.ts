export interface BankCardInfo{
    bankNumber:string
    bankName?:string  //IOS端可以识别出银行名称
}
export interface IdCardInfo{
    image:string, //图片
    idCard:string,//卡号
    name:string, //姓名
    gender:string, //性别
    nation:string, //民族
    address:string, //地址
    issue:string, //签发机关
    valid:string, //有效期
    type:string, //正面|反面
}
/**
 * 银行卡识别
 */
export function bankCardOcr():Promise<BankCardInfo>;


/**
 * 身份证识别
 */
export enum IdCardType {
    FRONT="FRONT",
    BACK="BACK"
}
export function idCardOcr(idCardType:IdCardType):Promise<IdCardInfo>;
