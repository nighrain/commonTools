package win.tools.singleTool;

import win.tools.entity.Demo;

import javax.persistence.Table;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *    
 * Title         [解析工具类,独立版]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-22--08:59]
 * Version:      [v1.0]
 * Description:  [解析class生成getter/setter方法和oracle-sql语句]
 * <p>
 *     注意: 多表关联的无法自动解析,需要在解析完成后手动处理多表关系
 * </p>
 *  
 */
public class AutoGenerateTool {

    private static final String enumSize = "VARCHAR2(32)";         //枚举对应的字符串的大小
    private static final String stringSize = "VARCHAR2(64)";       //String对应的字符串的大小
    private static final String descriptionSize = "VARCHAR2(128)"; //描述类字段对应的字符串的大小
    private static final String[] markArr = {"desc","info","remark"};        //描述类字段对应的字段名的公有标识

    public static void main(String[] args) {

        //要解析的类
        Class<?> clazz = Demo.class;

        StringBuffer stringBuffer = reflex(clazz, true);
        //将结果输出到文件
        output2File(stringBuffer,"D:\\.autoGetSet", clazz.getSimpleName() + ".txt");
    }

    /**
     * <p>
     *     枚举 的固定大小 VARCHAR2(32) enumSize
     *     String 的固定大小 VARCHAR2(64) stringSize
     * </p>
     * @param clazz 要解析的类
     * @param isAllFieldMode 是否全字段模式 true 则把该类的所有字段解析,适用于新建的表
     *                                      false 则只解析没有get方法的字段,适用于修改表补充字段
     * @return 解析后的结果
     */
    public static StringBuffer reflex(Class clazz, boolean isAllFieldMode) {
        //获取注解上的表名
        Annotation annotation = clazz.getAnnotation(Table.class);
        String tableName = null;
        if (annotation != null) {
            String s = annotation.toString();
            if (s != null) {
                tableName = s.substring(s.indexOf("name") + 5, s.length() - 1);
            }
        }

        int fieldAll = 0;       //全部字段
        int fieldAnalysis = 0;  //已经解析的字段
        int fieldExisting = 0;    //已存在get方法的字段
        int fieldIgnore = 0;    //忽略的字段
        int fieldSpecial = 0;    //需要手动处理的特殊字段

        //获取已存在的方法
        ArrayList<String> existing = new ArrayList<String>();
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                existing.add(methods[i].getName());
            }
        }

        StringBuffer sbGetSet = new StringBuffer();//带注解的Getter Setter 方法
        StringBuffer sbSql = new StringBuffer();//oracle-sql语句
        StringBuffer sbInfo = new StringBuffer();

        //拼装sql前置
        if (isAllFieldMode) {
            if (tableName == null) {
                sbSql.append("CREATE TABLE XXX手动配置表名 (\r\n");
            } else {
                sbSql.append("CREATE TABLE " + tableName + " (\r\n");
            }
            sbSql.append("ID NUMBER NOT NULL PRIMARY KEY ,\r\n");
            sbSql.append("OPTIMISTIC              NUMBER ");
        } else {
            if (tableName == null) {
                sbSql.append("ALTER TABLE XXX手动配置表名  ADD (\r\n");
            } else {
                sbSql.append("ALTER TABLE " + tableName + " ADD (");
            }
        }

        //获得所有的字段
        Field[] fields = clazz.getDeclaredFields();
        fieldAll = fields.length;
        for (Field field : fields) {
            if (existing.contains("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))) {
                fieldExisting += 1;
                //已存在该get方法 并且是追加模式则 continue
                if (!isAllFieldMode) {
                    fieldIgnore += 1;
                    continue;
                }
            }
            fieldAnalysis += 1;
            Map<String, String> map = parseField2Mapper(field);
            sbGetSet = doGenerateGS(sbGetSet, map);           //拼接getter/setter
            sbSql = doGenerateSql(sbSql, map);                //拼接oracle-sql

            if ("true".equals(map.get("hasDesc"))) sbInfo.append(map.get("desc"));  //描述类字段

            if (map != null && "o".equals(map.get("javaType"))) fieldSpecial += 1 ; //需要手动处理的字段
        }
        sbSql.append("\r\n)");
        if (!isAllFieldMode) {
            sbSql = sbSql.deleteCharAt(sbSql.indexOf("(") + 1);
        }

        //输出本次操作的信息

        sbInfo.append("---时间: \t\t\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\r\n")
                .append("---操作的实体类: \t" + clazz.getName() + "\r\n")
                .append("---对应的表名: \t\t" + tableName + "\r\n")
                .append("---模式: \t\t\t" + (isAllFieldMode ? "全字段模式" : "追加字段模式") + "\r\n")
                .append("---需要手动处理的特殊字段: " + fieldSpecial + " 个" + "\r\n")
                .append("---全部字段: \t\t" + fieldAll + " 个" + "\r\n")
                .append("---已经解析的字段: \t" + fieldAnalysis + " 个" + "\r\n")
                .append("---忽略的字段: \t\t" + fieldIgnore + " 个" + "\r\n")
                .append("---已存在get方法的字段: " + fieldExisting + " 个" + "\r\n");
        sbInfo.append("==============================");
        System.out.println(sbInfo.toString());

        return sbInfo.append("\r\n")
                .append(sbGetSet)
                .append("\r\n=============== oracle-sql ===============\r\n")
                .append(sbSql);
    }

    /**
     * <p>
     * 根据 java 字段解析 oracle类型的方法:
     * 可以 自动识别 Integer,Double,Float,Long 等数字类型 对应 NUMBER 类型;
     *          Date 对应 DATE 类型;
     *          String 对应 VARCHAR2(64) 类型;
     *          Enum 枚举 对应 VARCHAR2(32) 类型;
     *      注意: [多表关联] 对象类型 对应的 oracle类型 需要 生成后手动修改
     * </p>
     * @param field 要解析的字段
     * @return 包含四个字段的 Map
     */
    private static Map<String, String> parseField2Mapper(Field field) {
        HashMap<String, String> map = new HashMap<String, String>();
        Class<?> type = field.getType();
        map.put("javaType", null);       //常见类型和对象类型
        map.put("fieldType", type.getSimpleName());   //字段的类型
        map.put("field", field.getName());       //字段名
        map.put("oracleType", null);     //oracle 的类型
        map.put("hasDesc", "false");       //是否有描述类字段
        map.put("desc", null);          //描述类信息的字段的记录
        if (type.isEnum()) {
            map.put("javaType", "e");
            map.put("oracleType", enumSize);
        } else if (type.equals(String.class)) {
            map.put("javaType", "-1");
            map.put("oracleType", stringSize);
            if(isDesc(field.getName())){
                map.put("hasDesc", "true");
                map.put("desc", "-" + field.getName() + " 可能为描述类字段对应: "+descriptionSize+"\r\n");
                map.put("oracleType", descriptionSize);
            }
        } else if (type.equals(int.class) || type.equals(float.class) || type.equals(double.class) || type.equals(long.class) ||
                type.equals(Integer.class) || type.equals(Float.class) || type.equals(Double.class) || type.equals(Long.class) ||
                type.equals(BigDecimal.class) || type.equals(BigInteger.class)) {
            map.put("javaType", "-1");
            map.put("oracleType", "NUMBER");
        } else if (type.equals(Date.class)) {
            map.put("javaType", "-1");
            map.put("oracleType", "DATE");
        } else {
            map.put("javaType", "o");
            map.put("oracleType", null);
        }

        return map;
    }

    private static boolean isDesc(String str){
        if (str == null) return false;
        for (String mark : markArr) {
            if (str.toLowerCase().contains(mark.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    //根据包含四个字段的map 拼接getter/setter代码的核心方法
    private static StringBuffer doGenerateGS(StringBuffer sbGetSet, Map<String, String> map) {
        if (null == map) return sbGetSet;
        return doGenerateGS(sbGetSet, map.get("javaType"), map.get("fieldType"), map.get("field"), map.get("oracleType"));
    }

    /**
     * 拼接getter/setter代码的核心方法
     * @param sbGetSet     要往后拼接的 StringBuffer
     * @param javaType     标识该java类型是否可自动解析为oracle类型
     * @param fieldTypeStr 字段类型
     * @param field        字段名
     * @param oracleType   对应的oracle类型
     * @return 拼接后的StringBuffer
     */
    private static StringBuffer doGenerateGS(StringBuffer sbGetSet, String javaType, String fieldTypeStr, String field, String oracleType) {
        String firstUpCase = field.substring(0, 1).toUpperCase() + field.substring(1); //首字母大写
        if (!"-1".equals(javaType)) {
            if ("e".equals(javaType)) {                               //枚举类型
                sbGetSet.append("@Enumerated(value = EnumType.STRING)\r\n");
            } else if ("o".equals(javaType)) {                         // 不可自动解析的类型
                sbGetSet.append("//TODO XXX手动处理映射关系\r\n");
            }
        }
        //常见类型
        if (oracleType == null) {
            sbGetSet.append("@Column(name=\"" + hump2Underline(field) + "\" )\r\n");
        } else {
            sbGetSet.append("@Column(name=\"" + hump2Underline(field) + "\", columnDefinition = \"" + oracleType + "\")\r\n");
        }
        sbGetSet.append("public " + fieldTypeStr + " get" + firstUpCase + "() {\r\n");
        sbGetSet.append("\treturn " + field + ";\r\n");
        sbGetSet.append("}\r\n");
        sbGetSet.append("public void set" + firstUpCase + "(" + fieldTypeStr + " " + field + ") {\r\n");
        sbGetSet.append("\tthis." + field + " = " + field + ";\r\n");
        sbGetSet.append("}\r\n");
        sbGetSet.append("\r\n");
        return sbGetSet;
    }

    //根据包含三个字段的map 拼接sql代码的方法
    private static StringBuffer doGenerateSql(StringBuffer sbSql, Map<String, String> map) {
        if (null == map) return sbSql;
        return doGenerateSql(sbSql, map.get("javaType"), map.get("field"), map.get("oracleType"));
    }

    /**
     * 拼接sql的核心方法
     * @param sbSql      要往后拼接的 StringBuffer
     * @param javaType   标识该java类型是否可自动解析为oracle类型
     * @param field      驼峰式字段名
     * @param oracleType oracle类型
     * @return 拼接后的StringBuffer
     */
    private static StringBuffer doGenerateSql(StringBuffer sbSql, String javaType, String field, String oracleType) {
        if ("-1".equals(javaType) || "e".equals(javaType)) {        //已自动解析
            sbSql.append(",\r\n" + hump2Underline(field) + " \t " + oracleType);
        } else {                                                    //无法自动解析
            sbSql.append(",\r\n" + hump2Underline(field) + " \t " + oracleType + " XXX手动处理该字段");
        }
        return sbSql;
    }

    /**
     * @param str abcDef => ABC_DEF
     * @return 驼峰转下划线的方法
     */
    public static String hump2Underline(String str) {
        if (null == str || "".equals(str.trim())) return "";
        StringBuffer sb = new StringBuffer();
        char[] array = str.toCharArray();
        //循环处理
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
            if (c >= 'A' && c <= 'Z') {
                sb.append("_" + c);
            } else if (c >= 'a' && c <= 'z') {
                sb.append((char) (c - 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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
