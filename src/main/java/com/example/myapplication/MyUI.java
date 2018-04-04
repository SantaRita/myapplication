package com.example.myapplication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.myapplication.pantallas.PantallaCustomers;
import com.example.myapplication.pantallas.PantallaDashboard;
import com.example.myapplication.pantallas.PantallaObjects;
import com.example.myapplication.pantallas.PantallaPacks;
import com.example.myapplication.pantallas.PantallaPacksByLevel;
import com.example.myapplication.pantallas.PantallaQuestions;
import com.example.myapplication.pantallas.PantallaQuestionsByLevel;
import com.example.myapplication.pantallas.PantallaTerms;
import com.example.myapplication.pantallas.PantallaTips;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("tests-valo-combobox-height")
@Title("InnerMap Dashboard MVP 0.1" )
public class MyUI extends UI {
	
	
	private String hostName = "http://meditacionapi20171120100738.azurewebsites.net";
    private String dbName = "MeditacionDB";
    	
	private VerticalLayout layout;
	
	private CustomPrincipal principal = new CustomPrincipal();


	Label lblProveedores = new Label("InnerMap");
	private Button login = new Button("Connect");
	
	private TextField usuarioField;
	private PasswordField passwordField;
	private String msgErrorConexion;
    String error = null;
    String error_description = null;	
	
	HorizontalLayout vEspera = new HorizontalLayout();
	Label lEspera = new Label("Conneting...");
	private FieldGroup binder;
	Navigator navigator;	
	

    @Override
	protected void init(VaadinRequest request) {
		
		//service2.ejecutarConsulta();
			
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1800);
		VerticalLayout main = new VerticalLayout();
		main.setHeight("100%");
		main.setWidth("100%");
		main.setMargin(true);
		main.setSpacing(false);
		//main.setStyleName("mainlogo-panel");
		main.setStyleName("valo-menu");
		main.setSizeFull();
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setSizeUndefined();		
		
		Resource res = new ThemeResource("img/logo_innermap.png");
		Image image = new Image(null, res);
		image.setWidth("300px");
		layout.addComponent(image);
		layout.setComponentAlignment(image, Alignment.TOP_CENTER);

		usuarioField = new TextField("User:");

		usuarioField.setId("usuario");
		usuarioField.setWidth("300px");
		usuarioField.setRequired(true);
		usuarioField.setRequiredError("User required");
		//usuario.setInputPrompt(messageResolver.getMessage("pantalla.login.usuario.inputExample"));
		usuarioField.setInvalidAllowed(true);
		usuarioField.setIcon(FontAwesome.USER);
		usuarioField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		usuarioField.setValidationVisible(false);
		//System.out.println("Pinemos el usuaruio:"+System.getProperty("user.name").toUpperCase());
		usuarioField.setValue(System.getProperty("user.name").toUpperCase());
		
		
		layout.addComponent(usuarioField);

		passwordField = new PasswordField("Password:");
		passwordField.setId("contrasena");
		passwordField.setWidth("300px");
		passwordField.setRequired(true);
		passwordField.setRequiredError("Password required");
		passwordField.setValue("");
		passwordField.setIcon(FontAwesome.LOCK);
		passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		passwordField.setNullRepresentation("");
		passwordField.setValidationVisible(false);
		layout.addComponent(passwordField);
		
		usuarioField.setValue("jadosgames@gmail.com");
		passwordField.setValue("Jadosgames1974..");
		
		login.setId("login");
		login.setClickShortcut(KeyCode.ENTER);
		login.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				doLogin();
			}
		});
		
		layout.addComponent(login);
		layout.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);

		vEspera.setMargin(false);
		vEspera.setSpacing(false);
		lEspera.setValue("");
		vEspera.setWidth("300px");
		vEspera.addComponent(lEspera);
		lEspera.setStyleName("blink");
		lEspera.setWidth("300px");
		vEspera.setHeight("20px");
		vEspera.setComponentAlignment(lEspera, Alignment.MIDDLE_CENTER);
		layout.addComponent(vEspera);
		
		layout.setComponentAlignment(vEspera, Alignment.MIDDLE_CENTER);
		

		layout.setStyleName("login-panel");
		
		
		usuarioField.focus();
		


		//super.setCompositionRoot(layout);
		layout.setStyleName("loginfondo-panel");
		main.addComponent(layout);
		main.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		main.setSizeFull();
		
		setContent(main);
		
		main.setResponsive(true);
		

		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("usuario", new ObjectProperty<String>(""));
		item.addItemProperty("password", new ObjectProperty<String>(""));
		
		

		binder = new FieldGroup(item);
		binder.setBuffered(true);

		binder.bindMemberFields(this);
		
		
		usuarioField.setTabIndex(1);
		passwordField.setTabIndex(2);
		login.setTabIndex(3);
	
		usuarioField.setValue("jadosgames@gmail.com");
		passwordField.setValue("Jadosgames1974.");
		System.out.println("Hacemos el click del login");
		//doLogin();
		//
		setErrorHandler(new ErrorHandler() {
			
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				// TODO Auto-generated method stub
				event.getThrowable().printStackTrace();
				System.out.println(event.getThrowable().getCause());
				new Notification("Unexpected error",
						event.getThrowable().toString(),
						Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
				
			}
		});;
	}


