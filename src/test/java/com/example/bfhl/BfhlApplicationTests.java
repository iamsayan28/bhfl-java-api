package com.example.bfhl;

import com.example.bfhl.dto.RequestDTO;
import com.example.bfhl.dto.ResponseDTO;
import com.example.bfhl.service.BFHLServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BFHLServiceImpl service;

    // ── Service-layer unit tests ─────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed input - numbers, alphabets, special chars")
    void testExampleA() {
        RequestDTO req = new RequestDTO();
        req.setData(Arrays.asList("a", "1", "334", "4", "R", "$"));

        ResponseDTO res = service.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("1");
        assertThat(res.getEvenNumbers()).containsExactly("334", "4");
        assertThat(res.getAlphabets()).containsExactly("A", "R");
        assertThat(res.getSpecialCharacters()).containsExactly("$");
        assertThat(res.getSum()).isEqualTo("339");
        assertThat(res.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Example B: mixed input - multiple numbers, alphabets, multiple special chars")
    void testExampleB() {
        RequestDTO req = new RequestDTO();
        req.setData(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));

        ResponseDTO res = service.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("5");
        assertThat(res.getEvenNumbers()).containsExactly("2", "4", "92");
        assertThat(res.getAlphabets()).containsExactly("A", "Y", "B");
        assertThat(res.getSpecialCharacters()).containsExactly("&", "-", "*");
        assertThat(res.getSum()).isEqualTo("103");
        assertThat(res.getConcatString()).isEqualTo("ByA");
    }

    @Test
    @DisplayName("Example C: only alphabetic strings, no numbers, no special chars")
    void testExampleC() {
        RequestDTO req = new RequestDTO();
        req.setData(Arrays.asList("A", "ABCD", "DOE"));

        ResponseDTO res = service.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Empty input data returns zeroed response with is_success=true")
    void testEmptyInput() {
        RequestDTO req = new RequestDTO();
        req.setData(List.of());

        ResponseDTO res = service.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEqualTo("");
    }

    @Test
    @DisplayName("Numbers are returned as strings, not integers")
    void testNumbersReturnedAsStrings() {
        RequestDTO req = new RequestDTO();
        req.setData(Arrays.asList("1", "2", "3"));

        ResponseDTO res = service.process(req);

        // All must be String type in lists
        assertThat(res.getOddNumbers()).containsExactly("1", "3");
        assertThat(res.getEvenNumbers()).containsExactly("2");
        assertThat(res.getSum()).isEqualTo("6");
    }

    // ── Controller integration tests ─────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl returns HTTP 200 for valid request")
    void testPostBfhlReturns200() throws Exception {
        String json = "{\"data\":[\"a\",\"1\",\"334\",\"4\",\"R\",\"$\"]}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl response contains user_id, email, roll_number")
    void testPostBfhlContainsUserFields() throws Exception {
        String json = "{\"data\":[\"1\"]}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.roll_number").isNotEmpty());
    }

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
    }
}
