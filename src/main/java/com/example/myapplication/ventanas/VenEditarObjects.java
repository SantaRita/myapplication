package com.example.myapplication.ventanas;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaObjects;
import com.meditacionback.utiles.BotoneraDoble;
import com.meditacionback.utiles.UploadBox;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenEditarObjects extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	
	// Data de conexión de Azure
	public static final String storageConnectionString =
	"DefaultEndpointsProtocol=https;AccountName=multimediablob;AccountKey=VLUsHCFjTEux65BxBgKOV0kLBpHe9qkPiv7HvbUaTXestWjheAKsSnL6VW00XjrTN/P+YcEjVotwTW06jiCkMQ==;EndpointSuffix=core.windows.net";

	String ruta;

	Item regpregunta;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;

	// Ponemos los campos
	// Campos del titular para cartera o propuesta
	public UploadBox uploadbox;
	public int idObject;
	public TextField txObject = new TextField("Object Name");
	public TextField txUrl = new TextField("Url");
	public ComboBox cbType= new ComboBox("Object Type");
       
    ByteArrayOutputStream os =
            new ByteArrayOutputStream(10240);

    String fileName;
    String mensajes;


	public VenEditarObjects( PantallaObjects pantallaObjects) {

		
		
		// TODO Auto-generated constructor stub
		
		setModal(true);
		setWidth("90%");
		setClosable(true);
		setResizable(false);
		setStyleName("ventanamodal");
		
		VerticalLayout vl = new VerticalLayout();
		GridLayout hl = new GridLayout(3,3);
		hl.setMargin(true);
		vl.setMargin(true);
		vl.setWidth("100%");
		hl.setWidth("100%");


		item = new PropertysetItem();
		binder = new FieldGroup(item);
		binder.setBuffered(true);

		txObject.setRequired(true);
		txObject.setWidth("100%");
		txObject.setValidationVisible(true);
		txObject.setNullRepresentation("");
		txObject.setRequiredError("The field Object Name is required");
		hl.addComponent(txObject,0,0);
		

	
		cbType.setRequired(true);
		cbType.setValidationVisible(true);
		cbType.setTextInputAllowed(false);
		cbType.setNullSelectionAllowed(false);
		cbType.setReadOnly(true);
		cbType.setWidth("100%");
		cbType.setRequiredError("The field Object Type is required");
		cbType.addItem(new BigDecimal("1"));
		cbType.addItem(new BigDecimal("2"));
		cbType.addItem(new BigDecimal("3"));
		cbType.setItemCaption(new BigDecimal("1"),"Video");
		cbType.setItemCaption(new BigDecimal("2"),"Audio");
		cbType.setItemCaption(new BigDecimal("3"),"Image");
		cbType.setItemCaption(new BigDecimal("4"),"Tip");
		hl.addComponent(cbType,2,0);
		
		txUrl.setWidth("100%");
		txUrl.setValidationVisible(true);
		txUrl.setNullRepresentation("");
		hl.addComponent(txUrl,0,1);

		
		
		hl.setColumnExpandRatio(0, 70);
		hl.setColumnExpandRatio(1, 10);
		hl.setColumnExpandRatio(2, 30);

		item.addItemProperty("txObject", new ObjectProperty<String>(null, String.class));
		item.addItemProperty("cbType", new ObjectProperty<java.math.BigDecimal>(null, java.math.BigDecimal.class));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);	
		binder.bindMemberFields(this);	
		

	    uploadbox = new UploadBox();
		hl.addComponent(uploadbox,0,2);

	    
	    botonera = new BotoneraDoble();
	    vl.addComponent(hl);
	    vl.addComponent(botonera);	
		setContent(vl);
		
		
		
		botonera.btAceptar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					
					if ( acceso == "INSERT") {

							mensajes = null;
							if (uploadbox.nombreFicheroSeleccionado==null) {
								mensajes = "The field File is required";
							}
							binder.commit();
							if ( mensajes!=null ) {
								throw new CommitException();
							}
							
							System.out.println("Nombre del fichero: " + uploadbox.nombreFicheroSeleccionado);
							System.out.println("Entramos nombre fichero extension: " + txObject.getValue().toString() 
									+ uploadbox.nombreFicheroSeleccionado.substring(uploadbox.nombreFicheroSeleccionado.indexOf(".", -1)));
							
							uploadbox.nombreFichero = txObject.getValue().toString() 
									+ uploadbox.nombreFicheroSeleccionado.substring(uploadbox.nombreFicheroSeleccionado.indexOf(".", -1));
							uploadbox.upload.submitUpload();
					}
					else {
						binder.commit();
					}
					

					conexion = new Conexion();
					Connection con = conexion.getConnection();
					Statement statement = con.createStatement();
					
					String cadena;
					if ( acceso.equals("NEW" )) {
						cadena= "INSERT INTO OBJECTS ( OBJECTNAME, OBJECTTYPE, OBJECTSIZE, OBJECTDURATION, CREATIONDATE, USERCREATION, URL ) VALUES ("
							+ "'" + txObject.getValue().toString() + "',"
							+ cbType.getValue() + ","
							+ "null" + ","
							+ "null" + ","
							+ "GETUTCDATE()" + ","
							+ "'" + UI.getCurrent().getSession().getAttribute("username") + "',"
							+ "null"
							
							+ ")" ;
					}
					else {
						cadena= "UPDATE OBJECTS "
								+ "SET OBJECTNAME = '" + txObject.getValue().toString() + "',"
								+ "OBJECTTYPE = " + cbType.getValue()
								+ " WHERE IDOBJECT = " + idObject;
								 						
					}
					
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					statement.close();
					con.close();
					
					new Notification("Process OK",
							acceso + " Object successly.",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenEditarObjects.this.close();
					pantallaObjects.buscar.click();
					
				} catch (CommitException e) {

					
		            for (Field<?> field: binder.getFields()) {
		                ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		                
		                if (errMsg != null) {
		                	//System.out.println("Error:"+errMsg.getFormattedHtmlMessage());
		                	mensajes+=errMsg.getFormattedHtmlMessage();
		                }
		            }
		            
					new Notification("Errors detected",
							mensajes,
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());					
					
				
 
		        } 
				 catch (SQLException e) {
					// TODO Auto-generated catch block
		        	e.printStackTrace();
		        	new Notification("Got an exception!",
							e.getMessage(),
							Notification.Type.ERROR_MESSAGE, true)
							.show(Page.getCurrent());
		        	
					
				}
				
			}
		});
		
		botonera.btCancelar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().getSession().setAttribute("botonpulsado","CANCELAR");
				
				close();
				
			}
		});
		
				
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
	public void init( String tipoAcceso, Item item) {

		
		acceso = tipoAcceso;
		
		cbType.setValue(null);
		txObject.setValue(null);
		regpregunta = item;
		txUrl.setVisible(false);
		txUrl.setValue(null);
		txUrl.setEnabled(false);
		
		
		
		if ( tipoAcceso.equals("UPDATE")) {
			uploadbox.setVisible(false);
			txUrl.setVisible(true);
			cbType.setValue(new BigDecimal(item.getItemProperty("ObjectType").getValue().toString()));
			txObject.setValue(item.getItemProperty("ObjectName").getValue().toString());
			idObject = Integer.valueOf(item.getItemProperty("IdObject").getValue().toString());
		}
		this.setCaption(tipoAcceso + " Object");

	}

	

}