/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.kkovarik.jsonplaceholder.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;

import cz.kkovarik.jsonplaceholder.core.JsonPlaceholderClient;
import cz.kkovarik.jsonplaceholder.core.domain.Post;
import cz.kkovarik.jsonplaceholder.core.domain.UserInfo;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Simple test suite for {@link UserInfoController}.
 *
 * @author Karel Kovarik
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserInfoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private JsonPlaceholderClient jsonPlaceholderClient;

    @Test
    void test_userDetail_ok() throws Exception {
        // mock of user
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("John Doe");
        userInfo.setUsername("doe");
        userInfo.setEmail("user@email.cz");
        userInfo.setPosts(new ArrayList<>());
        userInfo.getPosts().add(new Post());
        userInfo.getPosts().get(0).setId("1");
        userInfo.getPosts().get(0).setTitle("post-title");

        Mockito.when(jsonPlaceholderClient.userInfo(eq(42L)))
               .thenReturn(Mono.just(userInfo));

        // call REST endpoint
        final ResponseEntity<String> response = restTemplate
                .getForEntity(serverUrl() + "/api/users/42", String.class);

        // verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(
                fileAsString("/UserInfoControllerTest/ok-response.json"),
                response.getBody(),
                JSONCompareMode.LENIENT
        );
    }

    @Test
    void test_userDetail_notFound() {
        // user not found
        Mockito.when(jsonPlaceholderClient.userInfo(eq(42L)))
               .thenThrow(WebClientResponseException.create(
                       HttpStatus.NOT_FOUND.value(),
                       HttpStatus.NOT_FOUND.toString(),
                       null,
                       null,
                       StandardCharsets.UTF_8));

        // call REST endpoint
        final ResponseEntity<String> response = restTemplate
                .getForEntity(serverUrl() + "/api/users/42", String.class);

        // verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private String serverUrl() {
        return "http://localhost:" + port + "/";
    }

    private String fileAsString(String fileName) {
        try {
            final InputStream is = getClass().getResourceAsStream(fileName);
            Assert.notNull(is, MessageFormat.format("the file {0} cannot be read.", fileName));

            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException("Unable to read file: ", e);
        }
    }
}
