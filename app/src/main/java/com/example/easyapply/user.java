package com.example.easyapply;

public class user {
    private Utilities utilities = new Utilities();
    private String iduser;
    private String firstname;
    private String lastname;
    private String email;
    private String urlpicture;
    private Long dateofcreationprofile;


    public user(String iduser, String firstname, String lastname, String email, String urlpicture) {
        this.iduser = iduser;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.urlpicture = urlpicture;
        this.dateofcreationprofile=utilities.getDate();
    }

    public String getIduser() {
        return iduser;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlpicture() {
        return urlpicture;
    }

    public Long getDateofcreationprofile() {
        return dateofcreationprofile;
    }
}
