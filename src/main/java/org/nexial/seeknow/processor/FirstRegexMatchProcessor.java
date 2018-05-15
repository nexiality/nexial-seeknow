package org.nexial.seeknow.processor;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.nexial.seeknow.SeeknowData;

public class FirstRegexMatchProcessor extends RegexMatchProcessor {

	public FirstRegexMatchProcessor(String regex) { super(regex); }

	@Override
	public boolean processMatch(SeeknowData match) {
		if (match == null || StringUtils.isBlank(match.getText())) { return !stopOnEmptyText; }

		Matcher matcher = pattern.matcher(match.getText());
		if (matcher.find()) {
			data.add(match);
			return false;
		}

		return true;
	}

}
