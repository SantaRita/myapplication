package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarTips;
import com.vaadin.data.Item;
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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaTips extends Panel implements View {
	
	VenEditarTips venEditarTips = new VenEditarTips(this);
	private static Conexion conexion;
	Table tableTips = new Table();
	private FieldGroup binder;
	TextField mailTerm;
	public Button buscar = new Button("Search");
	Button limpiar = new Button("Clear");
	VerticalLayout layout = new VerticalLayout();
	Button nuevo = new Button("Add");
	

	public PantallaTips() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Tips & Policy");
		
		
		PropertysetItem item = new PropertysetItem();
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
	
		mailTerm = new TextField();
		mailTerm.setWidth("150px");
		
		GridLayout gridCamposBusqueda = new GridLayout(5,4);
		gridCamposBusqueda.setMargin(true);
		gridCamposBusqueda.setSpacing(true);
		gridCamposBusqueda.setWidth("100%");
		//gridCamposBusqueda.addComponent(new Label("Username: "),0,0);
		//gridCamposBusqueda.addComponent(mailTerm,1,0);
		//gridCamposBusqueda.setComponentAlignment(mailTerm, Alignment.MIDDLE_LEFT);
		
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

		
		buscar.setClickShortcut(KeyCode.ENTER);
		buscar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				// TODO Auto-generated method stub
				
				buscarTips();
			}
		});
		
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				venEditarTips.init("NEW",null);
				UI.getCurrent().addWindow(venEditarTips);
				
			}
		});
		
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		

		
		//mailTerm.focus();
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"TipId","Description","Borrar","Modificar"};
		Object[] typesexp = {Integer.class, String.class, Button.class, Button.class };
		Object[] visibleColumnsexp = 
				new Object[]{"TipId","Description","Borrar","Modificar"};

		Util.defineTable(tableTips, columnsexp, typesexp, visibleColumnsexp,true);
		tableTips.setColumnHeaders(new String[] {"Id","Description","Delete","Update"});
		
		tableTips.setColumnExpandRatio("Id", 18);
		tableTips.setColumnExpandRatio("Description", 50);
		tableTips.setColumnExpandRatio("Borrar", 8);		
		tableTips.setColumnExpandRatio("Modificar", 8);

	
		tableTips.setPageLength(0);
		tableTips.setWidth("100%");
		tableTips.setHeight("100%");
		tableTips.setVisible(true);
		gridCamposBusqueda.addComponent(tableTips,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		
		setContent(layout);
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Tips");
		
	}

	//	************************ BUSCAR TERMANDCONDITIONS *************************
	@SuppressWarnings("unchecked")
	private void buscarTips() {
		try {

			tableTips.removeAllItems();
			conexion = new Conexion();
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.Tips ")) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableTips.addItem();
						Item row1 = tableTips.getItem(newItemId);
						
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
								tableTips.select(data);
								Item itemClickEvent = tableTips.getItem(data);
								Object rowId = tableTips.getValue(); // get the selected rows id
								System.out.println("Valor " + tableTips.getContainerProperty(rowId,"TipId").getValue());								
								venEditarTips.init("UPDATE",itemClickEvent);
								UI.getCurrent().addWindow(venEditarTips);
		
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
								tableTips.select(data);
								Item itemClickEvent = tableTips.getItem(data);
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
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Tip", "Are you sure delete Tip?",
						    	        "Yes", "No", new ConfirmDialog.Listener() {

						    	            public void onClose(ConfirmDialog dialog) {
						    	                if (dialog.isConfirmed()) {

						    	                	// Borramos el registro
						    	                	try {

						    	    						conexion = new Conexion();
						    	    						Connection con = conexion.getConnection();
						    	    						Statement statement = con.createStatement();
						    	    						
						    	    						Object rowId = tableTips.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tableTips.getContainerProperty(rowId,"TipId").getValue();
						    	    						
						    	    						String cadena = "DELETE tips WHERE tipid =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tableTips.removeItem(rowId);

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
						
						//"TipId","Condiciones","Idtipo","DescTipo","Activo","DescStatus","Borrar","Modificar"
						row1.getItemProperty("TipId").setValue(resultSet.getInt("TipId"));
						row1.getItemProperty("Description").setValue(resultSet.getString("DESCRIPTION"));
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableTips.setFooterVisible(true);
						tableTips.setVisible(true);
						tableTips.setSelectable(true);
						tableTips.setImmediate(true);
                		
                    }
				tableTips.setFooterVisible(true);
				tableTips.setColumnFooter("TipId", "Records: "+String.valueOf(tableTips.size()));
		        con.close();
		        statement.close();
		       }                   
	    }
	    catch (Exception e) {
	            e.printStackTrace();
	    }
	}
			
	
}

