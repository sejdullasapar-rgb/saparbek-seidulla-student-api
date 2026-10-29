package kz.iitu.studentapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String STUDENT_JSON = """
            {"fullName":"Saparbek Seidulla","email":"student@iitu.edu.kz","groupName":"IT2-2302","course":3}
            """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private long createStudent() throws Exception {
        String response = mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(STUDENT_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Saparbek Seidulla"))
                .andReturn().getResponse().getContentAsString();
        JsonNode node = mapper.readTree(response);
        return node.get("id").asLong();
    }

    @Test
    void createsAndReadsStudent() throws Exception {
        long id = createStudent();
        mvc.perform(get("/api/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@iitu.edu.kz"));
        mvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void returnsNotFoundForMissingStudent() throws Exception {
        mvc.perform(get("/api/students/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(200)); // CONTROLLED ERROR: the API returns 404 here
    }

    @Test
    void rejectsInvalidStudent() throws Exception {
        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"fullName\":\"\",\"email\":\"bad\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletesStudent() throws Exception {
        long id = createStudent();
        mvc.perform(delete("/api/students/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/students/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void updatesStudent() throws Exception {
        long id = createStudent();
        String updated = """
                {"fullName":"Saparbek Seidulla","email":"updated@iitu.edu.kz","groupName":"IT2-2302","course":4}
                """;
        mvc.perform(put("/api/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@iitu.edu.kz"))
                .andExpect(jsonPath("$.course").value(4));
    }

    @Test
    void showsStudentInInfo() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student").value("Saparbek Seidulla"));
    }
}
