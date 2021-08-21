package org.prgrms.kdt.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kdt")
public class OrderProperties implements InitializingBean {

    private String version;

    private Integer minimumOrderAmount;

    private List<String> supportVendors;

    private String description;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(MessageFormat.format("[Bean 생성시] version -> {0}", version));
        System.out.println(MessageFormat.format("[Bean 생성시] minimumOrderAmount -> {0}", minimumOrderAmount));
        System.out.println(MessageFormat.format("[Bean 생성시] supportVendors -> {0}", supportVendors));
        System.out.println(MessageFormat.format("[Bean 생성시] description -> {0}", description));
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMinimumOrderAmount(Integer minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public void setSupportVendors(List<String> supportVendors) {
        this.supportVendors = supportVendors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public Integer getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public List<String> getSupportVendors() {
        return supportVendors;
    }

    public String getDescription() {
        return description;
    }
}
