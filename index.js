import { NativeModules } from 'react-native';

let { CardOcr } = NativeModules;
/**
 * ios银行卡库加载后无法用模拟器
 */
if(!CardOcr){
   CardOcr={};
}

export const bankCardOcr=async()=>{
  const data=await CardOcr.bankCardOcr();
  return {...data,bankNumber:data.bankNumber.replace(/ /g,"")}
};

export const idCardOcr=CardOcr.idCardOcr;

