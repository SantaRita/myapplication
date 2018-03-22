package com.example.myapplication.ventanas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaQuestions;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
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

public class VenEditarAnswers extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	

	Item regpregunta;
	Integer idquestion;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;


	public Label lbQuestion = new Label();
	public TextField txAnswer = new TextField("AnswerText");
	


	public VenEditarAnswers( PantallaQuestions pantallaQuestions ) {

		
		// TODO Auto-generated constructor stub
		
		setModal(true);
		setWidth("90%");
		setClosable(true);
		setResizable(false);
		setStyleName("ventanamodal");
		
		VerticalLayout vl = new VerticalLayout();
		GridLayout hl = new GridLayout(3,1);
		hl.setMargin(true);
		vl.setMargin(true);
		vl.setWidth("100%");
		hl.setWidth("100%");


		item = new PropertysetItem();
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		
		

		txAnswer.setRequired(true);
		txAnswer.setWidth("100%");
		txAnswer.setValidationVisible(true);
		txAnswer.setNullRepresentation("");
		txAnswer.setRequiredError("The field Answer is required");
		hl.addComponent(txAnswer,0,0);
		

		
		hl.setColumnExpandRatio(0, 100);


		item.addItemProperty("txAnswer", new ObjectProperty<String>(null, String.class));
		
		
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
					
					String cadena;
					if ( acceso.equals("NEW" )) {
						cadena= "INSERT INTO ANSWERBYQUESTIONS ( IDQUESTION, ANSWER ) VALUES ("
							+ idquestion + ","								
							+ "'" + txAnswer.getValue().toString() + "'" 
							+ ")" ;
					}
					else {
						cadena= "UPDATE ANSWERBYQUESTIONS "
								+ "SET ANSWER = '" + txAnswer.getValue().toString() + "'"
								+ " WHERE idanswer = " + regpregunta.getItemProperty("IdAnswer").getValue().toString();
;
								 						
					}
					
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					statement.close();
					con.close();

					new Notification("Process OK",
							"Answer " + acceso,
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenEditarAnswers.this.close();
					Object data =  pantallaQuestions.tableQuestions.getValue();
					pantallaQuestions.tableQuestions.select(data);
					pantallaQuestions.buscaAnswers(idquestion);
					
					
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
	public void init( String tipoAcceso, Item item, String question, Integer pidquestion) {

		
		acceso = tipoAcceso;
		
		txAnswer.setValue(null);
		regpregunta = item;
		idquestion = pidquestion;
		
		lbQuestion.setValue(question);
		
		if ( tipoAcceso.equals("UPDATE")) {

			txAnswer.setValue(item.getItemProperty("Answer").getValue().toString());
		}
		this.setCaption(tipoAcceso + " Answer for: " + question);

	}
	

	
	/*@Override
	public void close() {
		// TODO Auto-generated method stub
		//super.close();
		//System.out.println("Cerramos la ventana de anular");
		UI.getCurrent().removeWindow(this);
		
	}*/

}