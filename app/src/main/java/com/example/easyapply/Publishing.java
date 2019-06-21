package com.example.easyapply;


import java.text.ParseException;

public class Publishing {



    private Utilities getdate = new Utilities();

    public String publishid;
    public String userid;
    public String pictureurl;
    public String picturename;
    public String picdescription;
    public Long dateofcreation;
    public String lactitud;
    public String longiud;
    public String countryname;
    public String adminareaname;
    public String localityname;


public Publishing(){}


    public Publishing(String publishid, String userid, String pictureurl, String picturename, String picdescription, String lactitud, String longiud, String countryname, String adminareaname, String localityname) {
        this.publishid = publishid;
        this.userid = userid;
        this.pictureurl = pictureurl;
        this.picturename = picturename;
        this.picdescription = picdescription;
        this.lactitud = lactitud;
        this.longiud = longiud;
        this.countryname = countryname;
        this.adminareaname = adminareaname;
        this.localityname = localityname;
        this.dateofcreation= getdate.getDate();
    }

    public String getPublishid() {
        return publishid;
    }

    public String getUserid() {
        return userid;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public String getPicturename() {
        return picturename;
    }

    public String getPicdescription() {
        return picdescription;
    }

    public Long getDateofcreation() {
        return dateofcreation;
    }

    public String getLactitud() {
        return lactitud;
    }

    public String getLongiud() {
        return longiud;
    }

    public String getCountryname() {
        return countryname;
    }

    public String getAdminareaname() {
        return adminareaname;
    }

    public String getLocalityname() {
        return localityname;
    }
}
