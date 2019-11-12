package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
public class EnvController {
    private String PORT;
    private String MEMORY_LIMIT;
    private String CF_INSTANCE_INDEX;
    private String CF_INSTANCE_ADDR;

    public EnvController(@Value("${cf.instance.port:NOT SET}") String s,
                         @Value("${memory.limit:NOT SET}") String s1,
                         @Value("${cf.instance.index:NOT SET}") String s2,
                         @Value("${cf.instance.addr:NOT SET}") String s3) {
        PORT = s;
        MEMORY_LIMIT = s1;
        CF_INSTANCE_INDEX = s2;
        CF_INSTANCE_ADDR = s3;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map envMap = new HashMap();
        envMap.put("PORT",PORT);
        envMap.put("MEMORY_LIMIT",MEMORY_LIMIT);
        envMap.put("CF_INSTANCE_INDEX",CF_INSTANCE_INDEX);
        envMap.put("CF_INSTANCE_ADDR",CF_INSTANCE_ADDR);
        return envMap;
    }
}
