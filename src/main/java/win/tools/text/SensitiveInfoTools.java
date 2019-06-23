package win.tools.text;

/**
 *    
 * Title         [敏感信息处理工具类]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-18--14:44]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class SensitiveInfoTools {

    private SensitiveInfoTools() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @param srcStr
     * @return **前三后四脱敏
     */
    public static String desensitization3_4(String srcStr){
        if(null == srcStr || "".equals(srcStr.trim()) || srcStr.length() < 8){
            return srcStr;
        }
        return srcStr.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

//    /**
//     * @param phoneNo
//     * @return 手机号码前三后四脱敏
//     */
//    public static String phoneNoEncrypt(String phoneNo){
//        if(null == phoneNo || "".equals(phoneNo.trim()) || phoneNo.length()<8 || phoneNo.length()!=11){
//            return phoneNo;
//        }
//        return phoneNo.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//    }

//    /**
//     * @param identityCard
//     * @return 身份证前三后四脱敏
//     */
//    public static String idCardEncrypt(String identityCard){
//        if(null == identityCard || "".equals(identityCard.trim()) || identityCard.length()<8){
//            return identityCard;
//        }
//
//        return identityCard.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
//    }

//    /**
//     * @param idPassport
//     * @return 护照 护照一般为8或9位
//     */
//    public static String idPassportEncrypt(String idPassport) {
//        if (null == idPassport || "".equals(idPassport.trim()) || (idPassport.length() < 8)) {
//            return idPassport;
//        }
//        return idPassport.substring(0, 2) +
//                new String(new char[idPassport.length() - 5]).replace("\0", "*") +
//                idPassport.substring(idPassport.length() - 3);
//    }

    public static String bankAccountNoEncrypt(String bankAccountNo){
        if(null == bankAccountNo || "".equals(bankAccountNo.trim()) || bankAccountNo.length() < 8){
            return bankAccountNo;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(bankAccountNo.substring(0, 3)).append("******").append(bankAccountNo.substring(bankAccountNo.length()-4));
        return sb.toString();
    }


}
