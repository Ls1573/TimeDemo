package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:群
 * @date 2016-9-6下午18:51:15
 * ==============================
 */
@Entity(nameInDb = "mgroup")
public class MGroup extends BaseEntity {
	/**
	 * 信息对应的Id
	 */
	@Id
	@Property(nameInDb = "groupId")
	private String groupId;

	/**
	 * 是否有记忆描述[同学(36)]
	 */
	@Transient
	private String desc;

	/**
	 * 群名字
	 */
	@Property(nameInDb = "groupName")
	private String groupName;

	/**
	 * 是否有最新的图片
	 */
	@Transient
	private String pic;

	/**
	 * 创建日期
	 */
	@Property(nameInDb = "createDate")
	private String insDateForShow;

	/**
	 * 更新日期
	 */
	@Property(nameInDb = "updateDateForShow")
	private String updateDateForShow;

	/**
	 * 管理员Id
	 */
	@Property(nameInDb = "userId")
	private String adminUserId;

	/**
	 * 群成员数量
	 */
	@Property(nameInDb = "groupCount")
	private String groupCount;

	/**
	 * 群是否冻结(圈子冻结 0：正常 1：冻结)
	 */
	@Property(nameInDb = "freeze")
	private String freeze;

	/**
	 * 群是否已经激活了( 0：已正常 1：未激活)
	 */
	@Property(nameInDb = "activeFlg")
	private String activeFlg;

	/**
	 * 是否有最新的
	 */
	@Transient
	private boolean isNew;
	/**
	 * 管理员name
	 */
	@Property(nameInDb = "adminUserName")
	private String adminUserName;

	/**
	 * 是否显示
	 */
	@Transient
	private boolean isVisable;

	/**
	 * 创建人
	 */
	@Property(nameInDb = "createUserId")
	private String userId;

	/**
	 * 创建人的名字
	 */
	@Property(nameInDb = "userName")
	private String userName;

	/**
	 * 记忆数
	 */
	@Property(nameInDb = "memoryCnt")
	private String memoryCnt;

	/**
	 * 好友数
	 */
	@Property(nameInDb = "friendCnt")
	private String friendCnt;

	/**
	 * 未读消息数
	 */
	@Property(nameInDb = "unReadCnt")
	private String unReadCnt;

	/**
	 * 圈子第一篇记忆的标题
	 */
	@Property(nameInDb = "title")
	private String title;

	/**
	 * 圈子第一篇记忆的ID
	 */
	@Property(nameInDb = "memoryId")
	private String memoryId;
	/**
	 * 邀请人Id
	 */
	@Property(nameInDb = "inviteUserId")
	private String inviteUserId;
	/**
	 * 邀请人姓名
	 */
	@Property(nameInDb = "inviteUserName")
	private String inviteUserName;

	/**
	 * 类别区分(0:我的;1:他的;2:圈子的)
	 */
	@Property(nameInDb = "type")
	private int type;
	/**
	 * 来自哪个用户
	 */
	@Property(nameInDb = "comeFrom")
	private String comeFrom;
	/**
	 * 圈子号-生成二维码使用
	 */
	@Property(nameInDb = "groupNum")
	private String groupNum;

	/**
	 * 圈子展示头像
	 */
	@Property(nameInDb = "headPhoto1")
	private String headPhoto1;

	/**
	 * 圈子展示头像
	 */
	@Property(nameInDb = "headPhoto2")
	private String headPhoto2;

	/**
	 * 圈子展示头像
	 */
	@Property(nameInDb = "headPhoto3")
	private String headPhoto3;

	/**
	 * 圈子展示头像
	 */
	@Property(nameInDb = "headPhoto4")
	private String headPhoto4;

	/**
	 * 补充记忆数
	 */
	@Property(nameInDb = "addmemoryCnt")
	private String unReadMPointAddCnt;

	/**
	 * 总的未读记忆和点赞数 我的+他的+圈子的和
	 */
	@Property(nameInDb = "totalPCnt")
	private String totalPCnt;

	/**
	 * 新增记忆数
	 */
	@Property(nameInDb = "newmemoryCnt")
	private String unReadMemoryCnt;

	/**
	 * 头像图片集
	 */
	@Transient
	private List<String> headPhotos;

	/**
	 * 被选中
	 */
	@Transient
	private boolean isCheck;

	/**
	 * 解锁密码
	 */
//	@Property(nameInDb = "groupPw")
	private String groupPw;

	/**
	 * 图片集
	 */
	@Transient
	private List<Picture> pictureEntits;
	/**
	 * 圈子集
	 */
	@Transient
	private List<MGroup> groupInfoVoList;


	public String getUpdateDateForShow() {
		return updateDateForShow;
	}

