package org.nexial.seeknow.processor;

import org.apache.commons.lang3.StringUtils;
import org.nexial.seeknow.SeeknowData;

public class FirstContainsProcessor extends AcceptAllProcessor {
	protected String containing;

	public FirstContainsProcessor(String containing) { this.containing = containing; }

	@Override
	public boolean processMatch(SeeknowData match) {
		if (match == null || StringUtils.isBlank(match.getText())) { return !stopOnEmptyText; }

		if (StringUtils.contains(match.getText(), containing)) {
			data.add(match);
			return false;
		}

		return true;
	}

}
