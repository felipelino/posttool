package br.util.file.filter;


import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe que define um filtro para arquivos XML.
 * 
 * @author Felipe Lino
 * <BR>Atualizado: 22/08/2010
 */
public class FileFilterJar extends FileFilter
{
	/**
	 * Descreve extensão de arquivo JAR.
	 */
	public static final String EXTENSION_JAR = "jar";

	/**
	 * Método que descreve quais tipos de arquivos são aceitos
	 * por esse filtro. Aqui apenas arquivos JAR.
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
            if(extension.equalsIgnoreCase(EXTENSION_JAR))
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
        return "JAR File";
    }
}
