package com.fibo.ddp.common.model.datax.datamanage;

import com.fibo.ddp.common.model.common.BasePage;

import java.io.Serializable;
import java.util.Date;

public class CustList extends BasePage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 * */
	private Long id;
	
	/**
	 * 以下20个t开头为匿名字段
	 * */
	private String t0;
	private String t1;
	private String t2;
	private String t3;
	private String t4;
	private String t5;
	private String t6;
	private String t7;
	private String t8;
	private String t9;
	private String t10;
	private String t11;
	private String t12;
	private String t13;
	private String t14;
	private String t15;
	private String t16;
	private String t17;
	private String t18;
	private String t19;
	
	/**
	 * 创建人编号
	 * */
	private Long userId;
	
	/**
	 * 创建人昵称
	 * */
	private String nickName;
	
	/**
	 * 创建时间
	 * */
	private Date created;
	
	/**
	 * 检索客户信息是否存在的定制条件
	 */
	private String checkCol;
	
	/**
	 * 检索名单库的表名称
	 */
	private String tableName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getT0() {
		return t0;
	}
	public void setT0(String t0) {
		this.t0 = t0;
	}
	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public String getT2() {
		return t2;
	}
	public void setT2(String t2) {
		this.t2 = t2;
	}
	public String getT3() {
		return t3;
	}
	public void setT3(String t3) {
		this.t3 = t3;
	}
	public String getT4() {
		return t4;
	}
	public void setT4(String t4) {
		this.t4 = t4;
	}
	public String getT5() {
		return t5;
	}
	public void setT5(String t5) {
		this.t5 = t5;
	}
	public String getT6() {
		return t6;
	}
	public void setT6(String t6) {
		this.t6 = t6;
	}
	public String getT7() {
		return t7;
	}
	public void setT7(String t7) {
		this.t7 = t7;
	}
	public String getT8() {
		return t8;
	}
	public void setT8(String t8) {
		this.t8 = t8;
	}
	public String getT9() {
		return t9;
	}
	public void setT9(String t9) {
		this.t9 = t9;
	}
	public String getT10() {
		return t10;
	}
	public void setT10(String t10) {
		this.t10 = t10;
	}
	public String getT11() {
		return t11;
	}
	public void setT11(String t11) {
		this.t11 = t11;
	}
	public String getT12() {
		return t12;
	}
	public void setT12(String t12) {
		this.t12 = t12;
	}
	public String getT13() {
		return t13;
	}
	public void setT13(String t13) {
		this.t13 = t13;
	}
	public String getT14() {
		return t14;
	}
	public void setT14(String t14) {
		this.t14 = t14;
	}
	public String getT15() {
		return t15;
	}
	public void setT15(String t15) {
		this.t15 = t15;
	}
	public String getT16() {
		return t16;
	}
	public void setT16(String t16) {
		this.t16 = t16;
	}
	public String getT17() {
		return t17;
	}
	public void setT17(String t17) {
		this.t17 = t17;
	}
	public String getT18() {
		return t18;
	}
	public void setT18(String t18) {
		this.t18 = t18;
	}
	public String getT19() {
		return t19;
	}
	public void setT19(String t19) {
		this.t19 = t19;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCheckCol() {
		return checkCol;
	}
	public void setCheckCol(String checkCol) {
		this.checkCol = checkCol;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Override
	public String toString() {
		return "CustList [userId=" + id + ", t0=" + t0 + ", t1=" + t1 + ", t2=" + t2
				+ ", t3=" + t3 + ", t4=" + t4 + ", t5=" + t5 + ", t6=" + t6
				+ ", t7=" + t7 + ", t8=" + t8 + ", t9=" + t9 + ", t10=" + t10
				+ ", t11=" + t11 + ", t12=" + t12 + ", t13=" + t13 + ", t14="
				+ t14 + ", t15=" + t15 + ", t16=" + t16 + ", t17=" + t17
				+ ", t18=" + t18 + ", t19=" + t19 + ", userId=" + userId
				+ ", nickName=" + nickName + ", created=" + created
				+ ", checkCol=" + checkCol + ", tableName=" + tableName + "]";
	}
	
	
	
}
