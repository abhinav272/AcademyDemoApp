package com.abhinav.academydemoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class AcademyItem implements Parcelable {
    private String academyName;
    private String user;
    private String website;
    private String email;
    private String mobile;
    private String location;
    private String description;
    private Date addedOn;
    private String imagePath;

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.academyName);
        dest.writeString(this.user);
        dest.writeString(this.website);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeLong(addedOn != null ? addedOn.getTime() : -1);
        dest.writeString(this.imagePath);
    }

    public AcademyItem() {
    }

    protected AcademyItem(Parcel in) {
        this.academyName = in.readString();
        this.user = in.readString();
        this.website = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.location = in.readString();
        this.description = in.readString();
        long tmpAddedOn = in.readLong();
        this.addedOn = tmpAddedOn == -1 ? null : new Date(tmpAddedOn);
        this.imagePath = in.readString();
    }

    public static final Creator<AcademyItem> CREATOR = new Creator<AcademyItem>() {
        public AcademyItem createFromParcel(Parcel source) {
            return new AcademyItem(source);
        }

        public AcademyItem[] newArray(int size) {
            return new AcademyItem[size];
        }
    };
}
