package com.time.memory.mt.vo;

public class UserVo extends BaseVo {

	private static final long serialVersionUID = -8665183797654544075L;

	/** 用户ID */
	private String userId;

	/** 头像 */
	private String headPhoto;

	/** 手机号码 */
	private String userMobile;

	/** 用户名称 */
	private String userName;

	/** 密码 */
	private String userPw;

	/** 性别 */
	private String userGender;

	/** 地址 */
	private String address;

	/** 地址-省 */
	private String province;

	/** 地址-市 */
	private String city;

	/** 个性签名 */
	private String sign;

	/** 邮箱 */
	private String email;

	/** 公司 */
	private String company;

	/** 公司介绍 */
	private String companyIntroduce;

	/** 激活状态 */
	private String activeFlg;

	/** 冻结状态 */
	private String accountFreeze;

	/** 帐号状态 */
	private String accountState;

	/** 是否导入通讯录 */
	private String importContactsStatus;


	/** token */
	private String userToken;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getAccountFreeze() {
		return accountFreeze;
	}

	public void setAccountFreeze(String accountFreeze) {
		this.accountFreeze = accountFreeze;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getImportContactsStatus() {
		return importContactsStatus;
	}

	public void setImportContactsStatus(String importContactsStatus) {
		this.importContactsStatus = importContactsStatus;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	/** 验证码 */
	private String verifyCode;

}