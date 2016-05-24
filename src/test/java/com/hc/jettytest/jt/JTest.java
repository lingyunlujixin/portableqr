package com.hc.jettytest.jt;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
// import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// import org.h2.jdbcx.JdbcConnectionPool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hc.jettytest.jt.h2.H2Util;

public class JTest {

	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		//QfEntry e = H2Util.select(2L).get(0);
		
		//String s = JSON.toJSONString(e);
		
		//System.out.println(s);
		
		//String json = "{\"battery\":\"12V46AH\",\"capacity\":\"500kw\",\"company\":\"庆丰\",\"id\":4,\"manuDate\":\"2015/1/1\",\"motorType\":\"EX2\",\"nation\":\"中国\",\"price\":\"5000\",\"prodName\":\"初代车\",\"sealer\":\"销售商1\",\"sealerTel\":\"0635-09090909\",\"seatCount\":\"4\",\"serialNum\":\"EX500Z8010JQF124\",\"weight\":\"500Kg\"}";
		
		//e = JSON.parseObject(json, QfEntry.class);
		
		//System.out.println(e.toHTMLString());
		
		System.out.println(H2Util.MD5("EX500Z8010JQF124"));
		String origin = "41991AX";
		Byte len = 4;
		System.out.println(origin.substring(1, len + 1) + " , " + origin.substring(len + 1));
		
//	  JdbcConnectionPool pool = null;
//		
//
//			pool = JdbcConnectionPool.create( H2Util.get("h2.url"), "sa", "");
//
//			pool = JdbcConnectionPool.create( H2Util.get("h2.url"), "sa", "");
		
	}
	
	/**
	 * 生成图像
	 * 
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void encodeURL(String content, String fileName, String filePath) throws WriterException, IOException {
//		String filePath = "D://";
//		String fileName = "zxing.png";
//		JSONObject json = new JSONObject();
//		json.put(
//				"zxing",
//				"https://github.com/zxing/zxing/tree/zxing-3.0.0/javase/src/main/java/com/google/zxing");
//		json.put("author", "shihy");
//		String content = json.toJSONString();// 内容
		Writer w = new MultiFormatWriter();
		int width = 200; // 图像宽度
		int height = 200; // 图像高度
		String format = "png";// 图像类型
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = w.encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		Path path = FileSystems.getDefault().getPath(filePath, fileName);
		MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
		System.out.println("输出成功.");
		
	}

}
