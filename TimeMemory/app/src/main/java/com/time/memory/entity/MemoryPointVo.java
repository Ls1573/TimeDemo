package com.time.memory.entity;

import android.os.Parcel;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/21 15:49
 */
public class MemoryPointVo extends BaseEntity {
	/**
	 * 第几个片段
	 */
	private String pointOrder;
	/**
	 * 内容
	 */
	private String detail;
	/**
	 * 图片地址
	 */
	private String photo1;
	private String photo2;
	private String photo3;
	private String photo4;
	private String photo5;
	private String photo6;
	/**
	 * 图片数量
	 */
	private String photoCount;
	/**
	 * 记忆时间
	 */
	private String memoryPointDate;
	/**
	 * 记忆时间
	 */
	private String insdForShow;
	/**
	 * 地址
	 */
	private String local;

	private String groupId;

	private String userId;

	private String memoryId;

	private String memoryPointId;

	private String addUserId;

	/**
	 * 评论数
	 */
	private String cCnt;
	/**
	 * 发布日期
	 */
	private String mpDateForShow;
	/**
	 * 点赞flag(0:true;1:false)
	 */
	private String mpFlag;

	/**
	 * 片段Id
	 */
	private String mpId;

	/**
	 * 片段Id的源Id
	 */
	private String mpSrcId;


	/**
	 * 片段Id的源Id
	 */
	private String memoryPointIdSource;


	/**
	 * 记忆Id源Id
	 */
	private String memoryIdSource;

	/**
	 * 1:我的记忆;2:其他书的记忆
	 */
	private String source;

	/**
	 * 点赞数
	 */
	private String pCnt;

	/**
	 * 发布人
	 */
	private String uId;

	/**
	 * 发布人name
	 */
	private String uname;

	private String p1;
	private String p2;
	private String p3;
	private String p4;
	private String p5;
	private String p6;

	public void setMemoryPointDate(String memoryPointDate) {
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy.MM.dd").parse(memoryPointDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.memoryPointDate = new SimpleDateFormat("yyyy-MM-dd").format(parse);
	}

