import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.transform.Transformer; 
import javax.xml.transform.TransformerFactory; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document; 
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;

public class GenerateChangeLogs {

    private static final String rootfolder="D:\\Projects\\jenkins-test\\Release 1.0.0";

    private static String curFolder="";

    static int id=1;
    static int fileid=1;

    /*Generate XML
    public static Element generateXML() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder builder = factory.newDocumentBuilder(); 
  
        // Create a new Document 
        Document document = builder.newDocument(); 
  
        // Create root element 
        Element root = document.createElement("databaseChangeLog");
        root.setAttribute("xmlns","http://www.liquibase.org/xml/ns/dbchangelog");
        root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns:ext","http://www.liquibase.org/xml/ns/dbchangelog-ext");
        root.setAttribute("xmlns:pro","http://www.liquibase.org/xml/ns/pro");
        root.setAttribute("xsi:schemaLocation","http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-latest.xsd");

        TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
        Transformer transformer = transformerFactory.newTransformer(); 
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource();
        StreamResult result = new StreamResult("D:\\Projects\\jenkins-test\\changelog.xml"); 
        transformer.transform(source, result);  
        return root;
    }*/

    public static void checksql(File[] files) throws Exception
    {
        ArrayList<String> sqlfiles=new ArrayList<>();

        for (File filename : files) 
        {
            
            if (filename.isDirectory()) 
            {
                curFolder=filename.getAbsolutePath();
                findsqlFiles(filename,rootfolder);
            }

            else if(filename.getName().endsWith(".sql"))
            {
                sqlfiles.add(filename.getAbsolutePath());
            }

            else if(filename.getName().endsWith(".xml"))
            {
                continue;
            }
        }
        generateChangeSets(sqlfiles,curFolder);
    }

    public static void generateChangeSets(ArrayList<String> sqlfiles,String curFolder) throws Exception
    {
        if(sqlfiles.size()==0)
            return;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder builder = factory.newDocumentBuilder(); 
      
        // Create a new Document 
        Document document = builder.newDocument(); 
      
        // Create root element 
        Element root = document.createElement("databaseChangeLog");
        root.setAttribute("xmlns","http://www.liquibase.org/xml/ns/dbchangelog");
        root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns:ext","http://www.liquibase.org/xml/ns/dbchangelog-ext");
        root.setAttribute("xmlns:pro","http://www.liquibase.org/xml/ns/pro");
        root.setAttribute("xsi:schemaLocation","http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-latest.xsd");
    
        document.appendChild(root);
        
        for(String apath:sqlfiles)
        {
            Element ch=document.createElement("changeSet");
            ch.setAttribute("id",Integer.valueOf(id).toString());
            id++;
            ch.setAttribute("author","user");

            root.appendChild(ch);

            Element sqlFile=document.createElement("sqlFile");
            sqlFile.setAttribute("path", getRelativePath(apath,curFolder));

            ch.appendChild(sqlFile);

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
        Transformer transformer = transformerFactory.newTransformer(); 
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        String path=curFolder+"\\"+fileid+"changelog.xml";
        StreamResult result = new StreamResult(path);
        fileid++;
        transformer.transform(source, result); 

    }


    public static String getRelativePath(String fpath,String curFolder)
    {
        String res="";

        for(int i=0;i<fpath.length();i++)
        {
            if(i>curFolder.length())
                res+=fpath.charAt(i);
        }

        return res;
    }



    public static void generateIncludeTag(ArrayList<String> xmlfiles)
    {
        if(xmlfiles.size()==0)
            return;
    }


    public static void findsqlFiles(File curFolder,String rootFolder) throws Exception
    {
        File[] files=curFolder.listFiles();
        checksql(files);
    }

    public static void findxmlFiles(File curFolder,String rootFolder) throws Exception
    {
        File[] files=curFolder.listFiles();
        checkxml(files);
    }

    public static void checkxml(File[] files) throws Exception
    {
        ArrayList<String> xmlfiles=new ArrayList<>();

        for (File filename : files) 
        {
            
            if (filename.isDirectory()) 
            {
                curFolder=filename.getAbsolutePath();
                findxmlFiles(filename,rootfolder);
            }

            else if(filename.getName().endsWith(".sql"))
            {
                continue;
            }

            else if(filename.getName().endsWith(".xml"))
            {
                xmlfiles.add(filename.getAbsolutePath());
            }
        }
        //generateIncludeTag(xmlfiles,curFolder);
    }

    public static void main(String[] args) throws Exception {
        findsqlFiles(new File(rootfolder),rootfolder);
    
    }

    
}
