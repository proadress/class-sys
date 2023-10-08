package main;  
import javax.swing.JOptionPane;
import course.Course;
import j.adminpenal;
import j.spenal;
import j.tpenal;
import user.AdminUsers;
import user.Student;
import user.Teacher;
import user.User;

public class MainControl {
	
	public static boolean isexit;
	User u;
	Student s;
	Teacher t;
	static adminpenal c = new adminpenal();
	static spenal sp = new spenal();
	static tpenal tp = new tpenal();
	
	public static void main(String[] args) {
		Course.readFromFile();		//將課程訊息讀入
		
		User.readStuInfo(); 		//學生訊息讀入
		User.readTchInfo();			//教授訊息讀入
		Student.prepareStuCourse(); //讀取學生選課訊息建立對應關係，map<int, vector<int>>
		
		menu();
	}
	
	public static void menu() {
		isexit = true;
		//while(isexit) {
		String[] a = {"退出","管理員","教授","學生"};
		int choice = JOptionPane.showOptionDialog(null, "學生選課系統", "期末作品",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, a, 0);
		switch(choice) {
		case 1://管理員登入
			if (AdminUsers.admin.login()!=null) {
				isexit = false;
				c.open();
				isexit = false;
			}
			isexit = false;
			break;
		case 2: //教授登入
			Teacher tmp = new Teacher();
			Teacher t = tmp.login();
			if (t!=null)
				tp.open(t); 
			isexit = false;
			break;
		case 3:
			Student tmp_ = new Student();
			Student s = tmp_.login();
			if (s!=null) {
				sp.open(s);
				isexit = false;
			}
			break;
		case 0:
			end();
		}
		//}
	}
	
	public static void end() {
		User.saveStuInfo();			//保存學生訊息
		User.saveTchInfo();			//保存教授訊息
		Student.saveToFile();		//保存學生選課訊息
		Course.saveTofile();		//保存課程訊息
		User.saveAdminInfo();		//保存管理員訊息
		System.exit(0);
	}
	
}
