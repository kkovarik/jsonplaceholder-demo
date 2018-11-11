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

package cz.kkovarik.jsonplaceholder.core.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.List;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import cz.kkovarik.jsonplaceholder.core.JsonPlaceholderClient;
import cz.kkovarik.jsonplaceholder.core.domain.Post;
import cz.kkovarik.jsonplaceholder.core.domain.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * Simple test suite for {@link DefaultJsonPlaceholderClient}.
 *
 * @author Karel Kovarik
 */
class DefaultJsonPlaceholderClientTest {

    protected WireMockServer WIREMOCK;

    // tested instance
    private JsonPlaceholderClient jsonPlaceholderClient;

    @BeforeEach
    void setupTest() {
        WIREMOCK = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        WIREMOCK.start();
        WireMock.configureFor(WIREMOCK.port());

        jsonPlaceholderClient = new DefaultJsonPlaceholderClient("http://localhost:" + WIREMOCK.port() + "/");
    }

    @Test
    void test_fetchUserDetails() {
        // setup wiremock
        WireMock.stubFor(get("/users/42")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("DefaultJsonPlaceholderClientTest/user-detail.json")
                )
        );

        WireMock.stubFor(get("/posts?userId=42")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("DefaultJsonPlaceholderClientTest/user-posts.json")
                )
        );

        // fetch userInfo
        final Mono<UserInfo> userInfoMono = jsonPlaceholderClient.userInfo(42L);

        final UserInfo response = userInfoMono
                .block(Duration.ofSeconds(10));
        assertNotNull(response, "the response must not be null");
        assertEquals("Leanne Graham", response.getName());
        assertEquals("Sincere@april.biz", response.getEmail());
        assertEquals("Bret", response.getUsername());
        final List<Post> postList = response.getPosts();
        assertNotNull(postList, "the response.posts must not be null");
        assertEquals("1", postList.get(0).getId());
        assertEquals("reprehenderit", postList.get(0).getTitle());
    }
}
