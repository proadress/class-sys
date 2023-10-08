package user;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import course.Course;
import course.OptionalCourse;
import main.MainControl;

public class Student extends User {
	private int stuId; //學號
	private String class_; //班級
	
	
	public static Map<Integer, Vector<Integer>> allStuCourses = null;
	public Student(String name, String pwd, int stuId, String class_) {
		this.name = name;
		this.pwd = pwd;
		this.stuId = stuId;
		this.class_ = class_;
	}
	
	public Student() {}

	public String showInfo(boolean a) {
		if(a) {
			JOptionPane.showMessageDialog(null,stuId+" "+name+" "+class_);
			return null;
		}
		else return stuId+" "+name+" "+class_ + "\n";
	}
	
	public Student login() {
		try {
		int stuId = Integer.parseInt(JOptionPane.showInputDialog("請輸入學號",null));
		String pwd = JOptionPane.showInputDialog("請輸入密碼:","123456");
		for (Student s : AdminUsers.s)
			if (s.stuId==stuId && s.pwd.equals(pwd)) {
				JOptionPane.showMessageDialog(null,s.name+"同學, 你好!");
				return s;
			}
		JOptionPane.showMessageDialog(null,"用戶密碼錯誤, 登入失敗...");
		MainControl.menu();
		return null;
		} catch (Exception e) {
			MainControl.menu();
			return null;
			}
	}

	public int getStuId() {
		return stuId;
	}
	
	public String toString() {
		return stuId+"  "+name+"  "+class_+"  "+pwd;
	}
	
	public static void stuMenu(Student s,int choice) {
		Student.prepareStuCourse();
			switch(choice) {
				case 0: s.changePwd(); break;
				case 1: Student.viewSelectedCourse(s); break;
				case 2: Student.selectCourses(s); break;
				case 3: return; //保存退出
				case 4: 
				default: break;
			}
	}
	
	
	public static void prepareStuCourse() {
		if (allStuCourses==null) {
			allStuCourses = new HashMap<Integer, Vector<Integer>>();
			Student.readFromFile();
		}
	}
	
	public static void viewSelectedCourse(Student s) { //晚點弄
		String ss = "課號  課程名稱  類型  教授  學分 \n";

		Course.updateRequiredCourseStuNum();
		ss += Course.showAllRequiredCourses(); //變成一個新的字串，回傳S
		if (Student.allStuCourses.containsKey(s.stuId)==false)
			Student.allStuCourses.put(s.stuId, new Vector<>());
		
		Course tmpC = null;
		for (Integer cid : Student.allStuCourses.get(s.stuId))
			if ((tmpC=Course.getCourseById(cid.intValue()))!=null)
				ss += tmpC.show(false);
		JOptionPane.showMessageDialog(null, ss);//已排版ss輸出
	}

	public static void selectCourses(Student s) {
		try {
		int b = 0;
		if (Course.courseSet.size()!=0) {
			// 選課操作
			do {
				int courseId = Integer.parseInt(JOptionPane.showInputDialog(Course.showAllCourses(false) + "請輸入課號",null));
				boolean haveSelected = false;
				for (int i=0; i<Course.courseSet.size(); ++i) {
					Course c_ = Course.courseSet.get(i);
					
					if (c_.id==courseId) { //存在此門課程
						// 判斷是否已選此門課			
						if(Student.allStuCourses.get(s.stuId)!=null) {
							for (Integer cid : Student.allStuCourses.get(s.stuId))
								if (cid.intValue()==courseId)
									haveSelected = true;
						}
						if (c_.tp==0 && haveSelected==false) {//選修課且尚未選修該門課程
							OptionalCourse ctmp = (OptionalCourse)c_; 

							// 判斷選修課人數是否已滿
							if (ctmp.stuNum==ctmp.maxStuNum) {
								JOptionPane.showMessageDialog(null,"選課失敗，該選修課人數已滿...");
								return;
							}

							if (Student.allStuCourses.containsKey(s.stuId)==false)
								Student.allStuCourses.put(s.stuId, new Vector<>());
							Student.allStuCourses.get(s.stuId).add(c_.id);
							++ctmp.stuNum;
							Course.courseSet.set(i, ctmp); //上溯造型
						}
						else
							JOptionPane.showMessageDialog(null,"課程編號錯誤或你已選修該課程...");
						return;
					}
				}
				JOptionPane.showMessageDialog(null,"無該課程...");
				b  = JOptionPane.showConfirmDialog(null, "是否繼續選課?");
			} while (b == 0); 
		} else
			JOptionPane.showMessageDialog(null,"目前無任何課程...");
		} catch (Exception e) {return;}
	}
	
	public static void saveToFile() {
		if (Student.allStuCourses!=null)
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("student courses.txt"));
				
				for (Integer i : Student.allStuCourses.keySet()) {
					out.write(i.intValue()+"");
					for (Integer cid : Student.allStuCourses.get(i))
						out.write("  "+cid.intValue());
					out.write("\r\n");
				}
				
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void readFromFile() {
		File f = new File("student courses.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				int stuId = Integer.parseInt(info_[0]);
				allStuCourses.put(stuId, new Vector<Integer>());
				
				for (int i=1; i< info_.length; ++i)
					allStuCourses.get(stuId).add(Integer.parseInt(info_[i]));
			}


			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isSelectedSomeCourse(Student s, Course co) {
		for (Integer sid : Student.allStuCourses.keySet())
			if (s.stuId==sid.intValue()) // 找到學生
				for (Integer cid : Student.allStuCourses.get(sid))
						if (co.id==cid.intValue())
							return true;
		return false;
	}
}
