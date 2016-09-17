CREATE TABLE E_VEHICLE_INFO (

    id	    BIGINT		      DEFAULT 0	  AUTO_INCREMENT COMMENT 'id' PRIMARY KEY
  ,	nation	VARCHAR(100)		DEFAULT ''	COMMENT '制造国'
  ,	company	VARCHAR(100)		DEFAULT ''	COMMENT '制造商'
  ,	serialNum	VARCHAR(100)		DEFAULT ''	COMMENT '车架号（电机号）'
  ,	prodName	VARCHAR(100)		DEFAULT ''	COMMENT '车辆名称'
  ,	weight	VARCHAR(100)		DEFAULT ''	COMMENT '车辆质量'
  ,	seatCount	VARCHAR(100)		DEFAULT ''	COMMENT '乘员数'
  ,	motorType	VARCHAR(100)		DEFAULT ''	COMMENT '电动机型号'
  ,	capacity	VARCHAR(100)		DEFAULT ''	COMMENT '功率'
  ,	battery	VARCHAR(100)		DEFAULT ''	COMMENT '电池型号'
  ,	manuDate	VARCHAR(100)		DEFAULT ''	COMMENT '出厂日期'
  ,	sealer	VARCHAR(100)		DEFAULT ''	COMMENT '经销商'
  ,	sealerTel	VARCHAR(100)		DEFAULT ''	COMMENT '经销商联系方式'
  ,	price	    VARCHAR(100)		DEFAULT ''	COMMENT '全国统一零售价'
  , sizeof      VARCHAR(100)        DEFAULT '2420mm X 1250mm X 1560mm'  COMMENT '尺寸'
  , tire        varchar(255)        DEFAULT ''  COMMENT '轮胎编号'
  , detail      varchar(255)        DEFAULT ''  COMMENT '详细配置'
  ,	remark01	VARCHAR(100)		DEFAULT ''	COMMENT '预留'
  ,	remark02	VARCHAR(100)		DEFAULT ''	COMMENT '预留'
  ,	remark03	VARCHAR(100)		DEFAULT ''	COMMENT '预留'
  ,	remark04	VARCHAR(100)		DEFAULT ''	COMMENT '预留'
  ,	remark05	VARCHAR(100)		DEFAULT ''	COMMENT '预留'
  , remark06    VARCHAR(255)        DEFAULT ''  COMMENT '预留'
  , remark07    VARCHAR(255)        DEFAULT ''  COMMENT '预留'
  , remark08    VARCHAR(255)        DEFAULT ''  COMMENT '预留'
  , remark09    VARCHAR(255)        DEFAULT ''  COMMENT '预留'
  , remark10    VARCHAR(255)        DEFAULT ''  COMMENT '预留'
  ,	load_stamp	BIGINT		DEFAULT 0	COMMENT '时间戳'
) COMMENT = '';

-- insert test
INSERT INTO E_VEHICLE_INFO (
  nation,
  company,
  serialNum,
  prodName,
  weight,
  seatCount,
  motorType,
  capacity,
  battery,
  manuDate,
  sealer,
  sealerTel,
  price
) 
VALUES
  (
    '中国',
    '庆丰',
    'EX500Z8010JQF124',
    '初代车',
    '500Kg',
    '4',
    'EX2',
    '500kw',
    '12V46AH',
    '2015/1/1',
    '销售商1',
    '0635-09090909',
    '5000'
  ) ;
  
  
  ###
  
  ALTER TABLE E_VEHICLE_INFO ALTER COLUMN
price       VARCHAR(100)        DEFAULT ''  COMMENT '参考价';



