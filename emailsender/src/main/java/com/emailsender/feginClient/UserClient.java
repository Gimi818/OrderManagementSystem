package com.emailsender.feginClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${application.config.user-url}")
public interface UserClient {

    @GetMapping("/email/{id}")
    String getUserEmail(@PathVariable Long id);
}
