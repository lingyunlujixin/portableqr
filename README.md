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


