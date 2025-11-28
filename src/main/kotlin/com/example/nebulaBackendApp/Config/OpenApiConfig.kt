package com.example.nebulaBackendApp.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "Nebula Backend API",
        version = "1.0.0",
        description = "API documentation for Nebula Backend Application"
    ),
    servers = [
        Server(
            url = "http://localhost:9090",
            description = "Servidor Local"
        ),
        Server(
            url = "http://100.31.6.130:9090",
            description = "Servidor Producción"
        )
    ]
)
class OpenApiConfigApiConfig