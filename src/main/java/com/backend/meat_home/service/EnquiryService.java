package com.backend.meat_home.service;

import com.backend.meat_home.entity.Enquiry;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.EnquiryRepository;
import com.backend.meat_home.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;

    public Enquiry submitEnquiry(Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != User.Role.CUSTOMER) {
            throw new RuntimeException("Only customers can submit enquiries");
        }
        Enquiry enquiry = new Enquiry();
        enquiry.setContent(content);
        enquiry.setUserId(user.getId());
        enquiry.setUnread(true);
        enquiry.setHidden(false);
        return enquiryRepository.save(enquiry);
    }

    public List<Enquiry> getAllEnquiries(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view all enquiries");
        }
        return enquiryRepository.findAll();
    }

    public List<Enquiry> getUnreadEnquiries(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view unread enquiries");
        }
        List<Enquiry> unreadEnquiries = enquiryRepository.findByUnreadTrue();
        for (Enquiry enquiry : unreadEnquiries) {
            enquiry.setUnread(false);
        }
        enquiryRepository.saveAll(unreadEnquiries);
        return unreadEnquiries;
    }

    public void hideEnquiries(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can hide enquiries");
        }
        List<Enquiry> enquiries = enquiryRepository.findAll();
        for (Enquiry enquiry : enquiries) {
            enquiry.setHidden(true);
        }
        enquiryRepository.saveAll(enquiries);
    }
}
