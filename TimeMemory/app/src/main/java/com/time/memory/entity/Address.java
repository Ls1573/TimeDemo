package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Qiu
 * @version V1.0
 * @Description:地区地址
 * @date 2016/12/1 10:27
 */
@Entity(nameInDb = "address")
public class Address extends BaseEntity {

	@Property(nameInDb = "Id")
	private String Id;

	@Property(nameInDb = "name")
	private String name;

	@Property(nameInDb = "area_level")
	private String area_level;

	@Property(nameInDb = "parent_id")
	private String parent_id;

	@Property(nameInDb = "orby")
	private String orby;

	@Property(nameInDb = "initial")
	private String initial;

	@Property(nameInDb = "using_flg")
	private String using_flg;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea_level() {
		return area_level;
	}

	public void setArea_level(String area_level) {
		this.area_level = area_level;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getOrby() {
		return orby;
	}

	public void setOrby(String orby) {
		this.orby = orby;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getUsing_flg() {
		return using_flg;
	}

	public void setUsing_flg(String using_flg) {
		this.using_flg = using_flg;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.Id);
		dest.writeString(this.name);
		dest.writeString(this.area_level);
		dest.writeString(this.parent_id);
		dest.writeString(this.orby);
		dest.writeString(this.initial);
		dest.writeString(this.using_flg);
	}

	public Address() {
	}

	protected Address(Parcel in) {
		super(in);
		this.Id = in.readString();
		this.name = in.readString();
		this.area_level = in.readString();
		this.parent_id = in.readString();
		this.orby = in.readString();
		this.initial = in.readString();
		this.using_flg = in.readString();
	}

	@Generated(hash = 732724856)
	public Address(String Id, String name, String area_level, String parent_id,
				   String orby, String initial, String using_flg) {
		this.Id = Id;
		this.name = name;
		this.area_level = area_level;
		this.parent_id = parent_id;
		this.orby = orby;
		this.initial = initial;
		this.using_flg = using_flg;
	}

	public static final Creator<Address> CREATOR = new Creator<Address>() {
		public Address createFromParcel(Parcel source) {
			return new Address(source);
		}

		public Address[] newArray(int size) {
			return new Address[size];
		}
	};
}
