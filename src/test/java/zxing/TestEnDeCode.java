package zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

//https://www.cnblogs.com/tv151579/p/3516531.html
public class TestEnDeCode {

    public TestEnDeCode() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestEnDeCode t = new TestEnDeCode();
        t.encode();
//        t.decode();
    }

    // 编码
    public void encode() {
        try {
            String str = "CN:男;COP:公司;ZW:职务";// 二维码内容
            String path = "D:\\hwy.png";
            BitMatrix byteMatrix;
            //
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();

            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            //
            byteMatrix = new MultiFormatWriter().encode(
                 // 在Zxing转码之前，手动转码，避免了中文乱码的错误
                    new String(str.getBytes(), "iso-8859-1"),
                 BarcodeFormat.QR_CODE, 500, 500,hints);
            File file = new File(path);

            MatrixToImageWriter.writeToFile(byteMatrix, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解码
    public void decode() {
        try {
            String imgPath = "D:\\hwy.png";
            File file = new File(imgPath);
            BufferedImage image;
            try {
                image = ImageIO.read(file);
                if (image == null) {
                    System.out.println("Could not decode image");
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                        source));
                Result result;
                Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                result = new MultiFormatReader().decode(bitmap, hints);
                String resultStr = result.getText();
                System.out.println(resultStr);

            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            } catch (ReaderException re) {
                System.out.println(re.toString());
            }

        } catch (Exception ex) {

        }
    }

}