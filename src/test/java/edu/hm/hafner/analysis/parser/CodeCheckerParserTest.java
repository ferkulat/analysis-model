package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class CodeCheckerParserTest extends AbstractParserTest {

    CodeCheckerParserTest() {
        super("CodeChecker_with_linux_paths.txt");
    }

    @Override
    protected CodeCheckerParser createParser() {
        return new CodeCheckerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasLineStart(17)
                .hasColumnStart(8)
                .hasFileName("/path/to/projrct/csv2xlslib.Test/parsecmdTest.cpp")
                .hasMessage("_______^\nclass 'TheFixture' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(1))
                .hasLineStart(425)
                .hasColumnStart(33)
                .hasFileName("/path/to/projrct/extern/lib/workbook.cpp")
                .hasMessage("________________________________^\nCalled C++ object pointer is null")
                .hasCategory("core.CallAndMessage")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(2))
                .hasLineStart(212)
                .hasColumnStart(12)
                .hasFileName("/path/to/projrct/extern/lib/HPSF.cpp")
                .hasMessage("___________^\n'signed char' to 'int' conversion; consider casting to 'unsigned char' first.")
                .hasCategory("bugprone-signed-char-misuse")
                .hasSeverity(Severity.WARNING_NORMAL);

    }

    @Test
    void shouldParseWindowsPaths() {
        Report report = parse("CodeChecker_with_windows_paths.txt");
        assertThat(report).hasSize(5);
        assertThat(report.get(0))
                .hasLineStart(15)
                .hasColumnStart(22)
                .hasFileName("C:/path/to/project/cmake-build-debug/_deps/checked_cmd-src/Tests/ArgumentsTest.cpp")
                .hasMessage("_____________________^\n'strncpy' is deprecated: This function or variable may be unsafe. Consider using strncpy_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.")
                .hasCategory("clang-diagnostic-deprecated-declarations")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasLineStart(283)
                .hasColumnStart(22)
                .hasFileName("C:/Program Files (x86)/path/to/toolchain/include/abcddef")
                .hasMessage("_____________________^\n'auto' return without trailing return type; deduced return types are a C++14 extension")
                .hasCategory("clang-diagnostic-error")
                .hasSeverity(Severity.ERROR);

        assertThat(report.get(2))
                .hasLineStart(17)
                .hasColumnStart(8)
                .hasFileName("C:/path/to/project/csv2xlslib.Test/parsecmdTest.cpp")
                .hasMessage("_______^\nclass 'TheFixture' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(3))
                .hasLineStart(49)
                .hasColumnStart(8)
                .hasFileName("C:/path/to/project/csv2xlslib.Test/parseCsvStreamTest.cpp")
                .hasMessage("_______^\nclass 'Given_an_input_file_with_headline' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(report.get(4))
                .hasLineStart(924)
                .hasColumnStart(49)
                .hasFileName("C:/path/to/project/extern/lib/formula_expr.cpp")
                .hasMessage("________________________________________________^\nsuspicious usage of 'sizeof(A*)'; pointer to aggregate")
                .hasCategory("bugprone-sizeof-expression")
                .hasSeverity(Severity.WARNING_HIGH);

    }
}
