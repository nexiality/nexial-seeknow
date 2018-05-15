package org.nexial.seeknow.processor;

import java.util.List;

import org.nexial.seeknow.SeeknowData;

public interface SeeknowProcessor {


	boolean processMatch(SeeknowData match);

	List<SeeknowData> listMatch();
}
