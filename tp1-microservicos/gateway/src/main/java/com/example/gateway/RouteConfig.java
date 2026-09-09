package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("aluno-service")
                .route(path("/api/alunos/**"), http())
                .before(rewritePath("/api/alunos?(?<segment>.*)", "/alunos${segment}"))
                .filter(lb("alunoservice"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutesCursos() {
        return route("curso-service")
                .route(path("/api/cursos/**"), http())
                .before(rewritePath("/api/cursos?(?<segment>.*)", "/cursos${segment}"))
                .filter(lb("cursoservice"))
                .build();
    }
}
