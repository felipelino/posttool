
package br.posttool.engine;

import br.util.data.FieldValueList;

/**
 * @author Felipe Lino
 * Data: 11/01/2008
 * 
 */
public class RequestObject
{
	public static final int TYPE_POST 	= 0;
	public static final int TYPE_GET 	= 1;
	public static final int TYPE_PUT 	= 2;
	public static final int TYPE_DELETE	= 3;
	public static final int TYPE_HEAD	= 4;
	public static final int TYPE_JMS 	= 5;
	
	private String 	content;
	private String 	url;
	private int type  = TYPE_POST; 
	private int timeOut = 30000;
	private FieldValueList listHeader;
	private boolean isLogEnable = false;
	private String logName;
	
	public RequestObject()
	{
		
	}
	
	public RequestObject(FieldValueList listHeader, String content, String url, int type, int timeOut, boolean isLogEnable, String logName)
	{
		this.content 	= content;
		this.url 			= url;
		this.type 			= type;
		this.timeOut 		= timeOut;
		this.logName 		= logName;
		this.isLogEnable 	= isLogEnable;
		this.listHeader		= listHeader;
	}
	
	/**
	 * @param listHeader
	 */
	public void setListHeader(FieldValueList listHeader) {
		this.listHeader = listHeader;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param strXmlInput The content to set.
	 */
	public void setContent(String content)
	{
		this.content = content;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl()
	{
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	/**
	 * @return quantidade de elementos no header 
	 */
	public int getSizeHeader()
	{
		return listHeader.getSize();
	}
	
	/**
	 * @return Returns the propHeader
	 */
	public FieldValueList getListHeader()
	{
		return listHeader;
	}
	
	/**
	 * @return Returns the timeOut.
	 */
	public int getTimeOut()
	{
		return timeOut;
	}
	/**
	 * @param timeOut The timeOut to set.
	 */
	public void setTimeOut(int timeOut)
	{
		this.timeOut = timeOut;
	}
	/**
	 * @return Returns the log.
	 */
	public boolean isLogEnable()
	{
		return isLogEnable;
	}
	/**
	 * @param isLogEnable The isLogEnable to set.
	 */
	public void setLogEnable(boolean isLogEnable)
	{
		this.isLogEnable = isLogEnable;
	}
	/**
	 * @return Returns the logName.
	 */
	public String getLogName()
	{
		return logName;
	}
	/**
	 * @param logName The logName to set.
	 */
	public void setLogName(String logName)
	{
		this.logName = logName;
	}
}
