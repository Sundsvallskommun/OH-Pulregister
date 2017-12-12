/*******************************************************************************
 * Copyright (c) 2010 Robert "Unlogic" Olofsson (unlogic@unlogic.se).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 ******************************************************************************/
package se.unlogic.hierarchy.core.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.enums.DisplayType;
import se.unlogic.hierarchy.core.exceptions.InvalidSettingException;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

//TODO replace the usage of this class with the newer implementation in the se.unlogic.hierarchy.core.settings package
public class SettingDescriptor implements Elementable{

	private final String id;
	private final String name;
	private final String description;
	private final DisplayType displayType;
	private final boolean required;
	private final boolean splitOnLineBreak;

	private final List<String> defaultValues;
	private final StringFormatValidator formatValidator;
	private final List<ValueDescriptor> allowedValues;

	private SettingDescriptor(String id, String name, String description, DisplayType displayType, boolean required, String defaultValue, StringFormatValidator formatValidator, boolean splitOnLineBreak) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.displayType = displayType;
		this.required = required;
		this.formatValidator = formatValidator;
		this.allowedValues = null;

		if(defaultValue != null){
			
			this.defaultValues = Collections.singletonList(defaultValue);
			
		}else{
			
			this.defaultValues = null;
		}
		
		if(displayType == DisplayType.TEXTAREA){
			
			this.splitOnLineBreak = splitOnLineBreak;
			
		}else{
			
			this.splitOnLineBreak = false;
		}
	}	
	
	private SettingDescriptor(String id, String name, String description, DisplayType displayType, boolean required, String defaultValue, StringFormatValidator formatValidator, List<ValueDescriptor> allowedValues) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.displayType = displayType;
		this.required = required;
		this.formatValidator = formatValidator;
		this.allowedValues = allowedValues;
		this.splitOnLineBreak = false;
		
		if(defaultValue != null){
			
			this.defaultValues = Collections.singletonList(defaultValue);
			
		}else{
			
			this.defaultValues = null;
		}
	}
	
	private SettingDescriptor(String id, String name, String description, DisplayType displayType, boolean required, List<String> defaultValues, StringFormatValidator formatValidator, List<ValueDescriptor> allowedValues) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.displayType = displayType;
		this.required = required;
		this.formatValidator = formatValidator;
		this.allowedValues = allowedValues;
		this.defaultValues = defaultValues;
		this.splitOnLineBreak = false;
	}	
	
	private SettingDescriptor(String id, String name, String description, DisplayType displayType, boolean required, String defaultValue, StringFormatValidator formatValidator, ValueDescriptor... allowedValues) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.displayType = displayType;
		this.required = required;
		this.formatValidator = formatValidator;
		this.splitOnLineBreak = false;

		if(defaultValue != null){
			
			this.defaultValues = Collections.singletonList(defaultValue);
			
		}else{
			
			this.defaultValues = null;
		}		
		
		if (allowedValues != null) {
			this.allowedValues = Arrays.asList(allowedValues);
		} else {
			this.allowedValues = null;
		}
	}	
	
	private SettingDescriptor(String id, String name, String description, DisplayType displayType, boolean required, List<String> defaultValues, StringFormatValidator formatValidator, ValueDescriptor... allowedValues) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.displayType = displayType;
		this.required = required;
		this.formatValidator = formatValidator;
		this.defaultValues = defaultValues;
		this.splitOnLineBreak = false;

		if (allowedValues != null) {
			this.allowedValues = Arrays.asList(allowedValues);
		} else {
			this.allowedValues = null;
		}
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public DisplayType getDisplayType() {
		return this.displayType;
	}

	public boolean isRequired() {
		return this.required;
	}

	public List<ValueDescriptor> getAllowedValues() {
		return this.allowedValues;
	}

	@Override
	public final Element toXML(Document doc) {

		Element settingDescriptorElement = doc.createElement("settingDescriptor");

		settingDescriptorElement.appendChild(XMLUtils.createElement("id", this.id, doc));
		settingDescriptorElement.appendChild(XMLUtils.createCDATAElement("name", this.name, doc));

		if (this.description != null) {
			settingDescriptorElement.appendChild(XMLUtils.createCDATAElement("description", this.description, doc));
		}

		settingDescriptorElement.appendChild(XMLUtils.createElement("displayType", this.displayType.toString(), doc));
		settingDescriptorElement.appendChild(XMLUtils.createElement("required", Boolean.toString(this.required), doc));

		if (this.defaultValues != null) {
			
			for(String defaultValue : defaultValues){
				
				settingDescriptorElement.appendChild(XMLUtils.createElement("defaultValue", defaultValue, doc));
			}
		}

		if (this.allowedValues != null && !this.allowedValues.isEmpty()) {
			Element allowedValuesElement = doc.createElement("allowedValues");
			settingDescriptorElement.appendChild(allowedValuesElement);

			for (ValueDescriptor value : this.allowedValues) {
				allowedValuesElement.appendChild(value.toXML(doc));
			}
		}

		XMLUtils.appendNewElement(doc, settingDescriptorElement, "splitOnLineBreak", this.splitOnLineBreak);
		
		return settingDescriptorElement;
	}

	public static SettingDescriptor createTextFieldSetting(String id, String name, String description, boolean required, String defaultValue, StringFormatValidator formatValidator) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.TEXTFIELD, required, defaultValue, formatValidator, false);
	}

	public static SettingDescriptor createTextAreaSetting(String id, String name, String description, boolean required, String defaultValue, StringFormatValidator formatValidator) {
		
		return createTextAreaSetting(id, name, description, required, defaultValue, formatValidator, false);
	}
	
	public static SettingDescriptor createTextAreaSetting(String id, String name, String description, boolean required, String defaultValue, StringFormatValidator formatValidator, boolean splitOnLineBreak) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.TEXTAREA, required, defaultValue, formatValidator, splitOnLineBreak);
	}

	public static SettingDescriptor createHTMLEditorSetting(String id, String name, String description, boolean required, String defaultValue, StringFormatValidator formatValidator) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.HTML_EDITOR, required, defaultValue, formatValidator, false);
	}

	public static SettingDescriptor createCheckboxSetting(String id, String name, String description, boolean defaultValue) {
		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.CHECKBOX, false, Boolean.toString(defaultValue), null, false);
	}

	public static SettingDescriptor createRadioButtonSetting(String id, String name, String description, boolean required, String defaultValue, List<ValueDescriptor> allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (allowedValues == null || allowedValues.isEmpty()) {
			throw new InvalidSettingException("Allowedvalues cannot be null or empty");
		}

		return new SettingDescriptor(id, name, description, DisplayType.RADIOBUTTON, required, defaultValue, null, allowedValues);
	}

	public static SettingDescriptor createRadioButtonSetting(String id, String name, String description, boolean required, String defaultValue, ValueDescriptor... allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (allowedValues == null) {
			throw new InvalidSettingException("Allowedvalues cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.RADIOBUTTON, required, defaultValue, null, allowedValues);
	}

	public static SettingDescriptor createDropDownSetting(String id, String name, String description, boolean required, String defaultValue, List<ValueDescriptor> allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (allowedValues == null || allowedValues.isEmpty()) {
			throw new InvalidSettingException("Allowedvalues cannot be null or empty");
		}

		return new SettingDescriptor(id, name, description, DisplayType.DROPDOWN, required, defaultValue, null, allowedValues);
	}

	public static SettingDescriptor createDropDownSetting(String id, String name, String description, boolean required, String defaultValue, ValueDescriptor... allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (allowedValues == null) {
			throw new InvalidSettingException("Allowedvalues cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.DROPDOWN, required, defaultValue, null, allowedValues);
	}

	public static SettingDescriptor createMultiListSetting(String id, String name, String description, boolean required, List<String> defaultValues, List<ValueDescriptor> allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (required && (allowedValues == null || allowedValues.isEmpty())) {
			throw new InvalidSettingException("Allowedvalues cannot be null or empty");
		}

		return new SettingDescriptor(id, name, description, DisplayType.MULTILIST, required, defaultValues, null, allowedValues);
	}

	public static SettingDescriptor createMultiListSetting(String id, String name, String description, boolean required, List<String> defaultValues, ValueDescriptor... allowedValues) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		} else if (allowedValues == null) {
			throw new InvalidSettingException("Allowedvalues cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.MULTILIST, required, defaultValues, null, allowedValues);
	}

	public List<String> getDefaultValues() {
		return this.defaultValues;
	}

	public static SettingDescriptor createPasswordFieldSetting(String id,
			String name, String description, boolean required, String defaultValue,
			StringFormatValidator formatValidator) {

		if (id == null) {
			throw new InvalidSettingException("ID cannot be null");
		} else if (name == null) {
			throw new InvalidSettingException("Name cannot be null");
		}

		return new SettingDescriptor(id, name, description, DisplayType.PASSWORD, required, defaultValue, formatValidator, false);
	}


	public StringFormatValidator getFormatValidator() {
		return formatValidator;
	}

	
	public boolean isSplitOnLineBreak() {
	
		return splitOnLineBreak;
	}
}
