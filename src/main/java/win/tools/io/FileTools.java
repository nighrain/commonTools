package win.tools.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 *    
 * Title         [文件处理工具类]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-22--08:34]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 * 遍历某文件夹下的所有文件(递归/非递归)
 *
 *  
 */
public class FileTools {

    private FileTools() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @param date 要输出的信息
     * @param parentPath 文件父路径
     * @param fileName 文件名称
     */
    public static void output2File(StringBuffer date,String parentPath, String fileName) {

        //创建文件夹
        File parentDir = new File(parentPath);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        File file = new File(parentPath, fileName);

        //创建文件
        if (!file.exists()) {
            boolean newFile = false;
            try {
                newFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (!newFile) {
                System.out.println("文件创建失败");
                return;
            }
        }

        //信息输出到文件
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos,Charset.forName("utf-8"));
            osw.write(date.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.flush();
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("解析结果已输出到: "+file.getPath());
    }
}