private void doLogin() {


		usuarioField.setValidationVisible(true);
		passwordField.setValidationVisible(true);
		//try {
			String errores = "";
			
			if ( usuarioField.getValue() == null || usuarioField.getValue().length()==0) {
				errores += "Username required\n";
			}
		
			if ( passwordField.getValue() == null || passwordField.getValue().length()==0 ) {
				errores += "Password required\n";
			}
			
			if ( errores != ""  ) {
				new Notification("Required fields",
						errores,
						Notification.Type.TRAY_NOTIFICATION, true)
						.show(Page.getCurrent());
						return;
			}
			
			// Probamos la conexión con la obtención del Token

			 try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			        HttpPost request = new HttpPost(hostName + "/token");   
			        request.addHeader("content-type", "application/json");

			        List<NameValuePair> nameValuePairs = new
			                ArrayList<NameValuePair>(2);
			        
			        nameValuePairs.add(new BasicNameValuePair("username",usuarioField.getValue()));
			        nameValuePairs.add(new BasicNameValuePair("password",passwordField.getValue()));
			        nameValuePairs.add(new BasicNameValuePair("grant_type","password"));
			        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
			        HttpResponse result = httpClient.execute(request);
			        String json = EntityUtils.toString(result.getEntity(), "UTF-8");
			        
			        
			        
			        JsonObject jobj = new Gson().fromJson(json, JsonObject.class);
			        

			        String token = null;			        

			        if ( jobj.get("access_token")!=null) {
			        	token = jobj.get("access_token").toString();
			        	
			        }
			        else if (( jobj.get("error")!=null) && ( jobj.get("error_description")!=null)) {
			        	error = jobj.get("error").toString();
			        	error_description = jobj.get("error_description").toString();
			        	
						new Notification("Error credentials",
								error_description,
								Notification.Type.TRAY_NOTIFICATION, true)
								.show(Page.getCurrent());
						return;
			        }
			        // Obtenemos los datos del usuario de Customer
			        
			        /*request.setURI(new URI(hostName + "/api/customers?mail="+jobj.get("userName").toString().replace("\"", "")));
			        

			        
			        request.addHeader("Authorization","Bearer " + token.replaceAll("\"", ""));
			        result = httpClient.execute(request);
			        json = EntityUtils.toString(result.getEntity(), "UTF-8");
			        
			        
			        
			        com.google.gson.Gson gson = new com.google.gson.Gson();                       
			        Customers[] response = gson.fromJson(json, Customers[].class);

			           
			        for(Customers customer : response)
			        {
			        	
			        	UI.getCurrent().getSession().setAttribute("hostname", hostName);
				        UI.getCurrent().getSession().setAttribute("user", usuarioField.getValue().toUpperCase());
						UI.getCurrent().getSession().setAttribute("userfirstname", customer.getFirstName());
						UI.getCurrent().getSession().setAttribute("userlastname", customer.getLastName());
						UI.getCurrent().getSession().setAttribute("usermail", customer.getEmail());
						UI.getCurrent().getSession().setAttribute("token", token);
						
						break;
			        }*/
			        

					UI.getCurrent().getSession().setAttribute("username", usuarioField.getValue());


				    principal.init();
					UI.getCurrent().setContent(principal);
					navigator = new Navigator(this, principal.viewContainer);
					UI.getCurrent().setNavigator(navigator);
					
					//
					UI.getCurrent().getNavigator().addView("PantallaCustomers", PantallaCustomers.class);
					UI.getCurrent().getNavigator().addView("PantallaTerms", PantallaTerms.class);
					UI.getCurrent().getNavigator().addView("PantallaQuestions", PantallaQuestions.class);
					UI.getCurrent().getNavigator().addView("PantallaDashboard", PantallaDashboard.class);
					UI.getCurrent().getNavigator().addView("PantallaObjects", PantallaObjects.class);
					UI.getCurrent().getNavigator().addView("PantallaQuestionsByLevel", PantallaQuestionsByLevel.class);
					UI.getCurrent().getNavigator().addView("PantallaPacks", PantallaPacks.class);
					UI.getCurrent().getNavigator().addView("PantallaPacksByLevel", PantallaPacksByLevel.class);
					UI.getCurrent().getNavigator().addView("PantallaTips", PantallaTips.class);
					UI.getCurrent().getNavigator().navigateTo("PantallaDashboard");
					
					
					UI.getCurrent().setResponsive(true);
					
								        

			    } catch (IOException ex) {
			    	ex.fillInStackTrace();
					new Notification("Error",
							"Error to obtain token user",
							Notification.Type.TRAY_NOTIFICATION, true)
							.show(Page.getCurrent());
					return;
			    } /*catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/



	}


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
