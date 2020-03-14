package fitpay.engtest.controller;

import fitpay.engtest.service.FitPayAPIService;
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
    private FitPayAPIService fitpayAPIService;

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        when(fitpayAPIService.getUser("123")).thenReturn("User 123 Data");
        this.mockMvc.perform(get("/compositeUsers/123"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("User 123 Data")));
    }

}
