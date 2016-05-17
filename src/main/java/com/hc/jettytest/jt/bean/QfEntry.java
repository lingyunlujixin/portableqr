package com.hc.jettytest.jt.bean;

import java.lang.reflect.Field;

import com.hc.jettytest.jt.h2.H2Util;

public class QfEntry {
	
	private long id;
	
	private String nation;
	
	private String company;
	
	private String serialNum;
	
	/**
	 * 产品名称
	 */
	private String prodName;
	
	/**
	 * 整备质量
	 */
	private String weight;

	/**
	 * 乘员数
	 */
	private String seatCount;
	
	/**
	 * 电机型号	
	 */
	private String motorType;
	
	/**
	 * 功率
	 */
	private String capacity;
	
	/**
	 * 电池电压容量
	 */
	private String  battery;

	/**
	 * 出厂日期
	 */
	private String  manuDate;
	
	/**
	 * 经销商
	 */
	private String  sealer;

	/**
	 * 经销商联系方式
	 */
	private String  sealerTel;
	
	/**
	 * 出厂价
	 */
	private String  price;
	
	/**
	 * 备用
	 */
	private String  remark01;
	
	/**
	 * 备用
	 */
	private String  remark02;
	

	/**
	 * 备用
	 */
	private String  remark03;
	

	/**
	 * 备用
	 */
	private String  remark04;
	
	/**
	 * 备用
	 */
	private String  remark05;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSeatCount() {
		return seatCount;
	}

	public void setSeatCount(String seatCount) {
		this.seatCount = seatCount;
	}

	public String getMotorType() {
		return motorType;
	}

	public void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getManuDate() {
		return manuDate;
	}

	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}

	public String getSealer() {
		return sealer;
	}

	public void setSealer(String sealer) {
		this.sealer = sealer;
	}

	public String getSealerTel() {
		return sealerTel;
	}

	public void setSealerTel(String sealerTel) {
		this.sealerTel = sealerTel;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark01() {
		return remark01;
	}

	public void setRemark01(String remark01) {
		this.remark01 = remark01;
	}

	public String getRemark02() {
		return remark02;
	}

	public void setRemark02(String remark02) {
		this.remark02 = remark02;
	}

	public String getRemark03() {
		return remark03;
	}

	public void setRemark03(String remark03) {
		this.remark03 = remark03;
	}

	public String getRemark04() {
		return remark04;
	}

	public void setRemark04(String remark04) {
		this.remark04 = remark04;
	}

	public String getRemark05() {
		return remark05;
	}

	public void setRemark05(String remark05) {
		this.remark05 = remark05;
	}
	
	public String toHTMLString() {
		
		StringBuffer b = new StringBuffer();
		
		Class<?> c = this.getClass();
		
		try {
			for( Field f : c.getDeclaredFields()) {
				
				if(!f.getName().contains("remark")) {

					b.append(  "</br><font size=\"6\" face=\"微软雅黑\" color=\"" 
					         + color(f.getName()) + "\">" 
							 + H2Util.meta.get(f.getName().toUpperCase()) 
							 + " : " 
							 + String.valueOf(f.get(this)) 
							 + "</font></br>");

				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return b.toString();
		
	}
	
	private String color(String col) {

		return "serialNum".equals(col) || "company".equals(col) ? "#FF0000" : "#0000FF";
	}
}
