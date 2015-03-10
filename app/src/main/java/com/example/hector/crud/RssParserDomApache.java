package com.example.hector.crud;


import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RssParserDomApache
{
    private URL rssUrl;
    HashMap<String, String> queryValues =  new  HashMap<String, String>();

    public RssParserDomApache(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Coches> parse() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Coches> coches = new ArrayList<Coches>();
        
        try 
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();

            NodeList items = root.getElementsByTagName("Coche");

            for (int i=0; i<items.getLength(); i++)
            {
               Log.e("numero elementos",String.valueOf(items.getLength()));
               Coches coche = new Coches();
                
                Node item = items.item(i);
                NodeList datosCoches = item.getChildNodes();

               Log.e("datosCoches", String.valueOf(datosCoches.getLength()));

                for (int j=0; j<datosCoches.getLength(); j++)
                {

                    Node dato = datosCoches.item(j);
                    String etiqueta = dato.getNodeName();


                   Log.e("etiqueta",etiqueta);

                 if (etiqueta.equals("idfoto"))
                  {
                        coche.setImageURI(Uri.parse(dato.getFirstChild().getNodeValue()));

                        queryValues.put("idfoto", coche.getImageURI().toString());
                        Log.e("idfoto", coche.getImageURI().toString());
                    } 
                   else if (etiqueta.equals("matricula"))
                    {
                        String texto = obtenerTexto(dato);

                        coche.setMatricula(texto);
                        queryValues.put("matricula", coche.getMatricula());
                        Log.e("matricula", coche.getMatricula().toString());
                    } 
                    else if (etiqueta.equals("marca"))
                    {
                        coche.setMarca(dato.getFirstChild().getNodeValue());
                        queryValues.put("marca",coche.getMarca());
                        Log.e("marca", coche.getMarca().toString());
                    }
                    else if (etiqueta.equals("modelo"))
                    {
                        coche.setModelo(dato.getFirstChild().getNodeValue());
                        queryValues.put("modelo", coche.getModelo());
                        Log.e("modelo", coche.getModelo().toString());
                    }
                    else if (etiqueta.equals("motorizacion"))
                    {
                        coche.setMotorizacion(dato.getFirstChild().getNodeValue());
                        queryValues.put("motorizacion", coche.getMotorizacion());
                        Log.e("motorizacion", coche.getMotorizacion().toString());
                    }

                    else if (etiqueta.equals("cilindrada"))
                    {
                        coche.setCilindrada(dato.getFirstChild().getNodeValue());
                        queryValues.put("cilindrada", coche.getCilindrada());
                        Log.e("cilindrada", coche.getCilindrada().toString());
                    }

                     else if (etiqueta.equals("fechaCompra"))
                    {
                        coche.setFechaCompra(dato.getFirstChild().getNodeValue());
                        queryValues.put("fechaCompra",coche.getFechaCompra());
                        Log.e("fechaCompra", coche.getFechaCompra().toString());
                    }
                    Log.e("Cocheeeeeeeeee", "");
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

    private InputStream getInputStream() {
        try {
            return rssUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
