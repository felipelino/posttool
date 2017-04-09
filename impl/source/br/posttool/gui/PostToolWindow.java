package br.posttool.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.xmlbeans.XmlObject;

import br.posttool.engine.ExecutionResult;
import br.posttool.engine.HttpJmsExecute;
import br.posttool.engine.RequestObject;
import br.util.data.FieldValueData;
import br.util.data.FieldValueList;
import br.util.file.filter.FileFilterLog;
import br.util.file.filter.FileFilterUtil;
import br.util.file.filter.FileFilterXML;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.thoughtworks.xstream.XStream;

/**
 * Janela da ferramenta de Post
 * 
 * @author Felipe Lino
 * Data: 16/08/2008
 * Atualizado: 22/08/2010
 */
public class PostToolWindow extends JFrame 
implements ActionListener, ListSelectionListener
{
	private static final long	serialVersionUID	= 1L;

	/* Componentes gráficos da janela */
	private Container cp; 
	private DefaultFormBuilder builder;
	private FormLayout layout;
	private CellConstraints cc;
	
	/* Menu */
	private JMenuBar 	menuBar;
	private JMenu 		menu1File;
	private JMenuItem	m1New;
	private JMenuItem	m1Load;
	private JMenuItem	m1SaveConfig;
	private JMenuItem	m1ChoiceLog;
	private JMenuItem	m1Exit;
	
	private JMenu 		menuJms;
	private JMenuItem	mJmsConfig; 
	
	private JMenu		menu2Xml;
	private JMenuItem	m2SoapRequest;
	private JMenuItem	m2FormatXml;
	
	private JMenu		menu3Help;
	private JMenuItem	m3About;
	
	private JLabel lbTitle;
	private JLabel lbHeader;
	private JLabel lbInputHeader;
	private JLabel lbUrl;
	private JLabel lbXmlInput;
	private JLabel lbXmlOutput;
	private JLabel lbHttpCode;
	private JLabel lbHeaderProperty;
	private JLabel lbHeaderContent;
	private JLabel lbTimeOut;
	private JLabel lbLog;
	
	private JButton btRemoveHeader;
	private JButton btAddHeader;
	private JButton btExecute;
		
	private JTextField fieldShowHeaderContent;
	private JTextField fieldUrl;
	private JTextField fieldHttpCode;
	private JTextField fieldHeaderProperty;
	private JTextField fieldHeaderContent;
	private JTextField fieldLogName;
	
	private JSpinner spinTimeOut;
	
	private JTextArea txtXmlInput;
	private JTextArea txtXmlOutput;
	
	private JComboBox comboMethod;
	private JComboBox comboLog;
	
	private JList listHeaderProperty;
	private DefaultListModel ltModelProperty;
		
	private JScrollPane scrollXmlInput;
	private JScrollPane scrollXmlOutput;

	private JScrollPane scrollList;
		
	private FieldValueList listHeader;
	
	/* Monta a Janela do Programa */
	public PostToolWindow()
	{
		super("Post Tool");
		this.setSize(540,700);
		this.setResizable(true);
		initializeObjects();
				
		cp = this.getContentPane();
		cp.add(designPanel());
		
		refreshListOfHeader();
	}
	
	private void initializeObjects()
	{
		/* Monta o Menu */
		m1New  = new JMenuItem("New Config File");
		m1New.addActionListener(this);
		
		m1Load = new JMenuItem("Load config file...");
		m1Load.addActionListener(this);
	
		m1SaveConfig = new JMenuItem("Save config file...");
		m1SaveConfig.addActionListener(this);
		
		m1ChoiceLog = new JMenuItem("Save log file...");
		m1ChoiceLog.addActionListener(this);
		
		m1Exit = new JMenuItem("Exit");
		m1Exit.addActionListener(this);
		
		menu1File = new JMenu("File");
		menu1File.addActionListener(this);
		menu1File.add(m1New);
		menu1File.add(m1Load);
		menu1File.add(m1ChoiceLog);
		menu1File.add(m1SaveConfig);
		menu1File.add(m1ChoiceLog);
		menu1File.addSeparator();
		menu1File.add(m1Exit);
		
		menuJms = new JMenu("JMS");
		menuJms.addActionListener(this);
		mJmsConfig = new JMenuItem("Set JMS Config");
		mJmsConfig.addActionListener(this);
		menuJms.add(mJmsConfig);
		
		m2SoapRequest = new JMenuItem("Create SOAP Request");
		m2SoapRequest.addActionListener(this);
		
		m2FormatXml = new JMenuItem("Format XML");
		m2FormatXml.addActionListener(this);
		
		menu2Xml = new JMenu("XML");
		menu2Xml.add(m2SoapRequest);
		menu2Xml.add(m2FormatXml);
		
		m3About = new JMenuItem("About");
		m3About.addActionListener(this);
		
		menu3Help = new JMenu("Help");
		menu3Help.addActionListener(this);
		menu3Help.add(m3About);
		
		menuBar = new JMenuBar();
		menuBar.add(menu1File);
		menuBar.add(menu2Xml);
		menuBar.add(menuJms);
		menuBar.add(menu3Help);
		
		lbTitle 		= new JLabel("                    Post Tool v4.0");
		lbHeader 		= new JLabel("Header");
		lbInputHeader 	= new JLabel("Input Header");
		lbUrl 			= new JLabel("URL");
		lbXmlInput 		= new JLabel("Request");
		lbXmlOutput 	= new JLabel("Response");
		lbHttpCode 		= new JLabel("Code");
		lbHeaderProperty= new JLabel("Property");
		lbHeaderContent = new JLabel("Content");
		lbTimeOut		= new JLabel("Time Out");
		lbLog			= new JLabel("Log File");
		
		btRemoveHeader 	= new JButton("Remove");
		btRemoveHeader.addActionListener(this);
		
		btAddHeader 	= new JButton("Add");
		btAddHeader.addActionListener(this);
		
		btExecute 		= new JButton("Execute");
		btExecute.addActionListener(this);
		
		SpinnerNumberModel spinModel = new SpinnerNumberModel(30000, 0, 3600000, 1 );
		spinTimeOut			= new JSpinner(spinModel);
				
		fieldUrl 			= new JTextField();
		
		fieldHttpCode 		= new JTextField();
		fieldHttpCode.setEditable(false);
		
		fieldHeaderProperty = new JTextField();
		fieldHeaderContent 	= new JTextField();
		
		fieldShowHeaderContent = new JTextField();
		fieldShowHeaderContent.setEditable(false);
		
		fieldLogName = new JTextField();
		fieldLogName.setEditable(false);
		
		comboMethod = new JComboBox();
		comboMethod.addItem("POST");
		comboMethod.addItem("GET");
		comboMethod.addItem("PUT");
		comboMethod.addItem("DELETE");
		comboMethod.addItem("HEAD");
		comboMethod.addItem("JMS");
		
		comboLog = new JComboBox();
		comboLog.addItem("OFF");
		comboLog.addItem("ON");
		
		/* "Content-type",  "application/x-www-form-urlencoded" */
		listHeader = new FieldValueList();
		listHeader.add("Content-type","application/x-www-form-urlencoded");
		
		ltModelProperty = new DefaultListModel();
		listHeaderProperty 	= new JList(ltModelProperty);
		listHeaderProperty.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listHeaderProperty.addListSelectionListener(this);
		
		scrollList = new JScrollPane(listHeaderProperty);
		scrollList.setAutoscrolls(true);
		scrollList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
		Font defaultFont = new Font("Courier",Font.PLAIN,11);
		
		txtXmlInput 	= new JTextArea();
		txtXmlOutput 	= new JTextArea();
		
		txtXmlInput.setFont(defaultFont);
		txtXmlOutput.setFont(defaultFont);
		txtXmlOutput.setEditable(false);
		
		scrollXmlInput 	= new JScrollPane(this.txtXmlInput);
		scrollXmlInput.setAutoscrolls(true);
		scrollXmlInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollXmlInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		scrollXmlOutput = new JScrollPane(this.txtXmlOutput);
		scrollXmlOutput.setAutoscrolls(true);
		scrollXmlOutput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollXmlOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.setJMenuBar(menuBar);
	}
	
	private Component designPanel()
	{
		layout = new FormLayout(
				"fill:3dlu," +
				"fill:pref:grow," +  //2
				"fill:3dlu," +  //3
				"fill:pref:grow," +  //4
				"right:50dlu:grow," + //5
				"fill:3dlu," +  //6
				"fill:80dlu:grow," + //7
				"fill:pref:grow," +  //8
				"fill:3dlu," +  //9
				"fill:pref:grow," +  //10
				"fill:3dlu"   //11
				, 
				"pref," + 		// 1
				"20dlu," + 		// 2
				"3dlu," + 		// 3
				"pref," + 		// 4
				"3dlu," +		// 6
				"pref," +		// 7
				"pref," +		// 8
				"pref," +		// 9  
				"3dlu," +		// 12
				"pref," +		// 10
				"3dlu," +		// 11
				"pref," +		// 12
				"3dlu,"+		// 13
				"pref," +		// 14
				"3dlu," +		// 15
				"fill:70dlu:grow," +		// 16
				"3dlu," +		// 17
				"pref," +		// 18
				"3dlu," +		// 19
				"pref," +		// 20
				"3dlu," +		// 21
				"fill:70dlu:grow,"+ //22 
				"3dlu," +		// 23
				"pref," +		// 24
				"3dlu" 		// 25
				); 	// 

		cc = new CellConstraints();
		/* builder = new DefaultFormBuilder(layout, new FormDebugPanel()); */
		builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		/* Coluna , Linha, Celulas Linha, Células Coluna */
		builder.add(lbTitle,  cc.xyw(5,2,5));
		
		builder.add(lbHeaderProperty,  cc.xyw(4,4,2));
		builder.add(lbHeaderContent,  cc.xyw(7,4,2));
		
		builder.add(lbHeader,  cc.xyw(2,6,1));
		
		builder.add(new JLabel(" "),  cc.xyw(2,7,1));
		builder.add(new JLabel(" "),  cc.xyw(2,8,1));
		
		builder.add(scrollList, cc.xywh(4,6,2,3));
		builder.add(fieldShowHeaderContent, cc.xyw(7,6,2));
		
		builder.add(lbTimeOut, cc.xyw(5,14,1));
		builder.add(spinTimeOut, cc.xyw(7,14,1));
		
		builder.add(btRemoveHeader, cc.xyw(10,6,1));
		
		builder.add(lbInputHeader, cc.xyw(2,10,1));
		
		builder.add(fieldHeaderProperty, cc.xyw(4,10,2));
		builder.add(fieldHeaderContent, cc.xyw(7,10,2));
		
		builder.add(btAddHeader, cc.xyw(10,10,1));
		
		builder.add(lbUrl, cc.xyw(2,12,1));
		
		builder.add(fieldUrl, cc.xyw(4,12,5));
		
		builder.add(btExecute, cc.xyw(10,12,1));
		
		builder.add(lbXmlInput, cc.xyw(2,14,1));
		
		builder.add(comboMethod, cc.xyw(4,14,1));
				
		/* builder.add(btStop, cc.xyw(10,14,1)); */
		
		builder.add(scrollXmlInput, cc.xyw(2,16,9));
		
		builder.add(lbHttpCode, cc.xyw(2,18,1));
		
		builder.add(fieldHttpCode, cc.xyw(4,18,3));
		
		builder.add(lbXmlOutput, cc.xyw(2,20,1));
		
		builder.add(scrollXmlOutput, cc.xyw(2,22,9));
		
		builder.add(lbLog, cc.xyw(2,24,1));
		
		builder.add(fieldLogName, cc.xyw(4,24,4));
		
		builder.add(comboLog, cc.xyw(10,24,1));
		
		return builder.getPanel();
	}
	
	/* Tratadaores de evento */
	
	public void actionPerformed(ActionEvent evt) 
	{
		Object source = evt.getSource();
		if(source == btAddHeader) addHeader();
		else if(source == btRemoveHeader) removeHeader();
		else if(source == btExecute) execute();
		else if(source == m1New) newFile();
		else if(source == m1SaveConfig) saveConfigFile();
		else if(source == m1ChoiceLog) choiceLogFile();
		else if(source == m1Load) loadConfigFile();
		else if(source == m1Exit) exitProgram();
		else if(source == m2SoapRequest) createSoapRequest();
		else if(source == m2FormatXml) formatXml();
		else if(source == m3About) showAboutMessage();
		else if(source == mJmsConfig) setJmsConfig();
	}
	
	private void setJmsConfig()
	{
		JmsConfigWindow.setDefaultLookAndFeelDecorated(true);
		JmsConfigWindow jmsConfigWindow = new JmsConfigWindow(this.listHeader);
		jmsConfigWindow.setFocusable(true);
		jmsConfigWindow.setVisible(true);
	}

	private void addHeader()
	{
		String strProp = "" + fieldHeaderProperty.getText().trim();
		String strCont = "" + fieldHeaderContent.getText().trim();
		
		if(   (strProp.length() > 0)
			&&(strCont.length() > 0))
		{
			listHeader.add(strProp,strCont);
			fieldHeaderProperty.setText("");
			fieldHeaderContent.setText("");
		}
		
		refreshListOfHeader();
	}
	
	private void removeHeader()
	{
		int index = listHeaderProperty.getSelectedIndex();
		String strProp = listHeader.get(index).getField();
		listHeader.remove(index);
		ltModelProperty.removeElement(strProp);
		
		refreshListOfHeader();
	}
	
	private void refreshListOfHeader()
	{
		for(int i = 0; i < listHeader.getSize(); i++)
		{
			FieldValueData fieldValue =  listHeader.get(i);
			String prop = fieldValue.getField();
			if(! ltModelProperty.contains(prop))
				ltModelProperty.addElement(prop);
		}
		
		if(listHeader.getSize() > 0)
		{
			FieldValueData fieldValue = listHeader.get(0);
			String content = fieldValue.getValue();
			fieldShowHeaderContent.setText(content);
		}
	}
	
	private void execute() 
	{
		RequestObject postObj = createPostGetObject();
		ExecutionResult result = HttpJmsExecute.execute(postObj);
		
		txtXmlOutput.setText(result.getReceivedContent());
		fieldHttpCode.setText(result.getHttpCode());

		if(postObj.isLogEnable())
		{
			if(result.isApplicationXml())
			{
				try
				{
					XmlObject xmlObj = XmlObject.Factory.parse(postObj.getContent());
					postObj.setContent("" + xmlObj);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
			}
			
			HttpJmsExecute.log(postObj, result);
		}
	}
	
	private void listPropertyContent()
	{
		int index = listHeaderProperty.getSelectedIndex();
		if(index > -1 && index < listHeader.getSize())
		{
			String strCont = listHeader.get(index).getValue();
			fieldShowHeaderContent.setText(strCont);
		}
	}
	
	public void valueChanged(ListSelectionEvent evt) 
	{
		if(evt.getSource() == listHeaderProperty)
		{
			listPropertyContent();
		}
	}
	
	/* Tratamento dos eventos do Menu */
	
	private void exitProgram()
	{
		System.exit(0);
	}
	
	private void createSoapRequest()
	{
		String soapRequest ="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >" +
								"\n  <soap:Header>"+
								"\n  </soap:Header>"+
								"\n  <soap:Body>" +
								"\n      <!-- Put your message here -->" +
								"\n  </soap:Body>" +
							"\n</soap:Envelope>";  
		String header = "SOAPAction";
		
		fieldHeaderProperty.setText(header);
		txtXmlInput.setText(soapRequest);
	}
	
	private void loadConfigFile()
	{
		JFileChooser openDialog = new JFileChooser();
		
		openDialog.addChoosableFileFilter(new FileFilterXML());
		openDialog.setFileFilter(new FileFilterXML());
		openDialog.setAcceptAllFileFilterUsed(false);
		
		if(openDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = openDialog.getSelectedFile();
			
			try
			{
				FileInputStream fileInput = new FileInputStream(file);
				
				XStream xStream = new XStream();
				RequestObject postGetObject = (RequestObject) xStream.fromXML(new InputStreamReader(fileInput));
				updateWindow(postGetObject);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
				showErrorOpenFile(exc);
			}
		}
	}
	
	private void newFile()
	{
		listHeader.removeAll();
		listHeader.add("Content-type","application/x-www-form-urlencoded");
		String 	xmlInput = "";
		String 	url		 = "";
		int 	type	 = RequestObject.TYPE_POST;
		int 	timeOut	 = 30000;
		String  logName	 = "";
		boolean logEnabled = false;
		
		RequestObject postGetObject = new RequestObject(listHeader, xmlInput, url, type, timeOut, logEnabled, logName);
		updateWindow(postGetObject);
	}
	
	private void saveConfigFile()
	{
		JFileChooser saveDialog = new JFileChooser();
		
		saveDialog.addChoosableFileFilter(new FileFilterXML());
		saveDialog.setFileFilter(new FileFilterXML());
		saveDialog.setAcceptAllFileFilterUsed(false);
		
		if(saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = saveDialog.getSelectedFile();
			String strFileName = null;
			String ext = FileFilterUtil.getExtensionOfFile(file);
			
			if( ext.equalsIgnoreCase(FileFilterXML.EXTENSION_XML) )
				strFileName = file + "";
			else		
				strFileName = file + "." + FileFilterXML.EXTENSION_XML;
			
			PrintWriter out = null;
			try
			{
				RequestObject postObj = createPostGetObject();
				XStream xStream = new XStream();
				String xml = xStream.toXML(postObj);
				out = new PrintWriter(new FileWriter(strFileName));
				
				out.print(xml);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
				showErrorSaveFile(exc);
			}
			finally
			{
				if(out != null)
				{
					out.close();
				}
			}
		}	
	}
	
	private void choiceLogFile()
	{
		JFileChooser saveDialog = new JFileChooser();
		
		saveDialog.addChoosableFileFilter(new FileFilterLog());
		saveDialog.setFileFilter(new FileFilterLog());
		saveDialog.setAcceptAllFileFilterUsed(false);
		
		if(saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = saveDialog.getSelectedFile();
			String strFileName = null;
			String ext = FileFilterUtil.getExtensionOfFile(file);
			
			if( ext.equalsIgnoreCase(FileFilterLog.EXTENSION_LOG) )
				strFileName = file + "";
			else		
				strFileName = file + "." + FileFilterLog.EXTENSION_LOG;
			
			fieldLogName.setText(strFileName);
		}	
	}
	
	private RequestObject createPostGetObject()
	{
		String xmlInput = "" + txtXmlInput.getText();
		String url 	= "" + fieldUrl.getText();
		int type 	= comboMethod.getSelectedIndex();
		int timeOut = 30000;
		boolean logEnabled = false;
		if(comboLog.getSelectedIndex() == 1)
			logEnabled = true;
		else
			logEnabled = false;
		
		String logName = "" + fieldLogName.getText();
		try
		{
			timeOut = new Integer(spinTimeOut.getValue()+"").intValue();
		}
		catch(Exception exc)
		{
			timeOut = 30000;
		}
		
		RequestObject postObj = new RequestObject(listHeader, xmlInput, url, type, timeOut, logEnabled, logName);
		
		return postObj;
	}
	
	private void updateWindow(RequestObject postGetObject)
	{
		String xmlInput = postGetObject.getContent();
		String url		= postGetObject.getUrl();
		String logName	= postGetObject.getLogName();
		int	timeOut		= postGetObject.getTimeOut();
		boolean isLogEnable = postGetObject.isLogEnable();
		
		txtXmlInput.setText(xmlInput);
		fieldUrl.setText(url);
		fieldLogName.setText(logName);
		if(isLogEnable)
			comboLog.setSelectedIndex(1);
		else
			comboLog.setSelectedIndex(0);
		
		spinTimeOut.setValue(new Integer(timeOut));	
		
		ltModelProperty.removeAllElements();
		listHeader.removeAll();
		listHeader = postGetObject.getListHeader();
		for(int i=0 ; i < listHeader.getSize(); i++)
		{
			String property = listHeader.get(i).getField();
			ltModelProperty.addElement(property);
		}
		
		refreshListOfHeader();
	}
	
	private void formatXml()
	{
		String input = txtXmlInput.getText();
		String output= txtXmlOutput.getText();
		
		try
		{
			if((input != null) && (input.trim().length() > 0))
			{
				XmlObject xmlObj = XmlObject.Factory.parse(input);
				input = "" + xmlObj;
				txtXmlInput.setText(input);
			}
			
			if((output != null) && (output.trim().length() > 0))
			{
				XmlObject xmlObj = XmlObject.Factory.parse(output);
				output = "" + xmlObj;
				txtXmlOutput.setText(output);
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
	}
	
	private void showAboutMessage()
	{
		String strMsg01 = "\"The quality of the winner is never quit.\"";
		String strMsg02 = "PostTool v4.0" + "\n" +
						  "Author: Felipe Lino" + "\n" +
						  "E-Mail: felipelino44@gmail.com" + "\n" +
						  "Rio de Janeiro, Brazil\n" +
						  "First Release: 16 of August of 2008\n" +
						  "Last Release: 19 of July of 2014";
		String title = "About PostTool";
		
		JOptionPane.showMessageDialog(this,strMsg01+ "\n\n" + strMsg02, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibição de mensagem de erro caso não consiga salvar o arquivo.
	 * @param exc 
	 */
	private void showErrorSaveFile(Exception exc)
	{
		String errorTitle = "Failed to save file";
		String errorMsg   = "Error occured when try to save file";
		JOptionPane.showMessageDialog(null,errorMsg+"\n"+exc,errorTitle,JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Exibe mensagem de erro na tela informando
	 * falha ao tentar ler o arquivo.
	 * @param exc Exceção
	 */
	private void showErrorOpenFile(Exception exc)
	{
		String errorTitle = "Failed to open file";
		String errorMsg   = "Error occured when try to open file";
		JOptionPane.showMessageDialog(null,errorMsg+"\n"+exc,errorTitle,JOptionPane.ERROR_MESSAGE);
	}
}

