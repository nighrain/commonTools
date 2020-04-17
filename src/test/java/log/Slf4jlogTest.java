package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-07-05--01:17]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */
public class Slf4jlogTest {
    private static final Logger log = LoggerFactory.getLogger(Slf4jlogTest.class);
    public static void main(String[] args) {
        log.info("123");
        log.info("now {}" , "starting server");
    }
}
