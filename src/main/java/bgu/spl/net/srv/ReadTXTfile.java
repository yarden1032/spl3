package bgu.spl.net.srv;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadTXTfile {

    public static  Map<Integer,Course> readTXTfile(String inputlocation) throws Exception {
        Map<Integer,Course> mapToAdd = new HashMap<Integer, Course>();
            File myObj = new File(inputlocation);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //Read the course number
                int i = 0;
                int j = data.indexOf('|', i);
                String partString = data.substring(i, j );
                int courseNum = Integer.parseInt(partString);
              //Read the course name
                i = j + 1;
                j = data.indexOf('|', i);
                partString = data.substring(i, j );
                String courseName = partString;
                //Read the Kdam courses
                i = j + 1;
                j = data.indexOf('|', i);
                partString = data.substring(i, j );
                String plainStr = partString.substring(1, partString.length() - 1); // clear braces []
                String[] parts = plainStr.split(",");
                int [] KdamCoursesList;
                if (partString.equals("[]")){
                   KdamCoursesList=new int[0];
                }else {
                     KdamCoursesList = Stream.of(parts).mapToInt(Integer::parseInt).toArray();
                }
                //read the students capacity
                i=j+1;
               j=data.length();
                partString = data.substring(i, j );
                int numOfMaxStudents  = Integer.parseInt(partString);
                //build course

                //add course

               addCourse(courseNum,courseName,KdamCoursesList,numOfMaxStudents,mapToAdd);

            }
            myReader.close();

            return mapToAdd;

    }

    private static void addCourse(int courseNum, String courseName, int[] KdamCoursesList, int numOfMaxStudents, Map<Integer, Course> maptoadd) throws Exception
    {

        for(int i=0;i<KdamCoursesList.length;i++)
        {
            if (KdamCoursesList[i]<0)
            {
                Exception e=new Exception();
                throw e;
            }
        }

        if(courseNum>=0&&!courseName.isEmpty()&&numOfMaxStudents>=5)
        {
            maptoadd.put(courseNum,new Course(courseName,KdamCoursesList,numOfMaxStudents));
        }
        else
        {
            Exception e=new Exception();
            throw e;
        }
    }
}
