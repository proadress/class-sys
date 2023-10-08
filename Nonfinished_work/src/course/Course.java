package course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.JOptionPane;
import user.AdminUsers;
import user.Student;

public class Course {
	public int id; //課號
	public String name;    //課名
	public int tp; //課程選修類型，0選修，1必修
	public String teacher; //教授
	public int stuNum; //選課人數
	
	public static Vector<Course> courseSet = new Vector<Course>(); // 全部課程集合
	
	public Course() {}

	public Course(int id, String name, int tp, String teacher, int stuNum) {
		this.id = id;
		this.name = name;
		this.tp = tp;
		this.teacher = teacher;
		this.stuNum = stuNum;
	}

	
	public String show(boolean a) {
		String s = id + "  " + name+"  ";
		if (tp==0)s += "選修";
		else if (tp==1)s += "必修";
		else s += "其他";	
		j.adminpenal.ss.add(s + "  "+teacher+"  "+stuNum);
		if(a) {
			JOptionPane.showMessageDialog(null,s + "  "+teacher+"  "+stuNum);
			return null;
		}
		else return s+="  "+teacher+"  "+stuNum+"\n";
	}
	
	public static void addCourses() {
		while(true) {
			try {			
			Course c = null;
			int cid = Integer.parseInt(JOptionPane.showInputDialog("輸入课程ID:",null));
			String cname = JOptionPane.showInputDialog("輸入课名:",null);
			String[] a = {"選修","必修"};
			int ctp = JOptionPane.showOptionDialog(
					null, 
					"學生選課系統", 
					"期末作品", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.INFORMATION_MESSAGE, 
					null, 
					a, 
					0);
			
			String ctcher = JOptionPane.showInputDialog("輸入上課教授:",null);
			int cstuNum = Integer.parseInt(JOptionPane.showInputDialog("輸入選課人數:",null));
			if (ctp==0) {
				int maxStuNum = Integer.parseInt(JOptionPane.showInputDialog("輸入課程最大人數:",null));
				c = new OptionalCourse(cid, cname, ctp, cstuNum, ctcher, maxStuNum); //添加到選修课程Vector中
			} 
			else {
				float credit = Integer.parseInt(JOptionPane.showInputDialog("輸入課程學分:",null));
				c = new RequiredCourse(cid, cname, ctp, ctcher, cstuNum, credit); //添加到必修课程Vector中
			}
			Course.courseSet.add(c);
			int b = JOptionPane.showConfirmDialog(null,"課程添加成功，是否繼續添加？（y/n）");			
			if (b == 0)continue;
			else break;	
		} catch (Exception e) {return;}
		}
	}
	
	public static String showAllCourses(boolean a) { //這邊等等弄
		if (isempty()) return null;		
		String s = "課程號  課名  類型  教授  選課人數  學分或最大選課人数\n";
		Course.updateRequiredCourseStuNum();
		for (Course c : Course.courseSet) {
			s += c.show(false);
		}
		if(a) {
			JOptionPane.showMessageDialog(null, s);
			return null;
		}
		else return s;
		
	}
	
	public static void sortCourses() {
		if (isempty()) return; //如果沒有，則不需顯示

		Course s1, s2;
		
		for (int i=Course.courseSet.size()-1; i>0; --i) //bubble sort
			for (int j=0; j<i; ++j) {
				s1=Course.courseSet.get(j);
				s2=Course.courseSet.get(j+1);
				if (s1.stuNum>s2.stuNum) { //大小交換
					Course.courseSet.set(j, s2);
					Course.courseSet.set(j+1, s1);
				}
			}
	}
	
	public static void searchCourseByTeacher() {
		try {
		if (isempty()) return;
		String teacherName = JOptionPane.showInputDialog("輸入教授名:",null);
		String s="";
		for (Course c : Course.courseSet) {
			if (c.teacher.equals(teacherName))
				s += c.show(false);
		}
		JOptionPane.showMessageDialog(null, s);
		} catch (Exception e) {return;}
	}
	
