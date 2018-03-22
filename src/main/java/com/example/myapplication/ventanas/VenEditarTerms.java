package com.example.myapplication.ventanas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaTerms;
import com.meditacionback.utiles.BotoneraDoble;
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
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenEditarTerms extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	

	Item regterm;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;

	// Ponemos los campos
	// Campos del titular para cartera o propuesta
	public TextArea  txTerm = new TextArea ("Text Conditions");
	public ComboBox cbType= new ComboBox("Type Text");
	public ComboBox cbEnabled = new ComboBox("Status");


	public VenEditarTerms( PantallaTerms pantallaTerms ) {

		
		
		// TODO Auto-generated constructor stub
		
		setModal(true);
		setWidth("90%");
		setClosable(true);
		setResizable(false);
		setStyleName("ventanamodal");
		
		VerticalLayout vl = new VerticalLayout();
		GridLayout hl = new GridLayout(3,2);
		hl.setMargin(true);
		vl.setMargin(true);
		vl.setWidth("100%");
		hl.setWidth("100%");


		item = new PropertysetItem();
		binder = new FieldGroup(item);
		binder.setBuffered(true);

		txTerm.setRequired(true);
		txTerm.setHeight(this.getHeight(),Unit.PIXELS);
		txTerm.setWidth("100%");
		txTerm.setValidationVisible(true);
		txTerm.setNullRepresentation("");
		txTerm.setRequiredError("The field Pack is required");
		hl.addComponent(txTerm,0,0,0,1);
		

	
		cbType.setRequired(true);
		cbType.setValidationVisible(true);
		cbType.setTextInputAllowed(false);
		cbType.setNullSelectionAllowed(false);
		cbType.setReadOnly(true);
		cbType.setWidth("100%");
		cbType.setRequiredError("The field Type is required");
		cbType.addItem("TC");
		cbType.addItem("PO");
		cbType.setItemCaption("TC","Terms & Conditions");
		cbType.setItemCaption("PO","Policy");
		hl.addComponent(cbType,2,0);
		
		cbEnabled.setRequired(true);
		cbEnabled.setValidationVisible(true);
		cbEnabled.setTextInputAllowed(false);
		cbEnabled.setNullSelectionAllowed(false);
		cbEnabled.setReadOnly(false);
		cbEnabled.setWidth("80%");
		cbEnabled.setRequiredError("The field Status is required");
		cbEnabled.addItem(new BigDecimal("0"));
		cbEnabled.addItem(new BigDecimal("1"));
		cbEnabled.setItemCaption(new BigDecimal("0"),"Disabled");
		cbEnabled.setItemCaption(new BigDecimal("1"),"Enabled");
		hl.addComponent(cbEnabled,2,1);		
		
		hl.setColumnExpandRatio(0, 70);
		hl.setColumnExpandRatio(1, 10);
		hl.setColumnExpandRatio(2, 30);

		item.addItemProperty("txTerm", new ObjectProperty<String>(null, String.class));
		item.addItemProperty("cbType", new ObjectProperty<String>(null, String.class));
		item.addItemProperty("cbEnabled", new ObjectProperty<java.math.BigDecimal>(null, java.math.BigDecimal.class));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);	
		binder.bindMemberFields(this);	
		
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
				try {
					
					
					binder.commit();
					
					conexion = new Conexion();
					Connection con = conexion.getConnection();
					Statement statement = con.createStatement();
					txTerm.setValue(txTerm.getValue().replace("'", "´"));
					
					String cadena;
					if ( acceso.equals("NEW" )) {
						cadena= "INSERT INTO TERMANDCONDITIONS ( CONDICIONES, IDTIPO, ACTIVO ) VALUES ("
							+ "'" + txTerm.getValue().toString() + "',"
							+ "'" + cbType.getValue() + "',"
							+ cbEnabled.getValue()
							+ ")" ;
					}
					else {
						cadena= "UPDATE TERMANDCONDITIONS "
								+ "SET CONDICIONES = '" + txTerm.getValue().toString() + "',"
								+ "IDTIPO = '" + cbType.getValue() + "',"
								+ "ACTIVO = " + cbEnabled.getValue()
								+ " WHERE IDTERM = " + regterm.getItemProperty("IdTerm").getValue();
								 						
					}
					
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					statement.close();
					con.close();
					
					new Notification("Process OK",
							"Record " + acceso,
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenEditarTerms.this.close();
					pantallaTerms.buscar.click();
				} catch (CommitException e) {

					
					String mensajes = "";
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
					
				
 
		        }  catch (SQLException e) {
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
		txTerm.setValue(null);
		regterm = item;
		
		if ( tipoAcceso.equals("UPDATE")) {

			cbType.setValue(item.getItemProperty("Idtipo").getValue().toString());
			txTerm.setValue(item.getItemProperty("Condiciones").getValue().toString());
			cbEnabled.setValue(new BigDecimal(item.getItemProperty("Activo").getValue().toString()));
		}
		this.setCaption(tipoAcceso + " Term & Policy");

	}
	

	
	/*@Override
	public void close() {
		// TODO Auto-generated method stub
		//super.close();
		//System.out.println("Cerramos la ventana de anular");
		UI.getCurrent().removeWindow(this);
		
	}*/

}