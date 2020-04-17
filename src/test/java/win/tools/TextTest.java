package win.tools;

import org.junit.Test;
import win.tools.entity.Demo;
import win.tools.singleTool.AutoGenerateTool;
import win.tools.text.SensitiveInfoTools;
import win.tools.text.StringTools;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-06-18--15:04]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class TextTest {

    @Test
    public void main() {
        String[] strings = {
                "abcdefgh",
                "abcdefghi",
                "abcdefghij",
                "abcdefghijk",
                "abcdefghijkl",
                "abcdefghijklm",
                "abcdefghijklmn",
                "abcdefghijklmno",
                "abcdefghijklmnop",
                "abcdefghijklmnopq"
        };

        String[] number = {
                "1234567",
                "12345678",
                "123456789",
                "1234567890",
                "12345678901",
                "123456789012",
                "1234567890123",
                "12345678901234",
                "123456789012345",
                "1234567890123456",
                "12345678901234567"
        };

//        String a = "12345678901";
//        System.out.println(a.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1****$3=$2"));

//
//        System.out.println(SensitiveInfoTools.phoneNoEncrypt("12345678901"));
//        String b = "12345678901234";
////        System.out.println(b.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
//        System.out.println(SensitiveInfoTools.idCardEncrypt(b));
//        System.out.println(SensitiveInfoTools.idPassportEncrypt(b));

        for (String string : strings) {
            System.out.println(SensitiveInfoTools.desensitization3_4(string));
        }

        for (String string : strings) {
            System.out.println(SensitiveInfoTools.bankAccountNoEncrypt(string));
        }
    }

    @Test
    public void underline2HumpTest(){
//        System.out.println(StringTools.underline2Hump("CREATE_TIME"));

        System.out.println("asdf".contains(""));

        AutoGenerateTool.init(1,32,128, new String[]{"no"});
        AutoGenerateTool.reflexAndOutput(Demo.class, true);
    }
}
