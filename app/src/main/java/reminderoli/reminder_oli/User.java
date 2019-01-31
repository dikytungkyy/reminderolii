package reminderoli.reminder_oli;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    String IdUser;
    String userName;
    String password;
    String email;

    public User() {

    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public User(JSONObject object){
        try {
            String id = object.getString("Id_User");
            String name = object.getString("userName");
            String pass = object.getString("Password");
            String emai = object.getString("Email");
            String status =object.getString("Status");

            this.IdUser = id;
            this.userName = name;
            this.password = pass;
            this.email = emai;
            this.status = status;
        }catch (JSONException e){
            e.printStackTrace();
        }

    }







    protected User(Parcel in) {
        IdUser = in.readString();
        userName = in.readString();
        password = in.readString();
        email = in.readString();
        status = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IdUser);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(status);
    }
}
