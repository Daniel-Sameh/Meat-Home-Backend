package com.backend.meat_home.controller;

import com.backend.meat_home.entity.Enquiry;
import com.backend.meat_home.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping("/submit")
    public Enquiry submitEnquiry(@RequestBody Enquiry request) {
        return enquiryService.submitEnquiry(request.getContent());
    }

    @GetMapping("/all-enquiries")
    public Page<Enquiry> getAllEnquiries(@RequestParam int page, @RequestParam int size) {
        return enquiryService.getAllEnquiries(page, size);
    }

    @GetMapping("/unread")
    public List<Enquiry> getUnreadEnquiries() {
        return enquiryService.getUnreadEnquiries();
    }

    @PutMapping("/read/{enquiryId}")
    public ResponseEntity<Enquiry> markAsRead(
            @PathVariable Long enquiryId) {
        Enquiry updatedEnquiry = enquiryService.markEnquiryAsRead(enquiryId);
        return ResponseEntity.ok(updatedEnquiry);
    }

    @PostMapping("/visibility/{enquiryId}")
    public ResponseEntity<String> hideEnquiries(@PathVariable Long enquiryId, @RequestParam boolean hidden) {
        enquiryService.toggleEnquiryVisibility(enquiryId, hidden);
        return ResponseEntity.ok("Review visibility updated successfully");
    }
}
