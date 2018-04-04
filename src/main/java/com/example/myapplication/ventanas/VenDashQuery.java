package com.example.myapplication.ventanas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.jdbc.Conexion;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class VenDashQuery extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	
	public String estadoRegistro;
	public int idRegistro;
	public String tablesUpdate;
	public String columnsUpdate;
	public String whereUpdate;
	

	Item regpregunta;
	Integer idquestion;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;
	
	TabSheet tabs = new TabSheet();
	
	public Label lbQuestion = new Label();
	public TextField txQueryName = new TextField("Query Name");
	public ListSelect cbTablesFrom = new ListSelect("Source Tables");
	public ListSelect cbTablesTo = new ListSelect("Selected Tables");
	public ListSelect cbColumnsFrom = new ListSelect("Source Columns");
	public ListSelect cbColumnsTo = new ListSelect("Selected Columns");
	public ListSelect cbWhereTo = new ListSelect("Conditions");
	
	ComboBox cbAndOr = new ComboBox("And/Or");
	ComboBox cbFieldWhere = new ComboBox("Field");
	ComboBox cbOperator = new ComboBox("Operator");
	TextField txValor = new TextField("Value");
	
	Button btColumnsFromTo = new Button ( ">");
	Button btColumnsToFrom = new Button ( "<");
	Button btFromTo = new Button ( ">");
	Button btToFrom = new Button ( "<");
	Button btWhereFrom = new Button ( ">");
	Button btWhereTo = new Button ( "<");
	Button btWhereClear = new Button ("Clear");
	
	TextArea txQuery = new TextArea("Query");
	TextArea txResult = new TextArea("Result");

	
	GridLayout tabTablesLayout = new GridLayout(4,2);
	GridLayout tabColumnsLayout = new GridLayout(4,2);
	GridLayout tabWhereLayout = new GridLayout(6,2);
	GridLayout tabResultLayout = new GridLayout(4,2);
	
	public Button btAceptar = new Button();
	public Button btCancelar = new Button();
	public Button btDelete = new Button();


	public VenDashQuery() {
		

		

		// TODO Auto-generated constructor stub
		setCaption("Query Constructor");
		setModal(true);
		setWidth("90%");
		setHeight("90%");
		setClosable(true);
		setResizable(false);
		setStyleName("ventanamodal");
		
		VerticalLayout vl = new VerticalLayout();
		GridLayout hl = new GridLayout(4,5);
		hl.setMargin(true);
		vl.setMargin(true);
		vl.setWidth("100%");
		hl.setWidth("100%");

		item = new PropertysetItem();
		binder = new FieldGroup(item);
		binder.setBuffered(true);

		txQueryName.setMaxLength(40);
		txQueryName.setRequired(true);
		txQueryName.setWidth("100%");
		txQueryName.setValidationVisible(true);
		txQueryName.setNullRepresentation("");
		txQueryName.setRequiredError("The field Answer is required");
		hl.addComponent(txQueryName,0,0);
		
		// TAB 1 - TABLES 
		
        IndexedContainer containerTablas = new IndexedContainer();
        containerTablas.addContainerProperty("tabla", String.class, null);
        cbTablesTo.setContainerDataSource(containerTablas);
        
		cbTablesFrom.setNullSelectionAllowed(false);
		cbTablesFrom.setRequired(true);
		cbTablesFrom.setWidth("100%");
		cbTablesFrom.setRows(20);
		
		cbTablesTo.setNullSelectionAllowed(false);
		cbTablesTo.setRequired(true);
		cbTablesTo.setWidth("100%");
		cbTablesTo.setRows(20);	
		
		btFromTo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//System.out.println("Pulsamos btfrom to");
				//cbTablesTo.addItem(cbTablesFrom.getValue());
				//cbTablesTo.setItemCaption(cbTablesFrom.getValue(),cbTablesFrom.getItemCaption(cbTablesFrom.getValue()));
				//cbTablesFrom.removeItem(cbTablesFrom.getValue());
				if (cbTablesFrom.getValue()!=null) {
					Item itemTablas = containerTablas.addItem(cbTablesFrom.getValue());
			        itemTablas.getItemProperty("tabla").setValue(cbTablesFrom.getValue().toString());
					cbTablesTo.setItemCaption(cbTablesFrom.getValue(),cbTablesFrom.getItemCaption(cbTablesFrom.getValue()));
					cbTablesFrom.removeItem(cbTablesFrom.getValue());
				}
				
			}
		});

		btToFrom.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				cbTablesFrom.addItem(cbTablesTo.getValue());
				cbTablesFrom.setItemCaption(cbTablesTo.getValue(),cbTablesTo.getItemCaption(cbTablesTo.getValue()));
				cbTablesTo.removeItem(cbTablesTo.getValue());
			}
		});
		
		tabTablesLayout.addComponent(cbTablesFrom,0,0);
		tabTablesLayout.addComponent(cbTablesTo,2,0);
		
		VerticalLayout vlBotonera = new VerticalLayout();
		vlBotonera.addComponent(btFromTo);
		btFromTo.setWidth("100%");
		btToFrom.setWidth("100%");
		vlBotonera.setSpacing(true);
		vlBotonera.setMargin(true);
		vlBotonera.addComponent(btToFrom);
		
		
		
		tabTablesLayout.setMargin(true);
		tabTablesLayout.setSpacing(true);
		tabTablesLayout.addComponent(vlBotonera,1,0);
		tabTablesLayout.setComponentAlignment(vlBotonera, Alignment.MIDDLE_CENTER);
		tabTablesLayout.setColumnExpandRatio(0, 45);
		tabTablesLayout.setColumnExpandRatio(1, 10);
		tabTablesLayout.setColumnExpandRatio(2, 45);

		//hl.addComponent(tabTablesLayout, 0,1,2,1);
		
		// TAB 2 - COLUMNS
		
        IndexedContainer containerColumnas = new IndexedContainer();
        containerColumnas.addContainerProperty("columna", String.class, null);
        cbColumnsTo.setContainerDataSource(containerColumnas);
        
        
		tabColumnsLayout.setWidth("100%");
		cbColumnsFrom.setNullSelectionAllowed(false);
		
		cbColumnsFrom.setWidth("100%");
		cbColumnsFrom.setRows(20);
		cbColumnsTo.setNullSelectionAllowed(false);
		cbColumnsTo.setWidth("100%");
		cbColumnsTo.setRows(20);	
		
		btColumnsFromTo.addClickListener(new ClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if ( cbColumnsFrom.getValue()!=null) {
					Item itemColumnas = containerColumnas.addItem(cbColumnsFrom.getValue());
			        itemColumnas.getItemProperty("columna").setValue(cbColumnsFrom.getValue().toString());
					cbColumnsTo.setItemCaption(cbColumnsFrom.getValue(),cbColumnsFrom.getItemCaption(cbColumnsFrom.getValue()));
					cbColumnsFrom.removeItem(cbColumnsFrom.getValue());
				}
			}
		});

		btColumnsToFrom.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				cbColumnsFrom.addItem(cbColumnsTo.getValue());
				cbColumnsFrom.setItemCaption(cbColumnsTo.getValue(),cbColumnsTo.getItemCaption(cbColumnsTo.getValue()));
				cbColumnsTo.removeItem(cbColumnsTo.getValue());
				
		
		        
			}
		});
		
		tabColumnsLayout.addComponent(cbColumnsFrom,0,0);
		tabColumnsLayout.addComponent(cbColumnsTo,2,0);
		
		VerticalLayout vlBotoneraColumns = new VerticalLayout();
		vlBotoneraColumns.addComponent(btColumnsFromTo);
		btColumnsFromTo.setWidth("100%");
		btColumnsToFrom.setWidth("100%");
		vlBotoneraColumns.setSpacing(true);
		vlBotoneraColumns.setMargin(true);
		vlBotoneraColumns.addComponent(btColumnsToFrom);
		
		
		
		tabColumnsLayout.setMargin(true);
		tabColumnsLayout.setSpacing(true);
		tabColumnsLayout.addComponent(vlBotoneraColumns,1,0);
		tabColumnsLayout.setComponentAlignment(vlBotoneraColumns, Alignment.MIDDLE_CENTER);
		tabColumnsLayout.setColumnExpandRatio(0, 45);
		tabColumnsLayout.setColumnExpandRatio(1, 10);
		tabColumnsLayout.setColumnExpandRatio(2, 45);
		
		// TAB 3 - WHERE CONDICIONES
		
        IndexedContainer containerWhere = new IndexedContainer();
        containerWhere.addContainerProperty("where", String.class, null);
        cbWhereTo.setContainerDataSource(containerWhere);
        
		
		cbAndOr.removeAllItems();
		cbAndOr.addItem("AND");
		cbAndOr.addItem("OR");
		
		cbOperator.removeAllItems();
		cbOperator.addItem("=");
		cbOperator.addItem(">");
		cbOperator.addItem("<");
		cbOperator.addItem("<>");
		cbOperator.addItem("IN");
		cbOperator.addItem("NOT IN");
		
		cbAndOr.setRequired(true);
		cbFieldWhere.setRequired(true);
		cbOperator.setRequired(true);
		txValor.setRequired(true);
		txValor.setNullRepresentation("");
		cbAndOr.setWidth("80px");
		VerticalLayout vlAndOr = new VerticalLayout();
		vlAndOr.setWidth("90px");
		vlAndOr.addComponent(cbAndOr);
		vlAndOr.addComponent(btWhereClear);
		vlAndOr.setMargin(false);
		vlAndOr.setSpacing(true);

		btWhereClear.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		tabWhereLayout.addComponent(vlAndOr,0,0);
		
		tabWhereLayout.addComponent(cbFieldWhere,1,0);
		cbOperator.setWidth("110px");		
		tabWhereLayout.addComponent(cbOperator,2,0);
		tabWhereLayout.addComponent(txValor,3,0);
		VerticalLayout vlBotoneraWhere = new VerticalLayout();
		vlBotoneraWhere.addComponent(btWhereFrom);
		vlBotoneraWhere.addComponent(btWhereTo);
		vlBotoneraWhere.setWidth("100%");
		vlBotoneraWhere.setSpacing(true);
		vlBotoneraWhere.setMargin(true);		
		tabWhereLayout.addComponent(vlBotoneraWhere,4,0);
		tabWhereLayout.setComponentAlignment(vlBotoneraWhere, Alignment.MIDDLE_CENTER);		
		tabWhereLayout.addComponent(cbWhereTo,5,0);
		
		cbWhereTo.setNullSelectionAllowed(false);
		cbWhereTo.setWidth("100%");
		cbWhereTo.setRows(20);
		
		tabWhereLayout.setWidth("100%");
		tabWhereLayout.setMargin(true);
		tabWhereLayout.setSpacing(false);
		tabWhereLayout.setColumnExpandRatio(0, 5);
		tabWhereLayout.setColumnExpandRatio(1, 5);
		tabWhereLayout.setColumnExpandRatio(2, 5);
		tabWhereLayout.setColumnExpandRatio(3, 5);
		tabWhereLayout.setColumnExpandRatio(4, 10);
		tabWhereLayout.setColumnExpandRatio(5, 60);
		
		btWhereTo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("cbWhereTo: " + cbWhereTo.getValue());
				
				String[] partes = cbWhereTo.getValue().toString().split("\\|");
				
				System.out.println("Partes0: " + partes[0]);
				System.out.println("Partes0: " + partes[1]);
				System.out.println("Partes0: " + partes[2]);
				System.out.println("Partes0: " + partes[3]);
				System.out.println("Partes0: " + partes[4]);
				cbAndOr.setValue(partes[0]);
				cbFieldWhere.setValue(partes[1] + "|" + partes[2]);
				cbOperator.setValue(partes[3]);
				txValor.setValue(partes[4]);
				
				cbWhereTo.removeItem(cbWhereTo.getValue());

				
			}
		});
		
		btWhereClear.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				cbAndOr.setValue(null);
				cbFieldWhere.setValue(null);
				cbOperator.setValue(null);
				txValor.setValue(null);
			}
		});
		
		btWhereFrom.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if ( cbAndOr.getValue()==null 
					|| cbFieldWhere.getValue()==null
					|| cbOperator.getValue()==null
					|| txValor.getValue()==null
					|| txValor.getValue().length()==0
					)  {
					
					new Notification("Error",
							"Please, fill required fields",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
				}
				else {
					// Pasamos los datos a la combo
					String condicion = null;
					String columna = cbFieldWhere.getValue().toString().substring(cbFieldWhere.getValue().toString().indexOf("|")+1);
					condicion = cbAndOr.getValue() + " " + columna + " " + cbOperator.getValue() + " " + txValor.getValue();
					
					String idcondicion = null;
					idcondicion = cbAndOr.getValue() + "|" 
								+ cbFieldWhere.getValue() + "|" 
								+ cbFieldWhere.getValue().toString().substring(1, cbFieldWhere.getValue().toString().indexOf("|")) 
								+ cbOperator.getValue() + "|"
								+ txValor.getValue();
					System.out.println("Pasasmos: " + idcondicion);
					Item itemWhere = containerWhere.addItem(idcondicion);
			        itemWhere.getItemProperty("where").setValue(idcondicion);

					cbWhereTo.addItem(idcondicion);
					cbWhereTo.setItemCaption(idcondicion, condicion);
				}
			}
		});
						

		// TAB 4 - CONSULTA FINAL
		
		txQuery.setWidth("100%");
		txQuery.setRows(15);
		
		txResult.setWidth("100%");
		txResult.setRows(15);
		
		tabResultLayout.setMargin(true);
		tabResultLayout.setSpacing(true);
		tabResultLayout.addComponent(txQuery,0,0,1,1);
		tabResultLayout.setComponentAlignment(txQuery, Alignment.MIDDLE_CENTER);
		tabResultLayout.addComponent(txResult,2,0,3,1);
		tabResultLayout.setComponentAlignment(txResult, Alignment.MIDDLE_CENTER);
		tabResultLayout.setWidth("100%");

		
		// TABS

		tabTablesLayout.setWidth("100%");
		tabs.setWidth("100%");
		tabs.addTab(tabTablesLayout, "Tables");
		tabs.addTab(tabColumnsLayout, "Columns");
		tabs.addTab(tabWhereLayout, "Conditions");
		tabs.addTab(tabResultLayout, "Result");
		
	    
	    vl.addComponent(hl);
	    vl.addComponent(tabs);
	    btAceptar.setCaption("Apply");
	    btCancelar.setCaption("Cancel");
	    btDelete.setCaption("Delete");
	    btAceptar.setId("BTACEPTAR");
	    btCancelar.setId("BTCANCELAR");
	    btDelete.setId("BTDELETE");

	    btAceptar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	    //btCancelar.setStyleName(ValoTheme.BUT);
	    btDelete.setStyleName(ValoTheme.BUTTON_DANGER);

		hl.addComponent(btAceptar,1,0);
		hl.addComponent(btCancelar,2,0);
		hl.addComponent(btDelete,3,0);
	    hl.setComponentAlignment(btAceptar, Alignment.BOTTOM_CENTER);
	    hl.setComponentAlignment(btCancelar, Alignment.BOTTOM_CENTER);
	    hl.setComponentAlignment(btDelete, Alignment.BOTTOM_RIGHT);		
	    

	    btDelete.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
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
		    	ConfirmDialog.show(UI.getCurrent(), "DELETE", "Are you sure to delete this query ? ",
		    	        "Yes", "No", new ConfirmDialog.Listener() {

		    	            public void onClose(ConfirmDialog dialog) {
		    	                if (dialog.isConfirmed()) {
		    	                    // Confirmed to continue
		    	                	//VaadinSession.getCurrent().getSession().invalidate();
		    	                	
		    	                	// Vaciamos las variables de session
		    	                	// TODO Auto-generated method stub
		    	    				conexion = new Conexion();
		    	    				Connection con = conexion.getConnection();
		    	    				String cadena = "";
		    	    		        try (Statement statement = con.createStatement(); ) {
		    	    		        
		    	    		        	
		    	    		        	
		    	    		        		
		    	    		        	cadena = "DELETE DASHQUERIES WHERE IDQUERY = " + idRegistro;
		    	    		        	
		    	    		        	statement.executeUpdate(cadena);
		    	    			        con.close();
		    	    			        statement.close();
		    	    			        
		    	    					new Notification("Process ok",
		    	    						"Query has been deleted",
		    	    						Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());
		    	    					btCancelar.click();
		    	    					
		    	    			     
		    	    				} catch (SQLException e) {
		    	    					// TODO Auto-generated catch block
		    	    					new Notification("Errors detected",
		    	    							"Error ocurred when delete register",
		    	    							Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());	
		    	    						
		    	    					e.printStackTrace();
		    	    				}
		    	                } else {
		    	                    // User did not confirm
		    	                    
		    	                }
		    	            }
		    	        });


			    
			}
		});
	    
	    
		setContent(vl);

		// ****************************************************************
		// S E L E C C I Ó N   D E   P E S T A Ñ A S
		// C A L C U L O   D E   L A   Q U E R Y
		// ****************************************************************
		
		tabs.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {

				
				String select = "SELECT COUNT(1) MANY";
				String from = "FROM ";
				String where = "WHERE 0 = 0";
				String join = "";
				String groupby =  "GROUP BY ";
				
				// Recalculamos la query
				if ( tabs.getTab(tabs.getSelectedTab()).getCaption().equals("Result")  ) {
					 System.out.println("Bucle de Tabs");
					 // 1. RECORREMOS LAS TABLAS

					 // COLUMNAS
					 int cont = 0;
					 for (Iterator i = cbColumnsTo.getItemIds().iterator(); i.hasNext();) {
						 	
							Object iid = (Object) i.next();
							
							System.out.println("seleccioamos: "+ cbColumnsTo.getItemCaption(iid));
							System.out.println("propiedades:" + cbColumnsTo.getItem(iid).getItemProperty("columna").getValue());
							System.out.println(i);
							
							select += "," + cbColumnsTo.getItemCaption(iid).substring(0, cbColumnsTo.getItemCaption(iid).indexOf(" -"));
							if ( cont >0  ) groupby += ",";
							groupby += cbColumnsTo.getItemCaption(iid).substring(0, cbColumnsTo.getItemCaption(iid).indexOf(" -"));
							cont++;

						}
					 // TABLAS
					 cont = 0;
					 String joinTables = "[]";
					 for (Iterator i = cbTablesTo.getItemIds().iterator(); i.hasNext();) {
						 	
							Object iid = (Object) i.next();
							
							System.out.println("seleccioamos: "+ cbTablesTo.getItemCaption(iid));
							System.out.println("propiedades:" + cbTablesTo.getItem(iid).getItemProperty("tabla").getValue());
							System.out.println(i);
							
							joinTables += "," + cbTablesTo.getItem(iid).getItemProperty("tabla").getValue().toString()  ; 
							
							
							if ( cont > 0) from += ",";
							from += cbTablesTo.getItemCaption(iid).substring(0, cbTablesTo.getItemCaption(iid).indexOf(" -"));
							cont ++;
							
							// JOIN TABLES
							// Para cada tabla seleccionada tenemos que mirar que no estén 

 					 }
					 joinTables = joinTables.replace("[],", "");
					 
					 // JOIN TABLES 
					 // Volvemos a recorrer la lista y buscamos las joins
					 
					 for (Iterator i = cbTablesTo.getItemIds().iterator(); i.hasNext();) {

						 	Object iid = (Object) i.next();
						 	conexion = new Conexion();
							Connection con = conexion.getConnection();
							// Buscamos las joins con las tablas seleccionadas
							System.out.println("Jointables: " + joinTables);
					        try (Statement statement = con.createStatement();
					            ResultSet resultSet = statement.executeQuery("SELECT * from DASHJOINS  "
					            		+ "WHERE IDTABLEFROM = " + cbTablesTo.getItem(iid).getItemProperty("tabla").getValue().toString()
					            		+ "  AND IDTABLETO IN ( " + joinTables + " ) "
					            )
					        ) {
					            while (resultSet.next())
					                {
					            		System.out.println("Encontrado");
					            		join += resultSet.getString("JOINCONDITION");
						            	//cbTablesFrom.addItem(new BigDecimal(resultSet.getInt("IDTABLE")));
										//cbTablesFrom.setItemCaption(new BigDecimal(resultSet.getInt("IDTABLE")),resultSet.getString("TABLENAME") + " - " + resultSet.getString("DESCTABLE"));

					                }
						        con.close();
						        statement.close();
						     
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        
					 }					 
					 
					 // WHERE
					 cont = 0;
					 for (Iterator i = cbWhereTo.getItemIds().iterator(); i.hasNext();) {
						 	
							Object iid = (Object) i.next();
							//joinTables += "," + cbTablesTo.getItem(iid).getItemProperty("where").getValue().toString()  ; 
							where += " " + cbWhereTo.getItemCaption(iid).toString();
							
						}
					 txQuery.setValue( select + "\n" + from + "\n" + where + "\n" + join + "\n" + groupby);
					 
					 // EJECUTAR LA CONSULTA
					 txResult.setValue("");
					 conexion = new Conexion();
					 Connection con = conexion.getConnection();
			         try (Statement statement = con.createStatement();
			            ResultSet resultSet = statement.executeQuery(txQuery.getValue())) {
			            while (resultSet.next())
			                {
			            	System.out.println("Encontrada coluna");
			            	txResult.setValue(txResult.getValue() + "Count: " + resultSet.getString(1) + " Value:" + resultSet.getString(2));
			            	txResult.setValue(txResult.getValue() + "\n");
			                }
				        con.close();
				        statement.close();
				     
					 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
				}
				
				
				
				
				
			}
		});
		
		
		// VALIDACIONES
		
		item.addItemProperty("txQueryName", new ObjectProperty<String>(null, String.class));
		
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);	
		binder.bindMemberFields(this);	
		

		
		btAceptar.setCaption("Apply");

		// GUARDAMOS LA QUERY
		btAceptar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				System.out.println("Entramos");
				if ( txQueryName.getValue()==null || txQueryName.getValue().length()==0 ) 
				{ 
					System.out.println("Notificacion de campo query requerido");
					new Notification("Errors detected",
						"Please, Query Name is required",
						Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());	
					return;
				}

				// TODO Auto-generated method stub
				conexion = new Conexion();
				Connection con = conexion.getConnection();
				String tablas = "";
				String columnas = "";
				String condiciones = "";
				String cadena = "";
		        try (Statement statement = con.createStatement(); ) {
		        	// Buscamos tablas seleccionadas
		        	for (Iterator i = cbTablesTo.getItemIds().iterator(); i.hasNext();) {
						Object iid = (Object) i.next();
						tablas +=  cbTablesTo.getItem(iid).getItemProperty("tabla").getValue().toString() + "|" ; 
					 }
		        	
		        	// Buscamos columnas seleccionadas
		        	for (Iterator i = cbColumnsTo.getItemIds().iterator(); i.hasNext();) {
						Object iid = (Object) i.next();
						columnas +=  cbColumnsTo.getItem(iid).getItemProperty("columna").getValue().toString() + "|" ; 
					 }		        	
		        	
		        	// Buscamos condiciones seleccionadas
		        	for (Iterator i = cbWhereTo.getItemIds().iterator(); i.hasNext();) {
						Object iid = (Object) i.next();
						condiciones +=  cbWhereTo.getItem(iid).getItemProperty("where").getValue().toString() + "#" ; 
					 }		        	
		        	
		        	
		        	if (  estadoRegistro.equals("NEW") ) {
		        		
		        		cadena = "INSERT INTO DASHQUERIES ( DESCRIPTION, TABLES, COLUMNS, CONDITIONS ) "
		        				+ "VALUES ( '" + txQueryName.getValue().toString() + "'"
		        				+ ",'" + tablas + "'"
		        				+ ",'" + columnas + "'"
		        				+ ",'" + condiciones.replace("'", "''") + "')"		        				
		        				;
		        	}
		        	else if (  estadoRegistro.equals("UPDATE") ) {
		        		
		        		cadena = "UPDATE DASHQUERIES SET DESCRIPTION = '" + txQueryName.getValue().toString() + "'" 
		        				+ ", TABLES = '" + tablas + "'"
		        				+ ", COLUMNS = '" + columnas + "'"
		        				+ ", CONDITIONS = '" + condiciones.replace("'", "''") + "' "
		        				+ "WHERE IDQUERY = " + idRegistro;
		        		   
		        			
		        	}
		        	System.out.println( "Cadena: " + cadena );
		        	statement.executeUpdate(cadena);
			        con.close();
			        statement.close();
			        
					new Notification("Query ok",
						"Query has been saved",
						Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());
					btCancelar.click();
					
			     
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					new Notification("Errors detected",
							"Please verify syntax conditions",
							Notification.Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());	
						
					e.printStackTrace();
				}
				
				
			}
		});
		
		btCancelar.addClickListener(new ClickListener() {
			
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

	
	public void init() {

		
		System.out.println("*********************++ INIT ***************************");
		
		btDelete.setVisible(true);
	    if ( estadoRegistro.equals("NEW")  ) {
	     btDelete.setVisible(false);	
	    }

		// Vaciamos todos los datos
	
		cbTablesTo.removeAllItems();
		cbColumnsTo.removeAllItems();
		cbWhereTo.removeAllItems();
		txQuery.setValue("");
		txResult.setValue("");
		
		// Rellenamos las listas
		
		cbTablesFrom.removeAllItems();
		conexion = new Conexion();
		Connection con = conexion.getConnection();
	    try (Statement statement = con.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * from dashtables ")) {
	        while (resultSet.next())
	            {
	        		

	            	cbTablesFrom.addItem(new BigDecimal(resultSet.getInt("IDTABLE")));
					cbTablesFrom.setItemCaption(new BigDecimal(resultSet.getInt("IDTABLE")),resultSet.getString("TABLENAME") + " - " + resultSet.getString("DESCTABLE"));
					
	        	    System.out.println("Añadimos: " + resultSet.getInt("IDTABLE") + "  tableupdate: " + tablesUpdate );
	        	    if ( estadoRegistro.equals("UPDATE")) {
	        	    	
	        	    	if ( ("|" + tablesUpdate).contains("|" + resultSet.getInt("IDTABLE") + "|")) {
	        	    		System.out.println("Se encuentrado");
	        	    		cbTablesFrom.select(new BigDecimal(resultSet.getInt("IDTABLE")));
	        	    		btFromTo.click();
	        	    	}
	        	    }
	
	            }
	        con.close();
	        statement.close();
	     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	
	    
	    cbColumnsFrom.removeAllItems();
		conexion = new Conexion();
		con = conexion.getConnection();
	    try (Statement statement = con.createStatement();
	        ResultSet resultSet = statement.executeQuery("select idcolumn, idtable, columnname, DescColumn, " +
	        											 "( select tablename from dashtables where IdTable = dashcolumns.IdTable) tablename " + 
	        											 "from DashColumns ")) {
	        while (resultSet.next())
	            {
	            	cbColumnsFrom.addItem(new BigDecimal(resultSet.getInt("IDCOLUMN")));
					cbColumnsFrom.setItemCaption(new BigDecimal(resultSet.getInt("IDCOLUMN")),resultSet.getString("TABLENAME") + "." + resultSet.getString("COLUMNNAME") + " - " + resultSet.getString("DESCCOLUMN"));
	
	        	    System.out.println("Añadimos Columnas : " + resultSet.getInt("IDCOLUMN") + "  tableupdate: " + tablesUpdate );
	        	    if ( estadoRegistro.equals("UPDATE")) {
	        	    	
	        	    	if ( ("|" + columnsUpdate).contains("|" + resultSet.getInt("IDCOLUMN") + "|")) {
	        	    		System.out.println("Se encuentrado COLUMNA");
	        	    		cbColumnsFrom.select(new BigDecimal(resultSet.getInt("IDCOLUMN")));
	        	    		btColumnsFromTo.click();
	        	    	}
	        	    }
	            }
	        con.close();
	        statement.close();
	     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // Rellenamos las columnas de las condiciones
	    
		cbFieldWhere.removeAllItems();
		conexion = new Conexion();
		con = conexion.getConnection();
	    try (Statement statement = con.createStatement();
	        ResultSet resultSet = statement.executeQuery("select idcolumn, idtable, columnname, DescColumn, " +
	        											 "( select tablename from dashtables where IdTable = dashcolumns.IdTable) tablename " + 
	        											 "from DashColumns ")) {
	        while (resultSet.next())
	            {
	        	System.out.println("Encontrada coluna");
	        	cbFieldWhere.addItem(resultSet.getInt("IDCOLUMN") + "|" + resultSet.getString("TABLENAME") + "." + resultSet.getString("COLUMNNAME"));
	        	cbFieldWhere.setItemCaption(resultSet.getInt("IDCOLUMN") + "|" + resultSet.getString("TABLENAME") + "." + resultSet.getString("COLUMNNAME")
	        								,resultSet.getString("COLUMNNAME") + " (" + resultSet.getString("TABLENAME") + " ) - " + resultSet.getString("DESCCOLUMN"));
	        	
        	    

	
	            }
	        con.close();
	        statement.close();
	     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // En el caso de la modificacion UPDATE ponemos las where
	    if ( estadoRegistro.equals("UPDATE")) {
	    	
	    	// Spliteamos las where
		    String[] splitWhere = whereUpdate.split( "#" );
	
		    for ( String s : splitWhere ) 
		    {
		        // Spliteamos los campos de cada where
		    	String[] splitCampos = s.split( "\\|" );
		    	cbAndOr.setValue(splitCampos[0]);
				cbFieldWhere.setValue(splitCampos[1] + "|" + splitCampos[2]);
		    	cbOperator.setValue(splitCampos[3]);
		    	txValor.setValue(splitCampos[4]);
		    	
		    	btWhereFrom.click();
		    	btWhereClear.click();
		    }

	    }
	    

	}
	

	
	/*@Override
	public void close() {
		// TODO Auto-generated method stub
		//super.close();
		//System.out.println("Cerramos la ventana de anular");
		UI.getCurrent().removeWindow(this);
		
	}*/

}