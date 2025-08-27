package com.backend.meat_home.service;

import com.backend.meat_home.entity.Settings;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.SettingsRepository;
import com.backend.meat_home.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public SettingsService(SettingsRepository settingsRepository, UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    private void validateAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Access denied: only admins can perform this action");
        }
    }

    public Settings getPlatformSettings() {
        return settingsRepository.findById(SETTINGS_RECORD_ID)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    public Settings getAboutSection() {
        return settingsRepository.findById(SETTINGS_RECORD_ID)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    public Settings getTermsAndConditions() {
        return settingsRepository.findById(SETTINGS_RECORD_ID)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    public Settings updateSettings(Long userId, Settings newSettings) {
        validateAdmin(userId);
        Settings settings = settingsRepository.findById(SETTINGS_RECORD_ID).orElse(new Settings());
        settings.setPlatformName(newSettings.getPlatformName());
        settings.setLogoUrl(newSettings.getLogoUrl());
        settings.setFacebookUrl(newSettings.getFacebookUrl());
        settings.setWhatsappNumber(newSettings.getWhatsappNumber());
        settings.setPhoneNumber(newSettings.getPhoneNumber());
        settings.setSecondPhoneNumber(newSettings.getSecondPhoneNumber());
        settings.setAboutImageUrl(newSettings.getAboutImageUrl());
        settings.setAboutDescription(newSettings.getAboutDescription());
        settings.setTermsConditions(newSettings.getTermsConditions());
        return settingsRepository.save(settings);
    }

    public Settings previewSettings(Long userId, Settings newSettings) {
        validateAdmin(userId);
        Settings current = settingsRepository.findById(1L).orElse(new Settings());
        Settings preview = new Settings();
        preview.setPlatformName(newSettings.getPlatformName() != null ? newSettings.getPlatformName() : current.getPlatformName());
        preview.setLogoUrl(newSettings.getLogoUrl() != null ? newSettings.getLogoUrl() : current.getLogoUrl());
        preview.setFacebookUrl(newSettings.getFacebookUrl() != null ? newSettings.getFacebookUrl() : current.getFacebookUrl());
        preview.setWhatsappNumber(newSettings.getWhatsappNumber() != null ? newSettings.getWhatsappNumber() : current.getWhatsappNumber());
        preview.setPhoneNumber(newSettings.getPhoneNumber() != null ? newSettings.getPhoneNumber() : current.getPhoneNumber());
        preview.setSecondPhoneNumber(newSettings.getSecondPhoneNumber() != null ? newSettings.getSecondPhoneNumber() : current.getSecondPhoneNumber());
        preview.setAboutImageUrl(newSettings.getAboutImageUrl() != null ? newSettings.getAboutImageUrl() : current.getAboutImageUrl());
        preview.setAboutDescription(newSettings.getAboutDescription() != null ? newSettings.getAboutDescription() : current.getAboutDescription());
        preview.setTermsConditions(newSettings.getTermsConditions() != null ? newSettings.getTermsConditions() : current.getTermsConditions());
        return preview;
    }
}
