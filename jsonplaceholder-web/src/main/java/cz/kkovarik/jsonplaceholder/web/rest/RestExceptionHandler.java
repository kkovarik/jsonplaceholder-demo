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

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Custom exception handler.
 *
 * @author Karel Kovarik
 */
@Component
public class RestExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        // propagate exception from webClient
        if (ex instanceof WebClientResponseException) {
            WebClientResponseException webClientException = (WebClientResponseException) ex;

            exchange.getResponse().setStatusCode(webClientException.getStatusCode());
            return exchange.getResponse().setComplete();
        }

        return Mono.error(ex);
    }
}
