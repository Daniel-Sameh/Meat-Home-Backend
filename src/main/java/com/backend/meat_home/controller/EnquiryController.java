package com.backend.meat_home.controller;

import com.backend.meat_home.entity.Enquiry;
import com.backend.meat_home.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping("/submit/{userId}")
    public Enquiry submitEnquiry(@PathVariable Long userId, @RequestBody Enquiry request) {
        return enquiryService.submitEnquiry(userId, request.getContent());
    }

    @GetMapping("/all/{adminId}")
    public List<Enquiry> getAllEnquiries(@PathVariable Long adminId) {
        return enquiryService.getAllEnquiries(adminId);
    }

    @GetMapping("/unread/{adminId}")
    public List<Enquiry> getUnreadEnquiries(@PathVariable Long adminId) {
        return enquiryService.getUnreadEnquiries(adminId);
    }

    @PostMapping("/hide/{adminId}")
    public void hideEnquiries(@PathVariable Long adminId) {
        enquiryService.hideEnquiries(adminId);
    }
}
