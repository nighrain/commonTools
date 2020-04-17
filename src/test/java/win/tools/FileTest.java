package win.tools;

import org.apache.commons.io.FileUtils;
import win.tools.entity.Demo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-06-18--14:13]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class FileTest {

    public static void main(String[] args) throws NoSuchFieldException, IOException {
//        Field field = Demo.class.getDeclaredField("serialVersionUID");
//        System.out.println(field.getName());
//        System.out.println(field.getModifiers());
//        System.out.println(field.toGenericString());
//        System.out.println(field.toString());

        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(new File("").getCanonicalPath());
    }

}
