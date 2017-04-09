package br.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathUtil
{
    @SuppressWarnings("unchecked")
	public static void addClasspath(String strUrl) throws MalformedURLException, SecurityException, NoSuchMethodException,
    IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
       URL url                  = new URL(strUrl);
       URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
       Class sysclass           = URLClassLoader.class;
       Class[] parameters       = new Class[]{URL.class};
       Method method            = sysclass.getDeclaredMethod("addURL",parameters);

       method.setAccessible(true);
       method.invoke(sysloader,new Object[]{url});
   }
}
