/*
 * Created on 22/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.posttool.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.naming.Context;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import br.util.data.FieldValueList;
import br.util.file.filter.FileFilterJar;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class JmsConfigWindow extends JFrame
implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 1L;
	
	private FieldValueList listHeader;
	private JTextField fieldInitialContext;
	private JTextField fieldProviderUrl;
	private JTextField fieldQueue;
	private JTextField fieldConnectionFactory;
	private JTextField fieldClasspath;
	private JTextField fieldJmsCorrelationId;
	private JButton	btClasspath;
	private JButton btOK;
	
	public JmsConfigWindow(FieldValueList listHeader)
	{
		super("Set JMS Configuration");
		this.listHeader = listHeader;
		this.setSize(480,250);
		this.setResizable(true);
		Container cp = this.getContentPane();
		cp.add(designPanel());
		this.setDefaultCloseOperation(JmsConfigWindow.DISPOSE_ON_CLOSE);
	}

	private Component designPanel()
	{
		DefaultFormBuilder builder;
		CellConstraints cc;
		
		FormLayout layout = new FormLayout(
				"fill:3dlu," +
				"fill:pref:grow," +  //2
				"fill:3dlu," +  //3
				"fill:pref:grow," + //4
				"fill:3dlu," + //5
				"fill:pref:grow," + //6
				"fill:3dlu"  //7
				, 
				"3dlu," + 		// 1
				"pref," + 		// 2
				"3dlu," +		// 3
				"pref," +		// 4
				"3dlu," +		// 5
				"pref," +		// 6
				"3dlu," +		// 7
				"pref," +		// 8
				"3dlu," +		// 9
				"pref," +		// 10
				"3dlu,"	+		// 11
				"pref," +		// 12
				"3dlu," +		// 13
				"pref," +		// 14
				"3dlu" 		// 15
				); 	// 

		cc = new CellConstraints();
		//builder = new DefaultFormBuilder(layout, new FormDebugPanel());
		builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		/* Coluna , Linha, Celulas Linha, Células Coluna */
		builder.add(new JLabel(Context.INITIAL_CONTEXT_FACTORY + ":"),  cc.xyw(2,2,1));
		builder.add(new JLabel(Context.PROVIDER_URL + ":"),  			cc.xyw(2,4,1));
		builder.add(new JLabel("javax.jms.ConnectionFactory" + ":"),  	cc.xyw(2,6,1));
		builder.add(new JLabel("javax.jms.Queue" + ":"),  				cc.xyw(2,8,1));
		builder.add(new JLabel("JmsCorrelationId (Optional)" + ":"),  	cc.xyw(2,10,1));
		builder.add(new JLabel("Additional JAR" + ":"),  				cc.xyw(2,12,1));
		
		Map<String, String> properties = listHeader.getMap();
		String classpath = properties.get("classpath");
		String initialContextFactory = properties.get(Context.INITIAL_CONTEXT_FACTORY);
		String providerUrl = properties.get(Context.PROVIDER_URL);
		String queueName = properties.get("javax.jms.Queue");
		String connectionFactory = properties.get("javax.jms.ConnectionFactory");
		String jmsCorrelationId	 = properties.get("jmsCorrelationId");
		if(initialContextFactory == null)
		{
			initialContextFactory = "weblogic.jndi.WLInitialContextFactory";
		}
		if(providerUrl == null)
		{
			providerUrl = "t3://localhost:7001";
		}
		if(connectionFactory == null)
		{
			connectionFactory = "weblogic.jms.XAConnectionFactory";
		}
		fieldInitialContext 	= new JTextField(initialContextFactory, 25);
		fieldConnectionFactory 	= new JTextField(connectionFactory, 25);
		fieldProviderUrl 		= new JTextField(providerUrl, 25);
		fieldQueue 				= new JTextField(queueName, 25);
		fieldJmsCorrelationId	= new JTextField(jmsCorrelationId, 25);
		fieldClasspath			= new JTextField(classpath, 15);
		btClasspath				= new JButton("Browse");
		btClasspath.addActionListener(this);
		btOK					= new JButton("OK");
		btOK.addActionListener(this);
		builder.add(fieldInitialContext, 	cc.xyw(4, 2, 3));
		builder.add(fieldProviderUrl, 		cc.xyw(4, 4, 3));
		builder.add(fieldConnectionFactory, cc.xyw(4, 6, 3));
		builder.add(fieldQueue, 			cc.xyw(4, 8, 3));
		builder.add(fieldJmsCorrelationId, 	cc.xyw(4, 10, 3));
		builder.add(fieldClasspath, 		cc.xyw(4, 12, 1));
		builder.add(btClasspath, 			cc.xyw(6, 12, 1));
		builder.add(btOK, 					cc.xyw(1, 14, 6));
		
		return builder.getPanel();
	}

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if(source.equals(this.btClasspath))
		{
			loadFile();
		}
		else if(source.equals(btOK))
		{
			dispose();
		}
	}

	private void loadFile()
	{
		JFileChooser fileDialog = new JFileChooser();
		
		fileDialog.addChoosableFileFilter(new FileFilterJar());
		fileDialog.setAcceptAllFileFilterUsed(false);
		
		if(fileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileDialog.getSelectedFile();
			String absolutePath = file.getAbsolutePath();
			this.fieldClasspath.setText(absolutePath);
		}
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	public void dispose()
	{
		this.listHeader.add(Context.INITIAL_CONTEXT_FACTORY, (this.fieldInitialContext.getText() + "").trim());
		this.listHeader.add(Context.PROVIDER_URL, (this.fieldProviderUrl.getText() + "").trim());
		this.listHeader.add("javax.jms.Queue", (this.fieldQueue.getText() + "").trim());
		this.listHeader.add("javax.jms.ConnectionFactory", (this.fieldConnectionFactory.getText() + "").trim());
		this.listHeader.add("classpath", (this.fieldClasspath.getText() + "").trim());
		if(this.fieldJmsCorrelationId.getText() != null && this.fieldJmsCorrelationId.getText().length() > 0)
		{
			this.listHeader.add("jmsCorrelationId", this.fieldJmsCorrelationId.getText());
		}
		else
		{
			this.listHeader.remove("jmsCorrelationId");
		}
		super.dispose();	
	}
}

