package com.example.myapplication;


import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class CustomPrincipal extends CustomComponent {
	
	private static final long serialVersionUID = -641725931242567777L;

	private VerticalLayout layout;
	private HorizontalLayout topLayout;
	
	//private LogTablas logUsuario;
	private MenuPrincipal menuPrincipal;
	
	private HorizontalLayout menuLayout;
	
	private Button logout;

	private VerticalLayout main;
	private Label lblProveedores = new Label("WEB Proveedores");

	public Panel viewContainer;


	public void init(  ) {
		
		// TODO Auto-generated method stub
		
		// LAYOUT PRINCIPAL
		
		
		layout = new VerticalLayout();
		layout.setSizeFull();
		
		layout.setMargin(false);
		layout.setSpacing(false);

		
		Resource resMenu = new ThemeResource("img/hamburguesa2.png");
		//Resource resMenu = new ThemeResource("img/logo_innermap.png");

		Image imageMenu = new Image(null, resMenu);
		imageMenu.setHeight("28px");
		
		
		// LAYOUT LA BARRA DE MENU
		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.setMargin(false);
				
		menuLayout.setStyleName("menu-panel");
		//menuLayout.setHeight("40px");
		menuLayout.setWidth("100%");

		

		logout = new Button("Logout");
		logout.addClickListener((e) -> doLogout());
		logout.setStyleName(ValoTheme.BUTTON_SMALL);
		logout.setWidth("70px");
		
		

		HorizontalLayout barraH = new HorizontalLayout();
		barraH.setWidth("99%");
		barraH.setHeight("32px");
		
		menuPrincipal = new MenuPrincipal();
		menuPrincipal.menuOpcionesBoton.addComponent(imageMenu);
		barraH.addComponent(menuPrincipal);

		
		barraH.addComponent(logout);
		barraH.setMargin(true);
		barraH.setMargin(false);
		
		
		barraH.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
		

		
		barraH.setCaptionAsHtml(true);
		
		logout.setVisible(true);
		logout.setTabIndex(-1);
		

		menuLayout.addComponent(barraH);
		menuLayout.setComponentAlignment(barraH, Alignment.TOP_LEFT);

		viewContainer = new Panel();
		viewContainer.setStyleName("views-panel");
		viewContainer.setSizeFull();


		
		layout.addComponent(menuLayout);
		layout.addComponent(viewContainer);
		  
		layout.setExpandRatio(viewContainer,1);
		
        super.setCompositionRoot(layout);
		super.setSizeFull();
		

		
		
	}



	
	
	/**
	 * MÃ©todo que realiza el logout de la aplicaciÃ³n.
	 */
	private void doLogout() {
		
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
    	ConfirmDialog.show(UI.getCurrent(), "LOGOUT", "Are you sure disconnect from  session ? \n"+
    	"\n\n" + "Data has not been saved will be LOST",
    	        "Yes", "No", new ConfirmDialog.Listener() {

    	            public void onClose(ConfirmDialog dialog) {
    	                if (dialog.isConfirmed()) {
    	                    // Confirmed to continue
    	                	//VaadinSession.getCurrent().getSession().invalidate();
    	                	
    	                	// Vaciamos las variables de session
    	                	UI.getCurrent().getSession().close();
    	                	getUI().close();
    	            		getUI().getPage().reload();
    	                } else {
    	                    // User did not confirm
    	                    
    	                }
    	            }
    	        });
	
	}
	
   

}
