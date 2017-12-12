package se.unlogic.hierarchy.core.settings;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.validationerrors.FileCountExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.InvalidFileExtensionValidationError;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;

public class MultiFileUploadSetting extends Setting {

	protected final StringFormatValidator formatValidator;
	protected final List<String> allowedFileExtensions;
	protected final Integer maxFileSize;
	protected final int maxFileCount;

	MultiFileUploadSetting(String id, String name, String description, FormElement formElement, boolean required, boolean disabled, StringFormatValidator formatValidator, List<String> allowedFileExtensions, Integer maxFileSize, Integer maxFileCount) {

		super(id, name, description, formElement, required, disabled);

		this.formatValidator = formatValidator;
		this.allowedFileExtensions = allowedFileExtensions;
		this.maxFileSize = maxFileSize;
		this.maxFileCount = maxFileCount;
	}

	public MultiFileUploadSetting(String id, String name, String description, boolean required, List<String> allowedFileExtensions, Integer maxFileSize, Integer maxFileCount) {

		this(id, name, description, required, false, allowedFileExtensions, maxFileSize, maxFileCount);
	}

	public MultiFileUploadSetting(String id, String name, String description, boolean required, StringFormatValidator formatValidator, List<String> allowedFileExtensions, Integer maxFileSize, Integer maxFileCount) {

		this(id, name, description, required, false, formatValidator, allowedFileExtensions, maxFileSize, maxFileCount);
	}

	public MultiFileUploadSetting(String id, String name, String description, boolean required, boolean disabled, List<String> allowedFileExtensions, Integer maxFileSize, Integer maxFileCount) {

		super(id, name, description, FormElement.FILE_UPLOAD_MULTI, required, disabled);

		this.formatValidator = null;
		this.allowedFileExtensions = allowedFileExtensions;
		this.maxFileSize = maxFileSize;
		this.maxFileCount = maxFileCount;
	}

	public MultiFileUploadSetting(String id, String name, String description, boolean required, boolean disabled, StringFormatValidator formatValidator, List<String> allowedFileExtensions, Integer maxFileSize, Integer maxFileCount) {

		this(id, name, description, FormElement.FILE_UPLOAD_MULTI, required, disabled, formatValidator, allowedFileExtensions, maxFileSize, maxFileCount);
	}

	@Override
	public List<String> getDefaultValues() {

		return allowedFileExtensions;
	}

	@Override
	protected List<Alternative> getAlternatives() {

		List<Alternative> alternatives = new ArrayList<Alternative>();

		alternatives.add(new Alternative("MaxFileCount", Integer.toString(maxFileCount)));

		if (maxFileSize != null) {
			alternatives.add(new Alternative("FormattedMaxSize", BinarySizeFormater.getFormatedSize(maxFileSize)));
		}

		return alternatives;
	}

	@Override
	public List<String> parseAndValidate(List<String> values, List<ValidationError> validationErrors) {

		boolean errors = false;

		if (values.size() > maxFileCount) {

			validationErrors.add(new FileCountExceededValidationError(maxFileCount, getName()));
			errors = true;
		}

		if (formatValidator != null) {

			for (String value : values) {

				if (!formatValidator.validateFormat(value)) {

					validationErrors.add(new ValidationError("setting-" + getId(), getName(), ValidationErrorType.InvalidFormat));
					errors = true;
				}
			}
		}

		if (allowedFileExtensions != null) {

			for (String value : values) {

				String fileExtension = FileUtils.getFileExtension(value);

				if (!allowedFileExtensions.contains(fileExtension)) {

					validationErrors.add(new InvalidFileExtensionValidationError(value, allowedFileExtensions, getName()));
					errors = true;
				}
			}
		}

		if (values.size() > 1) {
			for (int i = 0; i < values.size(); i++) {

				String filename = values.get(i);

				for (int j = 1 + i; j < values.size(); j++) {

					String otherFilename = values.get(j);

					if (filename.equals(otherFilename)) {

						validationErrors.add(new ValidationError("DuplicateFilename", getName(), filename));
						errors = true;
					}

					String asciiFilename = FileUtils.toAsciiFilename(filename);
					String asciiFilename2 = FileUtils.toAsciiFilename(otherFilename);

					if (asciiFilename.equals(asciiFilename2)) {

						validationErrors.add(new ValidationError("DuplicateFilename", getName(), asciiFilename));
						errors = true;
					}
				}
			}
		}

		if (errors) {

			return null;
		}

		return values;
	}

	public Integer getMaxFileSize() {

		return maxFileSize;
	}

	public int getMaxFileCount() {

		return maxFileCount;
	}

}
