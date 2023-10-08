package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import course.Course;
import main.MainControl;


public class User {
	public String name = "admin" ; //用戶名 //不管在何時退出，都不會有null null
	public String pwd = "123456";  //密碼
	
	public User() {}
	
	/**
	 * 顯示用戶訊息
	 */
	public String showInfo(boolean a) {
		if(a) {
			JOptionPane.showMessageDialog(null,name+":"+pwd);
			return null;
		}
		else return name+":"+pwd + "\n";
	}

	/**
	 * 設置用戶密碼
	 */
	public void changePwd() {
		try {
		String originalPwd = JOptionPane.showInputDialog("輸入原密碼:","");
		if (originalPwd.equals(this.pwd))
			while(true) {
				JOptionPane.showMessageDialog(null,"輸入新的密碼(6-12位):");
				String pwd = JOptionPane.showInputDialog("請輸入密碼","");
				JOptionPane.showMessageDialog(null,"確認密碼:");
				String pwd2 = JOptionPane.showInputDialog("確認密碼:","");
				if (pwd.length()>=6 && pwd.length()<=12 && pwd.equals(pwd2)) {
					this.pwd = pwd;
					JOptionPane.showMessageDialog(null,"密碼修改成功!");
					break;
				} 
				else continue;
			}
		else JOptionPane.showMessageDialog(null,"原密碼不正確");
		} catch (Exception e) {return;}
	}
	
