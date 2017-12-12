package se.unlogic.hierarchy.core.settings;

import java.util.List;

import se.unlogic.standardutils.validation.StringFormatValidator;

public class SingleFileUploadSetting extends MultiFileUploadSetting {

	public SingleFileUploadSetting(String id, String name, String description, boolean required, List<String> allowedFileExtensions, Integer maxFileSize) {

		this(id, name, description, required, false, allowedFileExtensions, maxFileSize);
	}

	public SingleFileUploadSetting(String id, String name, String description, boolean required, StringFormatValidator formatValidator, List<String> allowedFileExtensions, Integer maxFileSize) {

		this(id, name, description, required, false, formatValidator, allowedFileExtensions, maxFileSize);
	}

	public SingleFileUploadSetting(String id, String name, String description, boolean required, boolean disabled, List<String> allowedFileExtensions, Integer maxFileSize) {

		super(id, name, description, FormElement.FILE_UPLOAD, required, disabled, null, allowedFileExtensions, maxFileSize, 1);
	}

	public SingleFileUploadSetting(String id, String name, String description, boolean required, boolean disabled, StringFormatValidator formatValidator, List<String> allowedFileExtensions, Integer maxFileSize) {

		super(id, name, description, FormElement.FILE_UPLOAD, required, disabled, formatValidator, allowedFileExtensions, maxFileSize, 1);
	}

}
