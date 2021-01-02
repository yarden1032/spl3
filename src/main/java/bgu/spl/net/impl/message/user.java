package bgu.spl.net.impl.message;

import java.util.LinkedList;

public class user {
    private String userName;
    private String passWord;
    private boolean is_admin;
    private LinkedList<Integer> RegCoursesList;
    public user(String userName,String passWord,boolean is_admin)
    {
        this.userName=userName;
        this.passWord=passWord;
        this.is_admin=is_admin;
        this.RegCoursesList=new LinkedList<>();
    }

    public String getPassWord() {
        return passWord;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public String getUserName() {
        return userName;
    }

    public LinkedList<Integer>  getRegCoursesList() {
        return RegCoursesList;
    }
}
