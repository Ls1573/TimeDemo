package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:用户改
 * @date 2016/9/11 9:28
 */
@Entity(nameInDb = "user")
public class User extends BaseEntity {

	@Id
	@Generated
	@Property(nameInDb = "Id")
	private String userId;

	@Property(nameInDb = "name")
	private String userName;

	@NotNull
	@Property(nameInDb = "phone")
	private String userMobile;

	@Property(nameInDb = "pic")
	private String headPhoto;

	@Property(nameInDb = "sex")
	private String userGender;

	@Property(nameInDb = "token")
	private String userToken;

	@Property(nameInDb = "qq")
	private String qq;

	@Property(nameInDb = "email")
	private String email;

	@Property(nameInDb = "wechat")
	private String wechat;

	@Property(nameInDb = "pwd")
	private String userPw;

	@Property(nameInDb = "sign")
	private String sign;

	@Property(nameInDb = "active")
	private int active;//是否当前用户

	@Transient
	private String activeFlg;//是否是激活的用户

	@Transient
	private String isTwoWayFlg;//是否是双向用户(0:是)

	@Property(nameInDb = "contacts_status")
	private String importContactsStatus;//是否导入了通讯录

	@Transient
	private String verifyCode;//验证码

	@Property(nameInDb = "address")
	private String address;//地址

	@Transient
	private String address1;//地址1

	@Property(nameInDb = "company")
	private String company;//公司

	@Property(nameInDb = "companyIntroduce")
	private String companyIntroduce;//行业

	@Property(nameInDb = "importgroup")
	private int importgroup;//是否导入圈子数据

	@Property(nameInDb = "importmessage")
	private int importmessage;//是否导入了消息

	@Property(nameInDb = "locpath")
	private String locpath;//本地图片地址

	@Property(nameInDb = "cityId")
	private String cityId;//选择城市Id

	@Property(nameInDb = "provinceId")
	private String provinceId;//选择省Id

	@Property(nameInDb = "cityName")
	private String cityName;//选择城市Name

	@Property(nameInDb = "provinceName")
	private String provinceName;//选择省Name

	@Transient
	private User userVo;//单个用户

	@Transient
	private List<User> userVoList;//用户列表

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getImportContactsStatus() {
		return importContactsStatus;
	}

	public void setImportContactsStatus(String importContactsStatus) {
		this.importContactsStatus = importContactsStatus;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public int getImportgroup() {
		return importgroup;
	}

	public void setImportgroup(int importgroup) {
		this.importgroup = importgroup;
	}

	public String getLocpath() {
		return locpath;
	}

	public void setLocpath(String locpath) {
		this.locpath = locpath;
	}

	public User getUserVo() {
		return userVo;
	}

	public void setUserVo(User userVo) {
		this.userVo = userVo;
	}

	public List<User> getUserVoList() {
		return userVoList;
	}

	public void setUserVoList(List<User> userVoList) {
		this.userVoList = userVoList;
	}

	public int getImportmessage() {
		return importmessage;
	}

	public void setImportmessage(int importmessage) {
		this.importmessage = importmessage;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", userMobile='" + userMobile + '\'' +
				'}';
	}

	public String getIsTwoWayFlg() {
		return isTwoWayFlg;
	}

	public void setIsTwoWayFlg(String isTwoWayFlg) {
		this.isTwoWayFlg = isTwoWayFlg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userId);
		dest.writeString(this.userName);
		dest.writeString(this.userMobile);
		dest.writeString(this.headPhoto);
		dest.writeString(this.userGender);
		dest.writeString(this.userToken);
		dest.writeString(this.qq);
		dest.writeString(this.email);
		dest.writeString(this.wechat);
		dest.writeString(this.userPw);
		dest.writeString(this.sign);
		dest.writeInt(this.active);
		dest.writeString(this.activeFlg);
		dest.writeString(this.isTwoWayFlg);
		dest.writeString(this.importContactsStatus);
		dest.writeString(this.verifyCode);
		dest.writeString(this.address);
		dest.writeString(this.address1);
		dest.writeString(this.company);
		dest.writeString(this.companyIntroduce);
		dest.writeInt(this.importgroup);
		dest.writeInt(this.importmessage);
		dest.writeString(this.locpath);
		dest.writeString(this.cityId);
		dest.writeString(this.provinceId);
		dest.writeString(this.cityName);
		dest.writeString(this.provinceName);
		dest.writeParcelable(this.userVo, 0);
		dest.writeTypedList(userVoList);
	}

	public User() {
	}

	protected User(Parcel in) {
		super(in);
		this.userId = in.readString();
		this.userName = in.readString();
		this.userMobile = in.readString();
		this.headPhoto = in.readString();
		this.userGender = in.readString();
		this.userToken = in.readString();
		this.qq = in.readString();
		this.email = in.readString();
		this.wechat = in.readString();
		this.userPw = in.readString();
		this.sign = in.readString();
		this.active = in.readInt();
		this.activeFlg = in.readString();
		this.isTwoWayFlg = in.readString();
		this.importContactsStatus = in.readString();
		this.verifyCode = in.readString();
		this.address = in.readString();
		this.address1 = in.readString();
		this.company = in.readString();
		this.companyIntroduce = in.readString();
		this.importgroup = in.readInt();
		this.importmessage = in.readInt();
		this.locpath = in.readString();
		this.cityId = in.readString();
		this.provinceId = in.readString();
		this.cityName = in.readString();
		this.provinceName = in.readString();
		this.userVo = in.readParcelable(User.class.getClassLoader());
		this.userVoList = in.createTypedArrayList(User.CREATOR);
	}

	@Generated(hash = 999474579)
	public User(String userId, String userName, @NotNull String userMobile,
			String headPhoto, String userGender, String userToken, String qq,
			String email, String wechat, String userPw, String sign, int active,
			String importContactsStatus, String address, String company,
			String companyIntroduce, int importgroup, int importmessage, String locpath,
			String cityId, String provinceId, String cityName, String provinceName) {
		this.userId = userId;
		this.userName = userName;
		this.userMobile = userMobile;
		this.headPhoto = headPhoto;
		this.userGender = userGender;
		this.userToken = userToken;
		this.qq = qq;
		this.email = email;
		this.wechat = wechat;
		this.userPw = userPw;
		this.sign = sign;
		this.active = active;
		this.importContactsStatus = importContactsStatus;
		this.address = address;
		this.company = company;
		this.companyIntroduce = companyIntroduce;
		this.importgroup = importgroup;
		this.importmessage = importmessage;
		this.locpath = locpath;
		this.cityId = cityId;
		this.provinceId = provinceId;
		this.cityName = cityName;
		this.provinceName = provinceName;
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
