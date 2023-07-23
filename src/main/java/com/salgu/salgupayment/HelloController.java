package com.salgu.salgupayment;

import com.salgu.salgupayment.util.response.ResponseWithData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public ResponseWithData hello() {
        log.info("HelloController.hello()");
        return ResponseWithData.success();
    }
}
