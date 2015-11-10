package com.example.elizabeth.logmein;

/**
 * Created by ehu on 11/9/2015.
 */
public class Gamer {
    private String username;
    private String score;
    public Gamer (){};//default constructor
    public Gamer(String username, String score){
        this.username = username;
        this.score = score;
    };
    public String getUsername(){
        return username;
    }
    public String getScore(){
        return score;
    }
}