	public void setNewDate(String mpDateForShow) {
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy-MM-dd").parse(mpDateForShow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.memoryPointDate = new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}

	public void setInsDate(String mpDateForShow) {
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy-MM-dd").parse(mpDateForShow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.insdForShow = new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}

	public void setDate(String date) {
		if (TextUtils.isEmpty(date)) {
			this.memoryPointDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
			return;
		}
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy年MM月dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.memoryPointDate = new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;

	}

	public String getInsdForShow() {
		return insdForShow;
	}

	public void setInsdForShow(String insdForShow) {
		this.insdForShow = insdForShow;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		this.p6 = p6;
	}

	public MemoryPointVo() {

	}

	public MemoryPointVo(String pointOrder, String detail, String photoCount, String memoryPointDate, String local) {
		this.pointOrder = pointOrder;
		this.photoCount = photoCount;
		this.detail = detail;
		this.local = local;
		setMemoryPointDate(memoryPointDate);
		this.photo1 = "";
		this.photo2 = "";
		this.photo3 = "";
		this.photo4 = "";
		this.photo5 = "";
		this.photo6 = "";
	}

	public MemoryPointVo(String pointOrder, String detail, String photoCount, String memoryPointDate, String local, String memoryId, String memoryPointId, String userId) {
		this.pointOrder = pointOrder;
		this.photoCount = photoCount;
		this.detail = detail;
		this.local = local;
		this.memoryId = memoryId;
		this.memoryPointId = memoryPointId;
		this.userId = userId;
		setMemoryPointDate(memoryPointDate);
		this.photo1 = "";
		this.photo2 = "";
		this.photo3 = "";
		this.photo4 = "";
		this.photo5 = "";
		this.photo6 = "";
	}

	public String getPointOrder() {
		return pointOrder;
	}

	public void setPointOrder(String pointOrder) {
		this.pointOrder = pointOrder;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}

	public String getPhoto5() {
		return photo5;
	}

	public void setPhoto5(String photo5) {
		this.photo5 = photo5;
	}

	public String getPhoto6() {
		return photo6;
	}

	public void setPhoto6(String photo6) {
		this.photo6 = photo6;
	}

	public String getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public String getMemoryPointDate() {
		return memoryPointDate;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getAddUserId() {
		return addUserId;
	}

	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}

	public String getcCnt() {
		return cCnt;
	}

	public void setcCnt(String cCnt) {
		this.cCnt = cCnt;
	}

	public String getMpDateForShow() {
		return mpDateForShow;
	}

	public void setMpDateForShow(String mpDateForShow) {
		this.mpDateForShow = mpDateForShow;
		this.memoryPointDate = mpDateForShow;
	}

	public String getMpFlag() {
		return mpFlag;
	}

	public void setMpFlag(String mpFlag) {
		this.mpFlag = mpFlag;
	}

	public String getMpId() {
		return mpId;
	}

	public void setMpId(String mpId) {
		this.mpId = mpId;
		this.memoryId = mpId;
	}

	public String getpCnt() {
		return pCnt;
	}

	public void setpCnt(String pCnt) {
		this.pCnt = pCnt;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
		this.userId = uId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getMemoryPointId() {
		return memoryPointId;
	}

	public void setMemoryPointId(String memoryPointId) {
		this.memoryPointId = memoryPointId;
	}

	public String getMemorySrcId() {
		return mpSrcId;
	}

	public void setMemorySrcId(String memorySrcId) {
		this.mpSrcId = memorySrcId;
	}

	public String getMemoryPointIdSource() {
		return memoryPointIdSource;
	}

	public void setMemoryPointIdSource(String memoryPointIdSource) {
		this.memoryPointIdSource = memoryPointIdSource;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.pointOrder);
		dest.writeString(this.detail);
		dest.writeString(this.photo1);
		dest.writeString(this.photo2);
		dest.writeString(this.photo3);
		dest.writeString(this.photo4);
		dest.writeString(this.photo5);
		dest.writeString(this.photo6);
		dest.writeString(this.photoCount);
		dest.writeString(this.memoryPointDate);
		dest.writeString(this.insdForShow);
		dest.writeString(this.local);
		dest.writeString(this.groupId);
		dest.writeString(this.userId);
		dest.writeString(this.memoryId);
		dest.writeString(this.memoryPointId);
		dest.writeString(this.addUserId);
		dest.writeString(this.cCnt);
		dest.writeString(this.mpDateForShow);
		dest.writeString(this.mpFlag);
		dest.writeString(this.mpId);
		dest.writeString(this.mpSrcId);
		dest.writeString(this.memoryPointIdSource);
		dest.writeString(this.memoryIdSource);
		dest.writeString(this.source);
		dest.writeString(this.pCnt);
		dest.writeString(this.uId);
		dest.writeString(this.uname);
		dest.writeString(this.p1);
		dest.writeString(this.p2);
		dest.writeString(this.p3);
		dest.writeString(this.p4);
		dest.writeString(this.p5);
		dest.writeString(this.p6);
	}

	protected MemoryPointVo(Parcel in) {
		super(in);
		this.pointOrder = in.readString();
		this.detail = in.readString();
		this.photo1 = in.readString();
		this.photo2 = in.readString();
		this.photo3 = in.readString();
		this.photo4 = in.readString();
		this.photo5 = in.readString();
		this.photo6 = in.readString();
		this.photoCount = in.readString();
		this.memoryPointDate = in.readString();
		this.insdForShow = in.readString();
		this.local = in.readString();
		this.groupId = in.readString();
		this.userId = in.readString();
		this.memoryId = in.readString();
		this.memoryPointId = in.readString();
		this.addUserId = in.readString();
		this.cCnt = in.readString();
		this.mpDateForShow = in.readString();
		this.mpFlag = in.readString();
		this.mpId = in.readString();
		this.mpSrcId = in.readString();
		this.memoryPointIdSource = in.readString();
		this.memoryIdSource = in.readString();
		this.source = in.readString();
		this.pCnt = in.readString();
		this.uId = in.readString();
		this.uname = in.readString();
		this.p1 = in.readString();
		this.p2 = in.readString();
		this.p3 = in.readString();
		this.p4 = in.readString();
		this.p5 = in.readString();
		this.p6 = in.readString();
	}

	public static final Creator<MemoryPointVo> CREATOR = new Creator<MemoryPointVo>() {
		public MemoryPointVo createFromParcel(Parcel source) {
			return new MemoryPointVo(source);
		}

		public MemoryPointVo[] newArray(int size) {
			return new MemoryPointVo[size];
		}
	};
}
