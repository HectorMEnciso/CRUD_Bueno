package com.example.hector.crud;


import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssParserDom
{
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
            Log.e("entroDoc","");
            NodeList items = root.getElementsByTagName("Coche");
            //NodeList items = root.getElementsByTagName("row");
            Log.e("entroROOT",String.valueOf(items.getLength()));
            for (int i=0; i<items.getLength(); i++)
            {
                Log.e("entroForrrr",String.valueOf(items.getLength()));
               Coches coche = new Coches();
                
                Node item = items.item(i);
                NodeList datosCoches = item.getChildNodes();
                
                for (int j=0; j<datosCoches.getLength(); j++)
                {
                    Node dato = datosCoches.item(j);
                    String etiqueta = dato.getNodeName();


                    Log.e("prueba",etiqueta.toString());
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
