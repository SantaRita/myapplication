package com.example.myapplication.pantallas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.example.myapplication.Util;
import com.example.myapplication.jdbc.Conexion;
import com.example.myapplication.ventanas.VenEditarObjects;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaObjects extends Panel implements View {
	
	private static Conexion conexion;
	VenEditarObjects venEditarObjects = new VenEditarObjects(this);
	
	Table tableObjects = new Table();
	Button nuevo = new Button("Add");
	private FieldGroup binder;
	TextField txObjectName;
	public Button buscar = new Button("Search");
	public Button limpiar = new Button("Clear");
	VerticalLayout layout = new VerticalLayout();
	

	
	public PantallaObjects() {
		// TODO Auto-generated constructor stub
		
		this.setCaption("Objects");
		
		
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("mail", new ObjectProperty<String>(""));
		
		binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);
	
		txObjectName = new TextField();
		txObjectName.setWidth("150px");
		
		GridLayout gridCamposBusqueda = new GridLayout(5,4);
		gridCamposBusqueda.setMargin(true);
		gridCamposBusqueda.setSpacing(true);
		gridCamposBusqueda.setWidth("100%");
		gridCamposBusqueda.addComponent(new Label("Object Name: "),0,0);
		gridCamposBusqueda.addComponent(txObjectName,1,0);
		gridCamposBusqueda.setComponentAlignment(txObjectName, Alignment.MIDDLE_LEFT);
		
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
				
				buscarCustomers();
			}
		});
		
		limpiar.setStyleName(ValoTheme.BUTTON_DANGER);
		
		limpiar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				txObjectName.setValue("");
				
			}
		});
		

		txObjectName.focus();
		
		// TABLAS
		layout.setMargin(true);
		String[] columnsexp ={"IdObject","ObjectName","ObjectType","DescType","CreationDate","ObjectSize","UserCreation","Url","Borrar","Modificar"};
		Object[] typesexp = {Integer.class, String.class, Integer.class, String.class,Date.class,Integer.class,String.class,String.class,Button.class, Button.class };
		Object[] visibleColumnsexp = 
				new Object[]{"IdObject","ObjectName","DescType","CreationDate","ObjectSize","UserCreation","Url","Borrar","Modificar"};

		Util.defineTable(tableObjects, columnsexp, typesexp, visibleColumnsexp,true);
		tableObjects.setColumnHeaders(new String[] {"IdObject","ObjectName","Desctype","CreationDate","ObjectSize","UserCreation","Url","Delete","Update"});
		
		tableObjects.setColumnExpandRatio("IdObject", 5);
		tableObjects.setColumnExpandRatio("ObjectName", 20);
		tableObjects.setColumnExpandRatio("ObjectType", 9);
		tableObjects.setColumnExpandRatio("CreationDate", 9);
		tableObjects.setColumnExpandRatio("ObjectSize", 5);
		tableObjects.setColumnAlignment("ObjectSize", Align.RIGHT);
		tableObjects.setColumnExpandRatio("UserCreation", 10);
		tableObjects.setColumnExpandRatio("Url", 21);
		tableObjects.setColumnExpandRatio("Borrar", 6);		
		tableObjects.setColumnExpandRatio("Modificar", 6);

		
		/*tableObjects = new Table()  {
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
		
		/*tableObjects.setCellStyleGenerator(new Table.CellStyleGenerator() {                
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
		
		tableObjects.setPageLength(0);
		tableObjects.setWidth("100%");
		tableObjects.setHeight("100%");
		tableObjects.setVisible(true);
		gridCamposBusqueda.addComponent(tableObjects,0,1,4,1);
		layout.addComponent(gridCamposBusqueda);
		
		
		setContent(layout);
		
		nuevo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				venEditarObjects.init("NEW",null);
				UI.getCurrent().addWindow(venEditarObjects);
				
			}
		});
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
		
	}

	//	************************ BUSCAR CUSTOMERS *************************
	@SuppressWarnings("unchecked")
	private void buscarCustomers() {
		try {

			tableObjects.removeAllItems();
			conexion = new Conexion();
			Connection con = conexion.getConnection();
			String where = "";
            if (txObjectName.getValue()!=null && txObjectName.getValue()!="") {
        		where = " where upper(OBJECTNAME) like upper('%" + txObjectName.getValue() + "%')" ;
            }
            try (Statement statement = con.createStatement();

          		
                ResultSet resultSet = statement.executeQuery("SELECT * from dbo.Objects " + where)) {
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
								venEditarObjects.init("UPDATE",itemClickEvent );
								UI.getCurrent().addWindow(venEditarObjects);
		
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
						    	ConfirmDialog.show(UI.getCurrent(), "Delete Object", "Are you sure delete this object ?",
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
						    	    						
						    	    						String cadena = "DELETE OBJECTS WHERE IDOBJECT =" + String.valueOf(id); 
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
						
						row1.getItemProperty("UserCreation").setValue(resultSet.getString("UserCreation"));
						row1.getItemProperty("IdObject").setValue(resultSet.getInt("IdObject"));
						row1.getItemProperty("ObjectName").setValue(resultSet.getString("ObjectName"));
						row1.getItemProperty("ObjectType").setValue(resultSet.getInt("ObjectType"));
						if ( resultSet.getInt("ObjectType") == 1) {
							row1.getItemProperty("DescType").setValue("Video");
						}
						else if ( resultSet.getInt("ObjectType") == 2) {
							row1.getItemProperty("DescType").setValue("Audio");
						}
						else if ( resultSet.getInt("ObjectType") == 3) {
							row1.getItemProperty("DescType").setValue("Photo");
						}
						else if ( resultSet.getInt("ObjectType") == 4) {
								row1.getItemProperty("DescType").setValue("Tip");							
						}
						row1.getItemProperty("ObjectSize").setValue(resultSet.getInt("ObjectSize"));
						row1.getItemProperty("CreationDate").setValue(resultSet.getDate("CreationDate"));
						row1.getItemProperty("Borrar").setValue(btBorrar);
						row1.getItemProperty("Modificar").setValue(btModificar);
						
						tableObjects.setFooterVisible(true);
						//tableObjects.setColumnFooter("Expediente", "Total: "+String.valueOf(tableObjects.size()));
						tableObjects.setVisible(true);
						tableObjects.setSelectable(true);
						tableObjects.setImmediate(true);
						//tableObjects.setPageLength((int)UI.getCurrent().getSession().getAttribute("resoluciony")/33);
                		
                    }
				tableObjects.setFooterVisible(true);
				tableObjects.setColumnFooter("IdObject", "Records: "+String.valueOf(tableObjects.size()));
		        con.close();
		        statement.close();
		       }                   
	    }
	    catch (Exception e) {
	            e.printStackTrace();
	    }
	}
			
	
	
	/*private void buscarCustomers() throws ParseException {
	
		try {

			tableObjects.removeAllItems();
			
			// Llamamos al api POST/CUSTOMERS
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost();
			request.setURI(new URI(UI.getCurrent().getSession().getAttribute("hostname") + "/api/customers?mail="+mailCustomer.getValue()));
	        request.addHeader("content-type", "application/json");
	        request.addHeader("Authorization","Bearer " + UI.getCurrent().getSession().getAttribute("token"));
	        HttpResponse result = httpClient.execute(request);
	        String json = EntityUtils.toString(result.getEntity(), "UTF-8");	        
	        result = httpClient.execute(request);
	        json = EntityUtils.toString(result.getEntity(), "UTF-8");
	        com.google.gson.Gson gson = new com.google.gson.Gson();                       
	        Customers[] response = gson.fromJson(json, Customers[].class);
	        for(Customers customer : response)
	        {
	        	
	        	System.out.println("Entramos");

	        }	        
			
			if(retorno!=null) {

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					String fecha;
					Date date = null;
					List<Map> valor = (List<Map>) retorno.get("REGISTROS");
					//System.out.println("Registros hay" + valor.size());
					
					if ( valor.size()==0) {
						//System.out.println("no hay registros");
						gl.setStyleName("box_rojo");
						msgAlta.setVisible(true);
						gl.setVisible(true);
					} else {
						gl.setStyleName("box_verde");
						gl.setVisible(false);
						btConsultar.setVisible(true);
						msgAlta.setVisible(false);
					}
					btConsultar.setVisible(false);
					for (Map map : valor) {
						
						Object newItemId = tableObjects.addItem();
						Item row1 = tableObjects.getItem(newItemId);
					
						
						// Botonera buscar
						
						Button btBuscar = new Button();
						btBuscar.setId(newItemId.toString());
						btBuscar.setData(newItemId);
						btBuscar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						btBuscar.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btBuscar.setIcon(FontAwesome.SEARCH);
						btBuscar.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
								btConsultar.setVisible(false);
								UI.getCurrent().getSession().setAttribute("expediente",(String) itemClickEvent.getItemProperty("Expediente").getValue());
								UI.getCurrent().getSession().setAttribute("tit.nombretit",(String) itemClickEvent.getItemProperty("Nombre").getValue());
								UI.getCurrent().getSession().setAttribute("tit.estadoexp",(String) itemClickEvent.getItemProperty("Estado").getValue());
								UI.getCurrent().getSession().setAttribute("tit.cia",(String) itemClickEvent.getItemProperty("Cia").getValue());
								UI.getCurrent().getSession().setAttribute("tit.direccion",(String) itemClickEvent.getItemProperty("Direccion").getValue());
								UI.getCurrent().getSession().setAttribute("tit.poblacion",(String) itemClickEvent.getItemProperty("Poblacion").getValue());
								UI.getCurrent().getSession().setAttribute("tit.provincia",(String) itemClickEvent.getItemProperty("Provincia").getValue());
								UI.getCurrent().getSession().setAttribute("tit.cp",(String) itemClickEvent.getItemProperty("Cp").getValue());
								UI.getCurrent().getSession().setAttribute("revisar",(String) itemClickEvent.getItemProperty("Revisar").getValue());
								UI.getCurrent().getSession().setAttribute("estadoExpediente",EstadoExpediente());
								telefonosExpediente(new BigDecimal(itemClickEvent.getItemProperty("Expediente").getValue().toString()));								
								
								UI.getCurrent().getSession().setAttribute("ordenColumna",tableObjects.getSortContainerPropertyId());
								UI.getCurrent().getSession().setAttribute("orden",tableObjects.isSortAscending());

								UI.getCurrent().getSession().setAttribute("ckRev",ckRev.getValue());
								UI.getCurrent().getSession().setAttribute("ckInc",ckInc.getValue());
								
								UI.getCurrent().getNavigator().navigateTo("ProvPantallaConsultaExpediente");		
							}
						});
						
						// Botones Rechaza y Aceptarexpediente
						// "ColBtRechazar","ColBtAceptar"
						
						Button btColRechazar = new Button();
						btColRechazar.setId(newItemId.toString());
						btColRechazar.setDescription("Rechazar Expediente");
						btColRechazar.setData(newItemId);
						btColRechazar.addStyleName(ValoTheme.BUTTON_DANGER);
						btColRechazar.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btColRechazar.setIcon(FontAwesome.TIMES);
						btColRechazar.addClickListener(new ClickListener() {
							
								@Override
								public void buttonClick(ClickEvent event) {
									
									
										//recargarComunicados();
									Object data =  event.getButton().getData();
									tableObjects.select(data);
									Item itemClickEvent = tableObjects.getItem(data);
									provVenRechazoExpediente.init();
									UI.getCurrent().getSession().setAttribute("exprechazar",(String) itemClickEvent.getItemProperty("Expediente").getValue());
									UI.getCurrent().addWindow(provVenRechazoExpediente);
									
						}
								
						});
						
						
						
						Button btColAceptar = new Button();
						if ( map.get("TIENECF")!=null && !map.get("TIENECF").equals("0")) {
							btColAceptar.setVisible(true); 
						}
						else
							btColAceptar.setVisible(false);						
						btColAceptar.setDescription("Aceptar");
						btColAceptar.setId(newItemId.toString());
						btColAceptar.setData(newItemId);
						btColAceptar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						btColAceptar.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btColAceptar.setIcon(FontAwesome.CHECK);
						btColAceptar.addClickListener(new ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
								
								MessageBox.createInfo()
								.withCaption("Rechazar expediente")
								.withMessage("¿Esta seguro qué quiere aceptar el expediente: "+ (String) itemClickEvent.getItemProperty("Expediente").getValue() )
								.withYesButton( new Runnable() {
									@Override
									public void run() {
										
										PAC_SHWEB_PROVEEDORES llamada = null;
										try {
											llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
										} catch (SQLException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										HashMap respuesta = null;
										try {
											respuesta = llamada.ejecutaPAC_SHWEB_PROVEEDORES__ACCION_MENSAJE_SMS(
													new BigDecimal("1"),
													"S",
													new BigDecimal((String) itemClickEvent.getItemProperty("Expediente").getValue()),
													new BigDecimal(UI.getCurrent().getSession().getAttribute("user").toString().toUpperCase().replace("PROV_", "")),
													new BigDecimal("1"),
													""
													);
											
											Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
										
											if (retorno.get("CODIGOERROR").toString().equals("0"))  {
												
												tableObjects.removeItem(data);
												new Notification("Proceso finalizado",
														"Se ha aceptado el expediente correctamente",
														Notification.Type.TRAY_NOTIFICATION, true)
														.show(Page.getCurrent());
											} else {
												
												new Notification("Error al aceptar el expediente",
														retorno.get("TEXTOERROR").toString(),
														Notification.Type.ERROR_MESSAGE, true)
														.show(Page.getCurrent());
												
											}
											
											
											
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											new Notification("Error",
													"Error no contralado al aceptar el expediente",
													Notification.Type.ERROR_MESSAGE, true)
													.show(Page.getCurrent());											
										}
										         								
										
									}
									
								}, ButtonOption.caption("Aceptar Expediente"))
								.withNoButton( new Runnable() {
									@Override
									public void run() {

										
									}
									
								}, ButtonOption.caption("Cancelar"))

								.open()
								;
								
							}
								
						});

						// Hoja de encargo
						Button btHoja = new Button();
						btHoja.setWidth("10px");
						btHoja.setId(newItemId.toString());
						btHoja.setData(newItemId);
						btHoja.addStyleName(ValoTheme.BUTTON_PRIMARY);
						btHoja.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btHoja.setIcon(FontAwesome.FILE_ARCHIVE_O);
						btHoja.addClickListener(new ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
								
								MessageBox.createInfo()
								.withCaption("Hoja de Encargo")
								.withMessage("¿Esta seguro qué quiere enviar la Hoja de encargo ?" )
								.withYesButton( new Runnable() {
									@Override
									public void run() {
										
										PAC_SHWEB_PROVEEDORES llamada = null;
										try {
											llamada = new PAC_SHWEB_PROVEEDORES(service.plsqlDataSource.getConnection());
										} catch (SQLException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										HashMap respuesta = null;
										try {
											respuesta = llamada.ejecutaPAC_SHWEB_PROVEEDORES__F_CREAR_HOJA_ENCARGO(
													(BigDecimal) itemClickEvent.getItemProperty("gremioinicial").getValue()
													);
											
											Map<String, Object> retorno = new HashMap<String, Object>(respuesta);
										
											if (retorno.get("ERROR").toString().equals("0"))  {
												
												new Notification("Proceso finalizado",
														"Se ha aceptado la hoja de encargo",
														Notification.Type.TRAY_NOTIFICATION, true)
														.show(Page.getCurrent());
											} else {
												
												new Notification("Error",
														"Error al crear la hoja de encargo",
														Notification.Type.ERROR_MESSAGE, true)
														.show(Page.getCurrent());
												
											}
											
											
											
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											new Notification("Error",
													"Error no contralado al crear la hoja de encargo",
													Notification.Type.ERROR_MESSAGE, true)
													.show(Page.getCurrent());											
										}
										         								
										
									}
									
								}, ButtonOption.caption("Enviar Hoja de Encargo"))
								.withNoButton( new Runnable() {
									@Override
									public void run() {

										
									}
									
								}, ButtonOption.caption("Cancelar"))

								.open()
								;
								
							}
								
						});
									
						
						
						
						// Botón anular / Rechazar Expediente ( Este no sale ) 
						
						Button btAnular = new Button();
						btAnular.setId(newItemId.toString());
						btAnular.setData(newItemId);
						btAnular.addStyleName(ValoTheme.BUTTON_DANGER);
						btAnular.addStyleName(ChameleonTheme.BUTTON_DOWN);
						btAnular.setIcon(FontAwesome.GEAR);
						btAnular.addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Object data =  event.getButton().getData();
								tableObjects.select(data);
								Item itemClickEvent = tableObjects.getItem(data);
								btConsultar.setVisible(false);
		
							}
						});						
						
						
						if ( map.get("ESTADOEXP").toString().equals("FAB") ) {
							
							row1.getItemProperty("Expediente").setValue(map.get("IDEXPEDIENTE").toString());
							row1.getItemProperty("Incidencia").setValue("N");
							row1.getItemProperty("Revisar").setValue("N");
							
							row1.getItemProperty("Nro.Fra.Proveedor").setValue(map.get("NUMFRA"));
							row1.getItemProperty("Importe").setValue(map.get("IMPORTE"));
							row1.getItemProperty("Estado Fra.").setValue(map.get("ESTADO_FACT"));
							row1.getItemProperty("F.Factura").setValue(map.get("FECHA_FACTURA"));
							row1.getItemProperty("F.Vto").setValue(map.get("FECHA_VENCIMIENTO"));
							row1.getItemProperty("RgFactur").setValue(map.get("RGFACTUR"));
							
							row1.getItemProperty("Imprimir").setValue(btImprimirFactura);
							
						}
							
						else	if ( map.get("ESTADOEXP").toString().equals("FCO") ) {
								
								row1.getItemProperty("Expediente").setValue(map.get("IDEXPEDIENTE").toString());
								row1.getItemProperty("Incidencia").setValue("N");
								row1.getItemProperty("Revisar").setValue("N");
								
								row1.getItemProperty("Nro.Fra.Proveedor").setValue(map.get("NUMCOM"));
								row1.getItemProperty("Nro.Fra.Comis").setValue(map.get("FRA_COMISION"));
								row1.getItemProperty("Importe").setValue(map.get("IMBASEIMP"));
								row1.getItemProperty("F.Vto").setValue(map.get("FECHA_VENCIMIENTO"));
								row1.getItemProperty("RgFactur").setValue(map.get("RGFACTUR"));
								
								row1.getItemProperty("Imprimir").setValue(btImprimirFactura);

						} else if ( map.get("ESTADOEXP").toString().equals("VIS") ) {
							
							date = null;
							row1.getItemProperty("Consultar").setValue(btBuscar);
							row1.getItemProperty("Hoja").setValue(btHoja);
							row1.getItemProperty("ColBtAceptar").setValue(btColAceptar);
							row1.getItemProperty("ColBtRechazar").setValue(btColRechazar);
							row1.getItemProperty("Anular").setValue(btAnular);
							
							// Fecha visita
							fecha = (String) map.get("FHVISITA");
	
								try {
									date = formatter.parse(fecha);
								} catch ( Exception e ) {
									
								}
							
							if (date!=null) row1.getItemProperty("Fhvisita").setValue(date);
							
							// Fecha asignacion
							// Fecha visita
							date = null;
							fecha = (String) map.get("FHASIGNACION");
	
								try {
									date = formatter.parse(fecha);
								} catch ( Exception e ) {
									
								}
							
							if (date!=null) row1.getItemProperty("Fhasignacion").setValue(date);
							
							// Fecha PC
							try {
								date = null;
		
									try {
										if ( map.get("FHPC")!=null ) {
											//System.out.println(map.get("FHPC"));
											date = formatter.parse((String) map.get("FHPC"));
										}
									} catch ( Exception e ) {
										e.printStackTrace();
									}
								
								if (date!=null) row1.getItemProperty("Fhpc").setValue(date);
							} catch (ReadOnlyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							
							
	
							
		
							row1.getItemProperty("Agrupacion").setValue(map.get("AGRUPACION"));
							row1.getItemProperty("Expediente").setValue(map.get("IDEXPEDIENTE").toString());
							row1.getItemProperty("Nombre").setValue(map.get("NOMBRE"));
							row1.getItemProperty("Direccion").setValue(map.get("DIRECCION"));
							row1.getItemProperty("Cp").setValue(map.get("CP"));
							row1.getItemProperty("Poblacion").setValue(map.get("POBLACION"));
							row1.getItemProperty("Provincia").setValue(map.get("PROVINCIA"));
							row1.getItemProperty("Estado").setValue(map.get("ESTADOEXP"));
							row1.getItemProperty("Incidencia").setValue(map.get("INCIDENCIA"));
							row1.getItemProperty("Revisar").setValue(map.get("REVISAR"));
							row1.getItemProperty("Cia").setValue(map.get("CIA"));
							row1.getItemProperty("gremioinicial").setValue(map.get("GREMIO_INICIAL"));
							
							if ( map.get("CAUSA")!=null ) {
								row1.getItemProperty("Causa").setValue(map.get("CAUSA"));	
							}
							if ( map.get("FECHA_CIERRE")!=null ) {
								row1.getItemProperty("F.Cierre").setValue(map.get("FECHA_CIERRE"));	
							}						
	
							
				
											
						} else {
						
									date = null;
									row1.getItemProperty("Consultar").setValue(btBuscar);
									row1.getItemProperty("ColBtAceptar").setValue(btColAceptar);
									row1.getItemProperty("ColBtRechazar").setValue(btColRechazar);
									row1.getItemProperty("Anular").setValue(btAnular);
									
									// Fecha visita
									fecha = (String) map.get("FHVISITA");
			
										try {
											date = formatter.parse(fecha);
										} catch ( Exception e ) {
											
										}
									
									if (date!=null) row1.getItemProperty("Fhvisita").setValue(date);
									
									// Fecha asignacion
									// Fecha visita
									date = null;
									fecha = (String) map.get("FHASIGNACION");
			
										try {
											date = formatter.parse(fecha);
										} catch ( Exception e ) {
											
										}
									
									if (date!=null) row1.getItemProperty("Fhasignacion").setValue(date);
									
									// Fecha PC
									try {
										date = null;
				
											try {
												if ( map.get("FHPC")!=null ) {
													//System.out.println(map.get("FHPC"));
													date = formatter.parse((String) map.get("FHPC"));
												}
											} catch ( Exception e ) {
												e.printStackTrace();
											}
										
										if (date!=null) row1.getItemProperty("Fhpc").setValue(date);
									} catch (ReadOnlyException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}							
									
			
									
				
									row1.getItemProperty("Agrupacion").setValue(map.get("AGRUPACION"));
									row1.getItemProperty("Expediente").setValue(map.get("IDEXPEDIENTE").toString());
									row1.getItemProperty("Nombre").setValue(map.get("NOMBRE"));
									row1.getItemProperty("Direccion").setValue(map.get("DIRECCION"));
									row1.getItemProperty("Cp").setValue(map.get("CP"));
									row1.getItemProperty("Poblacion").setValue(map.get("POBLACION"));
									row1.getItemProperty("Provincia").setValue(map.get("PROVINCIA"));
									row1.getItemProperty("Estado").setValue(map.get("ESTADOEXP"));
									row1.getItemProperty("Incidencia").setValue(map.get("INCIDENCIA"));
									row1.getItemProperty("Revisar").setValue(map.get("REVISAR"));
									row1.getItemProperty("Cia").setValue(map.get("CIA"));
									
									if ( map.get("CAUSA")!=null ) {
										row1.getItemProperty("Causa").setValue(map.get("CAUSA"));	
									}
									if ( map.get("FECHA_CIERRE")!=null ) {
										row1.getItemProperty("F.Cierre").setValue(map.get("FECHA_CIERRE"));	
									}						
			
									
						}
						

					}
					
					tableObjects.setFooterVisible(true);
					tableObjects.setColumnFooter("Expediente", "Total: "+String.valueOf(tableObjects.size()));
			
					
					tableObjects.setVisible(true);
					tableObjects.setSelectable(true);
					tableObjects.setImmediate(true);
					tableObjects.setPageLength((int)UI.getCurrent().getSession().getAttribute("resoluciony")/33);
					//tableObjects.setHeight("33%");
					
					//System.out.println("Recuperamos el orden ?" + UI.getCurrent().getSession().getAttribute("orden"));
					//System.out.println("Recuperamos el ordenColumna ?" + UI.getCurrent().getSession().getAttribute("ordenColumna"));
					tableObjects.setSortContainerPropertyId(UI.getCurrent().getSession().getAttribute("ordenColumna"));
					if ( UI.getCurrent().getSession().getAttribute("orden")!=null) {
						tableObjects.setSortAscending((boolean) UI.getCurrent().getSession().getAttribute("orden"));
						tableObjects.sort();
						
					}
					

			}
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			//System.out.println("Error");
			e.printStackTrace();
			

		}	
		
		
		
	}*/	

}



