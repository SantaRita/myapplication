package com.example.myapplication;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MenuPrincipal extends CustomComponent  {
    private static final long serialVersionUID = -3354143876393393750L;

    public HorizontalLayout menuOpcionesBoton;
    

    public MenuPrincipal ( )  {
    	
		// MENU DE OPCIONES
		
		HorizontalLayout menuOpcionesLayout = new HorizontalLayout();
		menuOpcionesBoton = new HorizontalLayout();
		HorizontalLayout menuOpcionesLayout3 = new HorizontalLayout();
		
		menuOpcionesBoton.setStyleName("menu-pestana");
		
		menuOpcionesLayout.setMargin(false);
		menuOpcionesLayout.setStyleName("menu-opciones-panel");
		menuOpcionesLayout.addComponent(menuOpcionesBoton);
		
		
		// opciones de menu
		
		VerticalLayout ventana = new VerticalLayout();
	
		VerticalLayout gridMenu = new VerticalLayout();
		
		//gridMenu.setStyleName("titulo-opciones-menu");
		gridMenu.setWidth("100px");
		Label opcion0 = new Label("&nbsp;&nbsp;Dashboard", Label.CONTENT_XHTML);
		Label opcion1 = new Label("&nbsp;&nbsp;Customers", Label.CONTENT_XHTML);
		Label opcion2 = new Label("&nbsp;&nbsp;Terms & Conditions", Label.CONTENT_XHTML);
		Label opcion3 = new Label("&nbsp;&nbsp;Questions & Answers", Label.CONTENT_XHTML);
		Label opcion4 = new Label("&nbsp;&nbsp;Questions Test By Level", Label.CONTENT_XHTML);
		Label opcion5 = new Label("&nbsp;&nbsp;Objects", Label.CONTENT_XHTML);
		Label opcion6 = new Label("&nbsp;&nbsp;Packs", Label.CONTENT_XHTML);
		Label opcion7 = new Label("&nbsp;&nbsp;Packs By Level", Label.CONTENT_XHTML);
		Label opcion8 = new Label("&nbsp;&nbsp;Tips", Label.CONTENT_XHTML);
		

		gridMenu.setWidth("200px");
		

		
		Resource resMenu = new ThemeResource("img/logo_arrow_orange_100.png_quitar");
		Image imageMenu = new Image(null, resMenu);
		imageMenu.setHeight("40px");
		

		VerticalLayout hlOpc0 = new VerticalLayout();
		hlOpc0.addStyleName("titulo-opciones-menu");
		hlOpc0.setHeight("30px");
		hlOpc0.setWidth("100%");
		hlOpc0.setMargin(false);
		hlOpc0.addComponent(opcion0);
		hlOpc0.setComponentAlignment(opcion0, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc1 = new VerticalLayout();
		hlOpc1.addStyleName("titulo-opciones-menu");
		hlOpc1.setHeight("30px");
		hlOpc1.setWidth("100%");
		hlOpc1.setMargin(false);
		hlOpc1.addComponent(opcion1);
		hlOpc1.setComponentAlignment(opcion1, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc2 = new VerticalLayout();
		hlOpc2.addStyleName("titulo-opciones-menu");
		hlOpc2.setHeight("30px");
		hlOpc2.setWidth("100%");
		hlOpc2.setMargin(false);
		hlOpc2.addComponent(opcion2);
		hlOpc2.setComponentAlignment(opcion2, Alignment.MIDDLE_LEFT);

		VerticalLayout hlOpc3 = new VerticalLayout();
		hlOpc3.addStyleName("titulo-opciones-menu");
		hlOpc3.setHeight("30px");
		hlOpc3.setWidth("100%");
		hlOpc3.setMargin(false);
		hlOpc3.addComponent(opcion3);
		hlOpc3.setComponentAlignment(opcion3, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc4 = new VerticalLayout();
		hlOpc4.addStyleName("titulo-opciones-menu");
		hlOpc4.setHeight("30px");
		hlOpc4.setWidth("100%");
		hlOpc4.setMargin(false);
		hlOpc4.addComponent(opcion4);
		hlOpc4.setComponentAlignment(opcion4, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc5 = new VerticalLayout();
		hlOpc5.addStyleName("titulo-opciones-menu");
		hlOpc5.setHeight("30px");
		hlOpc5.setWidth("100%");
		hlOpc5.setMargin(false);
		hlOpc5.addComponent(opcion5);
		hlOpc5.setComponentAlignment(opcion5, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc6 = new VerticalLayout();
		hlOpc6.addStyleName("titulo-opciones-menu");
		hlOpc6.setHeight("30px");
		hlOpc6.setWidth("100%");
		hlOpc6.setMargin(false);
		hlOpc6.addComponent(opcion6);
		hlOpc6.setComponentAlignment(opcion6, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc7 = new VerticalLayout();
		hlOpc7.addStyleName("titulo-opciones-menu");
		hlOpc7.setHeight("30px");
		hlOpc7.setWidth("100%");
		hlOpc7.setMargin(false);
		hlOpc7.addComponent(opcion7);
		hlOpc7.setComponentAlignment(opcion7, Alignment.MIDDLE_LEFT);
		
		VerticalLayout hlOpc8 = new VerticalLayout();
		hlOpc8.addStyleName("titulo-opciones-menu");
		hlOpc8.setHeight("30px");
		hlOpc8.setWidth("100%");
		hlOpc8.setMargin(false);
		hlOpc8.addComponent(opcion8);
		hlOpc8.setComponentAlignment(opcion8, Alignment.MIDDLE_LEFT);		
		

		VerticalLayout hlImagen = new VerticalLayout();
		hlImagen.addComponent(imageMenu);
		hlImagen.setStyleName("titulo-imagen-menu");
		hlImagen.setComponentAlignment(imageMenu, Alignment.BOTTOM_CENTER);
		gridMenu.addComponent(hlImagen);
		
		Label lblUsuarioLogin = new Label(UI.getCurrent().getSession().getAttribute("username").toString());
		VerticalLayout hlLabelUsu = new VerticalLayout();

		hlLabelUsu.addComponent(lblUsuarioLogin);
		lblUsuarioLogin.setStyleName("titulo-menu");
		gridMenu.addComponent(hlLabelUsu);		
		
		
		gridMenu.addComponent(lblUsuarioLogin);
	    gridMenu.addComponent(hlOpc0);
	    gridMenu.addComponent(hlOpc1);
		gridMenu.addComponent(hlOpc2);
		gridMenu.addComponent(hlOpc3);
		gridMenu.addComponent(hlOpc4);
		gridMenu.addComponent(hlOpc5);
		gridMenu.addComponent(hlOpc6);
		gridMenu.addComponent(hlOpc7);
		gridMenu.addComponent(hlOpc8);
		
		gridMenu.setHeight("330px");

        menuOpcionesLayout.addComponent(gridMenu);
		setWidth("100%");
        setCompositionRoot(menuOpcionesLayout);
        
        // eventos del click de lasopciones del menu
        
        // dashboard		
        hlOpc0.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaDashboard");	 
			}
		});
        
        // OPCION CUSTOMERS
        hlOpc1.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaCustomers");	 
			}
		});
        
        // OPCION TERMS & CONDITIONS
        hlOpc2.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaTerms");	 
			}
		});
        
        // OPCION QUESTIONS
        hlOpc3.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaQuestions");	 
			}
		});
        
        // OPCION QUESTIONS by LEVEL
        hlOpc4.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaQuestionsByLevel");	 
			}
		});

        // OPCION OBJECTS
        hlOpc5.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaObjects");	 
			}
		});
        
        // OPCION PACKS
        hlOpc6.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaPacks");	 
			}
		});
        
        // OPCION PACKS by level
        hlOpc7.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaPacksByLevel");	 
			}
		});
        
        // OPCION Tips
        hlOpc8.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				// TODO Auto-generated method stub
				UI.getCurrent().getNavigator().navigateTo("PantallaTips");	 
			}
		});        
      
    }

    private class HorizontalRule extends Label {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public HorizontalRule() {
          super("<hr style='color:blue;height:1px' />", ContentMode.HTML);
        }
      }
    
     
}
