package win.tools;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-06-14--14:11]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class Lang3Test {

    public static void main(String[] args) {
        String format = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZZ");
        System.out.println(format);
    }

}
