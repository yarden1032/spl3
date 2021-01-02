package bgu.spl.net;


import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.message.MessageImpl;
import bgu.spl.net.impl.message.ThreadPerClient;
import bgu.spl.net.impl.message.user;
import bgu.spl.net.srv.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	Server server;

	private Map<Thread,user> loginUsers=new HashMap<>(); //TODO to delete

//	private List<user> loginUsersList=new LinkedList<>();
	 private  List<user> users = new LinkedList<>();
	private Map<Integer,Course> coursesMap = new HashMap<Integer, Course>();
	private static class SingletonHolder{
		private static Database instance = new Database();


	}

	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
		//server.serve();
	}

	/**
	 * Retrieves the single instance of this class.
	 */

	public static Database getInstance() {

		return SingletonHolder.instance;
	}

	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */

	//todo: it was without public or private- need to ask
	public boolean  initialize(String coursesFilePath) {
		// TODO: implement
		try {
			coursesMap.putAll(ReadTXTfile.readTXTfile(coursesFilePath));
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	public void setServer(Server server)
	{
		this.server=server;
	}

	public List<user> getUsers() {
		return users;
	}

public Map<Thread, user> getLoginUsers() {
		return loginUsers;
	}

	public Server getServer() {
		return server;
	}

/*	public List<user> getLoginUsersList() {
		return loginUsersList;
	}*/

	public Map<Integer, Course> getCoursesMap() {
		return coursesMap;
	}
}
