package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/1 9:07
 */
@Entity(nameInDb = "mypush")
public class MyPush extends BaseEntity {

	/**
	 * 用户Id(主键)
	 */
	@Id
	@Generated
	@Property(nameInDb = "userId")
	private String userId;

	/**
	 * 推送状态(0:关闭;1:开启)
	 */
	@Property(nameInDb = "notifystate")
	private int notifystate;

	/**
	 * 推送声音
	 */
	@Property(nameInDb = "sound")
	private int sound;

	/**
	 * 推送震动
	 */
	@Property(nameInDb = "verfity")
	private int verfity;

	/**
	 * 圈子状态
	 */
	@Property(nameInDb = "circle")
	private int circle;

	public String getUserId() {
		return userId;
	}

	public MyPush(String userId) {
		this.userId = userId;
		notifystate = 1;
		sound = 1;
		verfity = 1;
		circle = 1;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getNotifystate() {
		return notifystate;
	}

	public void setNotifystate(int notifystate) {
		this.notifystate = notifystate;
	}

	public int getSound() {
		return sound;
	}

	public void setSound(int sound) {
		this.sound = sound;
	}

	public int getVerfity() {
		return verfity;
	}

	public void setVerfity(int verfity) {
		this.verfity = verfity;
	}

	public int getCircle() {
		return circle;
	}

	public void setCircle(int circle) {
		this.circle = circle;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userId);
		dest.writeInt(this.notifystate);
		dest.writeInt(this.sound);
		dest.writeInt(this.verfity);
		dest.writeInt(this.circle);
	}

	protected MyPush(Parcel in) {
		super(in);
		this.userId = in.readString();
		this.notifystate = in.readInt();
		this.sound = in.readInt();
		this.verfity = in.readInt();
		this.circle = in.readInt();
	}

	@Generated(hash = 1744504825)
	public MyPush(String userId, int notifystate, int sound, int verfity, int circle) {
		this.userId = userId;
		this.notifystate = notifystate;
		this.sound = sound;
		this.verfity = verfity;
		this.circle = circle;
	}

	@Generated(hash = 757195389)
	public MyPush() {
	}

	public static final Creator<MyPush> CREATOR = new Creator<MyPush>() {
		public MyPush createFromParcel(Parcel source) {
			return new MyPush(source);
		}

		public MyPush[] newArray(int size) {
			return new MyPush[size];
		}
	};

	@Override
	public String toString() {
		return "MyPush{" +
				"userId='" + userId + '\'' +
				", notifystate=" + notifystate +
				", sound=" + sound +
				", verfity=" + verfity +
				", circle=" + circle +
				'}';
	}
}
