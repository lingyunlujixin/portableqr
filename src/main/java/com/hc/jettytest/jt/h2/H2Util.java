package com.hc.jettytest.jt.h2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;
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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hc.jettytest.jt.bean.QfEntry;

/**
 * 
 * <br> 功能类 </br>
 * 
 * <br> 跟H2数据库有关的操作，资源文件载入、及其他通用功能类 </br>
 * 
 * @author Lujx
 *
 */
public class H2Util {

    // Slf4jLog
    public final static Logger logger = Log.getLogger(H2Util.class);

    /**
     * 资源文件属性信息
     */
    private final static Properties prop = new Properties();//属性集合对象

    /**
     * 连接池
     */
    private static JdbcConnectionPool pool;

    /**
     * 字段信息元数据
     */
    public final static Map<String, String> meta = new HashMap<String, String>(10);

    /**
     * 生成QR用
     */
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

    /**
     * 
     * 获取数据库连接
     * 
     * @return
     * @throws SQLException
     */
    private static Connection connect() throws SQLException {

        if (pool == null) {
            pool = JdbcConnectionPool.create( get("h2.url"), "sa", "");
        }

        return pool.getConnection();

    }

//    public static String insert(QfEntry e)  {
//        
//        Connection conn = null;
//        
//        PreparedStatement stmt = null;
//        
//        try {
//
//            conn = connect();
//        
//            String sql =      "INSERT INTO E_VEHICLE_INFO ("
//                            + "  nation,"
//                            + "  company,"
//                            + "  serialNum,"
//                            + "  prodName,"
//                            + "  weight,"
//                            + "  seatCount,"
//                            + "  motorType,"
//                            + "  capacity,"
//                            + "  battery,"
//                            + "  manuDate,"
//                            + "  sealer,"
//                            + "  sealerTel,"
//                            + "  price,"
//                            + "  load_stamp"
//                            + ") "
//                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";
//            
//        stmt = conn.prepareStatement(sql);
//        
//        // 制造国
//        stmt.setString(1, e.getNation());
//        
//        // 制造公司
//        stmt.setString(2, e.getCompany());
//        
//        // 车架号
//        stmt.setString(3, e.getSerialNum());
//        
//        // 车型名称
//        stmt.setString(4, e.getProdName());
//        
//        // 车重
//        stmt.setString(5, e.getWeight());
//        
//        // 乘员数
//        stmt.setString(6, e.getSeatCount());
//        
//        // 电动机型号
//        stmt.setString(7, e.getMotorType());
//        
//        // 功率
//        stmt.setString(8, e.getCapacity());
//        
//        // 蓄电池
//        stmt.setString(9, e.getBattery());
//        
//        // 制造日期
//        stmt.setString(10, e.getManuDate());
//        
//        // 销售商
//        stmt.setString(11, e.getSealer());
//        
//        // 销售商联系方式
//        stmt.setString(12, e.getSealerTel());
//        
//        // 价格
//        stmt.setString(13, e.getPrice());
//        
//        // 时间
//        stmt.setLong(14, System.currentTimeMillis());
//        
//        stmt.executeUpdate();
//            
//        } catch(Exception n) {
//            n.printStackTrace();
//        } finally {
//            
//            try {
//                stmt.close();
//                conn.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                
//            } 
//        }
//        
//        List<QfEntry> es = selectBySerialNum(e.getSerialNum());
//        
//        if(es.size() > 0) {
//            return es.get(0).getId() + "#" + H2Util.MD5(e.getSerialNum()) + " => " + e.getSerialNum();
//        } else {
//            return Long.valueOf(0) + "#" + "" + " => " + e.getSerialNum();
//        }
//
//    }

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
                            + "  sizeof,"
                            + "  tire,"
                            + "  detail,"
                            + "  load_stamp"
                            + ") "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";

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
            
            // 尺寸
            stmt.setString(14, e.getSizeof());
            
            // 轮胎
            stmt.setString(15, e.getTire());
            
            // 详细配置
            stmt.setString(16, e.getDetail());

            // 时间
            stmt.setLong(17, timestamp);

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

        // 把插入的关键信息反馈给客户端
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
                            + "  price    , \n"
                            + "  sizeof   , \n"
                            + "  tire     , \n"
                            + "  detail     \n"
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
            
