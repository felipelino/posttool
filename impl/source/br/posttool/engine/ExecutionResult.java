package br.posttool.engine;

import org.apache.http.Header;

/**
 * @author Felipe Lino
 * Data: 11/01/2008
 */
public class ExecutionResult
{
	private String httpCode;
	private String receivedContent;
	
	private String dateStart;
	private String dateFinish;
	
	private long timeExecution;
	
	private Header[] receivedHeaders;
	
	public ExecutionResult(String httpCode, String receivedContent)
	{
		this.httpCode = httpCode;
		this.receivedContent = receivedContent;
	}
	
	public ExecutionResult(String httpCode, String receivedContent, Header[] receivedHeaders)
	{
		this.httpCode = httpCode;
		this.receivedContent = receivedContent;
		this.receivedHeaders = receivedHeaders;
	}
	
	/**
	 * @return Returns the dateFinish.
	 */
	public String getDateFinish()
	{
		return dateFinish;
	}
	/**
	 * @param dateFinish The dateFinish to set.
	 */
	public void setDateFinish(String dateFinish)
	{
		this.dateFinish = dateFinish;
	}
	/**
	 * @return Returns the dateStart.
	 */
	public String getDateStart()
	{
		return dateStart;
	}
	/**
	 * @param dateStart The dateStart to set.
	 */
	public void setDateStart(String dateStart)
	{
		this.dateStart = dateStart;
	}
	/**
	 * @return Returns the timeExecution.
	 */
	public long getTimeExecution()
	{
		return timeExecution;
	}
	/**
	 * @param timeExecution The timeExecution to set.
	 */
	public void setTimeExecution(long timeExecution)
	{
		this.timeExecution = timeExecution;
	}
	
	/**
	 * @return Returns the httpCode.
	 */
	public String getHttpCode()
	{
		return httpCode;
	}
	/**
	 * @param httpCode The httpCode to set.
	 */
	public void setHttpCode(String httpCode)
	{
		this.httpCode = httpCode;
	}
	/**
	 * @return Returns the receivedContent.
	 */
	public String getReceivedContent()
	{
		return receivedContent;
	}
	/**
	 * @param xmlOutput The receivedContent to set.
	 */
	public void setReceivedContent(String receivedContent)
	{
		this.receivedContent = receivedContent;
	}

	/**
	 * @return
	 */
	public Header[] getReceivedHeaders() {
		return receivedHeaders;
	}

	/**
	 * @param receivedHeaders
	 */
	public void setReceivedHeaders(Header[] receivedHeaders) {
		this.receivedHeaders = receivedHeaders;
	}
	
	public boolean isApplicationXml()
	{
		if(this.receivedHeaders != null)
		{
			for(Header header : this.receivedHeaders)
			{
				if("Content-Type".equalsIgnoreCase(header.getName())
					&& "application/xml".equalsIgnoreCase(header.getValue()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public String getReceivedHeadersFormatted()
	{
		StringBuilder sb = new StringBuilder();
		if(this.receivedHeaders != null)
		{
			for(Header header : this.receivedHeaders)
			{
				if(header != null)
				{
					String name = header.getName();
					String value = header.getValue();
					sb.append("\tname:[").append(name).append("] - ");
					sb.append("value:[").append(value).append("]\n");
				}
			}
		}
		return sb.toString();
	}
}
