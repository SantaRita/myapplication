package com.example.myapplication.ventanas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaPacks;
import com.example.myapplication.pantallas.PantallaQuestionsByLevel;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenObjectsByPack extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	public Table tableObjectsByPack = new Table();

	

	Item regpregunta;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;

	// Ponemos los campos
	// Campos del titular para cartera o propuesta
	public ComboBox cbQuestion= new ComboBox("Question");

	PantallaPacks pantallaPacks;
	
	public VenObjectsByPack( PantallaPacks pantallaPacksLocal ) {

		pantallaPacks = pantallaPacksLocal;
		
		// TODO Auto-generated constructor stub
		
		setModal(true);
		setWidth("90%");
		setClosable(true);
		setResizable(false);
		setStyleName("ventanamodal");
		
		VerticalLayout vl = new VerticalLayout();
		GridLayout hl = new GridLayout(1,1);
		hl.setMargin(true);
		vl.setMargin(true);
		vl.setWidth("100%");
		hl.setWidth("100%");

		// TABLA
		String[] columnsexp ={"IdPack", "IdObject", "ObjectName","Select"};
		Object[] typesexp = {Integer.class, Integer.class, String.class, CheckBox.class};
		Object[] visibleColumnsexp = new Object[]{"ObjectName","Select"};

		Util.defineTable(tableObjectsByPack, columnsexp, typesexp, visibleColumnsexp,true);
		tableObjectsByPack.setColumnHeaders(new String[] {"Object Name","Select"});
		tableObjectsByPack.setPageLength(0);
		tableObjectsByPack.setWidth("100%");
		tableObjectsByPack.setHeight("100%");
		tableObjectsByPack.setVisible(true);
		tableObjectsByPack.setPageLength(10);
		tableObjectsByPack.setColumnExpandRatio("ObjectName", 90);
		tableObjectsByPack.setColumnExpandRatio("Select", 10);
		hl.addComponent(tableObjectsByPack,0,0);
		
		
	    botonera = new BotoneraDoble();
	    vl.addComponent(hl);
	    vl.addComponent(botonera);		
		setContent(vl);
		
		botonera.btAceptar.setCaption("Apply");
		
		
		botonera.btCancelar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().getSession().setAttribute("botonpulsado","CANCELAR");
				
				close();
				
			}
		});
		
		botonera.btAceptar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String numeros = "-1";
				try {
					

					for (Iterator i = tableObjectsByPack.getItemIds().iterator(); i.hasNext();) {
						int iid = (Integer) i.next();
					    
					    // Now get the actual item from the table.
					    Item item = tableObjectsByPack.getItem(iid);
					    CheckBox ck = (CheckBox)
					            (item.getItemProperty("Select").getValue());

					    if ((Boolean)ck.getValue()== true) {

					    	System.out.println("activado" + item.getItemProperty("IdObject").getValue() );
					    	
					    	numeros += "," + item.getItemProperty("IdObject").getValue();
					        //             .getValue() + " ";
					    }
					}
					
					
					
					
					conexion = new Conexion();
					Connection con = conexion.getConnection();
					Statement statement = con.createStatement();
					
					String cadena;
					
					// Primero borramos todos los registros de ese TEST.
					cadena= "delete ObjectByPacks WHERE "
							+ " idpack = " + Integer.valueOf(pantallaPacks.idPack);
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					// Segundo, insertamos los registros activos
					cadena= "INSERT INTO ObjectByPacks ( IdPack, IdObject ) "
							+ " SELECT " + pantallaPacks.idPack + ","
							+ " idobject"
							+ " FROM objects"
							+ " WHERE Idobject IN ( " + numeros + ")";
					System.out.println(cadena);
					statement.executeUpdate(cadena);					
					statement.close();
					con.close();
					
					new Notification("Process OK",
							"Pack Objects Updated ",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenObjectsByPack.this.close();
					pantallaPacks.buscar.click();

		        }  catch (Exception e) {
					// TODO Auto-generated catch block
		        	e.printStackTrace();
		        	new Notification("Got an exception!",
							e.getMessage(),
							Notification.Type.ERROR_MESSAGE, true)
							.show(Page.getCurrent());
		        	
					
				}
				
			}
		});
		
				
	}
	
	//	************************ BUSCAR QUESTIONS BY LEVEL *************************
	@SuppressWarnings("unchecked")
	private void buscarObjectsByPack() {
		try {

			tableObjectsByPack.removeAllItems();
			conexion = new Conexion();
			String where = "";
			/*if ( txPregunta.getValue()!=null ) {
				where = " where upper(QUESTIONDESC) like upper('%" + txPregunta.getValue() + "%')" ;
			}*/
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		" select  q.idobject, q.objectname, obp.idpack " + 
                		" from ObjectByPacks obp right JOIN objects q " + 
                		" ON q.IdObject = obp.IdObject order by q.objectname "
                		//" and qbl.idlevel = " + pantallaPacks.cbLevel.getValue().toString()
                		+ where )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableObjectsByPack.addItem();
						Item row1 = tableObjectsByPack.getItem(newItemId);
						
						
						//------ CHECK ACTIVAR 
						CheckBox ckActivo = new CheckBox();
						ckActivo.setId(newItemId.toString());
						ckActivo.setData(newItemId);
						
						//IdPack", "IdObject", "ObjectName","Select"
					

						row1.getItemProperty("IdPack").setValue(resultSet.getInt("IdPack"));
						row1.getItemProperty("IdObject").setValue(resultSet.getInt("IdObject"));
						row1.getItemProperty("ObjectName").setValue(resultSet.getString("ObjectName"));
						row1.getItemProperty("Select").setValue(ckActivo);
						
						
						//System.out.println(String.valueOf(resultSet.getInt("idlevel")));
						//System.out.println(">" + pantallaPacks.cbLevel.getValue().toString());
						if ( String.valueOf(resultSet.getInt("idPack")).equals(String.valueOf(pantallaPacks.idPack)))   {
							System.out.println("Activamos");
							ckActivo.setValue(true);
						}
						else {
							ckActivo.setValue(false);
						}

						tableObjectsByPack.setFooterVisible(false);
						tableObjectsByPack.setVisible(true);
						tableObjectsByPack.setSelectable(true);
						tableObjectsByPack.setImmediate(true);
						//
                		
                    }
		        con.close();
		        statement.close();
		       }                   
	    }
	    catch (Exception e) {
	            e.printStackTrace();
	    }
	}
			


	private class HorizontalRule extends Label {
		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public HorizontalRule() {
		    super("<hr style='color:blue' />", ContentMode.HTML);
		  }
		}

	//@PostConstruct
	public void init( String tipoAcceso) {

		
		acceso = tipoAcceso;
		
		cbQuestion.setValue(null);
		regpregunta = item;
		//this.setCaption("Questions Test By Level: " + pantallaPacks.idPack.getItemCaption(pantallaPacks.cbLevel.getValue()));
		buscarObjectsByPack();

	}
	

	
	/*@Override
	public void close() {
		// TODO Auto-generated method stub
		//super.close();
		//System.out.println("Cerramos la ventana de anular");
		UI.getCurrent().removeWindow(this);
		
	}*/

}