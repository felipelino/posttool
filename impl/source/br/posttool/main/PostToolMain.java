package br.posttool.main;

import br.posttool.gui.PostToolWindow;

/**
 * @author Felipe Lino
 */
public class PostToolMain
{

	public static void main(String[] args)
	{
		PostToolWindow jMain;
		PostToolWindow.setDefaultLookAndFeelDecorated(true);
		jMain = new PostToolWindow();
		
		jMain.setDefaultCloseOperation(PostToolWindow.EXIT_ON_CLOSE);
		jMain.setFocusable(true);
		jMain.setVisible(true);
	}
}
