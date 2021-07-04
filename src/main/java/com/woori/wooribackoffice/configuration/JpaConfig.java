package com.woori.wooribackoffice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // audit 기능을 enable 이 필요
public class JpaConfig {

}
