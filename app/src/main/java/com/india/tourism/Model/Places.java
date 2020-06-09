package com.india.tourism.Model;

public class Places {
    private String Img,Name,date,description,pid,time,fuldes,EntryFee,Timings,url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFuldes() {
        return fuldes;
    }

    public void setFuldes(String fuldes) {
        this.fuldes = fuldes;
    }

    public String getEntryfee() {
        return EntryFee;
    }

    public void setEntryfee(String entryfee) {
        EntryFee = entryfee;
    }

    public String getTimings() {
        return Timings;
    }

    public void setTimings(String timings) {
        Timings = timings;
    }

    public Places() {
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
