package com.wny.wecare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphUser;

/**
 * Wrapper of {@link GraphUser} to be Parcelable.
 * 
 * @author Lucas Nobile
 */
public class FacebookUserWrapper implements Parcelable {

	private String id;
	private String name;
	private String firstName;
	private String middleName;
	private String lastName;
	private String link;
	private String username;
	private String birthday;

	public FacebookUserWrapper(GraphUser user) {
		id = user.getId();
		name = user.getName();
		firstName = user.getFirstName();
		middleName = user.getMiddleName();
		lastName = user.getLastName();
		link = user.getLink();
		username = user.getUsername();
		birthday = user.getBirthday();
	}

	public FacebookUserWrapper(Parcel in) {
		id = in.readString();
		name = in.readString();
		firstName = in.readString();
		middleName = in.readString();
		lastName = in.readString();
		link = in.readString();
		username = in.readString();
		birthday = in.readString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(firstName);
		dest.writeString(middleName);
		dest.writeString(lastName);
		dest.writeString(link);
		dest.writeString(username);
		dest.writeString(birthday);
	}

	public static final Parcelable.Creator<FacebookUserWrapper> CREATOR = new Creator<FacebookUserWrapper>() {

		@Override
		public FacebookUserWrapper[] newArray(int size) {
			return new FacebookUserWrapper[size];
		}

		@Override
		public FacebookUserWrapper createFromParcel(Parcel source) {
			return new FacebookUserWrapper(source);
		}
	};
}
