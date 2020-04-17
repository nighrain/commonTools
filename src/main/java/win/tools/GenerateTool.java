package win.tools;

import org.junit.Test;
import win.tools.entity.Demo;
import win.tools.io.FileTools;
import win.tools.text.StringTools;
import win.tools.time.DateTools;

import javax.persistence.Table;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;


/**
 *    
 * Title         [解析工具类]
 * Author:       [nighrain]
 * CreateDate:   [2019-06-22--08:59]
 * Version:      [v1.0]
 * Description:  [解析class生成getter/setter方法和oracle-sql语句]
 * <p>
 *     注意: 多表关联的无法自动解析,需要在解析完成后手动处理多表关系
 * </p>
 *  
 */
public class GenerateTool {

    private static final String enumSize = "VARCHAR2(32)";         //reflexMode 枚举对应的字符串的大小
    private static final String stringSize = "VARCHAR2(64)";       //reflexMode String对应的字符串的大小
    private static final String descriptionSize = "VARCHAR2(128)"; //reflexMode 描述类字段对应的字符串的大小
    private static final String[] markArr = {"desc","info","remark"};        //reflexMode 描述类字段对应的字段名的公有标识

    private GenerateTool() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * <p>
     *     读取文件的方式,需要准备文件 D:\w-temp\CREATE_SQL.txt
     *          内容如下:
     *   <eg>
            private String name;			//名称 此处字符串大小默认64,可自定义,
            private String description 128;	//描述 此处字符串大小可自定义,即在字段名后面空一格写字符串大小 此处对应VARCHAR2(128)
            private Integer count;			//个数
            private Double price;		    //价格
            private e-Status status;		//状态 此处是枚举类型Status,应该在类型上加前缀 "e-" 即 e-Status ,否则不识别
            private Date createTime;		//时间
            private o-Shop shop;		    //商家 此处是关联的其他表,应该在类型上加前缀 "o-" 即 o-Shop ,否则不识别
     *   </eg>
     *      把eg标签内的文字粘贴到 D:\w-temp\CREATE_SQL.txt 文件里即可尝试
     *          字符串和枚举对应的varchar2类型大小可自定义
     *              String 的默认大小 VARCHAR2(64)
     *              e-* 的默认大小 VARCHAR2(32)
     *          如 private e-Status status 16;
     *             private String name 255;
     * </p>
     */
//    @Test
    public static void main(String[] a) {
        //读取文件的方式
        //解析文件
        StringBuffer stringBuffer = readFile("D:\\w-temp\\CREATE_SQL.txt");
        //将结果输出到文件
//        FileTools.output2File(stringBuffer, "D:\\.autoGetSet", "Demo3.txt");
    }

    /**
     * <p>
     *     利用反射的方式,该方式方便不用准备源文件了,直接通过类的class文件解析,
     *          解析时个开关: 是否全字段解析,true 则把该类的所有字段解析,适用于新建的表
     *                                     false 则只解析没有get方法的字段,适用于修改表补充字段
     *     枚举 的固定大小 VARCHAR2(32) enumSize
     *     String 的固定大小 VARCHAR2(64) stringSize
     * </p>
     */
    @Test
    public void reflexMode() {

        //要解析的类
        Class<?> clazz = Demo.class;

        StringBuffer stringBuffer = doReflex(clazz, true);
        //将结果输出到文件
        FileTools.output2File(stringBuffer,"D:\\.autoGetSet", clazz.getSimpleName() + ".txt");
    }



