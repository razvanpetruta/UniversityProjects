package com.example.restapi.service.membership_service;

import com.example.restapi.dtos.readerdtos.MembershipDTO;
import com.example.restapi.exceptions.LibraryNotFoundException;
import com.example.restapi.exceptions.ReaderNotFoundException;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.exceptions.UserNotFoundException;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.membership.Membership;
import com.example.restapi.model.membership.MembershipKey;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
import com.example.restapi.repository.library_repository.LibraryRepository;
import com.example.restapi.repository.membership_repository.MembershipRepository;
import com.example.restapi.repository.reader_repository.ReaderRepository;
import com.example.restapi.repository.user_repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class MembershipService implements IMembershipService {
    private final MembershipRepository membershipRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;

    public MembershipService(MembershipRepository membershipRepository, LibraryRepository libraryRepository, UserRepository userRepository, ReaderRepository readerRepository) {
        this.membershipRepository = membershipRepository;
        this.libraryRepository = libraryRepository;
        this.userRepository = userRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public Membership createMembership(Long libraryID, Long readerID, Long userID) {
        Library library =  this.libraryRepository.findById(libraryID).orElseThrow(() -> new LibraryNotFoundException(libraryID));
        Reader reader = this.readerRepository.findById(readerID).orElseThrow(() -> new ReaderNotFoundException(readerID));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        if (!Objects.equals(user.getID(), reader.getUser().getID())) {
            boolean modOrAdmin = user.getRoles().stream().anyMatch((role) ->
               role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission " +
                        "to create membership for reader %s", user.getUsername(), reader.getUser().getUsername()));
            }
        }

        // start date: current date
        // membership will last 365 days
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(365);

        // create the key
        MembershipKey key = new MembershipKey();
        key.setLibraryID(libraryID);
        key.setReaderID(readerID);

        // create the membership
        Membership membership = new Membership();
        membership.setID(key);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setLibrary(library);
        membership.setReader(reader);
        this.membershipRepository.save(membership);

        // add the membership to the sets of library and reader
        library.addMembership(membership);
        reader.addMembership(membership);

        return membership;
    }

    @Override
    public List<MembershipDTO> addNewMemberships(List<MembershipDTO> memberships, Long id) {
        this.libraryRepository.findById(id).map(
                library -> {
                    for (MembershipDTO membership: memberships) {
                        if (this.membershipRepository.findById(new MembershipKey(id, membership.getReaderID())).isEmpty()) {
                            Reader reader = this.readerRepository.findById(membership.getReaderID()).orElseThrow(() -> new ReaderNotFoundException(membership.getReaderID()));

                            MembershipKey key = new MembershipKey();
                            key.setLibraryID(id);
                            key.setReaderID(reader.getID());

                            Membership newMembership = new Membership();
                            newMembership.setID(key);
                            newMembership.setStartDate(membership.getStartDate());
                            newMembership.setEndDate(membership.getEndDate());
                            newMembership.setLibrary(library);
                            newMembership.setReader(reader);
                            this.membershipRepository.save(newMembership);

                            library.addMembership(newMembership);
                            reader.addMembership(newMembership);
                        }
                    }
                    return library;
                }
        );

        return memberships;
    }
}
