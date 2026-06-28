package com.example.personbackend.controller;

import com.example.personbackend.dto.PersonResponse;
import com.example.personbackend.exception.ResourceNotFoundException;
import com.example.personbackend.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private PersonService personService;

        @Test
        void createPerson_withValidBody_returns201() throws Exception {

                PersonResponse response = PersonResponse.builder()
                                .id(1L)
                                .person1("Alice")
                                .person2("Bob")
                                .percentage(87)
                                .message("❤️ Soulmates")
                                .createdAt(LocalDateTime.now())
                                .build();

                when(personService.createPerson(any()))
                                .thenReturn(response);

                mockMvc.perform(post("/api/names")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "person1":"Alice",
                                                  "person2":"Bob"
                                                }
                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.person1").value("Alice"))
                                .andExpect(jsonPath("$.person2").value("Bob"))
                                .andExpect(jsonPath("$.percentage").value(87))
                                .andExpect(jsonPath("$.message").value("❤️ Soulmates"));
        }

        @Test
        void createPerson_withBlankPerson2_returns400() throws Exception {

                mockMvc.perform(post("/api/names")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "person1":"Alice",
                                                  "person2":""
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.fieldErrors.person2").exists());
        }

        @Test
        void getAllPersons_returnsList() throws Exception {

                PersonResponse response = PersonResponse.builder()
                                .id(1L)
                                .person1("Alice")
                                .person2("Bob")
                                .percentage(87)
                                .message("❤️ Soulmates")
                                .createdAt(LocalDateTime.now())
                                .build();

                when(personService.getAllPersons())
                                .thenReturn(List.of(response));

                mockMvc.perform(get("/api/names"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].person1").value("Alice"))
                                .andExpect(jsonPath("$[0].person2").value("Bob"))
                                .andExpect(jsonPath("$[0].percentage").value(87));
        }

        @Test
        void getPersonById_whenFound_returns200() throws Exception {

                PersonResponse response = PersonResponse.builder()
                                .id(1L)
                                .person1("Alice")
                                .person2("Bob")
                                .percentage(87)
                                .message("❤️ Soulmates")
                                .createdAt(LocalDateTime.now())
                                .build();

                when(personService.getPersonById(1L))
                                .thenReturn(response);

                mockMvc.perform(get("/api/names/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.person1").value("Alice"))
                                .andExpect(jsonPath("$.percentage").value(87));
        }

        @Test
        void getPersonById_whenMissing_returns404() throws Exception {

                when(personService.getPersonById(eq(99L)))
                                .thenThrow(new ResourceNotFoundException(
                                                "Person record not found with id: 99"));

                mockMvc.perform(get("/api/names/99"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Not Found"));
        }

        @Test
        void deletePerson_whenFound_returns204() throws Exception {

                doNothing().when(personService).deletePerson(1L);

                mockMvc.perform(delete("/api/names/1"))
                                .andExpect(status().isNoContent())
                                .andExpect(content().string(""));
        }
}