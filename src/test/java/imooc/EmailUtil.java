package imooc;

import java.util.List;

//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.HtmlEmail;
//import play.Logger;


//作者：霜花似雪
//链接：https://www.imooc.com/article/42293
//来源：慕课网
//本文原创发布于慕课网 ，转载请注明出处，谢谢合作
/**
 * 发送邮件工具类
 *
 * @description 
 *
 * @author lhf
 * @createDate 2018年7月7日
 */
public class EmailUtil {
//
//    /**
//     * 发送HMTL格式的邮件
//     *
//     * @param emailWebsite 邮件服务器地址
//     * @param mailAccount 发送邮件的账号
//     * @param mailPassword 发送邮件的密码
//     * @param toEmail 接收邮件的地址
//     * @param title 邮件的标题
//     * @param content 邮件的内容
//     * @return
//     *
//     * @author DaiZhengmiao
//     * @createDate 2015年12月18日
//     */
//    public static boolean sendHtmlEmail(String emailWebsite,String mailAccount,String mailPassword,String toEmail,String title,String content){
//        boolean flag = false;
//
//        try {
//
//            HtmlEmail sendEmail = new HtmlEmail();
//
//            sendEmail.setHostName(emailWebsite);
//            sendEmail.setAuthentication(mailAccount, mailPassword);
//            Logger.info("from email "+mailAccount);
//            sendEmail.setFrom(mailAccount);
//            //sendEmail.setSSL(true);
//            //sendEmail.setSmtpPort(465);
//            sendEmail.addTo(toEmail);
//
//            sendEmail.setSubject(title);
//            sendEmail.setCharset("utf-8");
//            sendEmail.setHtmlMsg(content);
//            sendEmail.send();
//            flag = true;
//        } catch (EmailException e) {
//            e.printStackTrace();
//            //LoggerUtil.error(false, "发送邮件时失败【toEmail:%s,title:%s】", toEmail,title);
//            System.out.println("你发出的邮件绕了地球一圈，发送失败了！");
//        }
//
//        return flag;
//    }
//
//    /**
//     * 发送HMTL格式的邮件(带附件)
//     *
//     * @param emailWebsite 邮件服务器地址
//     * @param mailAccount 发送邮件的账号
//     * @param mailPassword 发送邮件的密码
//     * @param toEmail 接收邮件的地址
//     * @param title 邮件的标题
//     * @param content 邮件的内容
//     * @param attachmentPath 附件的路径
//     * @param attachmentName 附件的名称
//     * @return
//     */
//    public static boolean sendHtmlEmail(String emailWebsite,String mailAccount,String mailPassword,String toEmail,String title,String content,String attachmentPath,String attachmentName){
//        boolean flag = false;
//        try {
//
//            EmailAttachment attachment = new EmailAttachment();
//            attachment.setPath(attachmentPath);
//            attachment.setDisposition(EmailAttachment.ATTACHMENT);
//            attachment.setName(attachmentName);
//
//            HtmlEmail sendEmail = new HtmlEmail();
//
//            sendEmail.setHostName(emailWebsite);
//            sendEmail.setAuthentication(mailAccount, mailPassword);
//            Logger.info("from email"+mailAccount);
//            sendEmail.setFrom(mailAccount);
//
//            sendEmail.addTo(toEmail);
//
//            sendEmail.setSubject(title);
//            sendEmail.setCharset("utf-8");
//            sendEmail.setHtmlMsg(content);
//            sendEmail.attach(attachment);
//            sendEmail.send();
//            flag = true;
//        } catch (EmailException e) {
//            //LoggerUtil.error(false, "发送邮件时失败【toEmail:%s,title:%s】", toEmail,title);
//            System.out.println("你发出的邮件绕了地球一圈，发送失败了！");
//        }
//
//        return flag;
//    }
//
//    /**
//     *
//     * @param emailWebsite    邮件服务器地址
//     * @param mailAccount  发送邮件的账号
//     * @param mailPassword  发送邮件的密码
//     * @param toEmails  收件人地址
//     * @param title  邮件主题
//     * @param content   邮件内容
//     * @return
//     * @description
//     */
//    public static boolean sendHtmlEmail(String emailWebsite,String mailAccount,String mailPassword,List toEmails,String title,String content){
//        boolean flag = false;
//
//        try {
//
//            HtmlEmail sendEmail = new HtmlEmail();
//
//            sendEmail.setHostName(emailWebsite);
//            sendEmail.setAuthentication(mailAccount, mailPassword);
//            Logger.info("from email"+mailAccount);
//            sendEmail.setFrom(mailAccount);
//
//            for (String email : toEmails) {
//                sendEmail.addTo(email);
//            }
//
//            sendEmail.setSubject(title);
//            sendEmail.setCharset("utf-8");
//            sendEmail.setMsg(content);
//            sendEmail.send();
//            flag = true;
//        } catch (EmailException e) {
//            //LoggerUtil.error(false, "发送邮件时失败【toEmail:%s,title:%s】", toEmails,title);
//            System.out.println("你发出的邮件绕了地球一圈，发送失败了！");
//        }
//
//        return flag;
//    }
//
//    public static void main(String[] args) {
//        //邮件服务器地址：QQ:smtp.qq.com;  136:smtp.163.com;  126:smtp.126.com;  新浪：smtp.sina.com.cn; 雅虎:smtp.mail.yahoo.com
//        String emailWebsite = "smtp.163.com";
//        String mailAccount = "lhf2013xxxx@163.com";
//        String mailPassword = "123456";
//        String toEmail = "2510736432@qq.com";
//        String title="你好！";
//        String content = "<h1>交个朋友吧!</h1><h1>";
//        boolean flag = sendHtmlEmail(emailWebsite,mailAccount,mailPassword,toEmail,title,content);
//        if(flag){
//            System.out.println("邮件发送成功");
//        }else{
//            System.out.println("很遗憾！邮件发送失败！");
//        }
//    }
}

