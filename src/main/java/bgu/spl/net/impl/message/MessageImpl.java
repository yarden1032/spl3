package bgu.spl.net.impl.message;

import bgu.spl.net.Database;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.ActorThreadPool;
import bgu.spl.net.srv.Course;
import bgu.spl.net.srv.Reactor;


import java.io.IOException;
import java.nio.channels.Selector;
import java.util.*;


public class MessageImpl implements MessagingProtocol<Object> {
    Boolean toTerminate = false;

    @Override

    public String process(Object msg2) {
        String msg=(String) msg2;
        String opcodestring = msg.substring(0, 2);
        int opcodeint=Integer.parseInt(opcodestring);
        String respond="";
        //ADMINREG
        if (opcodeint==1)
        {
            respond=ADMINREG(msg);
        }
        //STUDENTREG
        if (opcodeint==2)
        {
            respond=STUDENTREG(msg);
        }
        //LOGIN
        if (opcodeint==3)
        {
            respond=LOGIN(msg);
        }
        //LOGOUT
        if (opcodeint==4)
        {
            respond=LOGOUT(msg);
        }
        //COURSEREG
        if (opcodeint==5)
        {
            respond=COURSEREG(msg);
        }
        //KDAMCHECK
        if (opcodeint==6)
        {
            respond=KDAMCHECK(msg);
        }//COURSESTAT
        if (opcodeint==7)
        {
            respond=COURSESTAT(msg);
        }
        //STUDENTSTAT
        if (opcodeint==8)
        {
            respond=STUDENTSTAT(msg);
        }
        //ISREGISTERED
        if (opcodeint==9)
        {
            respond=ISREGISTERED(msg);
        }//UNREGISTER
        if (opcodeint==10)
        {
            respond=UNREGISTER(msg);
        }//MYCOURSES
        if (opcodeint==11)
        {
            respond=MYCOURSES(msg);
        }

        if(respond=="error" )
            return ERROR(msg,opcodestring);


            return ACK(respond,opcodestring);





    }

    @Override
    public boolean shouldTerminate() {
        return toTerminate;
    }

    public String STUDENTREG(String msg) {


        String username;
        String passWord;
        int index = msg.indexOf('0', 3);
        username = msg.substring(3, index);
        passWord = msg.substring(index + 1, msg.length() - 1);
        Thread t=Thread.currentThread();
       Reactor reactor= (Reactor) (Database.getInstance().getServer());
       ActorThreadPool actorThreadPool=reactor.getPool();


        List<user> userList = Database.getInstance().getUsers();
        for (int i = 0; i < userList.size(); i++) {

            if (userList.get(i).getUserName().equals(username)) {
                return "error";
            }
        }
        userList.add(new user(username, passWord, false));

        return "ACK";
    }

    public String ADMINREG(String msg) {

        String username;
        String passWord;
        int index = msg.indexOf('0', 3);
        username = msg.substring(3, index);
        passWord = msg.substring(index + 1, msg.length() - 1);


        List<user> userList = Database.getInstance().getUsers();
        for (int i = 0; i < userList.size(); i++) {

            if (userList.get(i).getUserName().equals(username)) {
                return "error";
            }
        }
        userList.add(new user(username, passWord, true));

        return "ACK";
    }

    public String LOGIN(String msg) {

        String username;
        String passWord;

        int index = msg.indexOf('0', 3);
        username = msg.substring(3, index);
        passWord = msg.substring(index + 1, msg.length() - 1);


        List<user> userList = Database.getInstance().getUsers();
        for (int i = 0; i < userList.size(); i++) {

            if (userList.get(i).getUserName().equals(username)) {
                if (userList.get(i).getPassWord().equals(passWord)) {
                    user current = userList.get(i);
                    //doStuff
                    if (Database.getInstance().getLoginUsers().containsValue(current)) { //here
                        return "error";
                    }


                   // Thread thread = Database.getInstance().getServer().executeForDB(Database.getInstance().getServer().getBlockingConnectionHandler());
                  Thread thread=Thread.currentThread();
                    if (Database.getInstance().getLoginUsers().containsKey(thread)) { //here
                        return "error";
                    }
                   Database.getInstance().getLoginUsers().put(thread, current);//here

                    return "ACK";

                } else {
                    return "error";
                }
            }

        }


        return "error";
    }

    public String LOGOUT(String msg)  {
        Map map = Database.getInstance().getLoginUsers();
        //if (map.containsKey(Thread.currentThread())) {
        Thread t= Thread.currentThread();
            map.remove(Thread.currentThread());
            toTerminate = true;
            return "ACK";
        //}

     //   return "error";



    }

    public String COURSEREG(String msg) {

        String CourseNumberString;
        CourseNumberString = msg.substring(2, msg.length() ); //fixed was with -1
        int CourseNumber = Integer.parseInt(CourseNumberString);
        if (Database.getInstance().getCoursesMap().containsKey(CourseNumber)) {
            if (Database.getInstance().getLoginUsers().containsKey(Thread.currentThread()) &&
                    Database.getInstance().getCoursesMap().get(CourseNumber).isAvailablePlaces()&&
                    !Database.getInstance().getLoginUsers().get(Thread.currentThread()).isIs_admin()) {

                Course c = Database.getInstance().getCoursesMap().get(CourseNumber);
                user user = Database.getInstance().getLoginUsers().get(Thread.currentThread());
                for (int i = 0; i < c.getKdamCoursesList().length; i++) {
                    int courseKdamCourrent = c.getKdamCoursesList()[i];
                    if (!user.getRegCoursesList().contains(courseKdamCourrent))
                        return "error";

                }
                if(Database.getInstance().getCoursesMap().get(CourseNumber).getRegeisteredStudentsList().contains(user)) //i added so no student will be registered twice
                    return "error";

                Database.getInstance().getCoursesMap().get(CourseNumber).register(user);
                user.getRegCoursesList().add(CourseNumber);
                return "ACK";
            }

        }
        return "error";


    }