	public User login() {
		User.getAdminInfo();
		String pwd = JOptionPane.showInputDialog("請輸入密碼","123456");
		if(pwd==null) {
			MainControl.menu();
			return null;
		}
		if (pwd.equals(AdminUsers.admin.pwd)) {
			JOptionPane.showMessageDialog(null, AdminUsers.admin.name + ", 歡迎您!");
			return AdminUsers.admin; //成功
		} 
		else {
			JOptionPane.showMessageDialog(null, "登入失敗，密碼錯誤");
			MainControl.menu();
			return null; //失敗
		}
	}
	public static void getAdminInfo() {
		File f = new File("admin pwd.txt");
		try {
			if (!f.exists())
				f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			if ((info=br.readLine())==null) {
				AdminUsers.admin.name = "admin"; //管理員初始名稱
				AdminUsers.admin.pwd = "123456"; //管理員初始密碼123456
			} else {
				String info_[] = info.split("  ");
				AdminUsers.admin.name = info_[0];
				AdminUsers.admin.pwd = info_[1];
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void saveAdminInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("admin pwd.txt"));
			
			out.write(AdminUsers.admin.name + "  " + AdminUsers.admin.pwd); // write password

			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeAdminName() {
		try {
		String newAdminName = JOptionPane.showInputDialog(null,"輸入新的管理員名稱:");
		AdminUsers.admin.name = newAdminName;
		JOptionPane.showMessageDialog(null,"名稱修改成功!");
		} catch (Exception e) {return;}
	}
	
	public static void funcDistribute(int funcId) {
			switch (funcId) {
			case 0: Course.addCourses(); 			break; //參考:輸入密碼 //已修改
			case 1: Course.deleteCourses();         break;
			case 2: Course.setCourseTeacher();      break;
			case 3: Course.showAllCourses(true); 	break;
			case 4: Course.sortCourses();    		break;
			case 5: User.addTeachers();   			break;
			case 6: User.deleteTeachers(); 			break;
			case 7: Course.searchCourseByTeacher(); break;
			case 8: AdminUsers.showAllTcher(); 		break;
			case 9: User.resetPwd(1); 				break;
			case 10: User.addStudents(); 			break;
			case 11: User.deleteStudents(); 		break;
			case 12: AdminUsers.showAllStudent(); 	break;
			case 13: User.resetPwd(0); 				break;
			case 14: AdminUsers.admin.changePwd();  break;
			case 15: User.changeAdminName();	    break;
			case 16:
				Course.saveTofile(); //保存所有課程訊息
				User.saveStuInfo();
				User.saveTchInfo();
				return;
			default:
			}
	}
	
	public static void addTeachers() {
		try {
		while(true) {
			String name = JOptionPane.showInputDialog("姓名:",null);
			int workId  = Integer.parseInt(JOptionPane.showInputDialog("編號:",null));		
			String occupation_ = JOptionPane.showInputDialog("職稱:",null);
			AdminUsers.t.add(new Teacher(name, "123456", workId, occupation_));
			int b = JOptionPane.showConfirmDialog(null, "教師訊息添加成功，是否繼續添加?");
			
			if (b == 0)
				continue;
			else
				break;
		}
		} catch (Exception e) {return;}
	}

	public static boolean deleteTeacher() {
		try {
		int objTeacher = Integer.parseInt(JOptionPane.showInputDialog("請輸入要刪除之教師編號",null));

		Teacher tmp = null;
		for (int i=0; i<AdminUsers.t.size(); ++i)
			if ((tmp=AdminUsers.t.get(i)).getWorkId()==objTeacher) {
				AdminUsers.t.remove(i); //刪除教師訊息

				//首先置空其教授的課程
				for (Course c : Course.courseSet)
					if (c.teacher.equals(tmp.name))
						c.teacher = "---";
				return true;
			}
		return false;
		} catch (Exception e) {return false;}
	}

	public static void deleteTeachers() {
		if (AdminUsers.t==null || AdminUsers.t.size()==0) {
			JOptionPane.showMessageDialog(null,"暫無任何教師訊息");
			return; //空
		}

		while(true) {
			int b = 0;
			if (User.deleteTeacher())
				b  = JOptionPane.showConfirmDialog(null, "教師訊息刪除成功，是否繼續刪除?");
			else
				b  = JOptionPane.showConfirmDialog(null, "教師訊息刪除失敗，是否繼續刪除?");
			if (b == 0) { //
				continue;
			} else
				break;
		}
	}

	public static void addStudents() {
		try {
		while(true) {
			int b = 0;
			String name = JOptionPane.showInputDialog("姓名:",null);
			int stuId = Integer.parseInt(JOptionPane.showInputDialog("學號:",null));
			String class_ = JOptionPane.showInputDialog("班級:",null);
			AdminUsers.s.add(new Student(name, "123456", stuId, class_));
			b  = JOptionPane.showConfirmDialog(null, "學生添加成功，是否繼續添加?");
			if (b == 0)
				continue;
			else
				break;
		}
		} catch (Exception e) {return;}
	}
	//這邊有問題0619 Student.updateStuCourses(t);
	public static boolean deleteStudent() {
		try {
		int objStu = Integer.parseInt(JOptionPane.showInputDialog("請輸入要刪除之學生訊息",null));

		Student tmp = null;
		for (int i=0; i<AdminUsers.s.size(); ++i)
			if (objStu==(tmp=AdminUsers.s.get(i)).getStuId()) {
				AdminUsers.s.remove(i);
				if(Student.allStuCourses.get(tmp.getStuId()) != null) {	
				// 該學生選修的課程人數减一
				for (Integer cid : Student.allStuCourses.get(tmp.getStuId()))
					for (Course c : Course.courseSet)
						if (c.id==cid.intValue())
							--c.stuNum;}
				return true;
			}
		return false;
		} catch (Exception e) {return false;}
	}

	public static void deleteStudents() {
		if (AdminUsers.s==null || AdminUsers.s.size()==0) {
			JOptionPane.showMessageDialog(null,"暫無任何學生訊息");
			//System.out.println("暫無任何學生訊息");
			return; //空
		}
		
		while(true) {
			int b = 0; //0表yes 其他表no
			if (User.deleteStudent())
				b = JOptionPane.showConfirmDialog(null,"學生訊息删除成功，是否繼續删除？");
			else
				b =  JOptionPane.showConfirmDialog(null,"删除失败，無匹配的學生訊息:");
			if (b == 0) {
				continue;
			} else
				break;
		}
	}
	
	public static void saveStuInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("student info.txt"));
			
			for (Student s : AdminUsers.s)
				out.write(s.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readStuInfo() {
		File f = new File("student info.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int stuId = Integer.parseInt(info_[0]);
				String name = info_[1];
				String class_ = info_[2];
				String pwd = info_[3];
				
				AdminUsers.s.add(new Student(name, pwd, stuId, class_));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTchInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("teacher info.txt"));
			
			for (Teacher t : AdminUsers.t)
				out.write(t.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readTchInfo() {
		File f = new File("teacher info.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int workId = Integer.parseInt(info_[0]);
				String name = info_[1];
				String occupation = info_[2];
				String pwd = info_[3];
				
				AdminUsers.t.add(new Teacher(name, pwd, workId, occupation));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void resetPwd(int flag) { //1 tch| 0 stu
		try {
		int Id=0, pos=0,b=0;
		Teacher u=null; Student s=null;
		do {
			
			Id = Integer.parseInt(JOptionPane.showInputDialog("輸入" +(flag==1?"教師":"學生")+"id:",null));
			pos=User.findPosById(Id, flag);
			if (pos!=-1) {
				if (flag==1) {
					u = AdminUsers.t.get(pos);
					u.pwd = "123456";
					AdminUsers.t.set(pos, u);
				} else {
					s = AdminUsers.s.get(pos);
					s.pwd = "123456";
					AdminUsers.s.set(pos, s);
				}
				JOptionPane.showMessageDialog(null,"修改成功");
			} else
				JOptionPane.showMessageDialog(null,"未搜索到此" +(flag==1?"教師":"學生")+"...");
			b  = JOptionPane.showConfirmDialog(null, "是否繼續修改?");
		} while (b == 0);
		} catch (Exception e) {return;}
	}
	public static int findPosById(int id, int flag) {
		int pos = 0;
		if (flag==1) //教師
			for (Teacher t : AdminUsers.t)
				if (t.getWorkId()==id)
					return pos;
				else ++pos;
		else
			for (Student s : AdminUsers.s)
				if (s.getStuId()==id)
					return pos;
				else ++pos;
		return -1;
	}
}
