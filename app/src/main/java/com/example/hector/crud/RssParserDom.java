package com.example.hector.crud;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssParserDom
{
    Context context;
	private String archivo;
    HashMap<String, String> queryValues =  new  HashMap<String, String>();
	
	public RssParserDom(String file)
	{
        archivo = file;
        Log.e("fichero",archivo.toString());
    }

    public ArrayList<Coches> parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Coches> coches = new ArrayList<Coches>();
        
        try 
        {
            DocumentBuilder builder = factory.newDocumentBuilder();

            File f = new File(archivo);
            Document dom = builder.parse(f);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("Coches");
            
            for (int i=0; i<items.getLength(); i++)
            {
                Coches coche = new Coches();
                
                Node item = items.item(i);
                NodeList datosCoches = item.getChildNodes();
                
                for (int j=0; j<datosCoches.getLength(); j++)
                {
                    Node dato = datosCoches.item(j);
                    String etiqueta = dato.getNodeName();
                    
                   if (etiqueta.equals("idfoto"))
                    {
                        coche.setImageURI(Uri.parse(dato.getFirstChild().getNodeValue()));
                        queryValues.put("idfoto", coche.getImageURI().toString());
                    } 
                    else if (etiqueta.equals("matricula"))
                    {
                        String texto = obtenerTexto(dato);

                        coche.setMatricula(texto);
                        queryValues.put("matricula", coche.getMatricula());
                    } 
                    else if (etiqueta.equals("marca"))
                    {
                        coche.setMarca(dato.getFirstChild().getNodeValue());
                        queryValues.put("marca",coche.getMarca());
                    }
                    else if (etiqueta.equals("modelo"))
                    {
                        coche.setModelo(dato.getFirstChild().getNodeValue());
                        queryValues.put("modelo", coche.getModelo());
                    }
                    else if (etiqueta.equals("motorizacion"))
                    {
                        coche.setMotorizacion(dato.getFirstChild().getNodeValue());
                        queryValues.put("motorizacion", coche.getMotorizacion());
                    }
                    else if (etiqueta.equals("cilindrada"))
                    {
                        coche.setCilindrada(dato.getFirstChild().getNodeValue());
                        queryValues.put("cilindrada", coche.getCilindrada());
                    }
                    else if (etiqueta.equals("fechaCompra"))
                    {
                        coche.setFechaCompra(dato.getFirstChild().getNodeValue());
                        queryValues.put("fechaCompra",coche.getFechaCompra());
                    }
                    Log.e("Cocheeeeeeeeee","");
                }
                Log.e("Cocheeeeeeeeee22","");
                coches.add(coche);
            }
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException(ex);
        }
        Log.e("numCoches",String.valueOf(coches.size()));
        return coches;

    }

	private String obtenerTexto(Node dato)
	{
		StringBuilder texto = new StringBuilder();
		NodeList fragmentos = dato.getChildNodes();
		
		for (int k=0;k<fragmentos.getLength();k++)
		{
		    texto.append(fragmentos.item(k).getNodeValue());
		}
		
		return texto.toString();
	}
}
