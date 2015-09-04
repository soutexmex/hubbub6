package edu.acc.j2ee.hubbub6;

public class User implements java.io.Serializable {   
    private String userName;
    private Profile profile;
    private int id;
    
    public User(String userName) {
        this.userName = userName;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public Profile getProfile() {
        return profile;
    }
    
    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return userName;
    }
}
