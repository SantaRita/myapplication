package com.example.myapplication.ventanas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaPacksByLevel;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.RegexpValidator;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenEditarPacksByLevel extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	public Table tablePackByLevels = new Table();

	

	Item regpregunta;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;

	// Ponemos los campos
	// Campos del titular para cartera o propuesta
	public ComboBox cbPack= new ComboBox("Pack");

	PantallaPacksByLevel pantallaPacksByLevel;
	
	public VenEditarPacksByLevel( PantallaPacksByLevel pantallaPacksByLevelLocal ) {

		pantallaPacksByLevel = pantallaPacksByLevelLocal;
		
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
		String[] columnsexp ={"IdPack", "PackName","Select","OrderBy","Free","Price"};
		Object[] typesexp = {Integer.class, String.class, CheckBox.class, TextField.class, CheckBox.class, TextField.class};
		Object[] visibleColumnsexp = new Object[]{"PackName","Select","OrderBy","Free","Price"};

		Util.defineTable(tablePackByLevels, columnsexp, typesexp, visibleColumnsexp,true);
		tablePackByLevels.setColumnHeaders(new String[] {"PackName","Select","Order by","Is Free", "Price"});
		tablePackByLevels.setPageLength(0);
		tablePackByLevels.setWidth("100%");
		tablePackByLevels.setHeight("100%");
		tablePackByLevels.setVisible(true);
		tablePackByLevels.setPageLength(10);
		tablePackByLevels.setColumnExpandRatio("PackName", 55);
		tablePackByLevels.setColumnExpandRatio("Select", 10);
		tablePackByLevels.setColumnExpandRatio("OrderBy", 10);
		tablePackByLevels.setColumnExpandRatio("Free", 10);
		tablePackByLevels.setColumnExpandRatio("Price", 15);
				
		hl.addComponent(tablePackByLevels,0,0);
		
		
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
					

					conexion = new Conexion();
					Connection con = conexion.getConnection();
					Statement statement = con.createStatement();
					
					String cadena;
					
					// Primero borramos todos los registros de ese TEST.
					cadena= "delete PACKBYLEVELS WHERE "
							+ " IDLEVEL = " + pantallaPacksByLevel.cbLevel.getValue();
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					// Segundo, insertamos los registros activos

					for (Iterator i = tablePackByLevels.getItemIds().iterator(); i.hasNext();) {
						int iid = (Integer) i.next();
					    
					    // Now get the actual item from the table.
					    Item item = tablePackByLevels.getItem(iid);
					    CheckBox ck = (CheckBox) (item.getItemProperty("Select").getValue());
					    CheckBox ckGratis = (CheckBox) (item.getItemProperty("Free").getValue());
					    double importe = 0;
					    if (!((TextField)item.getItemProperty("Price").getValue()).getValue().toString().equals("")) { 
					      importe = Double.parseDouble(((TextField)item.getItemProperty("Price").getValue()).getValue().toString().replace(",", "."));
					    }

					    if ((Boolean)ck.getValue()== true) {

					    	System.out.println("activado" + item.getItemProperty("IdPack").getValue() );
					    	
				    	
						    cadena= "INSERT INTO PACKBYLEVELS ( IDLEVEL, IDPACK, ISFREE, ORDERBY, PRICE) VALUES ( "
						    		
									+ pantallaPacksByLevel.cbLevel.getValue() + ","
									+ item.getItemProperty("IdPack").getValue() + "," 
									+ "'" +ckGratis.getValue().toString() + "',"
									+ ((TextField)item.getItemProperty("OrderBy").getValue()).getValue() + ","
								    + String.valueOf(importe).replace(",", ".")  +  " ) ";

						    System.out.println("Cadena: " + cadena);
							
							statement.executeUpdate(cadena);					    	
					    }
					    
					
					}
					
					statement.close();
					con.close();
					
					new Notification("Process OK",
							"Pack by Level Updated ",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenEditarPacksByLevel.this.close();
					pantallaPacksByLevel.buscar.click();

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
	
	//	************************ BUSCAR packs S BY LEVEL *************************
	@SuppressWarnings("unchecked")
	private void buscarPacksByLevel() {
		try {

			tablePackByLevels.removeAllItems();
			conexion = new Conexion();
			String where = "";
			/*if ( txPregunta.getValue()!=null ) {
				where = " where upper(packsDESC) like upper('%" + txPregunta.getValue() + "%')" ;
			}*/
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		" select  q.idpack, q.packname, qbl.idlevel, qbl.orderby, qbl.isfree, qbl.price " +
                		" from packbylevels qbl right JOIN packs q " +
                		" ON q.idpack = qbl.idpack " +
                		" and qbl.idlevel = " + pantallaPacksByLevel.cbLevel.getValue().toString()
                		+ where +  " order by qbl.orderby " )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tablePackByLevels.addItem();
						Item row1 = tablePackByLevels.getItem(newItemId);
						
						
						//------ CHECK ACTIVAR 
						CheckBox ckActivo = new CheckBox();
						ckActivo.setId(newItemId.toString());
						ckActivo.setData(newItemId);
						
						//------ ORDEN 
						TextField txOrderBy = new TextField();
						txOrderBy.setId(newItemId.toString());
						txOrderBy.setData(newItemId);
						txOrderBy.setWidth("30px");
						
						//------ IS FREE 
						CheckBox ckFree = new CheckBox();
						ckFree.setId(newItemId.toString());
						ckFree.setData(newItemId);						
						
						//------ IMPORTE
					    ObjectProperty<Double> property = new ObjectProperty<Double>(new Double("0"));
					    TextField txImporte= new TextField(null, property);					    
						txImporte.setWidth("100%");
						txImporte.setId(newItemId.toString());
						txImporte.setData(newItemId);
						txImporte.setValidationVisible(true);
						txImporte.addStyleName("align-right");
						txImporte.addValidator(new MyValidator());
						
						


						row1.getItemProperty("PackName").setValue(resultSet.getString("PackName"));
						row1.getItemProperty("Select").setValue(ckActivo);
						row1.getItemProperty("IdPack").setValue(resultSet.getInt("IdPack"));
						row1.getItemProperty("OrderBy").setValue(txOrderBy);
						txOrderBy.setValue(String.valueOf(resultSet.getInt("OrderBy")));
						row1.getItemProperty("Free").setValue(ckFree);
						ckFree.setValue(resultSet.getBoolean("IsFree"));
						row1.getItemProperty("Price").setValue(txImporte);
						txImporte.setValue(String.valueOf(resultSet.getDouble("Price")));
						
						//System.out.println(String.valueOf(resultSet.getInt("idlevel")));
						//System.out.println(">" + pantallaPacksByLevel.cbLevel.getValue().toString());
						if ( String.valueOf(resultSet.getInt("idlevel")).equals(pantallaPacksByLevel.cbLevel.getValue().toString()))   {
							System.out.println("Activamos");
							ckActivo.setValue(true);
						}
						else {
							ckActivo.setValue(false);
						}

						tablePackByLevels.setFooterVisible(false);
						tablePackByLevels.setVisible(true);
						tablePackByLevels.setSelectable(true);
						tablePackByLevels.setImmediate(true);
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
		
		cbPack.setValue(null);
		regpregunta = item;
		this.setCaption("Packs By Level: " + pantallaPacksByLevel.cbLevel.getItemCaption(pantallaPacksByLevel.cbLevel.getValue()));
		buscarPacksByLevel();

	}
	
	private class MyValidator implements Validator {
	    @Override
	    public void validate(Object value)
	            throws InvalidValueException {
	    	System.out.println("Valor: "+ value.getClass());
	        if (value instanceof Integer || value instanceof Double) {
	        	
	        }
	        else {
	        	System.out.println("No es un numero");
	            throw new InvalidValueException("The value is not a number");
	        }     
	    }
	}
	
	

}