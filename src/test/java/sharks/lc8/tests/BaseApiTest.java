package sharks.lc8.tests;

import org.testng.annotations.BeforeMethod;
import sharks.lc8.facade.AuthApiFacade;

/**
 * Родитель API-тестов: подготовка вынесена из самих тестов.
 * abstract: сам по себе не запускается, это только родитель для тестов.
 * Новый фасад перед КАЖДЫМ тестом специально: фасад помнит пользователя и токен,
 * а TestNG использует один объект тестового класса для всех его тестов -
 * общий фасад передал бы пользователя одного теста в следующий.
 */
public abstract class BaseApiTest {

    // protected: доступен в тестах-наследниках, например auth.registerNewUser()
    protected AuthApiFacade auth;

    // Выполняется перед каждым @Test в классах-наследниках
    @BeforeMethod
    public void setUp() {
        auth = new AuthApiFacade();
    }
}