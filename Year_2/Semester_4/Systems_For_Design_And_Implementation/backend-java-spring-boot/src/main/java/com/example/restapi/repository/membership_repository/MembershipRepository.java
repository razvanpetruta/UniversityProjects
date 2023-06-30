package com.example.restapi.repository.membership_repository;

import com.example.restapi.model.membership.Membership;
import com.example.restapi.model.membership.MembershipKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, MembershipKey> {
    Long countByLibraryID(Long libraryID);
    Long countByReaderID(Long readerID);
}
