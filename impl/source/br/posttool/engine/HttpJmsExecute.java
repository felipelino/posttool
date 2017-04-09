package br.posttool.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.springframework.jms.core.JmsTemplate;

import br.util.ClassPathUtil;
import br.util.data.FieldValueList;

/**
 * @author Felipe Lino Data: 14/08/2008
 * Atualizado: 22/08/2010
 */
public class HttpJmsExecute
{
	
	public static ExecutionResult executeHttpMethod(String url, String content, Map<String, String> propertiesMap, int httpMehod) throws Exception
	{
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest method = null;
		switch(httpMehod)
		{
			case RequestObject.TYPE_GET:
			{
				method = new HttpGet(url);
				break;
			}
			case RequestObject.TYPE_POST:
			{
				HttpPost post = new HttpPost(url);
				post.setEntity(new StringEntity(content));
				method = post;
				break;
			}
			case RequestObject.TYPE_PUT:
			{
				HttpPut put = new HttpPut(url);
				put.setEntity(new StringEntity(content));
				method = put;
				break;
			}
			case RequestObject.TYPE_DELETE:
			{
				HttpDelete delete = new HttpDelete(url);
				method = delete;
				break;
			}
			case RequestObject.TYPE_HEAD:
			{
				HttpHead head = new HttpHead(url);
				method = head;
				break;
			}
		}
		 
		if(propertiesMap != null)
        {
			Set<String> set = propertiesMap.keySet();
			for(String key : set)
			{
			    String value =  propertiesMap.get(key);
			    method.addHeader(key, value);
			}
        }
		
		HttpResponse httpResponse = client.execute(method);
		int httpCode = httpResponse.getStatusLine().getStatusCode();
		
		/* Read the response body. */
        InputStream inStream = httpResponse.getEntity().getContent();
        BufferedReader buff = new BufferedReader(new InputStreamReader(inStream));
        String line = buff.readLine();
        StringBuilder sb= new StringBuilder();
        while(line != null)
        {
            sb.append(line);
            sb.append("\n");
            line = buff.readLine();
        }
        
        ExecutionResult result = new ExecutionResult(String.valueOf(httpCode), sb.toString(), httpResponse.getAllHeaders());
        return result;

	}
    
	/**
	 * Realiza a execução do metodo http ou JMS
	 * @param requestObject
	 * @return
	 */
	public static ExecutionResult execute(RequestObject requestObject)
	{
		ExecutionResult result = null;
		Date dtStart = new Date();
		Date dtEnd;
		long start = 0;
		long dif = -1;
		try
		{
			String url = requestObject.getUrl();
			String body = requestObject.getContent();
			FieldValueList list = requestObject.getListHeader();
			Map<String, String> propertiesMap = list.getMap();
			
			start = System.currentTimeMillis();
			if(requestObject.getType() == RequestObject.TYPE_JMS)
			{ 
				result = HttpJmsExecute.sendJmsMessage(propertiesMap, body);
			}
			else
			{ 
				result = HttpJmsExecute.executeHttpMethod(url, body, propertiesMap, requestObject.getType());
			}
		}
		catch(Exception exc)
		{
			StringWriter strWriter = new StringWriter();
			exc.printStackTrace(new PrintWriter(strWriter));
			String strStackTrace = "" + strWriter;

			result = new ExecutionResult("Failed", strStackTrace);
		}
		finally
		{
			dtEnd = new Date();
			long end = System.currentTimeMillis();
			dif = end - start;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - H:mm:ss");
			String dateStart = format.format(dtStart);
			String dateFinish = format.format(dtEnd);
			if(result == null)
			{
				result = new ExecutionResult("Failed","Unknown Failure");
			}
			result.setDateStart(dateStart);
			result.setDateFinish(dateFinish);
			result.setTimeExecution(dif);
		}
		return result;
	}
	
