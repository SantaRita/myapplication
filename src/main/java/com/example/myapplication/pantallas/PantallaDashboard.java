package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenDashQuery;
import com.meditacionback.utiles.CuadroDash;
/*import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsLine;*/
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class PantallaDashboard extends Panel implements View {
	
	VenDashQuery venDashQuery = new VenDashQuery();
	

	private static Conexion conexion;
	VerticalLayout layout = new VerticalLayout();
	GridLayout gridDash = new GridLayout(6,1);
	Button btPrueba = new Button();
	
	public PantallaDashboard() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Dashboard MVP 0.1");
		

		/*Chart chartUsersMonth = new Chart(ChartType.LINE);
		Chart chartUsers2 = new Chart(ChartType.BUBBLE);
		Chart chartUsers3 = new Chart(ChartType.LINE);
		
		// Modify the default configuration a bit
		Configuration confBubble = chartUsers2.getConfiguration();
		confBubble.setTitle("Champagne Consumption by Country");
		confBubble.getLegend().setEnabled(false); // Disable legend
		confBubble.getTooltip().setFormatter("this.point.name + ': ' + " +
		  "Math.round(100*(this.point.z * this.point.z))/100.0 + " +
		  "' M bottles'");

		// World map as background
		String url = VaadinServlet.getCurrent().getServletContext()
		    .getContextPath() + "/VAADIN/themes/mytheme/img/map.png";
		confBubble.getChart().setPlotBackgroundImage(url);
		
		chartUsers2.setConfiguration(confBubble);
		
		
		// Chart user month
		
		Configuration conf = chartUsersMonth.getConfiguration();
		conf.setTitle("App users registration by last months");
		chartUsersMonth.setConfiguration(conf);
		chartUsers3.setConfiguration(conf);
		
		conexion = new Conexion();
		Connection con = conexion.getConnection();
		
		String consulta = "select (select  count(1) cuantos\r\n" + 
				"                from customers\r\n" + 
				"                where x.fecha = format(datecreated,'yyyy/MM')\r\n" + 
				" ) cuantos, x.fecha\r\n" + 
				"from ( select format(SYSDATETIME(),'yyyy/MM') fecha\r\n" + 
				"union\r\n" + 
				"select format(dateadd(month,-1,SYSDATETIME()),'yyyy/MM') fecha\r\n" + 
				"union\r\n" + 
				"select format(dateadd(month,-2,SYSDATETIME()),'yyyy/MM') fecha\r\n" + 
				"union\r\n" + 
				"select format(dateadd(month,-3,SYSDATETIME()),'yyyy/MM') fecha\r\n" + 
				"union\r\n" + 
				"select format(dateadd(month,-4,SYSDATETIME()),'yyyy/MM') fecha\r\n" + 
				"union\r\n" + 
				"select format(dateadd(month,-5,SYSDATETIME()),'yyyy/MM') fecha ) x order by 2";
		
		ListSeries series = new ListSeries("User Registration");

		try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(consulta )) 
		{
                while (resultSet.next())
                    {
						series.addData(resultSet.getInt("cuantos"));
						conf.getxAxis().addCategory(resultSet.getString("fecha"));
						
                    }

		        con.close();
		        statement.close();
		                         
	    }
	    catch (Exception e) {
	            e.printStackTrace();
	    }
	    

		PlotOptionsLine plotOptions = new PlotOptionsLine();
		//plotOptions.setPointStart(1959);
		series.setPlotOptions(plotOptions);
		conf.addSeries(series);		
		
		
		
		*/
		
		gridDash.setMargin(true);
		gridDash.setSpacing(true);
		gridDash.setWidth("100%");
		gridDash.setHeight("30%");
		
		//gridDash.addComponent(chartUsersMonth, 0,0,1,0);
		//gridDash.addComponent(chartUsers2,2,0,3,0);
		//gridDash.addComponent(chartUsers3,4,0,5,0);
		
		
		/*HorizontalLayout cuadro = new HorizontalLayout();
		cuadro.setStyleName("fondo-rojo");
		cuadro.setWidth("100%");
		cuadro.setHeight("100px");
		Label etiqueta = new Label("Este es un listado de prueba para ver como se visulizaria el título");
		etiqueta.addStyleName("wrap");
		etiqueta.setWidth(null);
		cuadro.addComponent(etiqueta);
		cuadro.setMargin(true);
		cuadro.setComponentAlignment(etiqueta, Alignment.MIDDLE_CENTER);*/

		
		// AÑADIMOS LAS QUERYS
		
		actualizarGrid();	
		
		
		
		layout.addComponent(gridDash);
		setContent(layout);
		gridDash.setRows(gridDash.getRows()+1);
		
		
		venDashQuery.addCloseListener(new CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Cerramos la ventana del dashboard");
				gridDash.removeAllComponents();
				actualizarGrid();
			}
		});
	
		
	}

	public void actualizarGrid() {
		CuadroDash cuadroNuevo = new CuadroDash("New Group","fondo-rojo");
		cuadroNuevo.layout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Pulsamos el cuadro");

			}
		});
		gridDash.addComponent(cuadroNuevo,0,0,0,0);
		
		CuadroDash cuadroGrupo = new CuadroDash("New Query","fondo-azul");
		cuadroGrupo.layout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				venDashQuery.idRegistro = -1;
				venDashQuery.txQueryName.setValue(null);
				venDashQuery.tablesUpdate = null;
				venDashQuery.columnsUpdate = null;
				venDashQuery.whereUpdate = null;
				System.out.println("Pulsamos el cuadro New Query");
				venDashQuery.estadoRegistro = "NEW";
				venDashQuery.init();
				UI.getCurrent().addWindow(venDashQuery);

			}
		});
		gridDash.addComponent(cuadroGrupo,1,0,1,0);
		
		// AÑADIMOS LAS QUERYS
		
		int fila = 0;
		int columna = 2;
		
		conexion = new Conexion();
		Connection con = conexion.getConnection();
        try (Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from dashQUERIES ")) {
            while (resultSet.next())
                {
            	
            	
            	columna ++;
            	System.out.println("Columna" + columna);
            	if ( columna==6) {
            		gridDash.setRows(gridDash.getRows()+1);
            		columna = 0;
            		fila++;
            	}
            	
        		CuadroDash cuadroGrupoDinamico = new CuadroDash(resultSet.getString("DESCRIPTION"),"fondo-azul");
        		cuadroGrupoDinamico.Description = resultSet.getString("DESCRIPTION");
        		cuadroGrupoDinamico.idQuery = resultSet.getInt("IdQuery");
        		cuadroGrupoDinamico.tablesUpdate = resultSet.getString("tables");
        		cuadroGrupoDinamico.columnsUpdate = resultSet.getString("columns");
        		cuadroGrupoDinamico.whereUpdate = resultSet.getString("conditions");
        		cuadroGrupoDinamico.layout.addLayoutClickListener(new LayoutClickListener() {
        			
        			@Override
        			public void layoutClick(LayoutClickEvent event) {
        				// TODO Auto-generated method stub

        				venDashQuery.estadoRegistro = "UPDATE";
        				venDashQuery.idRegistro = cuadroGrupoDinamico.idQuery;
        				venDashQuery.txQueryName.setValue(cuadroGrupoDinamico.Description);
        				venDashQuery.tablesUpdate = cuadroGrupoDinamico.tablesUpdate;
        				venDashQuery.columnsUpdate = cuadroGrupoDinamico.columnsUpdate;
        				venDashQuery.whereUpdate = cuadroGrupoDinamico.whereUpdate;

        				System.out.println("La descripcion es:" + cuadroGrupoDinamico.Description);
        				venDashQuery.init();
        				UI.getCurrent().addWindow(venDashQuery);

        			}
        		});
        		gridDash.addComponent(cuadroGrupoDinamico,columna,fila,columna,fila);
        		
	            	//cbTablesFrom.addItem(new BigDecimal(resultSet.getInt("IDTABLE")));
					//cbTablesFrom.setItemCaption(new BigDecimal(resultSet.getInt("IDTABLE")),resultSet.getString("TABLENAME") + " - " + resultSet.getString("DESCTABLE"));

                }
	        con.close();
	        statement.close();
	        gridDash.setRows(gridDash.getRows()+1);
	     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("ENtramos");
	}
	
	

	
	
}