    public String KDAMCHECK(String msg) {
        String CourseNumberString;
        CourseNumberString = msg.substring(2, msg.length()); //fixed was with -1
        int CourseNumber = Integer.parseInt(CourseNumberString);
        Course c = Database.getInstance().getCoursesMap().get(CourseNumber);

        if (c.getKdamCoursesList()!=null)
        return (Arrays.toString(c.getKdamCoursesList())); //fix here
        else
            return "error";
    }

    public String COURSESTAT(String msg) {

        if (Database.getInstance().getLoginUsers().containsKey(Thread.currentThread()) && Database.getInstance().getLoginUsers().get(Thread.currentThread()).isIs_admin()) {
            String CourseNumberString;
            CourseNumberString = msg.substring(2, msg.length() );
            int CourseNumber = Integer.parseInt(CourseNumberString);
            Course currentCourse = Database.getInstance().getCoursesMap().get(CourseNumber);
            String courseName = currentCourse.getCourseName();
            int numOfSeatsAvailalbe = currentCourse.getNumOfMaxStudents() - currentCourse.getCurrentRegisteredStudents();
            int MaxOfSeats = currentCourse.getNumOfMaxStudents();
            String[] arr = new String[currentCourse.getRegeisteredStudentsList().size()];

            String StudentsReg="";
            for (int i = 0; i < currentCourse.getRegeisteredStudentsList().size(); i++) {
                String st = currentCourse.getRegeisteredStudentsList().get(i).getUserName();
                arr[i] = st;
            }

            StudentsReg = stringOrder(arr);

            String returnVal = "Course: (" + CourseNumber + ") " + courseName + "*" + "Seats Available: " + numOfSeatsAvailalbe + "/" + MaxOfSeats + "*" + "Students Registered: " + StudentsReg;
            return returnVal;
        }
        return "error";

    }

    private String stringOrder(String[] str) {
        int count = str.length;
        String temp;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (str[i].compareTo(str[j]) > 0) {
                    temp = str[i];
                    str[i] = str[j];
                    str[j] = temp;
                }
            }
        }
        if(str.length==0)
            return "[]";
        return  Arrays.toString(str);


    }

    public String STUDENTSTAT(String msg) {
        if (Database.getInstance().getLoginUsers().containsKey(Thread.currentThread()) && Database.getInstance().getLoginUsers().get(Thread.currentThread()).isIs_admin()) {
            String StudentName;
            StudentName = msg.substring(3, msg.length() - 1);
            for (int i = 0; i < Database.getInstance().getUsers().size(); i++) {
                if (Database.getInstance().getUsers().get(i).getUserName().equals(StudentName)) {

                    String string = "Student: " + StudentName + "*" + "Courses: " + Database.getInstance().getUsers().get(i).getRegCoursesList().toString();
                    return string;
                }
            }

        }
        return "error";

    }

    public String ISREGISTERED(String msg) {
        String CourseNumberString;
        CourseNumberString = msg.substring(2, msg.length());
        int CourseNumber = Integer.parseInt(CourseNumberString);
        Map map = Database.getInstance().getLoginUsers();
        if (map.containsKey(Thread.currentThread())) {
            user user = Database.getInstance().getLoginUsers().get(Thread.currentThread());
            if (user.getRegCoursesList().contains(CourseNumber)) {
                return "REGISTERED";
            }

        }
        return "NOT REGISTERED";
    }


    public String UNREGISTER(String msg) {
        String CourseNumberString;
        CourseNumberString = msg.substring(2, msg.length() );
        int CourseNumber = Integer.parseInt(CourseNumberString);
        if (Database.getInstance().getCoursesMap().containsKey(CourseNumber)) {
            if (Database.getInstance().getLoginUsers().containsKey(Thread.currentThread())) {
                Course c = Database.getInstance().getCoursesMap().get(CourseNumber);
                user user = Database.getInstance().getLoginUsers().get(Thread.currentThread());

                Database.getInstance().getCoursesMap().get(CourseNumber).Unregister(user);
                int courseindex= user.getRegCoursesList().indexOf(CourseNumber);
               if (courseindex!=-1)
               {  user.getRegCoursesList().remove(courseindex);
                return "ACK";}
            }

        }
        return "error";

    }


    public String MYCOURSES(String msg) {
        if (Database.getInstance().getLoginUsers().containsKey(Thread.currentThread())) {
            user user = Database.getInstance().getLoginUsers().get(Thread.currentThread());
            return user.getRegCoursesList().toString();


        }
        return "error";

    }
    public String ACK(String respond,String opcode)
    {
        if(respond=="ACK")
        return "ACK "+opcode;

        return "ACK "+opcode +" "+ respond;
    }
    public String ERROR(String msg,String opcode)
    {
        return "ERROR "+opcode;
    }

}



