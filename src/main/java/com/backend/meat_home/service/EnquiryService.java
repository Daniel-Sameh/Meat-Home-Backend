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

    public Page<Enquiry> getAllEnquiries(Long adminId, int page, int size) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view all enquiries");
        }
        Pageable pageable = PageRequest.of(page, size);
        return enquiryRepository.findAll(pageable);
    }

    public List<Enquiry> getUnreadEnquiries(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admin can view unread enquiries");
        }
        return enquiryRepository.findByUnreadTrue();
    }

    public Enquiry markEnquiryAsRead(Long enquiryId) {

        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));

        enquiry.setUnread(false);
        return enquiryRepository.save(enquiry);
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
