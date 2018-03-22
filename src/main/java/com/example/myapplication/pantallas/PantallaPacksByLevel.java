package com.example.myapplication.pantallas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarPacksByLevel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaPacksByLevel extends Panel implements View {
	
	VenEditarPacksByLevel  venEditarPacksByLevel = new VenEditarPacksByLevel(this);
	
	private static Conexion conexion;
	public Table tablePacksByLevel = new Table();
	public Table tableAnswers = new Table();
	private FieldGroup binder;
	public ComboBox cbLevel = new ComboBox();
	
	public Button buscar = new Button("Search");
	
	Button nuevo = new Button("Edit");
	VerticalLayout layout = new VerticalLayout();
	

	public PantallaPacksByLevel() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Packs By Level");
		
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("mail", new ObjectProperty<String>(""));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
	
		
		cbLevel.setWidth("150px");
		cbLevel.setValidationVisible(true);
		cbLevel.setTextInputAllowed(false);
		cbLevel.setWidth("100%");
		cbLevel.addItem(new BigDecimal("1"));
		cbLevel.addItem(new BigDecimal("2"));
		cbLevel.addItem(new BigDecimal("3"));
		cbLevel.setItemCaption(new BigDecimal("1"),"Beginner");
		cbLevel.setItemCaption(new BigDecimal("2"),"Intermediate");
		cbLevel.setItemCaption(new BigDecimal("3"),"Professional");
		
		GridLayout gridCamposBusqueda = new GridLayout(5,4);
		GridLayout gridCamposRespuestas = new GridLayout(5,4);
		gridCamposBusqueda.setMargin(true);
		gridCamposBusqueda.setSpacing(true);
		gridCamposBusqueda.setWidth("100%");
		
		
		gridCamposBusqueda.addComponent(new Label("Level: "),0,0);
		gridCamposBusqueda.addComponent(cbLevel,1,0);
		gridCamposBusqueda.setComponentAlignment(cbLevel, Alignment.MIDDLE_LEFT);		
		
		gridCamposBusqueda.addComponent(nuevo,2,0);
		gridCamposBusqueda.addComponent(buscar,4,0);
		gridCamposBusqueda.setComponentAlignment(nuevo, Alignment.MIDDLE_CENTER);
		gridCamposBusqueda.setComponentAlignment(buscar, Alignment.MIDDLE_LEFT);
		nuevo.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		gridCamposBusqueda.setColumnExpandRatio(0, 10);
		gridCamposBusqueda.setColumnExpandRatio(1, 20);
		gridCamposBusqueda.setColumnExpandRatio(2, 70);
		
		
	
		buscar.setClickShortcut(KeyCode.ENTER);
		buscar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				// TODO Auto-generated method stub
			
				if ( cbLevel.getValue() == null ) {
					
					new Notification("Value required",
							"The field Level is required ",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
				} else
					buscarPacksByLevel();
			}
		});
		
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdPack","PackDesc","IdLevel","DescLevel","OrderBy","Free","Price"};
		Object[] typesexp = {Integer.class, String.class, Integer.class, String.class, Integer.class, Boolean.class, Double.class};
		Object[] visibleColumnsexp = 
				new Object[]{"DescLevel","PackDesc","OrderBy","Free", "Price"};

		Util.defineTable(tablePacksByLevel, columnsexp, typesexp, visibleColumnsexp,true);
		tablePacksByLevel.setColumnHeaders(new String[] {"DescLevel","PackDesc", "Order by","Is Free", "Price"});
		tablePacksByLevel.setColumnExpandRatio("DescLevel", 10);
		tablePacksByLevel.setColumnExpandRatio("PackDesc", 60);
		tablePacksByLevel.setColumnExpandRatio("OrderBy", 10);
		tablePacksByLevel.setColumnExpandRatio("Free", 10);
		tablePacksByLevel.setColumnExpandRatio("Price", 10);
		tablePacksByLevel.setColumnAlignment("Price", Align.RIGHT);

		tablePacksByLevel.setPageLength(0);
		tablePacksByLevel.setWidth("100%");
		tablePacksByLevel.setHeight("100%");
		tablePacksByLevel.setVisible(true);
		tablePacksByLevel.setPageLength(5);
		gridCamposBusqueda.addComponent(tablePacksByLevel,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		gridCamposBusqueda.setHeight("50%");

		
		tablePacksByLevel.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
	            if (event.getProperty().getValue() == null) {
	            	gridCamposRespuestas.setVisible(false);
	            }
	            else 
	            	gridCamposRespuestas.setVisible(true);

				
			}
		});
		

		setContent(layout);
		
		
	
		// Nueva PacksBYLEVEL
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				
				if ( cbLevel.getValue() == null ) {
					
					new Notification("Value required",
							"The field Level is required ",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
				} else {
				
					venEditarPacksByLevel.init("NEW");
					UI.getCurrent().addWindow(venEditarPacksByLevel);
				
				}
				
			}
		});
		
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Terms");
		
	}

	//	************************ BUSCAR packs S BY LEVEL *************************
	@SuppressWarnings("unchecked")
	private void buscarPacksByLevel() {
		try {

			tablePacksByLevel.removeAllItems();
			conexion = new Conexion();
			String where = "";
			/*if ( txPregunta.getValue()!=null ) {
				where = " where upper(PackDesc) like upper('%" + txPregunta.getValue() + "%')" ;
			}*/
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		" select l.idlevel, qbl.IdPack, l.desclevel, q.PackName, qbl.orderby, qbl.isfree, qbl.price " 
                		+" from levels l, packbylevels qbl, packs q"
                		+" where l.idlevel = qbl.idlevel"
                		+" and q.IdPack = qbl.IdPack"
                		+ " and l.idlevel = " + cbLevel.getValue().toString()
                		+ where + " " 
                		+ " order by qbl.orderby ")) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tablePacksByLevel.addItem();
						Item row1 = tablePacksByLevel.getItem(newItemId);
						
						
						//------ BOTON BORRAR
						/*Button btBorrar = new Button("Delete");
						btBorrar.setId(newItemId.toString());
						btBorrar.setData(newItemId);
						btBorrar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tablePacksByLevel.select(data);
								Item itemClickEvent = tablePacksByLevel.getItem(data);
								
								ConfirmDialog.Factory df = new DefaultConfirmDialogFactory() {

									
								    @Override
									public ConfirmDialog create(String caption, String message, String okCaption,
											String cancelCaption, String notOkCaption) {

								        ConfirmDialog  d = super.create(caption,message,okCaption, cancelCaption, notOkCaption
								               );
								        // Change the order of buttons
								        Button ok = d.getOkButton();
								        HorizontalLayout buttons = (HorizontalLayout) ok.getParent();
								        buttons.removeComponent(ok);
								        buttons.addComponent(ok,1);
								        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
								        return d;
								    }

								};
								ConfirmDialog.setFactory(df);		
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Question", "Are you sure delete this question ?",
						    	        "Yes", "No", new ConfirmDialog.Listener() {

						    	            public void onClose(ConfirmDialog dialog) {
						    	                if (dialog.isConfirmed()) {

						    	                	// Borramos el registro
						    	                	try {

						    	    						conexion = new Conexion();
						    	    						Connection con = conexion.getConnection();
						    	    						Statement statement = con.createStatement();
						    	    						
						    	    						Object rowId = tablePacksByLevel.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tablePacksByLevel.getContainerProperty(rowId,"IdPack").getValue();
						    	    						
						    	    						String cadena = "DELETE QUESTIONS WHERE IdPack =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tablePacksByLevel.removeItem(rowId);

						    	        					new Notification("Process OK",
						    	        							"Question deleted",
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
						    	            }
						    	        });

								}
						});	*/					
						
						//"IdPack","PackDesc","IdLevel","DescLevel"
						row1.getItemProperty("IdPack").setValue(resultSet.getInt("IdPack"));
						row1.getItemProperty("PackDesc").setValue(resultSet.getString("PackName"));
						row1.getItemProperty("DescLevel").setValue(resultSet.getString("desclevel"));
						row1.getItemProperty("IdLevel").setValue(resultSet.getInt("idlevel"));
						row1.getItemProperty("OrderBy").setValue(resultSet.getInt("OrderBy"));
						row1.getItemProperty("Free").setValue(resultSet.getBoolean("IsFree"));
						row1.getItemProperty("Price").setValue(resultSet.getDouble("Price"));
						
						

						//row1.getItemProperty("Borrar").setValue(btBorrar);
						//row1.getItemProperty("Modificar").setValue(btModificar);
						
						tablePacksByLevel.setFooterVisible(true);
						tablePacksByLevel.setVisible(true);
						tablePacksByLevel.setSelectable(true);
						tablePacksByLevel.setImmediate(true);
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
		
	
}

