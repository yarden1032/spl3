package bgu.spl.net.srv;

import bgu.spl.net.impl.message.user;

import java.util.LinkedList;

public class Course {
    private String courseName;
    private int [] KdamCoursesList;
    private int numOfMaxStudents;
    private int currentRegisteredStudents;
    private LinkedList<user> regeisteredStudentsList =new LinkedList<>();

    public Course (String courseName,int [] KdamCoursesList,int numOfMaxStudents)
    {
        this.courseName=courseName;
        this.KdamCoursesList=KdamCoursesList;
        this.numOfMaxStudents=numOfMaxStudents;
        this.currentRegisteredStudents=0;

    }
    public String getCourseName()
    {
        return courseName;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public int[] getKdamCoursesList() {
        return KdamCoursesList;
    }

    public synchronized void register(user user)
    {
        regeisteredStudentsList.add(user);
            currentRegisteredStudents++;



    }
    public synchronized boolean isAvailablePlaces()
    {
       return numOfMaxStudents-currentRegisteredStudents>0;
    }
    public synchronized void Unregister(user user)
    {
        regeisteredStudentsList.remove(user);
        currentRegisteredStudents--;



    }

    public LinkedList<user> getRegeisteredStudentsList() {
        return regeisteredStudentsList;
    }

    public int getCurrentRegisteredStudents() {
        return currentRegisteredStudents;
    }
}
