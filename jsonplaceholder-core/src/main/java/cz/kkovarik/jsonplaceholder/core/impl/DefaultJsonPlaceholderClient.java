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

import java.util.List;

import cz.kkovarik.jsonplaceholder.core.JsonPlaceholderClient;
import cz.kkovarik.jsonplaceholder.core.domain.Post;
import cz.kkovarik.jsonplaceholder.core.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link JsonPlaceholderClient}, does use {@link WebClient} to call REST API.
 *
 * @author Karel Kovarik
 * @since 0.0.1
 */
@Slf4j
public class DefaultJsonPlaceholderClient implements JsonPlaceholderClient {

    /**
     * Users endpoint uri in the REST API.
     */
    private static final String USERS_URI = "/users";

    /**
     * Posts endpoint uri in the REST API.
     */
    private static final String POSTS_URI = "/posts";

    /**
     * Parametrized type reference for list of posts.
     */
    private static final ParameterizedTypeReference<List<Post>> POST_LIST_PARAMETERIZED_TYPE_REFERENCE =
            new ParameterizedTypeReference<List<Post>>() {};

    /**
     * WebClient instance.
     */
    private final WebClient webClient;

    public DefaultJsonPlaceholderClient(final String hostName) {
        Assert.hasText(hostName, "the hostName must not be null");

        log.debug("Init webclient, hostName {}.", hostName);
        webClient = WebClient.builder()
                             .baseUrl(hostName)
                             .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .build();
        Assert.notNull(webClient, "the webClient must not be null");
    }

    // konstruktor co čte timeouty (connect + socket)

    @Override
    public Mono<UserInfo> userInfo(final Long userId) {
        Assert.notNull(userId, "the userId must not be null");
        log.trace("Fetch user info for userId {}", userId);

        final Mono<UserInfo> userInfoMono = webClient
                .get()
                .uri(USERS_URI + "/" + userId)
                .retrieve()
                .bodyToMono(UserInfo.class);

        final Mono<List<Post>> userPostsMono = webClient
                .get()
                .uri(POSTS_URI + "?userId=" + userId)
                .retrieve()
                .bodyToMono(POST_LIST_PARAMETERIZED_TYPE_REFERENCE);

        return userInfoMono.zipWith(
                userPostsMono,
                (user, postList) -> {
                    user.setPosts(postList);
                    return user;
                });
    }
}
