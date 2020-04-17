package gs.qrcode;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码工具类
 * 
 */
public class QRCodeUtil {

	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "JPG";

	/**
	 * 
	 * @param content
	 * @param imgPath
	 * @param needCompress
	 * @param qrCodeSize
	 * @param logoWidth
	 * @param logoHeight
	 * @return
	 * @throws Exception
	 */
	private static BufferedImage createImage(String content, String imgPath,
			boolean needCompress,int qrCodeSize,int logoWidth,int logoHeight) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();

		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
						: 0xFFFFFFFF);
			}
		}
		if (imgPath == null || "".equals(imgPath)) {
			return image;
		}
		// 插入图片
		QRCodeUtil.insertImage(image, imgPath, needCompress,qrCodeSize,logoWidth,logoHeight);
		return image;
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @param qrCodeSize  二维码尺寸
	 * @param logoWidth  logo宽度
	 * @param logoHeight logo高度
	 * @throws Exception
	 */
	private static void insertImage(BufferedImage source, String imgPath,
			boolean needCompress,int qrCodeSize,int logoWidth,int logoHeight) throws Exception {
		File file = new File(imgPath);
		if (!file.exists()) {
			System.err.println(""+imgPath+"   该文件不存在！");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > logoWidth) {
				width = logoWidth;
			}
			if (height > logoHeight) {
				height = logoHeight;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (qrCodeSize - width) / 2;
		int y = (qrCodeSize - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 生成二维码（内嵌logo）
	 * @param content 内容
	 * @param logoPath  logo路径
	 * @param destPath 二维码存放路径
	 * @param qrCodeSize  二维码尺寸
	 * @param logoWidth  logo宽度
	 * @param logoHeight logo高度
	 * @throws Exception
	 */
	public static void encode(String content, String logoPath, String destPath,
			int qrCodeSize,int logoWidth,int logoHeight) throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, logoPath,
				true,qrCodeSize,logoWidth,logoHeight);
		ImageIO.write(image, FORMAT_NAME, new File(destPath));
	}
	
	/**
	 * 生成二维码（内嵌logo）
	 * @param content 内容
	 * @param logoPath  logo路径
	 * @param output 二维码输出流
	 * @param qrCodeSize  二维码尺寸
	 * @param logoWidth  logo宽度
	 * @param logoHeight logo高度
	 * @throws Exception
	 */
	public static void encode(String content, String logoPath, OutputStream output,
			int qrCodeSize,int logoWidth,int logoHeight) throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, logoPath,
				true,qrCodeSize,logoWidth,logoHeight);
		ImageIO.write(image, FORMAT_NAME, output);
	}

	public static void main(String[] args) throws Exception {
		String customerNo = "8619138923";
		String text = "http://localhost:8080/mpos/jsp/registeCustomer.jsp?recommendNo="+customerNo;
		String logoPath = "D:\\w-temp\\1234.jpg";
		String basePath = "D:\\w-temp\\";
		String filePath = basePath + customerNo + "_qrCode.jpg";
		int qrCodeSize = 200;
		int logoWidth = 60;
		int logoHeight = 60;
		QRCodeUtil.encode(text,null, filePath,qrCodeSize,logoWidth,logoHeight);
		
//		String result ="";
//		System.out.println(result);
	}
}
