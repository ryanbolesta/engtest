package fitpay.engtest.controller;

import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebClient
@WebMvcTest(UsersController.class)
class TestUsersController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        CompositeUser user = new CompositeUser();
        user.setUserId("123");
        when(usersService.getCompositeUser("123")).thenReturn(user);
        this.mockMvc.perform(get("/compositeUsers/123"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\"userId\":\"123\",\"devices\":null,\"creditCards\": null}"));
    }

}
