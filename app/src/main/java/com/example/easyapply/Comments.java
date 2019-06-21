package com.example.easyapply;

import java.text.ParseException;

public class Comments {
    private Utilities utilities = new Utilities();

    private String commentsid;
    private String publishingid;
    private String userid;
    private Long dateofpublication;
    private String comentario;

    public Comments() {
    }

    public Comments(String commentsid, String publishingid, String userid, String comentario) {
        this.comentario = comentario;
        this.commentsid = commentsid;
        this.publishingid = publishingid;
        this.userid = userid;
        this.dateofpublication = utilities.getDate();

    }


    public String getCommentsid() {
        return commentsid;
    }

    public void setCommentsid(String commentsid) {
        this.commentsid = commentsid;
    }

    public String getPublishingid() {
        return publishingid;
    }

    public void setPublishingid(String publishingid) {
        this.publishingid = publishingid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Long getDateofpublication() {
        return dateofpublication;
    }

    public void setDateofpublication(Long dateofpublication) {
        this.dateofpublication = dateofpublication;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
