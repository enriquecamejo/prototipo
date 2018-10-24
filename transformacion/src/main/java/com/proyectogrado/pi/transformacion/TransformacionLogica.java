package com.proyectogrado.pi.transformacion;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

@Service
public class TransformacionLogica {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionLogica.class);

	public String transformacion(String texto, String solucion, Integer paso) {
		try {
			StringBuffer trnURL = new StringBuffer("transformacion.url.");
			trnURL.append(solucion).append(".paso").append(paso);
			StringBuffer trnContentType = new StringBuffer("transformacion.contentype.");
			trnContentType.append(solucion).append(".paso").append(paso);
	        String strConnection = env.getProperty(trnURL.toString());
	        String contentType = env.getProperty(trnContentType.toString());
	        URL url = new URL(strConnection);
	        URLConnection uc = url.openConnection();
	        HttpURLConnection conn = (HttpURLConnection) uc;
	        conn.setDoOutput(true);
	        conn.setRequestProperty("content-type", contentType);
	        conn.setRequestMethod("POST");
			OutputStream os = conn.getOutputStream();
			os.write(texto.getBytes());
			os.flush();
			os.close();
	        
	        int rspCode = conn.getResponseCode();
	        if (rspCode == HttpURLConnection.HTTP_OK) {
	        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println(response.toString());
				return response.toString();
	        }else {
				System.out.println("POST request not worked");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	public String transformarJsonToXml(String texto) throws Exception{
		JSONObject json = new JSONObject(texto);
		return XML.toString(json);
    }
	
	public String transformarXmlToJson(String texto) throws Exception{
		JSONObject xmlJSONObj = XML.toJSONObject(texto);
        return xmlJSONObj.toString(4);
    }
	
	public String transformacionXSLT(String texto, String solucion, Integer paso) {
		//final String XSLT_PATH = "src/main/resources/ejemplo.xsl";
        try {
        	StringBuffer trnXslt = new StringBuffer("transformacion.xslt.");
        	trnXslt.append(solucion).append(".paso").append(paso);
        	String archivoXSLT = env.getProperty(trnXslt.toString());
        	logger.info("XSLT en el Transformador: "+archivoXSLT);
	        StringWriter sw = new StringWriter();
	        net.sf.saxon.s9api.Processor processor = new net.sf.saxon.s9api.Processor(false);        
	        Serializer serializer = processor.newSerializer();
	        serializer.setOutputWriter(sw);   
	        XsltCompiler compiler = processor.newXsltCompiler();
	        XsltExecutable executable;
			executable = compiler.compile(new StreamSource(new File(archivoXSLT)));
	        XsltTransformer transformer = executable.load();
	        	       
			XdmNode source = processor.newDocumentBuilder().build(new StreamSource(new ByteArrayInputStream(texto.getBytes("UTF-8"))));
			transformer.setInitialContextNode(source);
							
			transformer.setDestination(serializer);
	        transformer.transform();
	        return sw.toString();
        } catch (SaxonApiException e) {
			e.printStackTrace();
			return null;
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
