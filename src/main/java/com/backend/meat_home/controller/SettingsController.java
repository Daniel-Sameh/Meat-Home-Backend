package com.backend.meat_home.controller;

import com.backend.meat_home.entity.Settings;
import com.backend.meat_home.service.SettingsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/platform")
    public Object getPlatformSettings() {
        Settings settings = settingsService.getPlatformSettings();
        return new Object() {
            public final String platformName = settings.getPlatformName();
            public final String logoUrl = settings.getLogoUrl();
            public final String facebookUrl = settings.getFacebookUrl();
            public final String whatsappNumber = settings.getWhatsappNumber();
            public final String phoneNumber = settings.getPhoneNumber();
            public final String secondPhoneNumber = settings.getSecondPhoneNumber();
        };
    }

    @GetMapping("/about")
    public Object getAboutSection() {
        Settings settings = settingsService.getAboutSection();
        return new Object() {
            public final String aboutImageUrl = settings.getAboutImageUrl();
            public final String aboutDescription = settings.getAboutDescription();
        };
    }

    @GetMapping("/terms")
    public Object getTermsAndConditions() {
        Settings settings = settingsService.getTermsAndConditions();
        return new Object() {
            public final String termsConditions = settings.getTermsConditions();
        };
    }

    @PutMapping("/update/{userId}")
    public Settings updateSettings(@PathVariable Long userId, @RequestBody Settings newSettings) {
        return settingsService.updateSettings(userId, newSettings);
    }

    @PostMapping("/preview/{userId}")
    public Settings previewSettings(@PathVariable Long userId, @RequestBody Settings newSettings) {
        return settingsService.previewSettings(userId, newSettings);
    }
}
