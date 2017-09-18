package org.asciidocgenerator.ui.admin;

import java.util.Map;

public class ManipulateSettingCommand {

	private final String setting;
	private final Map<String, String[]> values;

	public ManipulateSettingCommand(String setting, Map<String, String[]> values) {
		super();
		this.setting = setting;
		this.values = values;
	}

	public String getSetting() {
		return setting;
	}

	public Map<String, String[]> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "ManipulateSettingCommand [setting=" + setting + ", values=" + values + "]";
	}

}
