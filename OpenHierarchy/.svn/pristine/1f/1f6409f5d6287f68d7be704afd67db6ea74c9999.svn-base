package se.unlogic.hierarchy.foregroundmodules;

import java.lang.reflect.Field;

import se.unlogic.hierarchy.core.annotations.WebPublicMenuItem;

public class MenuItemMapping {

	private final WebPublicMenuItem menuItemAnnotation;
	private final Field nameField;
	private final Field descriptionField;

	public MenuItemMapping(WebPublicMenuItem menuItemAnnotation, Field nameField, Field descriptionField) {

		super();
		this.menuItemAnnotation = menuItemAnnotation;
		this.nameField = nameField;
		this.descriptionField = descriptionField;
	}

	public WebPublicMenuItem getMenuItemAnnotation() {

		return menuItemAnnotation;
	}

	public Field getNameField() {

		return nameField;
	}

	public Field getDescriptionField() {

		return descriptionField;
	}
}