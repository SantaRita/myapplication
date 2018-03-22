package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarAnswers;
import com.example.myapplication.ventanas.VenEditarQuestions;
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

public class PantallaQuestions extends Panel implements View {
	
	VenEditarQuestions  venEditarQuestions = new VenEditarQuestions(this);
	VenEditarAnswers    venEditarAnswers = new VenEditarAnswers(this);
	private static Conexion conexion;
	public Table tableQuestions = new Table();
	public Table tableAnswers = new Table();
	private FieldGroup binder;
	TextField txPregunta;
	TextField txRespuesta;
	public Button buscar = new Button("Search");
	Button limpiar = new Button("Clear");
	Button nuevo = new Button("Add");
	Button nuevoAnswer = new Button("Add");
	VerticalLayout layout = new VerticalLayout();
	

	public PantallaQuestions() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Questions");
		
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("mail", new ObjectProperty<String>(""));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
	
		txPregunta = new TextField();
		txPregunta.setWidth("150px");
		
		GridLayout gridCamposBusqueda = new GridLayout(5,4);
		GridLayout gridCamposRespuestas = new GridLayout(5,4);
		gridCamposBusqueda.setMargin(true);
		gridCamposBusqueda.setSpacing(true);
		gridCamposBusqueda.setWidth("100%");
		
		txPregunta = new TextField();
		txPregunta.setWidth("150px");
		