	public static ExecutionResult sendJmsMessage(Map<String, String> properties, String message) throws MalformedURLException, SecurityException, IllegalArgumentException, NamingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		String classpath = properties.get("classpath");
		String initialContextFactory = properties.get(Context.INITIAL_CONTEXT_FACTORY);
		String providerUrl = properties.get(Context.PROVIDER_URL);
		String queueName = properties.get("javax.jms.Queue");
		String connectionFactory = properties.get("javax.jms.ConnectionFactory");
		String jmsCorrelationId	 = properties.get("jmsCorrelationId");
		if(	classpath == null || 
			initialContextFactory == null ||
			providerUrl == null ||
			queueName == null ||
			connectionFactory == null)
		{
			ExecutionResult result = new ExecutionResult("Failure", "Some parameters must be setted.\n" +
															"Go to menu option \"JMS/Set JMS Config\"");
			return result;
		}
		else
		{
			classpath = "file://" + classpath;
			sendJmsMessage(	classpath, 
							initialContextFactory, 
							providerUrl, 
							connectionFactory, 
							queueName, 
							message,
							jmsCorrelationId);
			ExecutionResult result = new ExecutionResult("OK", "Message sended with success.");
			return result;
		}
	}
	
	public static void sendJmsMessage( String classpath,
            String initialContextFactory,
            String providerUrl,
            String connectionFactory,
            String queueName,
            String message,
            String jmsCorrelationId) throws  NamingException,
                                    MalformedURLException,
                                    SecurityException,
                                    IllegalArgumentException,
                                    NoSuchMethodException,
                                    IllegalAccessException,
                                    InvocationTargetException
	{
		ClassPathUtil.addClasspath(classpath);
		Hashtable<String,String> map = new Hashtable<String, String>();
		map.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		map.put(Context.PROVIDER_URL, providerUrl);
		Context ctx = new InitialContext(map);
		
		ConnectionFactory connFactory = (ConnectionFactory) ctx.lookup(connectionFactory);
		Queue queue                               = (Queue) ctx.lookup(queueName);

		CustomMessagePostProcessor messagePostProcessor = new CustomMessagePostProcessor(jmsCorrelationId);
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connFactory);
		jmsTemplate.setDefaultDestination(queue);
		
		jmsTemplate.convertAndSend(queue, message, messagePostProcessor);
	}
	
	public static void log(RequestObject postObj, ExecutionResult result)
	{
		PrintWriter out = null;
		try
		{
			if((postObj.getLogName() != null) && (postObj.getLogName().trim().length() >0))
			{
				StringBuffer strBuff = new StringBuffer();
				strBuff.append("\n\n#################################################");
				strBuff.append("\n\nDate Time: "+ result.getDateStart());
	
				strBuff.append("\nHeader:");
				
				FieldValueList listHeader = postObj.getListHeader();
				for(int i = 0; i < listHeader.getSize(); i++)
				{
					String property = listHeader.get(i).getField();
					String content	= listHeader.get(i).getValue();
					strBuff.append("\n\t name:["+ property+ "] - value:["+content+"]");
				}
				
				if(postObj.getType() != RequestObject.TYPE_JMS)
				{
					strBuff.append("\n\nURL:[" + postObj.getUrl()+ "]");
				}
				
				if(postObj.getType() == RequestObject.TYPE_POST){ 
					strBuff.append("\nType:[POST]");
				}else if(postObj.getType() == RequestObject.TYPE_GET){
					strBuff.append("\nType:[GET]");
				}else if(postObj.getType() == RequestObject.TYPE_PUT){
					strBuff.append("\nType:[PUT]");
				}else if(postObj.getType() == RequestObject.TYPE_DELETE){
					strBuff.append("\nType:[DELETE]");
				}else if(postObj.getType() == RequestObject.TYPE_HEAD){
					strBuff.append("\nType:[HEAD]");
				}else if(postObj.getType() == RequestObject.TYPE_JMS){
					strBuff.append("\nType:[JMS]");
				}
				else{
					strBuff.append("\nType:[UNKNOW]");
				}
	
				strBuff.append("\nTime Out:[" + postObj.getTimeOut() + "]ms");
				strBuff.append("\nRequest:" 
								+ "\n-------------------------------------------------\n"
								+ postObj.getContent()
								+ "\n-------------------------------------------------");
	
				strBuff.append("\n\nDate Time.....:[" + result.getDateFinish() + "]");
				strBuff.append("\nTime Execution..:["	+ result.getTimeExecution() + "]ms");
				strBuff.append("\nResponse Code...:[" + result.getHttpCode() + "]");
				strBuff.append("\nResponse Headers:[\n" + result.getReceivedHeadersFormatted() + "]");
				strBuff.append("\nResponse:"
								+ "\n-------------------------------------------------\n"
								+ result.getReceivedContent()
								+ "\n-------------------------------------------------");
	
				File file = new File(postObj.getLogName());
				FileWriter writer = new FileWriter(file, true);
				out = new PrintWriter(writer);
	
				out.print(strBuff);
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		finally
		{
			if(out != null)
				out.close();
		}
	}
}
