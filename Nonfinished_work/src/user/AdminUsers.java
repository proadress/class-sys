package user;

import java.util.Vector; //存一個class
import javax.swing.JOptionPane;

public class AdminUsers {
		public static User admin = new User(); 
		
		public static Vector<Student> s = new Vector<>(); 
		public static Vector<Teacher> t = new Vector<>(); 
		
		public static void showAllStudent() {
			String ss = "";
			if(s!= null && s.size()!= 0)
			{
				ss = "學號  姓名  班級\n";
				//System.out.println("學號  姓名  班級");
				for(Student s : AdminUsers.s)
					ss += s.showInfo(false);
			}
			else
				ss += "暫無學生訊息";
				//System.out.println("暫無學生訊息");
			JOptionPane.showMessageDialog(null,ss);
		}

		public static void showAllTcher() {
			String ss = "";
			if(t!=null && t.size()!=0)
			{
				ss += "教師編號  姓名  職位\n";
				//System.out.println("教師編號  姓名  職位");
				for(Teacher t : AdminUsers.t)
					ss += t.showInfo(false);
			}
			else
				ss += "暫無教師訊息";
				//System.out.println("暫無教師訊息");
			JOptionPane.showMessageDialog(null,ss);
		}
		public AdminUsers() {
		}
}