		gridCamposBusqueda.addComponent(new Label("Question text: "),0,0);
		gridCamposBusqueda.addComponent(txPregunta,1,0);
		gridCamposBusqueda.setComponentAlignment(txPregunta, Alignment.MIDDLE_LEFT);		
		
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
				txPregunta.setValue("");
				tableAnswers.removeAllItems();
				tableQuestions.removeAllItems();
				
			}
		});
		
		buscar.setClickShortcut(KeyCode.ENTER);
		buscar.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				// TODO Auto-generated method stub
				
				buscarQuestions();
			}
		});
		
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdQuestion","QuestionDesc","IdTypeQuestion","DescType","Borrar","Modificar"};
		Object[] typesexp = {Integer.class, String.class, Integer.class, String.class, Button.class, Button.class };
		Object[] visibleColumnsexp = 
				new Object[]{"IdQuestion","QuestionDesc","DescType","Borrar","Modificar"};

		Util.defineTable(tableQuestions, columnsexp, typesexp, visibleColumnsexp,true);
		tableQuestions.setColumnHeaders(new String[] {"Id","Question text","Type", "Delete","Update"});
		tableQuestions.setColumnExpandRatio("IdQuestion", 8);
		tableQuestions.setColumnExpandRatio("QuestionDesc", 46);
		tableQuestions.setColumnExpandRatio("DescType", 30);
		tableQuestions.setColumnExpandRatio("Borrar", 8);		
		tableQuestions.setColumnExpandRatio("Modificar", 8);
		tableQuestions.setPageLength(0);
		tableQuestions.setWidth("100%");
		tableQuestions.setHeight("100%");
		tableQuestions.setVisible(true);
		tableQuestions.setPageLength(5);
		gridCamposBusqueda.addComponent(tableQuestions,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		gridCamposBusqueda.setHeight("50%");

		
		tableQuestions.addValueChangeListener(new ValueChangeListener() {
			
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
		
		
		
		
		// Table Answers
		

		
		gridCamposRespuestas.addComponent(new Label("Answers by Question "),0,0,1,0);
		gridCamposRespuestas.setMargin(true);
		gridCamposRespuestas.setSpacing(true);
		gridCamposRespuestas.setWidth("100%");
		
		gridCamposRespuestas.addComponent(nuevoAnswer,2,0);
		gridCamposRespuestas.setComponentAlignment(nuevoAnswer, Alignment.MIDDLE_CENTER);
		nuevoAnswer.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		String[] columnsexpanswer ={"IdAnswer","Answer","Borrar","Modificar"};
		Object[] typesexpanswer = {Integer.class, String.class, Button.class, Button.class };
		Object[] visibleColumnsexpanswer = 
				new Object[]{"IdAnswer","Answer","Borrar","Modificar"};

		Util.defineTable(tableAnswers, columnsexpanswer, typesexpanswer, visibleColumnsexpanswer,true);
		tableAnswers.setColumnHeaders(new String[] {"IdAnswer","Answer","Delete","Update"});
		tableAnswers.setColumnExpandRatio("IdAnswer", 8);
		tableAnswers.setColumnExpandRatio("Answer", 78);
		tableAnswers.setColumnExpandRatio("Borrar", 8);		
		tableAnswers.setColumnExpandRatio("Modificar", 8);
		tableAnswers.setPageLength(0);
		tableAnswers.setWidth("100%");
		tableAnswers.setHeight("100%");
		tableAnswers.setVisible(true);
		gridCamposRespuestas.addComponent(tableAnswers,0,1,4,1);
		gridCamposRespuestas.setVisible(false);
		layout.addComponent(gridCamposRespuestas);
		
		
		setContent(layout);
		
		
		tableQuestions.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("buscar answers");
				
				buscaAnswers((int) event.getItem().getItemProperty("IdQuestion").getValue());
				
			}
		});
		
		// Nueva PREGUNTA - QUESTIONS
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				venEditarQuestions.init("NEW",null);
				UI.getCurrent().addWindow(venEditarQuestions);
				
			}
		});
		
		// Nueva RESPUESTA - ANSWERS
		
		nuevoAnswer.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				Object rowId = tableQuestions.getValue(); // get the selected rows id
				System.out.println("Valor " + tableQuestions.getContainerProperty(rowId,"IdQuestion").getValue());
				venEditarAnswers.init("NEW",null, tableQuestions.getContainerProperty(rowId,"QuestionDesc").getValue().toString(), (Integer)tableQuestions.getContainerProperty(rowId,"IdQuestion").getValue());
				UI.getCurrent().addWindow(venEditarAnswers);
				
			}
		});		
		
		
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Entramos enter del Terms");
		
	}

	//	************************ BUSCAR QUESTIONS *************************
	@SuppressWarnings("unchecked")
	private void buscarQuestions() {
		try {

			tableQuestions.removeAllItems();
			conexion = new Conexion();
			String where = "";
			if ( txPregunta.getValue()!=null ) {
				where = " where upper(QUESTIONDESC) like upper('%" + txPregunta.getValue() + "%')" ;
			}
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.Questions "
                		+ where )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableQuestions.addItem();
						Item row1 = tableQuestions.getItem(newItemId);
						
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
								tableQuestions.select(data);
								Item itemClickEvent = tableQuestions.getItem(data);
								venEditarQuestions.init("UPDATE",itemClickEvent );
								UI.getCurrent().addWindow(venEditarQuestions);

								
		
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
								tableQuestions.select(data);
								Item itemClickEvent = tableQuestions.getItem(data);
								
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
						    	    						
						    	    						Object rowId = tableQuestions.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tableQuestions.getContainerProperty(rowId,"IdQuestion").getValue();
						    	    						
						    	    						String cadena = "DELETE QUESTIONS WHERE IDQUESTION =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tableQuestions.removeItem(rowId);

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
						});						
						
						//{"IdQuestion","QuestionDesc","IdTypeQuestion"
						row1.getItemProperty("IdQuestion").setValue(resultSet.getInt("IdQuestion"));
						row1.getItemProperty("QuestionDesc").setValue(resultSet.getString("QuestionDesc"));
						row1.getItemProperty("IdTypeQuestion").setValue(resultSet.getInt("IdTypeQuestion"));
						if ( resultSet.getInt("IdTypeQuestion") == 1) {
							row1.getItemProperty("DescType").setValue("Combo");
						}
						else if ( resultSet.getInt("IdTypeQuestion") == 2) {
							row1.getItemProperty("DescType").setValue("Switch");
						}
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableQuestions.setFooterVisible(true);
						tableQuestions.setVisible(true);
						tableQuestions.setSelectable(true);
						tableQuestions.setImmediate(true);
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

	//	************************ BUSCAR QUESTIONS *************************
	@SuppressWarnings("unchecked")
	public void buscaAnswers(int id) {
		try {

			tableAnswers.removeAllItems();
			conexion = new Conexion();
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.AnswerbyQuestions"
                		+ " where idquestion = " + id + " order by idanswer ")) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableAnswers.addItem();
						Item row1 = tableAnswers.getItem(newItemId);
						
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
								tableAnswers.select(data);
								Item itemClickEvent = tableAnswers.getItem(data);
								Object rowId = tableQuestions.getValue(); // get the selected rows id
								System.out.println("Valor " + tableQuestions.getContainerProperty(rowId,"IdQuestion").getValue());								
								venEditarAnswers.init("UPDATE",itemClickEvent, tableQuestions.getContainerProperty(rowId,"QuestionDesc").getValue().toString(),(Integer)tableQuestions.getContainerProperty(rowId,"IdQuestion").getValue() );
								UI.getCurrent().addWindow(venEditarAnswers);
		
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
								tableAnswers.select(data);
								Item itemClickEvent = tableAnswers.getItem(data);
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
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Answer", "Are you sure delete this Answer ?",
						    	        "Yes", "No", new ConfirmDialog.Listener() {

						    	            public void onClose(ConfirmDialog dialog) {
						    	                if (dialog.isConfirmed()) {

						    	                	// Borramos el registro
						    	                	try {

						    	    						conexion = new Conexion();
						    	    						Connection con = conexion.getConnection();
						    	    						Statement statement = con.createStatement();
						    	    						
						    	    						Object rowId = tableAnswers.getValue(); // get the selected rows id
						    	    						Integer id = (Integer)tableAnswers.getContainerProperty(rowId,"IdAnswer").getValue();
						    	    						
						    	    						String cadena = "DELETE ANSWERBYQUESTIONS WHERE IDANSWER =" + String.valueOf(id); 
						    	    						statement.executeUpdate(cadena);
						    	    						statement.close();
						    	    						con.close();
						    	    						
						    	    						tableAnswers.removeItem(rowId);

						    	        					new Notification("Process OK",
						    	        							"Answer deleted",
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
						
						//{"IdQuestion","QuestionDesc","IdTypeQuestion"
						row1.getItemProperty("IdAnswer").setValue(resultSet.getInt("IdAnswer"));
						row1.getItemProperty("Answer").setValue(resultSet.getString("Answer"));
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableAnswers.setFooterVisible(true);
						tableAnswers.setVisible(true);
						tableAnswers.setSelectable(true);
						tableAnswers.setImmediate(true);
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

