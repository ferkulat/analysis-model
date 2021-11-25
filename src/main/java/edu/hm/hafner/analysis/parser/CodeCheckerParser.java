package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the clang-tidy static analysis warnings parsed by Codechecker.
 *
 * Codechecker parses the *.plist files and converts it into plain text file.
 *
 * Better for human readers and for using grep and diff
 * It also puts the human-readable Severity at the start of a line.
 *
 */
public class CodeCheckerParser extends LookaheadParser {
    private static final long serialVersionUID = -3015592762345283582L;
    private static final String CODE_CHECKER_DEFECT_PATTERN =
            "^\\[(?<severity>CRITICAL|HIGH|MEDIUM|LOW)\\] (?<path>.+):(?<line>\\d+):(?<column>\\d+): (?<message>.*?) \\[(?<category>[^\\s]*?)\\]$";

    /**
     * Creates a new instance of {@link CodeCheckerParser}.
     */
    public CodeCheckerParser() {
        super(CODE_CHECKER_DEFECT_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity severity = getSeverity(matcher.group("severity"));
        return builder.setFileName(matcher.group("path"))
                .setSeverity(severity)
                .setLineStart(matcher.group("line"))
                .setColumnStart(matcher.group("column"))
                .setCategory(matcher.group("category"))
                .setMessage(createLineWithPointerToColumn(Integer.parseInt(matcher.group("column"))) + matcher.group("message"))
                .buildOptional();
    }
    private String addUnderscores(final int column) {
        String result = "";
        for (int i = 0; i < column - 1; i++) {
            result = result.concat("_");
        }
        return result;
    }
    private String createLineWithPointerToColumn(final int column) {
        return addUnderscores(column) + "^\n    ";
    }
    private Severity getSeverity(final String severityText) {
        
        if (severityText.contains("CRITICAL")) {
            return Severity.ERROR;
        }
        if (severityText.contains("HIGH")) {
            return Severity.WARNING_HIGH;
        }
        if (severityText.contains("MEDIUM")) {
            return Severity.WARNING_NORMAL;
        }
        return  Severity.WARNING_LOW;
    }
}
