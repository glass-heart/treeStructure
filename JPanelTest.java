import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class JPanelTest extends JPanel implements ActionListener {

	JScrollBar hbar;
	JScrollBar vbar;
	JMenuBar menuBar;
	int distance;
	
	private static final long serialVersionUID = 3958289694757353118L;
	
	JPanelTest()
	{
		super(true);
		setLayout(new BorderLayout());
		hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 1000);
	    vbar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 1000);

	    hbar.addAdjustmentListener(new MyAdjustmentListener());
	    vbar.addAdjustmentListener(new MyAdjustmentListener());

	    add(hbar, BorderLayout.SOUTH);
	    add(vbar, BorderLayout.EAST);
	    menuInitialize();
	    distance = 200;
	    
	    //System.out.println("JPanelTest");
	}
	
	private NodeArray<String> initialTestTree()
	{
		NodeArray<String> na = new NodeArray<>();
		na.addChildren(Arrays.asList(new NodeArray.Node<String>("1"),
									new NodeArray.Node<String>("2"),
									new NodeArray.Node<String>("3")));
		na.getChild(0).addChildren(Arrays.asList(new NodeArray.Node<String>("4"),
												new NodeArray.Node<String>("5")));
		NodeArray.Node<String> element = na.getChild(1).addChild(new NodeArray.Node<String>("6"));
		element.addChildren(Arrays.asList(new NodeArray.Node<String>("10"),
									new NodeArray.Node<String>("11"),
									new NodeArray.Node<String>("12")));
		na.getChild(0).getChild(0).addChild(new NodeArray.Node<String>("7"));
		na.getChild(0).getChild(1).addChildren(Arrays.asList(new NodeArray.Node<String>("8"),
															new NodeArray.Node<String>("9")));
		na.getChild(1).getChild(0).getChild(2).addChildren(Arrays.asList(new NodeArray.Node<String>("14"),
																		new NodeArray.Node<String>("15")));
		na.getChild(1).getChild(0).getChild(2).getChild(0).addChildren(Arrays.asList(new NodeArray.Node<String>("16"),
																					new NodeArray.Node<String>("17"),
																					new NodeArray.Node<String>("18")));
		na.getChild(1).getChild(0).getChild(2).getChild(1).addChildren(Arrays.asList(new NodeArray.Node<String>("19"),
																					new NodeArray.Node<String>("20")));
		na.getChild(1).getChild(0).getChild(2).getChild(1).getChild(1).addChildren(Arrays.asList(new NodeArray.Node<String>("21"),
																					new NodeArray.Node<String>("22"),
																					new NodeArray.Node<String>("23")));
		na.getChild(1).addChild(new NodeArray.Node<String>("13"));
		na.getChild(0).getChild(1).getChild(0).addChild(new NodeArray.Node<String>("0"));
		return na;
	}
	

	@Override
    public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		int shiftX = hbar.getValue()*(-1);
		int shiftY = vbar.getValue()*(-1);
		List<positionShift> pl = new ArrayList<>();
		pl.add(new positionShift(0));
		
		int previous_level = 0;
		int x = 5;
		int y = 5;
		g.drawOval(x+shiftX, y+shiftY, distance/2, distance/2);
		pl.get(0).setHeadPosition(x + distance/4, y + distance/2);
		
		for (NodeArray.Node<String> n: initialTestTree())
		{
			if (pl.size() < n.getLevel())
			{
				pl.add(new positionShift(0));
				pl.get(n.getLevel()-1).setHeadPosition(x + distance/4, y + distance/2);
			}
			
			if (previous_level >= n.getLevel())
			{
				for (int i = n.getLevel(); i < pl.size(); i++)
				{
					//System.out.println("setting for level "+i+": x "+pl.get(i).getHeadX() + " -> " + (pl.get(i).getHeadX()+distance));
					pl.get(i).setHeadPosition(pl.get(i).getHeadX()+distance, pl.get(i).getHeadY());
				}
			}
			
			x = 5 + distance*(pl.get(n.getLevel()-1).getShift());
			pl.get(n.getLevel()-1).incrementShift();
			y = 5 + (distance*n.getLevel());
			//System.out.println(n.toString() + "; level:" + n.getLevel() + "; x:" + x + "; y:" + y + 
			//		"; HeadX:" + pl.get(n.getLevel()-1).getHeadX() + "; HeadY:" + pl.get(n.getLevel()-1).getHeadY());
			g.drawOval(x+shiftX, y+shiftY, distance/2, distance/2);
			g.drawString(n.toString(), x + distance/5 + shiftX, y + distance/3 + shiftY);
			g.drawLine(pl.get(n.getLevel()-1).getHeadX()+shiftX, pl.get(n.getLevel()-1).getHeadY()+shiftY, x+distance/4+shiftX, y+shiftY);
			previous_level = n.getLevel();
		}
		//System.out.println("paintComponent:"+distance);
		
		Comparator<positionShift> compY = (a0, a1) -> a0.getHeadY() < a1.getHeadY() ? -1 :
														a0.getHeadY() == a1.getHeadY() ? 0 : 1;
		
		Comparator<positionShift> compX = (a0, a1) -> a0.getHeadX() < a1.getHeadX() ? -1 :
														a0.getHeadX() == a1.getHeadX() ? 0 : 1;
		
		vbar.setMaximum(Collections.max(pl, compY).getHeadY());
		hbar.setMaximum(Collections.max(pl, compX).getHeadX());
		vbar.setUnitIncrement(Collections.max(pl, compY).getHeadY()/10);
		hbar.setUnitIncrement(Collections.max(pl, compX).getHeadX()/10);
    }
	
	static private class positionShift
	{
		private int shift;
		private int headX;
		private int headY;
		positionShift(int shift)
		{
			this.shift = shift;
		}
		
		public void incrementShift()
		{
			shift++;
		}
		
		public void setHeadPosition(int headX, int headY)
		{
			this.headX = headX;
			this.headY = headY;
		}
		
		public int getHeadX()
		{
			return headX;
		}
		
		public int getHeadY()
		{
			return headY;
		}
		
		public int getShift()
		{
			return shift;
		}
	}
	
	class MyAdjustmentListener implements AdjustmentListener {
	    public void adjustmentValueChanged(AdjustmentEvent e) {
	      repaint();
	    }
	}
	
	private void menuInitialize()
	{
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Scale");
		JMenuItem menuItem = new JMenuItem("Scale up");
		menuItem.setActionCommand("scaleup");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Scale down");
		menuItem.setActionCommand("scaledown");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
	}
	
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 500);
        JPanelTest jpt = new JPanelTest();
        jFrame.setContentPane(jpt);
        jFrame.setJMenuBar(jpt.menuBar);
        jFrame.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("scaleup"))
		{
			distance += 25;
		}
		if (arg0.getActionCommand().equals("scaledown"))
		{
			distance -= 25;
		}
		
	}
}
