/*******************************************************************************
 * Copyright (c) 2010 Robert "Unlogic" Olofsson (unlogic@unlogic.se).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 ******************************************************************************/
package se.unlogic.webutils.populators;

import se.unlogic.standardutils.populators.StringURLPopulator;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.webutils.http.HTTPUtils;


public class StringHTTPURLPopulator extends StringURLPopulator {

	private static final StringHTTPURLPopulator POPULATOR = new StringHTTPURLPopulator();
	
	public StringHTTPURLPopulator() {
		super();
	}

	public StringHTTPURLPopulator(String populatorID, StringFormatValidator formatValidator) {
		super(populatorID, formatValidator);
	}

	public StringHTTPURLPopulator(String populatorID) {
		super(populatorID);
	}

	@Override
	public boolean validateDefaultFormat(String value) {

		return HTTPUtils.isValidURL(value);
	}

	public static StringHTTPURLPopulator getPopulator() {

		return POPULATOR;
	}
}
