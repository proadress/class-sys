package j;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import main.MainControl;
import user.Teacher;

public class tpenal extends JPanel implements ActionListener
{
	/**
	 *  
	 */
	private static final long serialVersionUID = -2835957135373636255L;
	Teacher t;
	static Bframe f;
	JMenuBar mb;
	JMenu jop1;
	JMenuItem[] dialog1;
	String[] s = {"修改密碼","查看所受課程","查看課程學生","退出"};
	public void open(Teacher t) {
		this.t = t;
		f = new Bframe();
		this.setPreferredSize(new Dimension(500,200));
		f.add(this);
		mb=new JMenuBar();
		
		jop1=new JMenu("教師端");
	    dialog1=new JMenuItem[4];
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
		user.Teacher.tchMenu(t, i);
	}
}
