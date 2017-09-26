package radhakrishnan.familytracker.traking.model;

import java.io.Serializable;
import java.util.Date;

import radhakrishnan.familytracker.UserInfo.model.UserModel;

/**
 * Created by radhakrishnan on 30/8/17.
 */

public class Location implements Serializable {
    private double longt, lat;
    private Date date;
    private String userId;
    private UserModel user;

    Location() {
    }

    public Location(double longt, double lat) {
        this.longt = longt;
        this.lat = lat;
    }

    public String getUserId() {
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    public double getLongt() {
        return longt;
    }

    void setLongt(double longt) {
        this.longt = longt;
    }

    public double getLat() {
        return lat;
    }

    void setLat(double lat) {
        this.lat = lat;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
