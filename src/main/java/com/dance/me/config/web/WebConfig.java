package com.dance.me.config.web;

import org.springframework.context.annotation.Configuration;

// CORS gestionado centralmente en SecurityConfig.corsConfigurationSource()
// No configurar aquí — tener dos fuentes CORS activas al mismo tiempo causa conflictos
@Configuration
public class WebConfig {
}