	public static boolean deleteCourse() {
		try {
		String objCourse = JOptionPane.showInputDialog("輸入要刪除的課程名稱:",null);
		Course tmpC = null;
		for (int i=0; i<Course.courseSet.size(); ++i) {
			tmpC = Course.courseSet.get(i);
			if (tmpC.name.equals(objCourse)) {
				Course.courseSet.remove(i); //從課程Vector中删除
				
				// 如果是選修課则需要從學生選課中删除
				if (tmpC.tp==0) //我額外加的
					deleteCourseInStu(tmpC.id);
				return true;
			}
		}
		return false;
		} catch (Exception e) {return false;}
	}
	
	public static void deleteCourses() {
		if (isempty()) return;
		
		while(true) {
			int b =0;
			if (Course.deleteCourse())
				 b = JOptionPane.showConfirmDialog(null, "課程已刪除，是否繼續刪除？");
				
			else
				b = JOptionPane.showConfirmDialog(null, "刪除失敗，沒有此項課程...是否繼續刪除或是退出:");
				//System.out.print("刪除失敗，沒有此項課程...\n輸入y繼續刪除，輸入n退出:");
			if (b == 0) {
				continue;
			} else
				break;
		}
	}
	
	public static void setCourseTeacher() {
		try {
		if (isempty()) return;
		String courseName = JOptionPane.showInputDialog("輸入課程名稱:",null);
		
		for (int i=0; i<Course.courseSet.size(); ++i)
			if (Course.courseSet.get(i).name.equals(courseName)) {
				Course tmp = Course.courseSet.get(i); 
				JOptionPane.showMessageDialog(null,"查找到此課程:\n" + tmp.show(false));
				String tcherName = JOptionPane.showInputDialog("輸入更改後的教授名稱:",null);
				tmp.teacher = tcherName;
				Course.courseSet.set(i, tmp);
				JOptionPane.showMessageDialog(null,"已更改:\n" + tmp.show(false));
				
				return;
			}
		JOptionPane.showMessageDialog(null,"無此課程...");
		} catch (Exception e) {return;}
	}
	
	private static boolean isempty() {
		if (Course.courseSet.size()!=0)
			return false;
		else {
			JOptionPane.showMessageDialog(null,"操作失敗，暫無任何課程...");
			//System.out.println("操作失敗，暂無任何課程...");
			return true;
		}
	}
	
	public String toString() {
		return id+"  "+name+"  "+tp+"  "+stuNum+"  "+teacher;
	}
	
	public static void saveTofile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("course data.txt"));
			
			for (Course c : Course.courseSet)
				out.write(c.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromFile() {
		File f = new File("course data.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int id = Integer.parseInt(info_[0]);
				String name = info_[1];
				int tp = Integer.parseInt(info_[2]);
				int stuNum = Integer.parseInt(info_[3]);
				String teacher = info_[4];
				
				if (tp==0) {
					int maxStuNum = Integer.parseInt(info_[5]);
					Course.courseSet.add(new OptionalCourse(id, name, tp, stuNum, teacher, maxStuNum));
				} else {
					float credit = Float.parseFloat(info_[5]);
					Course.courseSet.add(new RequiredCourse(id, name, tp, teacher, stuNum, credit));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String showAllRequiredCourses() {
		String s = "";
		for (Course c : Course.courseSet)
			if (c.tp==1)
				s += c.show(false);
			return s;
		
	}
	
	public static Course getCourseById(int cid) {
		for (Course c_ : Course.courseSet)
			if (c_.id==cid)
				return c_;
		
		return null;
	}
	
	public static void updateRequiredCourseStuNum() {
		for (Course c_ : Course.courseSet)
			if (c_.tp==1)
				c_.stuNum = AdminUsers.s.size();
	}
	
	public static void deleteCourseInStu(int cid) {
		int i=0;
		Vector<Integer> tmpM = null;
		for (Integer sid : Student.allStuCourses.keySet()) {
			tmpM = Student.allStuCourses.get(sid.intValue());
			for (i=0; i<tmpM.size(); ++i) //顯示此學生課程列表
				if (tmpM.get(i)==cid)
					Student.allStuCourses.get(sid.intValue()).remove(i);
		}
	}
}
