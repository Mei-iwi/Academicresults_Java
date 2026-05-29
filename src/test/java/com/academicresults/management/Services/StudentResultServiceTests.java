package com.academicresults.management.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.academicresults.management.Entity.CourseSection;
import com.academicresults.management.Entity.Student;
import com.academicresults.management.Entity.StudentResult;
import com.academicresults.management.Entity.Subject;
import com.academicresults.management.Entity.enums.ResultStatus;
import com.academicresults.management.Repository.AccountRepository;
import com.academicresults.management.Repository.CourseSectionRepository;
import com.academicresults.management.Repository.StudentRepository;
import com.academicresults.management.Repository.StudentResultRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentResultServiceTests {

    @Mock
    private StudentResultRepository resultRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseSectionRepository courseSectionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private StudentResultService service;

    @Test
    void saveCalculatesTotalLetterGradeAndPassFail() {
        Student student = Student.builder().id(1L).studentCode("SV001").fullName("Student One").build();
        Subject subject = Subject.builder().id(2L).subjectCode("JAVA").subjectName("Java").credits((byte) 3).build();
        CourseSection section = CourseSection.builder().id(3L).sectionCode("JAVA-01").subject(subject).build();

        when(resultRepository.findByStudent_IdAndSection_Id(1L, 3L)).thenReturn(Optional.empty());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseSectionRepository.findById(3L)).thenReturn(Optional.of(section));
        when(resultRepository.save(any(StudentResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentResult saved = service.save(null, 1L, 3L, bd("8"), bd("7"), bd("9"), null, ResultStatus.PUBLISHED, null);

        assertThat(saved.getTotalScore()).isEqualByComparingTo("8.30");
        assertThat(saved.getLetterGrade()).isEqualTo("B");
        assertThat(saved.getGradePoint()).isEqualByComparingTo("3.00");
        assertThat(service.passFail(saved)).isEqualTo("PASS");
    }

    @Test
    void scoreMustBeBetweenZeroAndTen() {
        assertThatThrownBy(() -> service.save(null, 1L, 1L, bd("11"), bd("5"), bd("5"), null, ResultStatus.DRAFT, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0");

        verify(resultRepository, never()).save(any());
    }

    @Test
    void duplicateStudentAndCourseSectionIsRejectedOnCreate() {
        when(resultRepository.findByStudent_IdAndSection_Id(1L, 2L)).thenReturn(Optional.of(new StudentResult()));

        assertThatThrownBy(() -> service.save(null, 1L, 2L, bd("7"), bd("7"), bd("7"), null, ResultStatus.DRAFT, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đã có kết quả");
    }

    @Test
    void lockedResultCannotBeEditedOrDeleted() {
        StudentResult locked = new StudentResult();
        locked.setId(9L);
        locked.setResultStatus(ResultStatus.LOCKED);
        when(resultRepository.findById(9L)).thenReturn(Optional.of(locked));

        assertThatThrownBy(() -> service.save(9L, 1L, 2L, bd("7"), bd("7"), bd("7"), null, ResultStatus.DRAFT, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> service.delete(9L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void failingScoreReturnsFail() {
        StudentResult result = new StudentResult();
        result.setTotalScore(bd("3.99"));

        assertThat(service.passFail(result)).isEqualTo("FAIL");
    }

    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }
}
