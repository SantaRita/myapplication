package com.meditacionback.utiles;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class CuadroDash extends CustomComponent   {

	public HorizontalLayout layout = new HorizontalLayout(); 
	
	public int idQuery;
	public String Description;
	public String tablesUpdate;
	public String columnsUpdate;
	public String whereUpdate;
	public CuadroDash ( String caption, String fondo) {
		
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName(fondo);
		layout.setWidth("100%");
		layout.setHeight("100px");
		Label etiqueta = new Label(caption);
		etiqueta.addStyleName("wrap");
		etiqueta.setWidth(null);
		layout.addComponent(etiqueta);
		layout.setMargin(true);
		layout.setComponentAlignment(etiqueta, Alignment.MIDDLE_CENTER);
		
		
		setCompositionRoot(layout);
		

	}
}
