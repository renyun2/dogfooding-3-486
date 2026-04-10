package com.student.management.service;

import com.student.management.entity.SystemConfig;
import com.student.management.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository repository;

    @Value("${admin.password}")
    private String defaultAdminPassword;

    private static final String KEY_ADMIN_PASSWORD = "admin_password";

    public String getAdminPassword() {
        return repository.findByConfigKey(KEY_ADMIN_PASSWORD)
                .map(SystemConfig::getConfigValue)
                .orElse(defaultAdminPassword);
    }

    public void setAdminPassword(String newPassword) {
        SystemConfig config = repository.findByConfigKey(KEY_ADMIN_PASSWORD)
                .orElse(new SystemConfig(null, KEY_ADMIN_PASSWORD, newPassword));
        config.setConfigValue(newPassword);
        repository.save(config);
    }
}
