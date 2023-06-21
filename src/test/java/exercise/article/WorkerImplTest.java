package exercise.article;

import exercise.worker.WorkerImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.mockito.Mockito.*;

class WorkerImplTest {
    private WorkerImpl worker;
    @Mock private LibraryImpl mockedLibrary;
    List<Article> testArticles = new ArrayList<>();


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        worker = new WorkerImpl(mockedLibrary);
        testArticles.add(new Article(
                "Как составить резюме тестировщику",
                "Работодатель тратит всего 20 секунд на первичный просмотр резюме. Поэтому очень важно составить резюме Junior-тестировщика таким образом, чтобы hr-специалист сразу видел ваши ключевые навыки. О том, как это сделать, читайте в блоге Kata Academy.",
                "Сергей Сергеев",
                null));
        testArticles.add(new Article(
                "Почему важны soft skills?",
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));
        testArticles.add(new Article(
                "Почему важны soft skills?",
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));
        testArticles.add(new Article(
                null,
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));
        testArticles.add(new Article(
                "Почему важны soft skills?",
                null,
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));
        testArticles.add(new Article(
                "Почему важны soft skills?",
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                null,
                LocalDate.of(2023, 1, 6)));
    }


    @Test
    void getCatalogTest() throws  Exception {
        var actualResult = " Что такое функциональное чтение? Или как эффективно учиться программированию";
        when(mockedLibrary.getAllTitles()).thenReturn(Arrays.asList(actualResult));

        var expectedResult = "Список доступных статей:\n " +
                "    "+"Что такое функциональное чтение? Или как эффективно учиться программированию" + "\n";

        assertEquals(expectedResult, worker.getCatalog());
    }

    @Test
    void articlesPreparedCorrectlyTest() throws Exception {
        var result = worker.prepareArticles(testArticles);
        List<Article> expectedResult = new ArrayList<>();

        expectedResult.add(new Article(
                "Как составить резюме тестировщику",
                "Работодатель тратит всего 20 секунд на первичный просмотр резюме. Поэтому очень важно составить резюме Junior-тестировщика таким образом, чтобы hr-специалист сразу видел ваши ключевые навыки. О том, как это сделать, читайте в блоге Kata Academy.",
                "Сергей Сергеев",
                LocalDate.now()));
        expectedResult.add(new Article(
                "Почему важны soft skills?",
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));
        assertEquals(expectedResult, result);
    }
    @Test
    void articleWithoutDateHaveTodaysDateTest(){
        var result = worker.prepareArticles(testArticles);
        assertTrue(result.stream()
                .filter(s -> s.getTitle().equals("Как составить резюме тестировщику"))
                .anyMatch(s -> s.getCreationDate().equals(LocalDate.now())));
    }

    @Test
    void articlesWithoutTitleDoesNotSavedTest(){
        var result = worker.prepareArticles(testArticles);
        assertFalse(result.stream()
                .anyMatch(s -> s.getTitle().equals(isNull())));
    }
    @Test
    void articlesWithoutContentDoesNotSavedTest() {
        var result = worker.prepareArticles(testArticles);
        assertFalse(result.stream()
                .anyMatch(s -> s.getContent().equals(isNull())));
    }

    @Test
    void articlesWithoutAuthorDoesNotSavedTest() {
        var result = worker.prepareArticles(testArticles);
        assertFalse(result.stream()
                .anyMatch(s -> s.getAuthor().equals(isNull())));
    }


    @Test
    void addNewArticlesTest() throws Exception {
        var result = worker.prepareArticles(testArticles);

        worker.addNewArticles(result);

        verify(mockedLibrary,atLeastOnce()).updateCatalog();
        verify(mockedLibrary, atLeastOnce()).store(2023,result);
    }

    @Test
    void duplicateArticlesAreNotSaved(){
        var actualResult = worker.prepareArticles(testArticles);
        List<Article> expectedResult = new ArrayList<>();
        expectedResult.add(new Article(
                "Как составить резюме тестировщику",
                "Работодатель тратит всего 20 секунд на первичный просмотр резюме. Поэтому очень важно составить резюме Junior-тестировщика таким образом, чтобы hr-специалист сразу видел ваши ключевые навыки. О том, как это сделать, читайте в блоге Kata Academy.",
                "Сергей Сергеев",
                LocalDate.now()));
        expectedResult.add(new Article(
                "Почему важны soft skills?",
                "Мягкие навыки помогают решать задачи и взаимодействовать с другими людьми. Можно обладать хорошими знаниями и умениями, но без развитых soft skills очень трудно работать в современных компаниях. Особенно айтишникам. Рассказываем, почему.",
                "Иван Иванов",
                LocalDate.of(2023, 1, 6)));

        assertEquals(expectedResult, actualResult);
    }
}
