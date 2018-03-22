package com.example.myapplication.ventanas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.myapplication.jdbc.Conexion;
import com.meditacionback.utiles.BotoneraDoble;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VenDashQuery extends Window   {

	
	private static Conexion conexion;
	String acceso;
	PropertysetItem item;
	BotoneraDoble botonera;
	

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
	
	Button btColumnsFromTo = new Button ( ">");
	Button btColumnsToFrom = new Button ( "<");
	Button btFromTo = new Button ( ">");
	Button btToFrom = new Button ( "<");
	
	TextArea txResultado = new TextArea("Result");
	
	GridLayout tabTablesLayout = new GridLayout(4,2);
	GridLayout tabColumnsLayout = new GridLayout(4,2);
	GridLayout tabResultLayout = new GridLayout(4,2);
	
	public Button btAceptar = new Button();
	public Button btCancelar = new Button();

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
		GridLayout hl = new GridLayout(3,5);
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
				System.out.println("Pulsamos btfrom to");
				cbTablesTo.addItem(cbTablesFrom.getValue());
				cbTablesTo.setItemCaption(cbTablesFrom.getValue(),cbTablesFrom.getItemCaption(cbTablesFrom.getValue()));
				cbTablesFrom.removeItem(cbTablesFrom.getValue());
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
		tabColumnsLayout.setWidth("100%");
		cbColumnsFrom.setNullSelectionAllowed(false);
		cbColumnsFrom.setRequired(true);
		cbColumnsFrom.setWidth("100%");
		cbColumnsFrom.setRows(20);
		
		cbColumnsTo.setNullSelectionAllowed(false);
		cbColumnsTo.setRequired(true);
		cbColumnsTo.setWidth("100%");
		cbColumnsTo.setRows(20);	
		
		btColumnsFromTo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				cbColumnsTo.addItem(cbColumnsFrom.getValue());
				cbColumnsTo.setItemCaption(cbColumnsFrom.getValue(),cbColumnsFrom.getItemCaption(cbColumnsFrom.getValue()));
				cbColumnsFrom.removeItem(cbColumnsFrom.getValue());
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

		// TAB 4 - CONSULTA FINAL
		
		txResultado.setWidth("100%");
		txResultado.setRows(15);
		
		tabResultLayout.setMargin(true);
		tabResultLayout.setSpacing(true);
		tabResultLayout.addComponent(txResultado,0,0,3,1);
		tabResultLayout.setComponentAlignment(txResultado, Alignment.MIDDLE_CENTER);
		tabResultLayout.setWidth("100%");

		
		// TABS

		tabTablesLayout.setWidth("100%");
		tabs.setWidth("100%");
		tabs.addTab(tabTablesLayout, "Tables");
		tabs.addTab(tabColumnsLayout, "Columns");
		tabs.addTab(new Label("Conditions"), "Conditions");
		tabs.addTab(tabResultLayout, "Result");
		
	    
	    vl.addComponent(hl);
	    vl.addComponent(tabs);
	    btAceptar.setCaption("Apply");
	    btCancelar.setCaption("Cancel");
	    btAceptar.setId("BTACEPTAR");
	    btCancelar.setId("BTCANCELAR");
	    btAceptar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	    btCancelar.setStyleName(ValoTheme.BUTTON_DANGER);
		hl.addComponent(btAceptar,1,0);
		hl.addComponent(btCancelar,2,0);
	    hl.setComponentAlignment(btAceptar, Alignment.BOTTOM_CENTER);
	    hl.setComponentAlignment(btCancelar, Alignment.BOTTOM_CENTER);		


	    
		setContent(vl);
		
		
		// VALIDACIONES
		
		item.addItemProperty("txQueryName", new ObjectProperty<String>(null, String.class));
		
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);	
		binder.bindMemberFields(this);	
		

		
		btAceptar.setCaption("Apply");
		
		btCancelar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().getSession().setAttribute("botonpulsado","CANCELAR");
				
				close();
				
			}
		});
		
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

                }
	        con.close();
	        statement.close();
	     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	public void init( String tipoAcceso, Item item, String question, Integer pidquestion) {

		
		acceso = tipoAcceso;
		
		txQueryName.setValue(null);
		regpregunta = item;
		idquestion = pidquestion;
		
		lbQuestion.setValue(question);
		
		if ( tipoAcceso.equals("UPDATE")) {

			txQueryName.setValue(item.getItemProperty("Answer").getValue().toString());
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