
package com.example.aesdemo.controller;

import com.example.aesdemo.util.AESUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aes")
public class AESController {

    @Value("${aes.secret}")
    private String secret;

    @Value("${aes.salt}")
    private String salt;

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String text) throws Exception {
        return AESUtil.encrypt(text, secret, salt);
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String encryptedText) throws Exception {
        return AESUtil.decrypt(encryptedText, secret, salt);
    }
}
