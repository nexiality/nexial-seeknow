package org.nexial.seeknow.processor;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.nexial.seeknow.SeeknowColor;

public class SeeknowCriteria {
    /** if true, stop scanning when first match is found */
    private boolean stopOnMatch;

    /** specify the "substring" to match by */
    private String contains;

    /** if true, then only match the characters specified in contains.  DOES NOT WORK WHEN REGEX IS SPECIFIED */
    private boolean limitMatch;

    /** specify the regex to match by; takes precedence over "contains" match */
    private String regex;
    private Pattern pattern;

    /** if true, seeknow stops upon finding a "blank" row (rows with only white color) */
    private boolean stopOnEmptyText;

    /** only scanned the specified rows, which are specified as zero-based, single number or range */
    private String limitRows;
    private Set<Integer> rows;

    /** match only when a row contains the specified color */
    private SeeknowColor color;

    public boolean isStopOnMatch() { return stopOnMatch;}

    public void setStopOnMatch(boolean stopOnMatch) { this.stopOnMatch = stopOnMatch;}

    public String getContains() { return contains;}

    public void setContains(String contains) { this.contains = contains;}

    public String getRegex() { return regex;}

    public void setRegex(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public boolean matchByRegex(String text) {
        return !StringUtils.isEmpty(regex) && !StringUtils.isBlank(text) && this.pattern.matcher(text).find();
    }

    public boolean isStopOnEmptyText() { return stopOnEmptyText;}

    public void setStopOnEmptyText(boolean stopOnEmptyText) { this.stopOnEmptyText = stopOnEmptyText;}

    public String getLimitRows() { return limitRows;}

    public void setLimitRows(String limitRows) {
        this.limitRows = StringUtils.trim(limitRows);

        rows = new TreeSet<>();
        if (StringUtils.equals(this.limitRows, "*")) { return; }
        if (NumberUtils.isDigits(this.limitRows)) {
            rows.add(NumberUtils.toInt(limitRows));
            return;
        }

        String[] rowsArray = StringUtils.split(this.limitRows, ",");
        if (ArrayUtils.isEmpty(rowsArray)) { return; }

        Arrays.stream(rowsArray).forEach(r -> {
            r = StringUtils.trim(r);
            if (NumberUtils.isDigits(r)) {
                rows.add(NumberUtils.toInt(r));
            } else {
                throw new IllegalArgumentException("Invalid row index specified: " + r);
            }
        });
    }

    public boolean isValidRow(int rowIndex) { return CollectionUtils.isEmpty(rows) || rows.contains(rowIndex); }

    public boolean isLastRow(int rowIndex) {
        if (CollectionUtils.isEmpty(rows)) { return false; }

        Integer[] rowIndices = rows.toArray(new Integer[rows.size()]);
        int lastRow = rowIndices[rowIndices.length - 1];
        return rowIndex >= lastRow;
    }

    public boolean isLimitMatch() { return limitMatch; }

    public void setLimitMatch(boolean limitMatch) { this.limitMatch = limitMatch; }

    public SeeknowColor getColor() { return color;}

    public void setColor(SeeknowColor color) { this.color = color;}

    @Override
    public String toString() {
        return "contains=" + contains +
               "regex=" + regex +
               "stopOnEmptyText=" + stopOnEmptyText +
               "limitRows=" + limitRows +
               "limitMatch=" + limitMatch +
               "color=" + color;
    }
}
