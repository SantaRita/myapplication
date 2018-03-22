package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarTerms;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaTerms extends Panel implements View {
	
	VenEditarTerms venEditarTerms = new VenEditarTerms(this);
	private static Conexion conexion;
	Table tableTerms = new Table();
	private FieldGroup binder;
	TextField mailTerm;
	public Button buscar = new Button("Search");
	Button limpiar = new Button("Clear");
	VerticalLayout layout = new VerticalLayout();
	Button nuevo = new Button("Add");
	

	public PantallaTerms() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Terms & Policy");
		
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("mail", new ObjectProperty<String>(""));
		
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
				
				buscarTerms();
			}
		});
		
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				venEditarTerms.init("NEW",null);
				UI.getCurrent().addWindow(venEditarTerms);
				
			}
		});
		
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		

		
		//mailTerm.focus();
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdTerm","Condiciones","Idtipo","DescTipo","Activo","DescStatus","Borrar","Modificar"};
		Object[] typesexp = {Integer.class, String.class, String.class, String.class, Integer.class, String.class,Button.class, Button.class };
		Object[] visibleColumnsexp = 
				new Object[]{"IdTerm","Condiciones","DescTipo","DescStatus","Borrar","Modificar"};

		Util.defineTable(tableTerms, columnsexp, typesexp, visibleColumnsexp,true);
		tableTerms.setColumnHeaders(new String[] {"Id","Text Conditions","Type", "Active","Delete","Update"});
		
		tableTerms.setColumnExpandRatio("Id", 8);
		tableTerms.setColumnExpandRatio("Condiciones", 60);
		tableTerms.setColumnExpandRatio("DescTipo", 11);		
		tableTerms.setColumnExpandRatio("DescStatus", 7);
		tableTerms.setColumnExpandRatio("Borrar", 8);		
		tableTerms.setColumnExpandRatio("Modificar", 8);

		
		/*tableTerms = new Table()  {
		    @Override
		    protected String formatPropertyValue(Object rowId,
		            Object colId, Property property) {
		        // Format by property type
		        if (property.getType() == Date.class) {
		        	
		        	if (property.getValue() == null) {
		        		return null;
		        	}
		            SimpleDateFormat df =
		                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		            return df.format((Date)property.getValue());
		        }

		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};*/
		
		/*tableTerms.setCellStyleGenerator(new Table.CellStyleGenerator() {                
	        @Override
	        public String getStyle(Table source, Object itemId, Object propertyId) {

	        	if(propertyId != null ) {
	                if ( propertyId.toString().equals("Incidencia")
	                		&& source.getItem(itemId).getItemProperty("Incidencia").getValue().equals("S")
	                	)  {
	                	return "bgred"; 
		 			}
	                if ( propertyId.toString().equals("Revisar")
	                		&& source.getItem(itemId).getItemProperty("Revisar").getValue().equals("S")
	                	)  {
	                	return "bgred"; 
		 			}
	                return "normal";
	            } else {
	                return null;
	            }
	        }
	      });*/		
		
		tableTerms.setPageLength(0);
		tableTerms.setWidth("100%");
		tableTerms.setHeight("100%");
		tableTerms.setVisible(true);
		gridCamposBusqueda.addComponent(tableTerms,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		
		setContent(layout);
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Terms");
		
	}

	//	************************ BUSCAR TERMANDCONDITIONS *************************
	@SuppressWarnings("unchecked")
	private void buscarTerms() {
		try {

			tableTerms.removeAllItems();
			conexion = new Conexion();
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.TermAndConditions ")) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableTerms.addItem();
						Item row1 = tableTerms.getItem(newItemId);
						
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
								tableTerms.select(data);
								Item itemClickEvent = tableTerms.getItem(data);
								Object rowId = tableTerms.getValue(); // get the selected rows id
								System.out.println("Valor " + tableTerms.getContainerProperty(rowId,"IdTerm").getValue());								
								venEditarTerms.init("UPDATE",itemClickEvent);
								UI.getCurrent().addWindow(venEditarTerms);
		
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
								tableTerms.select(data);
								Item itemClickEvent = tableTerms.getItem(data);
		
							}
						});						
						
						//"IdTerm","Condiciones","Idtipo","DescTipo","Activo","DescStatus","Borrar","Modificar"
						row1.getItemProperty("IdTerm").setValue(resultSet.getInt("IDTERM"));
						row1.getItemProperty("Condiciones").setValue(resultSet.getString("CONDICIONES"));
						row1.getItemProperty("Idtipo").setValue(resultSet.getString("Idtipo"));
						if ( resultSet.getString("Idtipo").equals("TC")) {
							row1.getItemProperty("DescTipo").setValue("Terms & Conditions");
						}
						else if ( resultSet.getString("Idtipo").equals("PO")) {
							row1.getItemProperty("DescTipo").setValue("Policy");
						}
						if ( resultSet.getInt("Activo") == 1) {
							row1.getItemProperty("DescStatus").setValue("Enabled");
						}
						else if ( resultSet.getInt("Activo") == 0) {
							row1.getItemProperty("DescStatus").setValue("Disabled");
						}							
						row1.getItemProperty("Activo").setValue(resultSet.getInt("ACTIVO"));
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableTerms.setFooterVisible(true);
						//tableTerms.setColumnFooter("Expediente", "Total: "+String.valueOf(tableTerms.size()));
						tableTerms.setVisible(true);
						tableTerms.setSelectable(true);
						tableTerms.setImmediate(true);
						//tableTerms.setPageLength((int)UI.getCurrent().getSession().getAttribute("resoluciony")/33);
                		
                    }
				tableTerms.setFooterVisible(true);
				tableTerms.setColumnFooter("IdTerm", "Records: "+String.valueOf(tableTerms.size()));
		        con.close();
		        statement.close();
		       }                   
	    }
	    catch (Exception e) {
	            e.printStackTrace();
	    }
	}
			
	
}

