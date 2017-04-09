/*
 * Created on 22/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.posttool.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.xmlbeans.XmlObject;

import br.util.data.FieldValueList;

public class PostGetExecuteOld 
{
	public static ExecutionResult execute(RequestObject postObj)
	{
		ExecutionResult result = null;
		Date dtStart = new Date();
		Date dtEnd;
		long start = 0;
		long dif = -1;
		
		try
		{
			start = System.currentTimeMillis();
			/* Set Configuration */
			URL url = new URL(postObj.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if(postObj.getType() == RequestObject.TYPE_POST) 
				connection.setRequestMethod("POST");
			else 
				connection.setRequestMethod("GET");
			
			FieldValueList listHeader = postObj.getListHeader();
			for(int i = 0; i < listHeader.getSize(); i++)
			{
				String property = listHeader.get(i).getField();
				String content	= listHeader.get(i).getValue();
				connection.addRequestProperty(property, content);
			}

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(postObj.getTimeOut());

			OutputStream out = connection.getOutputStream();
			out.write(postObj.getContent().getBytes());
			out.flush();
			out.close();
			
			/* Do POST or GET */
			connection.connect();

			/* Get Response Code and String */
			int httpCode = connection.getResponseCode();
			String strCode = connection.getResponseMessage();

			/* Get Response XML */
			InputStream stream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			StringBuffer strBuff = new StringBuffer();
			String line;

			if((line = reader.readLine()) != null)
				strBuff.append(line);

			while((line = reader.readLine()) != null)
			{
				strBuff.append("\n");
				strBuff.append(line);
			}

			String s = strBuff.toString();
			String decoded = new String(s.getBytes(),"UTF-8");
			
			connection.disconnect();
			reader.close();
			stream.close();
			
			/* Formata o XML de saída */
			String xmlOutput = decoded;
			try
			{
				XmlObject xmlObj = XmlObject.Factory.parse("" + decoded);
				xmlOutput = "" + xmlObj;
			}
			catch(Exception exc)
			{
				xmlOutput = decoded;
			}
			
			result = new ExecutionResult(httpCode + " - " + strCode, xmlOutput);
			
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

			result.setDateStart(dateStart);
			result.setDateFinish(dateFinish);
			result.setTimeExecution(dif);
		}

		return result;
	}

}
