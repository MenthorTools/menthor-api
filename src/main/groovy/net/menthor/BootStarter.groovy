package net.menthor

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016  Menthor Consulting in Information Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.system.ApplicationPidFileWriter
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * @author John Guerson
 */
@RestController
@EnableAutoConfiguration
@ComponentScan(["net.menthor"])
@EnableWebMvcSecurity
class BootStarter {

    @Bean
    /** Enables Cross Origin Requests i.e. requests from the same host but from a different port */
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/upload/json").allowedOrigins("http://localhost:8000")
                registry.addMapping("/api/upload/ea").allowedOrigins("http://localhost:8000")
                registry.addMapping("/api/tree/package-hierarchy").allowedOrigins("http://localhost:8000")
                registry.addMapping("/api/tree/type-hierarchy").allowedOrigins("http://localhost:8000")
                registry.addMapping("/api/tree/type-composition").allowedOrigins("http://localhost:8000")
            }
        }
    }

    @RequestMapping("/")
    String home() {
        return "<b>Menthor API. Use as Rest API<b>";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(BootStarter.class);
        springApplication.addListeners(new ApplicationPidFileWriter("menthor.pid"));
        springApplication.run(args);
    }
}