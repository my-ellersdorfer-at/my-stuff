package at.steell.mystuff.application.web;

import at.steell.mystuff.application.configuration.WebMvcConfig;
import at.steell.mystuff.application.web.UserSessionController.UserSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static at.steell.mystuff.application.utils.SecurityMockUtils.setupOidcUserSecurityContext;
import static at.steell.mystuff.application.utils.SecurityMockUtils.setupSecurityContext;
import static at.steell.mystuff.application.web.UserSessionController.ANONYMOUS_AVATAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.config.Elements.ANONYMOUS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@SpringJUnitWebConfig(classes = UserSessionControllerTest.ContextConfiguration.class)
class UserSessionControllerTest {
    private static final String MOCK_USER = "MockUser";

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Configuration
    @Import({WebMvcConfig.class})
    public static class ContextConfiguration {
        @Bean
        public UserSessionController jumpPageController() {
            return new UserSessionController();
        }
    }

    @Autowired
    private UserSessionController userSessionController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void instantiate() {
        assertNotNull(userSessionController);
    }

    private UserSession currentUser(final String expectedUserName) {
        UserSession userSession = userSessionController.getCurrentUserSession();
        assertNotNull(userSession);
        assertEquals(expectedUserName, userSession.userName());
        return userSession;
    }

    @Test
    void currentUser_anonymous() {
        SecurityContextHolder.clearContext();
        UserSession userSession = currentUser(ANONYMOUS);
        assertEquals(ANONYMOUS_AVATAR, userSession.avatar());
    }

    @Test
    void currentUser() {
        setupSecurityContext(MOCK_USER);
        currentUser(MOCK_USER);
    }

    @Test
    void currentDefaultOidcUser() {
        setupOidcUserSecurityContext(MOCK_USER);
        currentUser(MOCK_USER);
    }

    private void testCurrentUserHttpCall(final String expectedUserName) throws Exception {
        MvcResult response = mockMvc.perform(get("/api/user-session/current-user-session"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        UserSession userSession = new ObjectMapper().readValue(
            response.getResponse().getContentAsString(),
            UserSession.class);
        assertNotNull(userSession);
        assertEquals(expectedUserName, userSession.userName());
    }

    @Test
    void currentUser_anonymous_http200() throws Exception {
        testCurrentUserHttpCall(ANONYMOUS);
    }

    @Test
    @WithMockUser(value = MOCK_USER)
    void currentUser_mockUser_http200() throws Exception {
        testCurrentUserHttpCall(MOCK_USER);
    }
}
