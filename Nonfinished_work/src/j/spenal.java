package j;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import main.MainControl;
import user.Student;

public class spenal extends JPanel implements ActionListener
{
	/**
	 *  
	 */
	private static final long serialVersionUID = -2835957135373636255L;
	Student ss;
	static Bframe f;
	JMenuBar mb;
	JMenu jop1;
	JMenuItem[] dialog1;
	String[] s = {"修改密碼","查看所選的課程","選課","退出","退課"};
	public void open(Student ss) {
		this.ss = ss;
		f = new Bframe();
		this.setPreferredSize(new Dimension(500,200));
		f.add(this);
		mb=new JMenuBar();
		
		jop1=new JMenu("學生端");
	    dialog1=new JMenuItem[s.length];
	    for (int i=0; i<dialog1.length; i++) {
	    	dialog1[i]=new JMenuItem(s[i]);
	    	jop1.add(dialog1[i]);
	        dialog1[i].addActionListener(this);
	    }
	    mb.add(jop1);
	    f.setJMenuBar(mb);
	    f.pack();
		f.setLocationRelativeTo(null);
	}
	
	public static void close(){
		f.dispose();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{		
		int i = 0;
		while(e.getActionCommand()!=s[i]) i++;
		if(i==3) {
			f.dispose();
			MainControl.menu();
		}
		user.Student.stuMenu(ss, i);
	}
}
