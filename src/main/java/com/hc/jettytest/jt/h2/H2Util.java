package com.hc.jettytest.jt.h2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import java.security.MessageDigest;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hc.jettytest.jt.bean.QfEntry;

public class H2Util {
	
	// Slf4jLog
	public final static Logger logger = Log.getLogger(H2Util.class);
	
	private final static Properties prop = new Properties();//属性集合对象

	private static JdbcConnectionPool pool;
	
	public final static Map<String, String> meta = new HashMap<String, String>(10);
	
	private static final Writer QR_WRITER = new MultiFormatWriter();

	static {
		
		InputStream is;
		
		try {

			logger.info("load jt.properties ...");
			
			is = ClassLoader.getSystemResourceAsStream("jt.properties"); //属性文件流      
			
			prop.load(is); //将属性文件流装载到Properties对象中 

		} catch (FileNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	
	static {

			Connection conn = null;
			
			Statement stmt = null;
			
			ResultSet rs = null;
			
			try {

				logger.info("load INFORMATION_SCHEMA of H2 ...");
				
				conn = connect();
			
				String sql =      "SELECT                          \n"
								+ "  COLUMN_NAME,                  \n"
								+ "  REMARKS                       \n"
								+ "FROM INFORMATION_SCHEMA.COLUMNS \n"
								+ "WHERE TABLE_NAME = 'E_VEHICLE_INFO';";

				logger.info(sql);
				
				stmt = conn.createStatement();
				
				
				rs = stmt.executeQuery(sql);
	
				while(rs.next()) {
					meta.put(rs.getString(1), rs.getString(2));
					logger.info(" values : " + rs.getString(1) + " , " + rs.getString(2));
				}
			
			} catch(Exception n) {
				n.printStackTrace();
			} finally {
				
				try {
					
					rs.close();
					stmt.close();
					conn.close();
					
				} catch (SQLException ex) {
					ex.printStackTrace();
					
				} 
			}

	}
	
	private static Connection connect() throws SQLException {
		
		if (pool == null) {
			pool = JdbcConnectionPool.create( get("h2.url"), "sa", "");
		}
		
		return pool.getConnection();
		
	}
	
//	public static String insert(QfEntry e)  {
//		
//		Connection conn = null;
//		
//		PreparedStatement stmt = null;
//		
//		try {
//
//			conn = connect();
//		
//			String sql =      "INSERT INTO E_VEHICLE_INFO ("
//							+ "  nation,"
//							+ "  company,"
//							+ "  serialNum,"
//							+ "  prodName,"
//							+ "  weight,"
//							+ "  seatCount,"
//							+ "  motorType,"
//							+ "  capacity,"
//							+ "  battery,"
//							+ "  manuDate,"
//							+ "  sealer,"
//							+ "  sealerTel,"
//							+ "  price,"
//							+ "  load_stamp"
//							+ ") "
//							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";
//			
//		stmt = conn.prepareStatement(sql);
//		
//		// 制造国
//		stmt.setString(1, e.getNation());
//		
//		// 制造公司
//		stmt.setString(2, e.getCompany());
//		
//		// 车架号
//		stmt.setString(3, e.getSerialNum());
//		
//		// 车型名称
//		stmt.setString(4, e.getProdName());
//		
//		// 车重
//		stmt.setString(5, e.getWeight());
//		
//		// 乘员数
//		stmt.setString(6, e.getSeatCount());
//		
//		// 电动机型号
//		stmt.setString(7, e.getMotorType());
//		
//		// 功率
//		stmt.setString(8, e.getCapacity());
//		
//		// 蓄电池
//		stmt.setString(9, e.getBattery());
//		
//		// 制造日期
//		stmt.setString(10, e.getManuDate());
//		
//		// 销售商
//		stmt.setString(11, e.getSealer());
//		
//		// 销售商联系方式
//		stmt.setString(12, e.getSealerTel());
//		
//		// 价格
//		stmt.setString(13, e.getPrice());
//		
//		// 时间
//		stmt.setLong(14, System.currentTimeMillis());
//		
//		stmt.executeUpdate();
//			
//		} catch(Exception n) {
//			n.printStackTrace();
//		} finally {
//			
//			try {
//				stmt.close();
//				conn.close();
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//				
//			} 
//		}
//		
//		List<QfEntry> es = selectBySerialNum(e.getSerialNum());
//		
//		if(es.size() > 0) {
//			return es.get(0).getId() + "#" + H2Util.MD5(e.getSerialNum()) + " => " + e.getSerialNum();
//		} else {
//			return Long.valueOf(0) + "#" + "" + " => " + e.getSerialNum();
//		}
//
//	}

	public static String insert(QfEntry e)  {
		
		List<QfEntry> params = new ArrayList<QfEntry>(1);
		
		params.add(e);
		
		return inserts(params).get(0);
	}
	
	/**
	 * 
	 * @param ee
	 * @return
	 */
	public static List<String> inserts(List<QfEntry> ee)  {
		
		Connection conn = null;
		
		PreparedStatement stmt = null;
		
		try {

			conn = connect();
		
			String sql =      "INSERT INTO E_VEHICLE_INFO ("
							+ "  nation,"
							+ "  company,"
							+ "  serialNum,"
							+ "  prodName,"
							+ "  weight,"
							+ "  seatCount,"
							+ "  motorType,"
							+ "  capacity,"
							+ "  battery,"
							+ "  manuDate,"
							+ "  sealer,"
							+ "  sealerTel,"
							+ "  price,"
							+ "  load_stamp"
							+ ") "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";
			
		logger.info(sql);
		
		stmt = conn.prepareStatement(sql);
		
		long timestamp = System.currentTimeMillis();
		
		for(QfEntry e : ee) {
				
			// 制造国
			stmt.setString(1, e.getNation());
			
			// 制造公司
			stmt.setString(2, e.getCompany());
			
			// 车架号
			stmt.setString(3, e.getSerialNum());
			
			// 车型名称
			stmt.setString(4, e.getProdName());
			
			// 车重
			stmt.setString(5, e.getWeight());
			
			// 乘员数
			stmt.setString(6, e.getSeatCount());
			
			// 电动机型号
			stmt.setString(7, e.getMotorType());
			
			// 功率
			stmt.setString(8, e.getCapacity());
			
			// 蓄电池
			stmt.setString(9, e.getBattery());
			
			// 制造日期
			stmt.setString(10, e.getManuDate());
			
			// 销售商
			stmt.setString(11, e.getSealer());
			
			// 销售商联系方式
			stmt.setString(12, e.getSealerTel());
			
			// 价格
			stmt.setString(13, e.getPrice());
			
			// 时间
			stmt.setLong(14, timestamp);
			
			stmt.addBatch();
		
		}
		
		stmt.executeBatch();
			
		} catch(Exception n) {
			n.printStackTrace();
			logger.warn(n.getMessage());
		} finally {
			
			try {
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		List<String> retVal = new ArrayList<String>();
		
		for(QfEntry e : ee) {
			
			List<QfEntry> es = selectBySerialNum(e.getSerialNum());
		
			// len#id#MD5#SerialNum
			if(es.size() > 0) {
				String id = String.valueOf(es.get(0).getId());
				retVal.add(Integer.toHexString(id.length()).toUpperCase() + "#" + id + "#" + H2Util.MD5(e.getSerialNum()) + "#" + e.getSerialNum());
			} else {
				retVal.add("1#" + Long.valueOf(0) + "#" + "" + "#" + e.getSerialNum());
			}
		}
		
		return retVal;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static List<QfEntry> select(Long id)  {
		
		Connection conn = null;
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		List<QfEntry> result = new ArrayList<QfEntry>(1);
		
		QfEntry qf = null;
		
		try {

			conn = connect();
		
			String sql =      "SELECT       \n"
							+ "  nation,    \n"
							+ "  company,   \n"
							+ "  serialNum, \n"
							+ "  prodName,  \n"
							+ "  weight,    \n"
							+ "  seatCount, \n"
							+ "  motorType, \n"
							+ "  capacity,  \n"
							+ "  battery,   \n"
							+ "  manuDate,  \n"
							+ "  sealer,    \n"
							+ "  sealerTel, \n"
							+ "  price      \n"
							+ "FROM E_VEHICLE_INFO \n"
							+ "WHERE id = " + id;
			
		stmt = conn.createStatement();
		
		
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			
			qf = new QfEntry();
			
			qf.setId(id);
			
			qf.setNation(rs.getString(1));
			
			qf.setCompany(rs.getString(2));
			
			qf.setSerialNum(rs.getString(3));
			
			qf.setProdName(rs.getString(4));
			
			qf.setWeight(rs.getString(5));
			
			qf.setSeatCount(rs.getString(6));
			
			qf.setMotorType(rs.getString(7));
			
			qf.setCapacity(rs.getString(8));
			
			qf.setBattery(rs.getString(9));
			
			qf.setManuDate(rs.getString(10));
			
			qf.setSealer(rs.getString(11));
			
			qf.setSealerTel(rs.getString(12));
			
			qf.setPrice(rs.getString(13));
			
			result.add(qf);
		}
		
		} catch(Exception n) {
			n.printStackTrace();
		} finally {
			
			try {
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private static List<QfEntry> selectBySerialNum(String serialNum)  {
		
		Connection conn = null;
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		List<QfEntry> result = new ArrayList<QfEntry>(1);
		
		QfEntry qf = null;
		
		try {

			conn = connect();
		
			String sql =      "SELECT       \n"
							+ "  id         \n"
							+ "FROM E_VEHICLE_INFO \n"
							+ "WHERE serialNum = '" + serialNum + "' ORDER BY id DESC";
			
		stmt = conn.createStatement();
		
		
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			
			qf = new QfEntry();
			
			qf.setId(rs.getLong(1));
			
			result.add(qf);
		}
		
		} catch(Exception n) {
			n.printStackTrace();
		} finally {
			
			try {
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param YYYY/MM/DD
	 * @return
	 */
	public static List<QfEntry> selectByManuDate(String manuDate)  {
		
		Connection conn = null;
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		List<QfEntry> result = new ArrayList<QfEntry>(1);
		
		QfEntry qf = null;
		
		try {

			conn = connect();
		
			String sql =      "SELECT       \n"
							+ "  id,        \n"
							+ "  serialNum  \n"
							+ "FROM E_VEHICLE_INFO \n"
							+ "WHERE manuDate = '" + manuDate + "' ORDER BY serialNum ASC, id DESC;";
			
		stmt = conn.createStatement();
		
		
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			
			qf = new QfEntry();
			
			qf.setId(rs.getLong(1));
			
			qf.setSerialNum(rs.getString(2));
			
			result.add(qf);
		}
		
		} catch(Exception n) {
			n.printStackTrace();
		} finally {
			
			try {
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static List<QfEntry> selectByStamp(String stamp)  {
		
		Connection conn = null;
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		List<QfEntry> result = new ArrayList<QfEntry>(1);
		
		QfEntry qf = null;
		
		 DateFormat stdFormat = new SimpleDateFormat("yyyy/MM/dd");
		 
		 Date d = null;
		 
		try {
			d = stdFormat.parse(stamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 long stamp1 = d.getTime(); 
		 
		 long stamp2 = stamp1 + 1 * 24 * 60 * 60 * 1000;
		
		try {

			conn = connect();
		
			String sql =      "SELECT       \n"
							+ "  id,        \n"
							+ "  serialNum  \n"
							+ "FROM E_VEHICLE_INFO \n"
							+ "WHERE load_stamp > " + stamp1 + " AND load_stamp < " + stamp2 + " ORDER BY serialNum ASC, id DESC;";
			
		logger.info(sql);
		
		stmt = conn.createStatement();
		
		
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			
			qf = new QfEntry();
			
			qf.setId(rs.getLong(1));
			
			qf.setSerialNum(rs.getString(2));
			
			result.add(qf);
		}
		
		} catch(Exception n) {
			n.printStackTrace();
		} finally {
			
			try {
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		return result;
	}
	
	public static Long count()  {
		
		Connection conn = null;
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		Long result = null;
		
		try {

			conn = connect();
		
			String sql =      "SELECT       \n"
							+ "  count(id)  \n"
							+ "FROM E_VEHICLE_INFO \n"
							+ ";";
			
		stmt = conn.createStatement();

		logger.info(sql);
		
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			
			result = rs.getLong(1);
			
			break;
		}
		
		} catch(Exception n) {
			n.printStackTrace();
		} finally {
			
			try {
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} 
		}
		
		return result;
	}
	


	public final static String get(String key ) {
		
		if(prop.containsKey(key)) {
			return prop.getProperty(key);
		}
		
		return null;
	}
	
	public final static String get(String key, String defautVal ) {
		
		if(prop.containsKey(key)) {
			return prop.getProperty(key);
		}
		
		return defautVal;
	}
	
	    public final static String MD5(String s) {
	    	
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
	        try {
	        	
	            byte[] btInput = s.getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public static final String[] extract(String origin, String delim) {
	    	
	    	String[] result;
	    	
	    	if (origin == null) return new String[]{"", ""};
	    	
	    	result = origin.split(delim);
	    	
	    	if(result.length > 1) {
	    		return result;
	    	} else {
	    		return new String[]{result[0], ""};
	    	}
	    }
	    
	    public static final String[] extract(String origin) {
	    	Character head;
	    	byte len;
	    	
	    	if (origin == null) return new String[]{"", ""};
	    	
	    	head = origin.charAt(0);
	    	
	    	len = Byte.valueOf(String.valueOf(head), 16);
	    	
	    	return new String[]{origin.substring(1, len + 1), origin.substring(len + 1)};
	    }
	    
	    public static void main(String[] args) {
	    	// count();
	    	// selectByManuDate("2015/09/09");
	    	// System.out.println(validDate("205/19/09"));
	    	
	    	String json = "[{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/13','motorType':'EX4','nation':'中华人民共和国','price':'8999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'ZX500Z8010JQF003','weight':'550Kg'},{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/13','motorType':'EX3','nation':'中华人民共和国','price':'9999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'ZX500Z8010JQF004','weight':'550Kg'} ]";
	    	
	    	List<QfEntry> ee = JSON.parseArray(json, QfEntry.class);
	    	
	    	inserts(ee);

	    }
	    
	    public static String validDate(String str) {
	        
	    	if(str == null)  return null;
	    	
	        List<DateFormat> formats = new ArrayList<DateFormat>(1);
	        List<DateFormat> errors = new ArrayList<DateFormat>(1);
	        

	        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
	        
	         DateFormat stdFormat = new SimpleDateFormat("yyyy/MM/dd");
	         DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	         DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
	         
	         formats.add(stdFormat);
	         formats.add(format1);
	         formats.add(format2);
	         
	         for(DateFormat f : formats) {
		         
		         try {
		        	 
		        	 // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期
		        	 // 比如2007/02/29会被接受，并转换成2007/03/01
		            f.setLenient(false);
		            return stdFormat.format(f.parse(str));
		            
		         } catch (ParseException e) {
		        	 errors.add(f);
		         } 
	         }
	         
	         return null;
	  }
	
	    /**
	     * 
	     * @param request   <request>
	     * @param param     <Array : len#id#MD5#SerialNum>
	     * @return
	     */
	public final static String makeRequestURL(HttpServletRequest request, String[] param) {
		
		String reqURL = request.getRequestURL().toString();

		logger.info(reqURL.toString());
		
		return reqURL.replace("push", "pull") + "?" + "id=" + param[0] + param[1] + param[2];

	}
	

	/**
	 * 
	 * 生成qr图像，返回图像对应的资源url
	 * 
	 * @param request
	 * @param content
	 * @param ymdPath
	 * @param fileName
	 * @return
	 */
	public static String encodeURL(HttpServletRequest request,String content, String ymdPath, String fileName) {
		
		int width = 200; // 图像宽度
		int height = 200; // 图像高度
		String format = "png";// 图像类型
		
    	File p = new File(H2Util.get("qr.encode.base") + "/" + ymdPath);

    	p.mkdirs();
		
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		
		try {
			
			BitMatrix bitMatrix = QR_WRITER.encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
			
			Path path = FileSystems.getDefault().getPath(p.getPath(), fileName + "." + format);
	
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
		
		} catch(IOException e) {

			logger.warn(e);
			e.printStackTrace();
			
		} catch (WriterException e) {

			e.printStackTrace();
			logger.warn(e);
		}
		
		return request.getRequestURL().toString()
				.replace("push", "res") + ymdPath + "/" + fileName + "." + format;
	}
	
    public static String yyyymmdd() {
        

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；

         DateFormat f = new SimpleDateFormat("yyyyMMdd");

	        
         try{

	            f.setLenient(true);
	            return f.format(new Date(System.currentTimeMillis()));
	            
	         } catch (Exception e) {
	        	 logger.warn(e);
	        	 return "default";
	         } 
     }
}
