package j;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
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
	int w = 800;
	int h = 800;
	Image student = new ImageIcon("student.png").getImage();
	private static final long serialVersionUID = -2835957135373636255L;
	Student ss;
	static Bframe f;
	JMenuBar mb;
	
	public static ArrayList<String> inputword = new ArrayList<>(); 
	
	JMenu jop1;
	JMenuItem[] dialog1;
	String[] s = {"修改密碼","查看所選的課程","選課","退出"};
	public void s() {
		inputword.add("學生端系統");
		inputword.add("修改密碼:");
		inputword.add("預設是123456，可以改成你想要的，");
		inputword.add("要記得確認以免忘記。");
		inputword.add("");
		inputword.add("查看所選課程:");
		inputword.add("可以看你選了甚麼課");
		inputword.add("");
		inputword.add("選課:");
		inputword.add("輸入課號可以進行選課，要記得必修不用選，");
		inputword.add("如果人滿也選不了。");
	}
	public void open(Student ss) {
		s();
		this.ss = ss;
		f = new Bframe();
		this.setPreferredSize(new Dimension(adminpenal.w,adminpenal.h));
		f.add(this);
		mb=new JMenuBar();
		
		jop1=new JMenu("學生端");
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
		user.Student.stuMenu(ss, i);
		repaint();
	}
	
	@Override
    protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D)graphics;
		
		 g.drawImage(student,0,0,w,h, this); // see javadoc for more info on the parameters 
		
		g.setPaint(Color.BLUE);
		
		g.setFont(new Font("Meet Tenorite",Font.LAYOUT_LEFT_TO_RIGHT,22));
		for(int i =0;i<inputword.size();i++)
			g.drawString(inputword.get(i),300,300+i*22);
			
                  
    }
 
}
