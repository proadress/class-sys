package user;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import course.Course;
import main.MainControl;


public class Teacher extends User {
	private int workId;
	private String occupation; //職位
	
	public static Map<Integer, Vector<Integer>> allTchCourses = new HashMap<>();
	
	public Teacher(String name, String pwd, int workId, String occupation) {
		this.name = name;
		this.pwd = pwd;
		this.workId = workId;
		this.occupation = occupation;
	}
	
	public Teacher() {}	

	public String showInfo(boolean a) {
		if(a) {
			JOptionPane.showMessageDialog(null,workId+" "+name+" "+occupation);
			return null;
		}
		else return workId+" "+name+" "+occupation + "\n";
	}

	public Teacher login() {
		try {
		int workId = Integer.parseInt(JOptionPane.showInputDialog("輸入教師編號:",null));
		String pwd = JOptionPane.showInputDialog("請輸入密碼:","123456");
		for (Teacher t : AdminUsers.t)
			if (t.workId==workId && t.pwd.equals(pwd)) {
				JOptionPane.showMessageDialog(null,t.name+"你好!");
				return t;
			}
		JOptionPane.showMessageDialog(null,"用戶名或密碼登入失敗...");
		MainControl.menu();
		return null;
		} catch (Exception e) {
			MainControl.menu();
			return null;}
	}

	public int getWorkId() {
		return workId;
	}

	public String toString() {
		return workId+"  "+name+"  "+occupation+"  "+pwd;
	}
	
	public static void tchMenu(Teacher t,int choice) {
			switch(choice) {
				case 0: t.changePwd(); break;
				case 1: Teacher.updateTchCourses(t); Teacher.viewTchCourses(t); break;
				case 2: Teacher.updateTchCourses(t); Teacher.viewCourseStu(t); break;
				case 3: return; //保存退出
				default: break;
		}
	}
	
	public static void viewTchCourses(Teacher t) {
		String ss = "課程號  課程名  類型  教師  選課人數  學分或最大選客人數\n";
		Course.updateRequiredCourseStuNum();
		boolean haveClasses = false;
		for (Integer workId : Teacher.allTchCourses.keySet())
			if (workId==t.workId)
				for (Integer cid : Teacher.allTchCourses.get(workId))
					for (Course c_ : Course.courseSet)
						if (c_.id==cid) {
							haveClasses = true;
							ss += c_.show(false);
						}
		if (haveClasses==false)
			ss += "暫無課程...";
		JOptionPane.showMessageDialog(null,ss);
	}

	public static void viewCourseStu(Teacher t) {
		try {
		int cid_ = Integer.parseInt(JOptionPane.showInputDialog("請輸入課號",null));
		boolean anyOneSelect = false;
		
		// 是否存在該課程
		System.out.println(Teacher.allTchCourses.get(t.workId));
		if(Teacher.allTchCourses.get(t.workId) != null) {
			for (Integer cid : Teacher.allTchCourses.get(t.workId))
				if (cid_==cid) {

					if (Course.getCourseById(cid).tp==1) {
						JOptionPane.showMessageDialog(null, "您無法進行此操作...");
					//System.out.println("您無法進行此操作...");
						return;
								}
				String ss = "選修該課程學生列表:\n";
				//System.out.println("選修該課程學生列表:");
				
				for (Student s : AdminUsers.s)
					if (Student.isSelectedSomeCourse(s, Course.getCourseById(cid))) {
						anyOneSelect = true;
						ss += s.showInfo(false);
					}
				if (!anyOneSelect)
					ss += "暫無學生數據...";
					//System.out.println("暫無學生數據...");
				JOptionPane.showMessageDialog(null,ss);
				return;
								}
			}
		
		JOptionPane.showMessageDialog(null,"課程編號有誤or您並未教授該門課程...or該門課程為必修課");
		} catch (Exception e) {return;}
	}
	
	/**
	 * 從課程列表中更新教授的課程
	 */
	public static void updateTchCourses(Teacher t) {
		Teacher.allTchCourses.clear();
		for (Course c_ : Course.courseSet)
			if (c_.teacher.equals(t.name)) {
				if (Teacher.allTchCourses.containsKey(t.workId)==false)
					Teacher.allTchCourses.put(t.workId, new Vector<Integer>());
				Teacher.allTchCourses.get(t.workId).add(c_.id);
			}
	}
	
}
