# portableqr
小型企业使用，二维码随扫随显RESTful 接口（Jetty Based）

使用到的其它开源项目：
* Jetty
* zxing
* fastjson
* slf4j - log4j

简单部署、运行方式：

(1) 打包 : jar cvf jt_basejt9.0__.jar -C classes/ .

(2) 将maven仓库库的依赖包copy到lib目录下（新建个lib目录）

(3) 端口号、日志文件路径、二维码图片保存位置等通过jt.properties配置

    采用H2数据库（embbed模式），数据库jdbc url也通过jt.properties配置
    
    第一次使用时，可以通过com/hc/jettytest/jt/sql/ddl.sql初始化表结构
 
(4) 启动方式：java -Xms64m -Xmx256M -cp .;lib/*;jt_basejt9.0__.jar com.hc.jettytest.jt.EmbeddedServerApp

curl -X POST http://121.42.59.173:8080/push/ -d "[{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/27','motorType':'EX4','nation':'中华人民共和国','price':'8999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'AX500Z8010JQF003','weight':'550Kg'},{'battery':'12V55AH','capacity':'600kw','company':'山东高唐庆丰有限公司','manuDate':'2016/05/27','motorType':'EX3','nation':'中华人民共和国','price':'9999元','prodName':'二代车','sealer':'销售商-某某某','sealerTel':'0635-09090909','seatCount':'4','serialNum':'AX500Z8010JQF004','weight':'550Kg'} ]" 

