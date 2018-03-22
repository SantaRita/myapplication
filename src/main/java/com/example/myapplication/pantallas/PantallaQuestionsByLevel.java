package com.example.myapplication.pantallas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarQuestionsByLevel;
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
import com.vaadin.ui.ComboBox;
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

public class PantallaQuestionsByLevel extends Panel implements View {
	
	VenEditarQuestionsByLevel  VenEditarQuestionsByLevel = new VenEditarQuestionsByLevel(this);
	
	private static Conexion conexion;
	public Table tableQuestionsByLevel = new Table();
	public Table tableAnswers = new Table();
	private FieldGroup binder;
	public ComboBox cbLevel = new ComboBox();
	
	public Button buscar = new Button("Search");
	
	Button nuevo = new Button("Edit");
	VerticalLayout layout = new VerticalLayout();
	

	public PantallaQuestionsByLevel() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Questions Test By Level");
		
		
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
					buscarQuestionsByLevel();
			}
		});
		
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdQuestion","QuestionDesc","IdLevel","DescLevel"};
		Object[] typesexp = {Integer.class, String.class, Integer.class, String.class};
		Object[] visibleColumnsexp = 
				new Object[]{"DescLevel","QuestionDesc"};

		Util.defineTable(tableQuestionsByLevel, columnsexp, typesexp, visibleColumnsexp,true);
		tableQuestionsByLevel.setColumnHeaders(new String[] {"DescLevel","QuestionDesc"});
		tableQuestionsByLevel.setColumnExpandRatio("DescLevel", 10);
		tableQuestionsByLevel.setColumnExpandRatio("QuestionDesc", 90);

		tableQuestionsByLevel.setPageLength(0);
		tableQuestionsByLevel.setWidth("100%");
		tableQuestionsByLevel.setHeight("100%");
		tableQuestionsByLevel.setVisible(true);
		tableQuestionsByLevel.setPageLength(5);
		gridCamposBusqueda.addComponent(tableQuestionsByLevel,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		gridCamposBusqueda.setHeight("50%");

		
		tableQuestionsByLevel.addValueChangeListener(new ValueChangeListener() {
			
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
		
		
	
		// Nueva QUESTIONBYLEVEL
		
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
				
					VenEditarQuestionsByLevel.init("NEW");
					UI.getCurrent().addWindow(VenEditarQuestionsByLevel);
				
				}
				
			}
		});
		
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Terms");
		
	}

	//	************************ BUSCAR QUESTIONS BY LEVEL *************************
	@SuppressWarnings("unchecked")
	private void buscarQuestionsByLevel() {
		try {

			tableQuestionsByLevel.removeAllItems();
			conexion = new Conexion();
			String where = "";
			/*if ( txPregunta.getValue()!=null ) {
				where = " where upper(QUESTIONDESC) like upper('%" + txPregunta.getValue() + "%')" ;
			}*/
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		" select l.idlevel, qbl.idquestion, l.desclevel, q.questionDesc"
                		+" from levels l, questionbylevels qbl, questions q"
                		+" where l.idlevel = qbl.idlevel"
                		+" and q.idquestion = qbl.idquestion"
                		+ " and l.idlevel = " + cbLevel.getValue().toString()
                		+ where )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableQuestionsByLevel.addItem();
						Item row1 = tableQuestionsByLevel.getItem(newItemId);
						
						
						//------ BOTON BORRAR
						/*Button btBorrar = new Button("Delete");
						btBorrar.setId(newItemId.toString());
						btBorrar.setData(newItemId);
						btBorrar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableQuestionsByLevel.select(data);
								Item itemClickEvent = tableQuestionsByLevel.getItem(data);
								
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
						    	    						
						    	    						Object rowId = tableQuestionsByLevel.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tableQuestionsByLevel.getContainerProperty(rowId,"IdQuestion").getValue();
						    	    						
						    	    						String cadena = "DELETE QUESTIONS WHERE IDQUESTION =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tableQuestionsByLevel.removeItem(rowId);

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
						
						//"IdQuestion","QuestionDesc","IdLevel","DescLevel"
						row1.getItemProperty("IdQuestion").setValue(resultSet.getInt("IdQuestion"));
						row1.getItemProperty("QuestionDesc").setValue(resultSet.getString("QuestionDesc"));
						row1.getItemProperty("DescLevel").setValue(resultSet.getString("desclevel"));
						row1.getItemProperty("IdLevel").setValue(resultSet.getInt("idlevel"));
						

						//row1.getItemProperty("Borrar").setValue(btBorrar);
						//row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableQuestionsByLevel.setFooterVisible(true);
						tableQuestionsByLevel.setVisible(true);
						tableQuestionsByLevel.setSelectable(true);
						tableQuestionsByLevel.setImmediate(true);
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

