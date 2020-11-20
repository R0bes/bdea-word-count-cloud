package com.example.tagcloud.web

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring5.view.ThymeleafViewResolver

@Configuration
@EnableConfigurationProperties
class WebConfig : ApplicationContextAware {

    val CHARACTER_ENCODING = "UTF-8"

    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    @Bean
    fun messageSource() : ResourceBundleMessageSource {
        val resourceBundleMessageSource = ResourceBundleMessageSource()
        resourceBundleMessageSource.setBasename("messages")
        return resourceBundleMessageSource
    }

    @Bean
    fun templateResolver() : SpringResourceTemplateResolver {
        val springResourceTemplateResolver = SpringResourceTemplateResolver()
        springResourceTemplateResolver.setApplicationContext(applicationContext)
        springResourceTemplateResolver.prefix = "/WEB-INF/"
        springResourceTemplateResolver.suffix = ".html"
        springResourceTemplateResolver.characterEncoding = CHARACTER_ENCODING
        springResourceTemplateResolver.isCacheable = false
        return springResourceTemplateResolver
    }


    @Bean
    fun templateEngine() : SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver())
        templateEngine.enableSpringELCompiler = true
        return templateEngine
    }


    @Bean
    fun viewResolver() : ThymeleafViewResolver {
        val thymeleafViewResolver = ThymeleafViewResolver()
        thymeleafViewResolver.templateEngine = templateEngine()
        thymeleafViewResolver.contentType = CHARACTER_ENCODING
        return thymeleafViewResolver
    }
}