    //读取文件方式生成带注解的getter/setter 和oracle-sql语句
    public static StringBuffer readFile(String fileName) {
        StringBuffer sbGetSet = new StringBuffer();//带注解的Getter Setter 方法
        StringBuffer sbSql = new StringBuffer();//oracle-sql语句

        File file = new File(fileName);//文件
        String beginMark = "private"; //开始标识
        String endMark = ";";  //结束标识

        int fieldAll = 0;       //全部字段
        int fieldSpecial = 0;    //需要手动处理的特殊字段

        FileInputStream fis = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
//            sbSql.append("\r\n\r\n\r\n");
            sbSql.append("CREATE TABLE XXX手动配置表名 (\r\n");
            sbSql.append("ID NUMBER NOT NULL PRIMARY KEY ,\r\n");
            sbSql.append("OPTIMISTIC              NUMBER ");

            String row = null;
            //循环读取每一行
            while ((row = br.readLine()) != null) {
                if ("".equals(row.trim())) continue;
                row = row.replaceAll("\\s+", " ").trim();
                if ("".equals(row) || row.startsWith("//")) continue;
//                System.out.println(row);

                //把字符串 "private Long policyNo 256;"截取成 Long policyNo 256
                /**
                 * <p>      String demo1 = "String,policyNo,128;";
                 *         String demo2 = "e-Status,status,32;";
                 *         String demo3 = "o-Customer,customer,32;";
                 * </p>
                 */
                row = row.substring(row.indexOf(beginMark) + beginMark.length(), row.indexOf(endMark)).trim();

//                sbGetSet.append(strGenerateGS(row, " "));
//                sbGetSet.append(row+"\r\n");
                Map<String, String> map = parseStr2Mapper(row, " ");
                sbGetSet = doGenerateGS(sbGetSet, map);     //拼接getter/setter
                sbSql = doGenerateSql(sbSql, map);          //拼接oracle-sql
                fieldAll += 1;
                if (map != null && "o".equals(map.get("javaType"))) fieldSpecial += 1 ; //需要手动处理的字段
            }
            sbSql.append("\r\n)");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //输出本次操作的信息
        StringBuffer sbInfo = new StringBuffer();
        sbInfo.append("---时间: \t\t\t" + DateTools.format(new Date()) + "\r\n")
                .append("---需要手动处理的特殊字段: " + fieldSpecial + " 个" + "\r\n")
                .append("---全部字段: \t\t" + fieldAll + " 个" + "\r\n");
        sbInfo.append("==============================");
        System.out.println(sbInfo.toString());

        return sbInfo.append("\r\n")
                .append(sbGetSet)
                .append( "========================================\r\n")
                .append(sbSql);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     *     String 的固定大小 VARCHAR2(64)
     *     枚举 的固定大小 VARCHAR2(32)
     * </p>
     * @param clazz 要解析的类
     * @param isAllFieldMode 是否全字段模式 true 则把该类的所有字段解析,适用于新建的表
     *                                      false 则只解析没有get方法的字段,适用于修改表补充字段
     * @return 解析后的结果
     */
    public static StringBuffer doReflex(Class clazz, boolean isAllFieldMode) {
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
        int fieldAnalysis = 0;  //本次处理的字段
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
//            Class<?> type = field.getType();
//            System.out.println(type.equals(String.class));
//            System.out.println(type.equals(int.class));
//            System.out.println(type.isEnum());//是否是枚举
//            System.out.println(type.getSimpleName());
//            System.out.println(field.getName());
//            System.out.println("=============");
            if (field.toGenericString().contains("static")) continue;
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

        sbInfo.append("---时间: \t\t\t" + DateTools.format(new Date()) + "\r\n")
                .append("---操作的实体类: \t" + clazz.getName() + "\r\n")
                .append("---对应的表名: \t\t" + tableName + "\r\n")
                .append("---模式: \t\t\t" + (isAllFieldMode ? "全字段模式" : "追加字段模式") + "\r\n")
                .append("---需要手动处理的特殊字段: " + fieldSpecial + " 个" + "\r\n")
                .append("---全部字段: \t\t" + fieldAll + " 个" + "\r\n")
                .append("---本次处理的字段: \t" + fieldAnalysis + " 个" + "\r\n")
                .append("---忽略的字段: \t\t" + fieldIgnore + " 个" + "\r\n")
                .append("---已存在get方法的字段: " + fieldExisting + " 个" + "\r\n");
        sbInfo.append("==============================");
        System.out.println(sbInfo.toString());

        return sbInfo.append("\r\n")
                .append(sbGetSet)
                .append("\r\n=============== oracle-sql ===============\r\n")
                .append(sbSql);
    }
    /////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * <title>
     * 根据 java 字段解析 oracle类型的方法:
     * 可以 自动识别 Integer,Double,Float,Long 等数字类型 对应 NUMBER 类型;
     * Date 对应 DATE 类型;
     * String 对应 VARCHAR2 类型 默认64 可自定义;
     * Enum 枚举 对应 VARCHAR2 类型 默认32 可自定义;
     * 枚举 一定要在属性名前面添加 "e-"
     * 例如 Status status; //状态
     * 转换成 e-Status status; //状态
     * [多表关联] 对象类型 对应的 oracle类型 需要 生成后手动修改
     * <p>
     * 如果 要自定义枚举或String的 对应的varchar2() 的大小 需要 在该行内字段名后面;前面空一格写1~255的数字
     * <p>
     * eg:
     *
     * </title>
     *
     * @param str       e-Status status 32    或 String customerNo 等
     * @param separator 分隔符 一般填写空格 " "
     * @return 包含四个字段的 Map
     */
    private static Map<String, String> parseStr2Mapper(String str, String separator) {
        HashMap<String, String> result = new HashMap<String, String>();
        String str_ = str;
        String[] rows = str_.split(separator);
        if (rows.length < 2) return null; //分割后数组长度小于两个则直接返回空

        int size = 64;  //默认的字符串大小
        int eSize = 32; //默认的枚举对应的字符串的大小
        if (rows.length == 3) {
            //输入的文件里面指定了字符串的大小
            size = Integer.parseInt(rows[2]);
            if (size > 255) size = 255;
            if (size < 1) size = 1;
            eSize = size;
        }

        String oracleType = null;           //字段oracle类型
        result.put("javaType", null);       //常见类型和对象类型
        result.put("fieldType", rows[0]);   //字段的类型
        result.put("field", rows[1]);       //字段名
        result.put("oracleType", null);     //oracle 的类型

        String fieldType = result.get("fieldType");
        if ('-' == fieldType.charAt(1)) {
            //带 "-" 的 非基本类型
            String[] arr = fieldType.split("-");
            result.put("fieldType", arr[1]);
            result.put("javaType", arr[0]);
            if ("e".equals(arr[0])) {
                //enum
                result.put("oracleType", "VARCHAR2(" + eSize + ")");
            }
        } else {
            //常见类型
            result.put("javaType", "-1");
            if ("String".equals(fieldType)) {         //字符串类型
                oracleType = "VARCHAR2(" + size + ")";
            } else if ("Integer".equals(fieldType) || "Double".equalsIgnoreCase(fieldType) ||
                    "Float".equalsIgnoreCase(fieldType) || "Long".equalsIgnoreCase(fieldType) ||
                    "int".equals(fieldType)) {      //数字类型
                oracleType = "NUMBER";
            } else if ("Date".equals(fieldType)) {     //日期类型
                oracleType = "DATE";
            } else {
                //不识别的类型 处理为o
                result.put("javaType", "o");
            }
            result.put("oracleType", oracleType);
        }
        return result;
    }


/////////////////////////////////////////////////////////////////////////////////////////

    //根据包含四个字段的map 拼接getter/setter代码的核心方法
    private static StringBuffer doGenerateGS(StringBuffer sbGetSet, Map<String, String> map) {
        if (null == map) return sbGetSet;
        return doGenerateGS(sbGetSet, map.get("javaType"), map.get("fieldType"), map.get("field"), map.get("oracleType"));
    }

    /**
     * 拼接getter/setter代码的核心方法
     *
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
            sbGetSet.append("@Column(name=\"" + StringTools.hump2Underline(field) + "\" )\r\n");
        } else {
            sbGetSet.append("@Column(name=\"" + StringTools.hump2Underline(field) + "\", columnDefinition = \"" + oracleType + "\")\r\n");
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

/////////////////////////////////////////////////////////////////////////////////////////

    //根据包含三个字段的map 拼接sql代码的方法
    private static StringBuffer doGenerateSql(StringBuffer sbSql, Map<String, String> map) {
        if (null == map) return sbSql;
        return doGenerateSql(sbSql, map.get("javaType"), map.get("field"), map.get("oracleType"));
    }

    /**
     * 拼接sql的核心方法
     *
     * @param sbSql      要往后拼接的 StringBuffer
     * @param javaType   标识该java类型是否可自动解析为oracle类型
     * @param field      驼峰式字段名
     * @param oracleType oracle类型
     * @return 拼接后的StringBuffer
     */
    private static StringBuffer doGenerateSql(StringBuffer sbSql, String javaType, String field, String oracleType) {
        if ("-1".equals(javaType) || "e".equals(javaType)) {        //已自动解析
            sbSql.append(",\r\n" + StringTools.toFixedLength(24,StringTools.hump2Underline(field)) + "\t " + oracleType);
        } else {                                                    //无法自动解析
            sbSql.append(",\r\n" + StringTools.toFixedLength(24,StringTools.hump2Underline(field)) + "\t " + oracleType + " XXX手动处理该字段");
        }
        return sbSql;
    }
/////////////////////////////////////////////////////////////////////////////////////////



}
