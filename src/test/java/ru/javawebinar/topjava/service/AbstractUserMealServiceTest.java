package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
abstract public class AbstractUserMealServiceTest extends AbstractServiceTest {

        @Autowired
        protected UserMealService service;

        @Test
        public void testDelete() throws Exception {
            service.delete(MealTestData.MEAL1_ID, USER_ID);
            MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), service.getAll(USER_ID));
        }

        @Test
        public void testDeleteNotFound() throws Exception {
            thrown.expect(NotFoundException.class);
            service.delete(MEAL1_ID, 1);
        }

        @Test
        public void testSave() throws Exception {
            UserMeal created = getCreated();
            service.save(created, USER_ID);
            MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(USER_ID));
        }

        @Test
        public void testGet() throws Exception {
            UserMeal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
            MATCHER.assertEquals(ADMIN_MEAL, actual);
        }

        @Test
        public void testGetNotFound() throws Exception {
            thrown.expect(NotFoundException.class);
            service.get(MEAL1_ID, ADMIN_ID);
        }

        @Test
        public void testUpdate() throws Exception {
            UserMeal updated = getUpdated();
            service.update(updated, USER_ID);
            MATCHER.assertEquals(updated, service.get(MEAL1_ID, USER_ID));
        }

        @Test
        public void testNotFoundUpdate() throws Exception {
            UserMeal item = service.get(MEAL1_ID, USER_ID);
            thrown.expect(NotFoundException.class);
            thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
            service.update(item, ADMIN_ID);
        }

        @Test
        public void testGetAll() throws Exception {
            MATCHER.assertCollectionEquals(USER_MEALS, service.getAll(USER_ID));
        }

        @Test
        public void testGetBetween() throws Exception {
            MATCHER.assertCollectionEquals(Arrays.asList(MEAL3, MEAL2, MEAL1),
                    service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID));
        }
}