	public void setUpdateDateForShow(String updateDateForShow) {
		this.updateDateForShow = updateDateForShow;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getInsDateForShow() {
		return insDateForShow;
	}

	public void setInsDateForShow(String insDateForShow) {
		this.insDateForShow = insDateForShow;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(String groupCount) {
		this.groupCount = groupCount;
	}

	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public boolean isVisable() {
		return isVisable;
	}

	public void setIsVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}

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

	public String getMemoryCnt() {
		return memoryCnt;
	}

	public void setMemoryCnt(String memoryCnt) {
		this.memoryCnt = memoryCnt;
	}

	public String getFriendCnt() {
		return friendCnt;
	}

	public void setFriendCnt(String friendCnt) {
		this.friendCnt = friendCnt;
	}

	public String getUnReadCnt() {
		return unReadCnt;
	}

	public void setUnReadCnt(String unReadCnt) {
		this.unReadCnt = unReadCnt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getInviteUserId() {
		return inviteUserId;
	}

	public void setInviteUserId(String inviteUserId) {
		this.inviteUserId = inviteUserId;
	}

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public List<String> getHeadPhotos() {
		return headPhotos;
	}

	public void setHeadPhotos(List<String> headPhotos) {
		this.headPhotos = headPhotos;
	}

	public List<Picture> getPictureEntits() {
		return pictureEntits;
	}

	public void setPictureEntits(List<Picture> pictureEntits) {
		this.pictureEntits = pictureEntits;
	}

	public List<MGroup> getGroupInfoVoList() {
		return groupInfoVoList;
	}

	public void setGroupInfoVoList(List<MGroup> groupInfoVoList) {
		this.groupInfoVoList = groupInfoVoList;
	}

	public String getUnReadMPointAddCnt() {
		return unReadMPointAddCnt;
	}

	public void setUnReadMPointAddCnt(String unReadMPointAddCnt) {
		this.unReadMPointAddCnt = unReadMPointAddCnt;
	}

	public String getUnReadMemoryCnt() {
		return unReadMemoryCnt;
	}

	public void setUnReadMemoryCnt(String unReadMemoryCnt) {
		this.unReadMemoryCnt = unReadMemoryCnt;
	}

	public String getHeadPhoto1() {
		return headPhoto1;
	}

	public void setHeadPhoto1(String headPhoto1) {
		this.headPhoto1 = headPhoto1;
	}

	public String getHeadPhoto2() {
		return headPhoto2;
	}

	public void setHeadPhoto2(String headPhoto2) {
		this.headPhoto2 = headPhoto2;
	}

	public String getHeadPhoto3() {
		return headPhoto3;
	}

	public void setHeadPhoto3(String headPhoto3) {
		this.headPhoto3 = headPhoto3;
	}

	public String getHeadPhoto4() {
		return headPhoto4;
	}

	public void setHeadPhoto4(String headPhoto4) {
		this.headPhoto4 = headPhoto4;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public String getTotalPCnt() {
		return totalPCnt;
	}

	public void setTotalPCnt(String totalPCnt) {
		this.totalPCnt = totalPCnt;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getGroupPw() {
		return groupPw;
	}

	public void setGroupPw(String groupPw) {
		this.groupPw = groupPw;
	}

	@Override
	public String toString() {
		return "MGroup{" +
				"groupId='" + groupId + '\'' +
				", desc='" + desc + '\'' +
				", groupName='" + groupName + '\'' +
				", pic='" + pic + '\'' +
				", updateDateForShow='" + updateDateForShow + '\'' +
				", adminUserId='" + adminUserId + '\'' +
				", groupCount='" + groupCount + '\'' +
				", activeFlg='" + activeFlg + '\'' +
				", isNew=" + isNew +
				", adminUserName='" + adminUserName + '\'' +
				", userId='" + userId + '\'' +
				", memoryCnt='" + memoryCnt + '\'' +
				", friendCnt='" + friendCnt + '\'' +
				", title='" + title + '\'' +
				", unReadCnt='" + unReadCnt + '\'' +
				", inviteUserId='" + inviteUserId + '\'' +
				", inviteUserName='" + inviteUserName + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.desc);
		dest.writeString(this.groupId);
		dest.writeString(this.groupName);
		dest.writeString(this.pic);
		dest.writeString(this.insDateForShow);
		dest.writeString(this.updateDateForShow);
		dest.writeString(this.adminUserId);
		dest.writeString(this.groupCount);
		dest.writeString(this.freeze);
		dest.writeString(this.activeFlg);
		dest.writeByte(isNew ? (byte) 1 : (byte) 0);
		dest.writeString(this.adminUserName);
		dest.writeByte(isVisable ? (byte) 1 : (byte) 0);
		dest.writeString(this.userId);
		dest.writeString(this.userName);
		dest.writeString(this.memoryCnt);
		dest.writeString(this.friendCnt);
		dest.writeString(this.unReadCnt);
		dest.writeString(this.title);
		dest.writeString(this.memoryId);
		dest.writeString(this.inviteUserId);
		dest.writeString(this.inviteUserName);
		dest.writeInt(this.type);
		dest.writeString(this.comeFrom);
		dest.writeString(this.groupNum);
		dest.writeString(this.headPhoto1);
		dest.writeString(this.headPhoto2);
		dest.writeString(this.headPhoto3);
		dest.writeString(this.headPhoto4);
		dest.writeString(this.unReadMPointAddCnt);
		dest.writeString(this.totalPCnt);
		dest.writeString(this.unReadMemoryCnt);
		dest.writeStringList(this.headPhotos);
		dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
		dest.writeString(this.groupPw);
		dest.writeList(this.pictureEntits);
		dest.writeTypedList(groupInfoVoList);
	}

	public MGroup() {
	}

	protected MGroup(Parcel in) {
		super(in);
		this.desc = in.readString();
		this.groupId = in.readString();
		this.groupName = in.readString();
		this.pic = in.readString();
		this.insDateForShow = in.readString();
		this.updateDateForShow = in.readString();
		this.adminUserId = in.readString();
		this.groupCount = in.readString();
		this.freeze = in.readString();
		this.activeFlg = in.readString();
		this.isNew = in.readByte() != 0;
		this.adminUserName = in.readString();
		this.isVisable = in.readByte() != 0;
		this.userId = in.readString();
		this.userName = in.readString();
		this.memoryCnt = in.readString();
		this.friendCnt = in.readString();
		this.unReadCnt = in.readString();
		this.title = in.readString();
		this.memoryId = in.readString();
		this.inviteUserId = in.readString();
		this.inviteUserName = in.readString();
		this.type = in.readInt();
		this.comeFrom = in.readString();
		this.groupNum = in.readString();
		this.headPhoto1 = in.readString();
		this.headPhoto2 = in.readString();
		this.headPhoto3 = in.readString();
		this.headPhoto4 = in.readString();
		this.unReadMPointAddCnt = in.readString();
		this.totalPCnt = in.readString();
		this.unReadMemoryCnt = in.readString();
		this.headPhotos = in.createStringArrayList();
		this.isCheck = in.readByte() != 0;
		this.groupPw = in.readString();
		this.pictureEntits = new ArrayList<Picture>();
		in.readList(this.pictureEntits, List.class.getClassLoader());
		this.groupInfoVoList = in.createTypedArrayList(MGroup.CREATOR);
	}

	@Generated(hash = 1042659816)
	public MGroup(String groupId, String groupName, String insDateForShow,
				  String updateDateForShow, String adminUserId, String groupCount,
				  String freeze, String activeFlg, String adminUserName, String userId,
				  String userName, String memoryCnt, String friendCnt, String unReadCnt,
				  String title, String memoryId, String inviteUserId, String inviteUserName,
				  int type, String comeFrom, String groupNum, String headPhoto1,
				  String headPhoto2, String headPhoto3, String headPhoto4,
				  String unReadMPointAddCnt, String totalPCnt, String unReadMemoryCnt,
				  String groupPw) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.insDateForShow = insDateForShow;
		this.updateDateForShow = updateDateForShow;
		this.adminUserId = adminUserId;
		this.groupCount = groupCount;
		this.freeze = freeze;
		this.activeFlg = activeFlg;
		this.adminUserName = adminUserName;
		this.userId = userId;
		this.userName = userName;
		this.memoryCnt = memoryCnt;
		this.friendCnt = friendCnt;
		this.unReadCnt = unReadCnt;
		this.title = title;
		this.memoryId = memoryId;
		this.inviteUserId = inviteUserId;
		this.inviteUserName = inviteUserName;
		this.type = type;
		this.comeFrom = comeFrom;
		this.groupNum = groupNum;
		this.headPhoto1 = headPhoto1;
		this.headPhoto2 = headPhoto2;
		this.headPhoto3 = headPhoto3;
		this.headPhoto4 = headPhoto4;
		this.unReadMPointAddCnt = unReadMPointAddCnt;
		this.totalPCnt = totalPCnt;
		this.unReadMemoryCnt = unReadMemoryCnt;
		this.groupPw = groupPw;
	}

	public static final Creator<MGroup> CREATOR = new Creator<MGroup>() {
		public MGroup createFromParcel(Parcel source) {
			return new MGroup(source);
		}

		public MGroup[] newArray(int size) {
			return new MGroup[size];
		}
	};
}