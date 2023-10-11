package course;

import javax.swing.JOptionPane;

public class OptionalCourse extends Course {
	public int maxStuNum;

	public OptionalCourse(int id, String name, int tp, int stuNum, String teacher, int maxStuNum) {
		this.id = id;
		this.name = name;
		this.tp = tp;
		this.stuNum = stuNum;
		this.teacher = teacher;
		this.maxStuNum = maxStuNum;
	}
	
	public void show() {
		JOptionPane.showMessageDialog(null,id + "  " + name+"  選修  "+teacher+"  "+stuNum + "  "+maxStuNum);
		//System.out.println(id + "  " + name+"  選修  "+teacher+"  "+stuNum + "  "+maxStuNum);
	}

	public String toString() {
		return super.toString()+"  "+maxStuNum;
	}
}
