package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarPacks;
import com.example.myapplication.ventanas.VenObjectsByPack;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaPacks extends Panel implements View {

	
	VenEditarPacks  venEditarPacks = new VenEditarPacks(this);
	VenObjectsByPack  venObjectsByPack = new VenObjectsByPack(this);
	private static Conexion conexion;
	public Table tablePacks = new Table();
	public Table tableObjects = new Table();
	private FieldGroup binder;
	TextField txPack;
	TextField txRespuesta;
	public Button buscar = new Button("Search");
	Button limpiar = new Button("Clear");
	Button nuevo = new Button("Add");
	Button nuevoObject = new Button("Edit");
	VerticalLayout layout = new VerticalLayout();
	public int idPack;
	

	public PantallaPacks() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Packs");
		
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("mail", new ObjectProperty<String>(""));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
	
		txPack = new TextField();
		txPack.setWidth("150px");
		
		GridLayout gridCamposBusqueda = new GridLayout(5,4);
		GridLayout gridCamposRespuestas = new GridLayout(5,4);
		gridCamposBusqueda.setMargin(true);
		gridCamposBusqueda.setSpacing(true);
		gridCamposBusqueda.setWidth("100%");
		
		txPack = new TextField();
		txPack.setWidth("150px");
		
		gridCamposBusqueda.addComponent(new Label("Pack text: "),0,0);
		gridCamposBusqueda.addComponent(txPack,1,0);
		gridCamposBusqueda.setComponentAlignment(txPack, Alignment.MIDDLE_LEFT);		
		
		gridCamposBusqueda.addComponent(nuevo,2,0);
		gridCamposBusqueda.addComponent(limpiar,3,0);
		gridCamposBusqueda.addComponent(buscar,4,0);
		gridCamposBusqueda.setComponentAlignment(nuevo, Alignment.MIDDLE_CENTER);
		gridCamposBusqueda.setComponentAlignment(limpiar, Alignment.MIDDLE_RIGHT);
		gridCamposBusqueda.setComponentAlignment(buscar, Alignment.MIDDLE_LEFT);
		nuevo.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		gridCamposBusqueda.setColumnExpandRatio(0, 10);
		gridCamposBusqueda.setColumnExpandRatio(1, 20);
		gridCamposBusqueda.setColumnExpandRatio(2, 70);
		
		
		limpiar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				txPack.setValue("");
				tableObjects.removeAllItems();
				tablePacks.removeAllItems();
				
			}
		});
		
		buscar.setClickShortcut(KeyCode.ENTER);
		buscar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				// TODO Auto-generated method stub
				
				buscarPacks();
			}
		});
		
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdPack","PackName","TypePack","DesckTypePack","Enabled","DescEnabled", "Borrar","Modificar"};
		Object[] typesexp = {Integer.class, String.class, Integer.class, String.class,Integer.class, String.class, Button.class, Button.class };
		Object[] visibleColumnsexp = 
				new Object[]{"IdPack","PackName","DesckTypePack","DescEnabled","Borrar","Modificar"};

		Util.defineTable(tablePacks, columnsexp, typesexp, visibleColumnsexp,true);
		tablePacks.setColumnHeaders(new String[] {"Id","Pack Name", "DescType","Enabled", "Delete","Update"});
		tablePacks.setColumnExpandRatio("IdPack", 8);
		tablePacks.setColumnExpandRatio("PackName", 46);
		tablePacks.setColumnExpandRatio("DesckTypePack", 15);
		tablePacks.setColumnExpandRatio("Enabled", 15);
		tablePacks.setColumnExpandRatio("Borrar", 8);		
		tablePacks.setColumnExpandRatio("Modificar", 8);
		tablePacks.setPageLength(0);
		tablePacks.setWidth("100%");
		tablePacks.setHeight("100%");
		tablePacks.setVisible(true);
		tablePacks.setPageLength(5);
		gridCamposBusqueda.addComponent(tablePacks,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		gridCamposBusqueda.setHeight("50%");

		
		tablePacks.addValueChangeListener(new ValueChangeListener() {
			
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
		
		
		
		
		// Table Objects
		

		
		gridCamposRespuestas.addComponent(new Label("Objects by Pack "),0,0,1,0);
		gridCamposRespuestas.setMargin(true);
		gridCamposRespuestas.setSpacing(true);
		gridCamposRespuestas.setWidth("100%");
		
		gridCamposRespuestas.addComponent(nuevoObject,2,0);
		gridCamposRespuestas.setComponentAlignment(nuevoObject, Alignment.MIDDLE_CENTER);
		nuevoObject.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		String[] columnsexpObject ={"IdObject","ObjectName","Borrar","Modificar"};
		Object[] typesexpObject = {Integer.class, String.class, Button.class, Button.class };
		Object[] visibleColumnsexpObject = 
				new Object[]{"IdObject","ObjectName"};

		Util.defineTable(tableObjects, columnsexpObject, typesexpObject, visibleColumnsexpObject,true);
		tableObjects.setColumnHeaders(new String[] {"IdObject","ObjectName"});
		tableObjects.setColumnExpandRatio("IdObject", 8);
		tableObjects.setColumnExpandRatio("ObjectName", 92);
		//tableObjects.setColumnExpandRatio("Borrar", 8);		
		//tableObjects.setColumnExpandRatio("Modificar", 8);
		tableObjects.setPageLength(0);
		tableObjects.setWidth("100%");
		tableObjects.setHeight("100%");
		tableObjects.setVisible(true);
		gridCamposRespuestas.addComponent(tableObjects,0,1,4,1);
		gridCamposRespuestas.setVisible(false);
		layout.addComponent(gridCamposRespuestas);
		
		
		setContent(layout);
		
		
		tablePacks.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("buscar Objects");
				idPack = (int) event.getItem().getItemProperty("IdPack").getValue();
				buscaObjectsByPacks((int) event.getItem().getItemProperty("IdPack").getValue());
				
			}
		});
		
		// Nuevo PACK - Packs
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				venEditarPacks.init("NEW",null);
				UI.getCurrent().addWindow(venEditarPacks);
				
			}
		});
		
		nuevoObject.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
		
						venObjectsByPack.init("NEW");
						UI.getCurrent().addWindow(venObjectsByPack);

					
				}
			});
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Terms");
		
	}

	//	************************ BUSCAR Packs *************************
	@SuppressWarnings("unchecked")
	private void buscarPacks() {
		try {

			tablePacks.removeAllItems();
			conexion = new Conexion();
			String where = "";
			if ( txPack.getValue()!=null ) {
				where = " where upper(packname) like upper('%" + txPack.getValue() + "%')" ;
			}
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.Packs "
                		+ where )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tablePacks.addItem();
						Item row1 = tablePacks.getItem(newItemId);
						
						//------ BOTON MODIFICAR
						Button btModificar = new Button("Update");
						btModificar.setId(newItemId.toString());
						btModificar.setData(newItemId);
						//btModificar.setIcon(FontAwesome.SEARCH);
						btModificar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub

								Object data =  event.getButton().getData();
								tablePacks.select(data);
								Item itemClickEvent = tablePacks.getItem(data);
								venEditarPacks.init("UPDATE",itemClickEvent );
								UI.getCurrent().addWindow(venEditarPacks);

								
		
							}
						});
						
						//------ BOTON BORRAR
						Button btBorrar = new Button("Delete");
						btBorrar.setId(newItemId.toString());
						btBorrar.setData(newItemId);
						btBorrar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tablePacks.select(data);
								Item itemClickEvent = tablePacks.getItem(data);
								
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
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Objects by Pack", "Are you sure delete objects of this pack ?",
						    	        "Yes", "No", new ConfirmDialog.Listener() {

						    	            public void onClose(ConfirmDialog dialog) {
						    	                if (dialog.isConfirmed()) {

						    	                	// Borramos el registro
						    	                	try {

						    	    						conexion = new Conexion();
						    	    						Connection con = conexion.getConnection();
						    	    						Statement statement = con.createStatement();
						    	    						
						    	    						Object rowId = tablePacks.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tablePacks.getContainerProperty(rowId,"IdPack").getValue();
						    	    						
						    	    						String cadena = "DELETE ObjectbyPacks WHERE idpack =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tablePacks.removeItem(rowId);

						    	        					new Notification("Process OK",
						    	        							"Objects by Pack deleted",
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
						});						
						
						
						row1.getItemProperty("IdPack").setValue(resultSet.getInt("IdPack"));
						row1.getItemProperty("PackName").setValue(resultSet.getString("PackName"));
						
						row1.getItemProperty("TypePack").setValue(resultSet.getInt("TypePack"));
						row1.getItemProperty("Enabled").setValue(resultSet.getInt("Enabled"));
						if ( resultSet.getInt("TypePack") == 1) {
							row1.getItemProperty("DesckTypePack").setValue("App Pack");
						}
						else if ( resultSet.getInt("TypePack") == 2) {
							row1.getItemProperty("DesckTypePack").setValue("Others");
						}
						else if ( resultSet.getInt("TypePack") == 3) {
							row1.getItemProperty("DesckTypePack").setValue("Welcome Pack");
						}						
						
						if ( resultSet.getInt("Enabled") == 1) {
							row1.getItemProperty("DescEnabled").setValue("True");
						}
						else if ( resultSet.getInt("Enabled") == 0) {
							row1.getItemProperty("DescEnabled").setValue("False");
						}						
						
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tablePacks.setFooterVisible(true);
						tablePacks.setVisible(true);
						tablePacks.setSelectable(true);
						tablePacks.setImmediate(true);
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

	//	************************ BUSCAR Objects *************************
	@SuppressWarnings("unchecked")
	public void buscaObjectsByPacks(int id) {
		try {

			tableObjects.removeAllItems();
			conexion = new Conexion();
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		"SELECT  obp.idpack, obp.idobject, o.objectname" + 
                		" from dbo.ObjectbyPacks obp, dbo.Objects o" + 
                		" where obp.idobject = o.idobject"
                		+ " and obp.idpack = " + id + " order by o.objectname ")) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableObjects.addItem();
						Item row1 = tableObjects.getItem(newItemId);
						
						//------ BOTON MODIFICAR
						Button btModificar = new Button("Update");
						btModificar.setId(newItemId.toString());
						btModificar.setData(newItemId);
						//btModificar.setIcon(FontAwesome.SEARCH);
						btModificar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub

								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
								Object rowId = tablePacks.getValue(); // get the selected rows id
								System.out.println("Valor " + tablePacks.getContainerProperty(rowId,"IdPack").getValue());								
								//venEditarObjects.init("UPDATE",itemClickEvent, tablePacks.getContainerProperty(rowId,"PackName").getValue().toString(),(Integer)tablePacks.getContainerProperty(rowId,"IdPack").getValue() );
								UI.getCurrent().addWindow(venEditarPacks);
		
							}
						});
						
						//------ BOTON BORRAR
						Button btBorrar = new Button("Delete");
						btBorrar.setId(newItemId.toString());
						btBorrar.setData(newItemId);
						btBorrar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
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
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Pack", "Are you sure delete all objects of this Pack ?",
						    	        "Yes", "No", new ConfirmDialog.Listener() {

						    	            public void onClose(ConfirmDialog dialog) {
						    	                if (dialog.isConfirmed()) {

						    	                	// Borramos el registro
						    	                	try {

						    	    						conexion = new Conexion();
						    	    						Connection con = conexion.getConnection();
						    	    						Statement statement = con.createStatement();
						    	    						
						    	    						Object rowId = tableObjects.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tableObjects.getContainerProperty(rowId,"IdObject").getValue();
						    	    						
						    	    						String cadena = "DELETE ObjectBYPacks WHERE IDpack =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tableObjects.removeItem(rowId);

						    	        					new Notification("Process OK",
						    	        							"Object deleted",
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
						});						
						
						//{"IdPack","PackName","TypePack"
						row1.getItemProperty("IdObject").setValue(resultSet.getInt("IdObject"));
						row1.getItemProperty("ObjectName").setValue(resultSet.getString("ObjectName"));
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableObjects.setFooterVisible(true);
						tableObjects.setVisible(true);
						tableObjects.setSelectable(true);
						tableObjects.setImmediate(true);
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

