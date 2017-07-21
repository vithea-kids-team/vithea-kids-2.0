package com.l2f.vitheakids.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class LoginInfoParcelable extends LoginInfo implements Parcelable {

	public LoginInfoParcelable() {}
	public LoginInfoParcelable(LoginInfo li) {
		super();
		super.setGreetingMsg(li.getGreetingMsg());
		super.setExListMsg(li.getExListMsg());
	}
	public LoginInfoParcelable(Parcel in) {
		super.setGreetingMsg(in.readString());
		super.setExListMsg(in.readString());		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(super.getGreetingMsg());
		dest.writeString(super.getExListMsg());	
	}
	
/*	private void readFromParcel(Parcel in) {
        super.setGreetingMsg(in.readString());
        super.setExListMsg(in.readString());
}
*/
	
	public static final Parcelable.Creator<LoginInfoParcelable> CREATOR = new Parcelable.Creator<LoginInfoParcelable>() {

        @Override
        public LoginInfoParcelable createFromParcel(Parcel source) {
            return new LoginInfoParcelable(source);
        }
        @Override
        public LoginInfoParcelable[] newArray(int size) {
            return new LoginInfoParcelable[size];
        }
    };

}
