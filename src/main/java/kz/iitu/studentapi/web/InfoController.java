package kz.iitu.studentapi.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final String studentName;
    private final ObjectProvider<BuildProperties> buildProperties;

    public InfoController(@Value("${app.student-name}") String studentName,
                          ObjectProvider<BuildProperties> buildProperties) {
        this.studentName = studentName;
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public Map<String, Object> info() {
        BuildProperties build = buildProperties.getIfAvailable();
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", "Student API");
        info.put("student", studentName);
        info.put("version", build != null ? build.getVersion() : "dev");
        return info;
    }
}