            qf.setSizeof(rs.getString(14));
            
            qf.setTire(rs.getString(15));
            
            qf.setDetail(rs.getString(16));

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

            // String json = "[{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/13','motorType':'EX4','nation':'中华人民共和国','price':'8999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'ZX500Z8010JQF003','weight':'550Kg'},{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/13','motorType':'EX3','nation':'中华人民共和国','price':'9999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'ZX500Z8010JQF004','weight':'550Kg'} ]";

            // List<QfEntry> ee = JSON.parseArray(json, QfEntry.class);

            // inserts(ee);
        	// System.out.println(replaceLast(""));
        	// pressText("abcde", "E:\\tmp\\imgs\\20160901\\2337C4A5D2B0E89EF330C77F881AE9C3740_p.png", "E:\\tmp\\imgs\\20160901\\2337C4A5D2B0E89EF330C77F881AE9C3740.png");

        }

        public static String formatDate(String str) {

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
    
    public final static String makeRequestId(Long autoId, String serialNum) {
    	
    	return Integer.toHexString(String.valueOf(autoId).length()).toUpperCase() + autoId + MD5(serialNum);
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
    public static String encodeURL(HttpServletRequest request,String content, String ymdPath, String[] s) {

        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型
        String fileName = s[0] + s[1] + s[2];

        File p = new File(H2Util.get("qr.encode.base") + "/" + ymdPath);

        p.mkdirs();

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();

        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {

            BitMatrix bitMatrix = QR_WRITER.encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵

            Path path = FileSystems.getDefault().getPath(p.getPath(), fileName + "." + format);

            MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
            
            // 加入文本
            pressText(s[3], path.toString().replace(".", "_2."), path.toString());

        } catch(IOException e) {

            logger.warn(e);
            e.printStackTrace();

        } catch (WriterException e) {

            e.printStackTrace();
            logger.warn(e);
        }

        return request.getRequestURL().toString()
                .replace("push", "res") + ymdPath + "/" + fileName + "_2." + format;
    }
    
    /**
     * @为图片添加文字
     * 
     * @param pressText 文字
     * @param newImg    带文字的图片
     * @param targetImg 需要添加文字的图片
     * @param fontStyle 
     * @param color
     * @param fontSize
     * @param width
     * @param heigh
     */
    public static void pressText(String pressText, String newImg, String targetImg) { 
        
        try {
        	
            File file = new File(targetImg);
            
            Image src = ImageIO.read(file);
            
            int imageW = src.getWidth(null);
            
            int imageH = src.getHeight(null);
            
            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            
            Graphics g = image.createGraphics();
            
            //计算文字开始的位置
            
            //x开始的位置：（图片宽度-字体大小*字的个数）/2
            
            int fontW = 0;
            for(int i = 0; i < pressText.length(); i++) {
            	fontW += g.getFontMetrics().charWidth(pressText.charAt(i));
            }
            
            int startX = (imageW - fontW) / 2;
            
            //y开始的位置：图片高度-（图片高度-图片宽度）/2
            
            int startY = imageH-(imageH-imageW)/2 - 13;   
            
            g.setColor(Color.BLACK);
            
            g.drawImage(src, 0, 0, imageW, imageH, null);

            g.drawString(pressText, startX, startY);
            
            g.dispose();

            FileOutputStream out = new FileOutputStream(newImg);
            
            ImageIO.write(image, "png", out);
//            
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            
//            encoder.encode(image);
            
            out.close();
            
            System.out.println("image press success");
            
        } catch (Exception e) {
        	
        	e.printStackTrace();
            System.out.println(e);
        }
    }

    public static String yyyymmdd() {

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；

         DateFormat f = new SimpleDateFormat("yyyyMMdd");


         try {

                f.setLenient(true);
                return f.format(new Date(System.currentTimeMillis()));

             } catch (Exception e) {
            	 
                 logger.warn(e);
                 
                 return "default";
             } 
     }
    
    private static String replaceLast(String target, String oldStr, String newStr) {
    	
    	if(target == null) {
    		return null;
    	}
    	
    	int pos = target.lastIndexOf(oldStr);
    	
    	return target.substring(0, pos) + newStr + target.substring(pos + oldStr.length());
    	
    }
}
