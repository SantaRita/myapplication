package com.example.myapplication.ventanas;

import java.awt.Checkbox;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.pantallas.PantallaQuestionsByLevel;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.PropertysetItem;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenEditarQuestionsByLevel extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	public Table tableQuestionByLevels = new Table();

	

	Item regpregunta;
	
	private static final long serialVersionUID = 1L;

	private FieldGroup binder;

	// Ponemos los campos
	// Campos del titular para cartera o propuesta
	public ComboBox cbQuestion= new ComboBox("Question");

	PantallaQuestionsByLevel pantallaQuestionsByLevel;
	
	public VenEditarQuestionsByLevel( PantallaQuestionsByLevel pantallaQuestionsByLevelLocal ) {

		pantallaQuestionsByLevel = pantallaQuestionsByLevelLocal;
		
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
		String[] columnsexp ={"IdQuestion", "Question","Select"};
		Object[] typesexp = {Integer.class, String.class, CheckBox.class};
		Object[] visibleColumnsexp = new Object[]{"Question","Select"};

		Util.defineTable(tableQuestionByLevels, columnsexp, typesexp, visibleColumnsexp,true);
		tableQuestionByLevels.setColumnHeaders(new String[] {"Question","Select"});
		tableQuestionByLevels.setPageLength(0);
		tableQuestionByLevels.setWidth("100%");
		tableQuestionByLevels.setHeight("100%");
		tableQuestionByLevels.setVisible(true);
		tableQuestionByLevels.setPageLength(10);
		tableQuestionByLevels.setColumnExpandRatio("Question", 90);
		tableQuestionByLevels.setColumnExpandRatio("Select", 10);
		hl.addComponent(tableQuestionByLevels,0,0);
		
		
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
					

					for (Iterator i = tableQuestionByLevels.getItemIds().iterator(); i.hasNext();) {
						int iid = (Integer) i.next();
					    
					    // Now get the actual item from the table.
					    Item item = tableQuestionByLevels.getItem(iid);
					    CheckBox ck = (CheckBox)
					            (item.getItemProperty("Select").getValue());

					    if ((Boolean)ck.getValue()== true) {

					    	System.out.println("activado" + item.getItemProperty("IdQuestion").getValue() );
					    	
					    	numeros += "," + item.getItemProperty("IdQuestion").getValue();
					        //             .getValue() + " ";
					    }
					}
					
					
					
					
					conexion = new Conexion();
					Connection con = conexion.getConnection();
					Statement statement = con.createStatement();
					
					String cadena;
					
					// Primero borramos todos los registros de ese TEST.
					cadena= "delete QUESTIONBYLEVELS WHERE "
							+ " IDLEVEL = " + pantallaQuestionsByLevel.cbLevel.getValue();
					System.out.println(cadena);
					statement.executeUpdate(cadena);
					// Segundo, insertamos los registros activos
					cadena= "INSERT INTO QUESTIONBYLEVELS ( IDLEVEL, IDQUESTION ) "
							+ " SELECT " +pantallaQuestionsByLevel.cbLevel.getValue() + ","
							+ " IDQUESTION"
							+ " FROM QUESTIONS"
							+ " WHERE IDQUESTION IN ( " + numeros + ")";
					System.out.println(cadena);
					statement.executeUpdate(cadena);					
					statement.close();
					con.close();
					
					new Notification("Process OK",
							"Test Questions Updated ",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					
					
					VenEditarQuestionsByLevel.this.close();
					pantallaQuestionsByLevel.buscar.click();

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
	
	//	************************ BUSCAR QUESTIONS BY LEVEL *************************
	@SuppressWarnings("unchecked")
	private void buscarQuestionsByLevel() {
		try {

			tableQuestionByLevels.removeAllItems();
			conexion = new Conexion();
			String where = "";
			/*if ( txPregunta.getValue()!=null ) {
				where = " where upper(QUESTIONDESC) like upper('%" + txPregunta.getValue() + "%')" ;
			}*/
			System.out.println("pregunta: " + where);
			Connection con = conexion.getConnection();
            try (Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                		" select  q.idquestion, q.questionDesc, qbl.idlevel " +
                		" from questionbylevels qbl right JOIN questions q " +
                		" ON q.idquestion = qbl.idquestion " +
                		" and qbl.idlevel = " + pantallaQuestionsByLevel.cbLevel.getValue().toString()
                		+ where )) {
                while (resultSet.next())
                    {
                		////////////  VISUALIZAMOS LOS REGISTROS ////////////////////
						Object newItemId = tableQuestionByLevels.addItem();
						Item row1 = tableQuestionByLevels.getItem(newItemId);
						
						
						//------ CHECK ACTIVAR 
						CheckBox ckActivo = new CheckBox();
						ckActivo.setId(newItemId.toString());
						ckActivo.setData(newItemId);
						//btModificar.setIcon(FontAwesome.SEARCH);
					

						row1.getItemProperty("Question").setValue(resultSet.getString("QuestionDesc"));
						row1.getItemProperty("Select").setValue(ckActivo);
						row1.getItemProperty("IdQuestion").setValue(resultSet.getInt("IdQuestion"));
						
						//System.out.println(String.valueOf(resultSet.getInt("idlevel")));
						//System.out.println(">" + pantallaQuestionsByLevel.cbLevel.getValue().toString());
						if ( String.valueOf(resultSet.getInt("idlevel")).equals(pantallaQuestionsByLevel.cbLevel.getValue().toString()))   {
							System.out.println("Activamos");
							ckActivo.setValue(true);
						}
						else {
							ckActivo.setValue(false);
						}

						tableQuestionByLevels.setFooterVisible(false);
						tableQuestionByLevels.setVisible(true);
						tableQuestionByLevels.setSelectable(true);
						tableQuestionByLevels.setImmediate(true);
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
		
		cbQuestion.setValue(null);
		regpregunta = item;
		this.setCaption("Questions Test By Level: " + pantallaQuestionsByLevel.cbLevel.getItemCaption(pantallaQuestionsByLevel.cbLevel.getValue()));
		buscarQuestionsByLevel();

	}
	

	
	/*@Override
	public void close() {
		// TODO Auto-generated method stub
		//super.close();
		//System.out.println("Cerramos la ventana de anular");
		UI.getCurrent().removeWindow(this);
		
	}*/

}