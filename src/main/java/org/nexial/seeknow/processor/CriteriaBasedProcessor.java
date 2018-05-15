package org.nexial.seeknow.processor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.nexial.seeknow.SeeknowData;

public class CriteriaBasedProcessor extends AcceptAllProcessor {

    private SeeknowCriteria criteria;

    public CriteriaBasedProcessor(SeeknowCriteria criteria) {
        this.criteria = criteria;
        this.stopOnEmptyText = criteria.isStopOnEmptyText();
    }

    public boolean skipLine(int lineNumber) {

        if (this.criteria.getLimitRows() == null || this.criteria.getLimitRows().isEmpty()) { return false; }

        String[] lines = this.criteria.getLimitRows().split(",");
        List<String> numberList = Arrays.asList(lines);
        return !numberList.contains(lineNumber + "");

    }

    @Override
    public boolean processMatch(SeeknowData match) {
        // match 'stopOnEmptyText'
        if (match == null) { return !this.stopOnEmptyText; }

        String matchText = match.getText();
        if (StringUtils.isBlank(matchText)) { return !this.stopOnEmptyText; }

        // match 'rows'
        int lineNumber = match.getLineNumber();
        boolean isLastRow = criteria.isLastRow(lineNumber);
        if (!criteria.isValidRow(lineNumber)) { return !isLastRow; }

        // match 'color'
        if (criteria.getColor() != null) {
            boolean colorFound = match.getColors().contains(criteria.getColor());

            // don't want this line, but we don't need to stop either
            if (!colorFound) { return !isLastRow; }
        }

        // match 'regex' or match 'contains'
        String regex = criteria.getRegex();
        if (StringUtils.isNotBlank(regex)) {
            if (criteria.matchByRegex(matchText)) {
                this.data.add(match);
                return !criteria.isStopOnMatch() && !isLastRow;
            } else {
                return !isLastRow;
            }
        }

        String contains = criteria.getContains();
        if (StringUtils.isNotBlank(contains)) {
            if (StringUtils.contains(matchText, contains)) {
                this.data.add(match);
                return !criteria.isStopOnMatch() && !isLastRow;
            } else {
                return !isLastRow;
            }
        }

        this.data.add(match);
        return !isLastRow;
    }
}

