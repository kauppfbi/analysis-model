package edu.hm.hafner.analysis;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issues.IssueFilterBuilder;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link IssueFilterBuilder}.
 *
 * @author Raphael Furch
 */
public class IssueFilterTest {

    /**
     * Get issues containing issue 1, 2 and 3.
     *
     * @return issues.
     */
    private Issues<Issue> getIssues() {
        Issues<Issue> issues = new Issues<>();
        issues.add(ISSUE1, ISSUE2, ISSUE3);
        return issues;
    }

    private static final Issue ISSUE1 = new IssueBuilder()
            .setFileName("FileName1")
            .setPackageName("PackageName1")
            .setModuleName("ModuleName1")
            .setCategory("CategoryName1")
            .setType("Type1")
            .build();
    private static final Issue ISSUE2 = new IssueBuilder()
            .setFileName("FileName2")
            .setPackageName("PackageName2")
            .setModuleName("ModuleName2")
            .setCategory("CategoryName2")
            .setType("Type2")
            .build();

    private static final Issue ISSUE3 = new IssueBuilder()
            .setFileName("FileName3")
            .setPackageName("PackageName3")
            .setModuleName("ModuleName3")
            .setCategory("CategoryName3")
            .setType("Type3")
            .build();

    @Test
    void shouldNothingChangeWhenNoFilterIsAdded() {
        Predicate<? super Issue> filter = new IssueFilterBuilder().build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);

    }

    @Test
    void shouldPassAllWhenUselessFilterIsAdded() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("[a-zA-Z1]*")
                .setIncludeFilenameFilter("[a-zA-Z2]*")
                .setIncludeFilenameFilter("[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);
    }

    @Test
    void shouldPassAllWhenUselessFilterIsAddedAsList() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);
    }

    @Test
    void shouldPassNoWhenMasterFilterIsAdded() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("[a-zA-Z_1-3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    @Test
    void shouldPassNoWhenMasterFilterIsAddedAsList() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    @Test
    void shouldFindIssue1ByAFileNameIncludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIssue1ByAFileNameExcludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("FileName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2, ISSUE3);
    }

    @Test
    void shouldFindIssue2ByAPackageNameIncludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludePackageNameFilter("PackageName2")
                .build();

        applyFilterAndCheckResult(filter, getIssues(), ISSUE2);
    }

    @Test
    void shouldFindIssue2ByAPackageNameExcludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludePackageNameFilter("PackageName2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE3);
    }

    @Test
    void shouldFindIssue3ByAModuleNameIncludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeModuleNameFilter("ModuleName3")
                .build();

        applyFilterAndCheckResult(filter, getIssues(), ISSUE3);
    }

    @Test
    void shouldFindIssue3ByAModuleNameExcludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeModuleNameFilter("ModuleName3")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2);
    }

    @Test
    void shouldFindIssue1ByACategoryIncludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeCategoryFilter("CategoryName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIssue1ByACategoryExcludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeCategoryFilter("CategoryName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2, ISSUE3);
    }

    @Test
    void shouldFindIssue2ByATypeIncludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2);
    }

    @Test
    void shouldFindIssue2ByACategoryExcludeMatch() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE3);
    }

    @Test
    void shouldFindIntersectionFromIncludeAndExcludeBySameProperty() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileName1")
                .setIncludeFilenameFilter("FileName2")
                .setExcludeFilenameFilter("FileName2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIntersectionFromIncludeAndExcludeByOtherProperty() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileName1")
                .setIncludeFilenameFilter("FileName2")
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindNoIntersectionFromEmptyIncludeAndExclude() {
        Predicate<? super Issue> filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileNameNotExisting")
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    /**
     * Applies filter and checks if result is equal to expected values.
     *
     * @param criterion
     *         the filter criterion
     * @param issues
     *         the issues to filter.
     * @param expectedOutput
     *         the expected filter result.
     */
    private void applyFilterAndCheckResult(final Predicate<? super Issue> criterion, final Issues<Issue> issues,
            final Issue... expectedOutput) {
        Issues<Issue> result = issues.filter(criterion);
        assertThat(result.iterator()).containsExactly(expectedOutput);
    }
}