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

package cz.kkovarik.jsonplaceholder.core;

import cz.kkovarik.jsonplaceholder.core.domain.UserInfo;
import reactor.core.publisher.Mono;

/**
 * @author Karel Kovarik
 * @since 0.0.1
 */
public interface JsonPlaceholderClient {

    /**
     * Get user info for user identified by userId.
     *
     * @param userId the id of user.
     * @return Mono with {@link UserInfo}.
     */
    Mono<UserInfo> userInfo(Long userId);
}
