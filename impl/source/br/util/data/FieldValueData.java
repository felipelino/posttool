package br.util.data;

/**
 * Classe que representa um par nome-valor
 * @author Felipe Lino
 * <br>Data: 16/08/2008
 */
public class FieldValueData
{
	/** Nome do campo */
	private String field;
	
	/** Valor do campo */
	private String value;
	
	/**
	 * Default 
	 */
	public FieldValueData() {
		super();
	}

	/**
	 * Construtor completo
	 * @param field
	 * @param value
	 */
	public FieldValueData(String field, String value)
	{
		this.field = field;
		this.value = value;
	}
	
	/**
	 * @return the field
	 */
	public String getField()
	{
		return field;
	}
	
	/**
	 * @param field the field to set
	 */
	public void setField(String field)
	{
		this.field = field;
	}
	
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String ret = "Field:["+field+"] - Value:["+value+"]";
		return ret;
	}
}
