package com.example.coachassistantbackend.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "com.example.coachassistantbackend.Repository" })
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {}
