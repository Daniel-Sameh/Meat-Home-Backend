package com.backend.meat_home.service;

import com.backend.meat_home.entity.Settings;
import com.backend.meat_home.repository.SettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    @Value("${settings.record.id}")
    private Long settingsRecordId;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    // Get Platform Settings
    public Settings getPlatformSettings() {
        return settingsRepository.findById(settingsRecordId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    // Get About Section
    public Settings getAboutSection() {
        return settingsRepository.findById(settingsRecordId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    // Get Terms and Conditions
    public Settings getTermsAndConditions() {
        return settingsRepository.findById(settingsRecordId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    // Update Settings
    public Settings updateSettings(Settings newSettings) {
        Settings settings = settingsRepository.findById(settingsRecordId).orElse(new Settings());
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

    // Preview Settings
    public Settings previewSettings(Settings newSettings) {
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
