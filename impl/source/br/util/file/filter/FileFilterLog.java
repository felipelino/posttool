package br.util.file.filter;


import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe que define um filtro para arquivos Log.
 * 
 * @author Felipe Lino
 * <BR>Data: 09/02/2007
 * <BR>Atualizado: 18/02/2007
 */
public class FileFilterLog extends FileFilter
{
	/**
	 * Descreve extensão de arquivo Log.
	 */
	public static final String EXTENSION_LOG = "log";

	/**
	 * Método que descreve quais tipos de arquivos são aceitos
	 * por esse filtro. Aqui apenas arquivos XML.
	 */
    public boolean accept(File file) 
    {
        if (file.isDirectory()) 
        {
            return true;
        }

        String extension = FileFilterUtil.getExtensionOfFile(file);
        if (extension != null) 
        {
            if(extension.equalsIgnoreCase(EXTENSION_LOG))
            {
            	return true;
            }
            else 
            {
                return false;
            }
        }

        return false;
    }

    /**
     * Retorna a descrição para esse tipo de arquivo
     */
    public String getDescription() 
    {
        return "Log File";
    }
}
