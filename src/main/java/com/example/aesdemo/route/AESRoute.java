package com.example.aesdemo.route;

import com.example.aesdemo.util.AESUtil;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESRoute extends RouteBuilder {

    @Value("${aes.secret}")
    private String secret;

    @Value("${aes.salt}")
    private String salt;

    @Override
    public void configure() {


        onException(Exception.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setBody(simple("${exception.message}"));


        restConfiguration()
                .component("servlet")
                .bindingMode(org.apache.camel.model.rest.RestBindingMode.off);

        rest("/aes")
                .description("AES (Advanced Encryption Standard) API")
                .consumes("text/plain")
                .produces("text/plain")

                .post("/encrypt")
                .description("Encrypt plain text")
                .responseMessage()
                .code(200)
                .message("Text encrypted successfully")
                .endResponseMessage()
                .to("direct:encrypt")

                .post("/decrypt")
                .description("Decrypt encrypted text")
                .responseMessage()
                .code(200)
                .message("Text decrypted successfully")
                .endResponseMessage()
                .to("direct:decrypt");

        from("direct:encrypt")
                .process(exchange -> {
                    String text = exchange.getIn().getBody(String.class);
                    log.info("body-"+text);
                    String encrypted = AESUtil.encrypt(text, secret, salt);
                    log.info("encrypted-"+encrypted);
                    exchange.getMessage().setBody(encrypted);
                })
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"));


        from("direct:decrypt")
                .process(exchange -> {
                    String encryptedText = exchange.getIn().getBody(String.class);

                    log.info("received encryptedText-"+encryptedText);
                    String decrypted = AESUtil.decrypt(encryptedText, secret, salt);
                    log.info("decrypted-"+decrypted);
                    exchange.getMessage().setBody(decrypted);
                })
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"));
    }
}
