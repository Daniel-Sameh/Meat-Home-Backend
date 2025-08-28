package com.backend.meat_home.service;

import com.backend.meat_home.entity.Enquiry;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.EnquiryRepository;
import com.backend.meat_home.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;

    // Submit Enquiry
    public Enquiry submitEnquiry(String content) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Enquiry enquiry = new Enquiry();
        enquiry.setContent(content);
        enquiry.setUserId(user.getId());
        enquiry.setUnread(true);
        enquiry.setHidden(false);
        return enquiryRepository.save(enquiry);
    }

    // Get All Enquiries
    public Page<Enquiry> getAllEnquiries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return enquiryRepository.findAll(pageable);
    }

    // Get Unread Enquiries
    public List<Enquiry> getUnreadEnquiries() {
        List<Enquiry> unreadEnquiries = enquiryRepository.findByUnreadTrue();

        for (Enquiry enquiry : unreadEnquiries) {
            enquiry.setUnread(false);
        }

        enquiryRepository.saveAll(unreadEnquiries);
        return unreadEnquiries;
    }

    // Mark As Read
    public Enquiry markEnquiryAsRead(Long enquiryId) {

        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));

        enquiry.setUnread(false);
        return enquiryRepository.save(enquiry);
    }

    // View & Hide Enquiry
    public void toggleEnquiryVisibility(Long id, boolean hidden) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));
        enquiry.setHidden(hidden);
        enquiryRepository.save(enquiry);
    }
}
