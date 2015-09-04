package edu.acc.j2ee.hubbub6;

import java.util.Date;

public class Profile implements java.io.Serializable {
    private Date joinDate;
    private String firstName;
    private String lastName;
    private String email;
    private String zip;
    private User user;
    private String biography;
    private int id;
    
    public Profile(Date jd, String fn, String ln, String e, String z) {
        joinDate = jd;
        firstName = fn;
        lastName = ln;
        email = e;
        zip = z;
    }
    
    public Profile() {}

    public Date getJoinDate() {
        return joinDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getZip() {
        return zip;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
    
    public String getBiography() {
        return biography;
    }
   
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setBiography(String bio) {
        biography = bio;
    }
    
    @Override
    public String toString() {
        return String.format("Profile of %s %s (%d)",
                firstName, lastName, id);
    }
}
