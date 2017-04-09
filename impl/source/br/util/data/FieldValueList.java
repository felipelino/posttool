
package br.util.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Felipe Lino
 */
public class FieldValueList
{
	private Map<String, String> map;
	private List<FieldValueData> list;
	
	public FieldValueList()
	{
		map = new HashMap<String, String>();
		list = new ArrayList<FieldValueData>();
	}
	
	public void add(String field, String value)
	{
		if(map.containsKey(field))
		{
			this.remove(field);
		}
		this.list.add(new FieldValueData(field, value));
		this.map.put(field, value);
	}
	
	public void remove(String field)
	{
		map.remove(field);
		int count = -1;
		boolean found = false;
		for(FieldValueData data : list)
		{
			count++;
			String key = data.getField();
			if(field.equals(key)){
				found = true;
				break;
			}
		}
		
		if(count >= 0 && found)
		{
			list.remove(count);
		}
	}
	
	public FieldValueData get(int index)
	{
		return list.get(index);
	}
	
	public void remove(int index)
	{
		FieldValueData data = list.remove(index);
		this.map.remove(data.getField());
	}
	
	public void removeAll()
	{
		this.map = new HashMap<String, String>();
		this.list = new ArrayList<FieldValueData>();
	}
	
	public int getSize()
	{
		return map.size();
	}
	
	public FieldValueData get(String field)
	{
		String value = map.get(field);
		return new FieldValueData(field, value);
	}
	
	public Map<String, String> getMap()
	{
		return this.map;
	}

	
}
