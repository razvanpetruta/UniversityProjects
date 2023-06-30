package com.example.restapi.model.library;

import com.example.restapi.model.book.Book;
import com.example.restapi.model.membership.Membership;
import com.example.restapi.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libraries", indexes = {
        @Index(name = "name_libraries_index", columnList = "name")
})
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long ID;

    @Column
    @NotEmpty
    private String name;

    @Column
    @NotEmpty
    private String address;

    @Column
    @Email
    private String email;

    @Column
    @NotEmpty
    private String website;

    @Column
    @Min(1800)
    @Max(2023)
    private Integer yearOfConstruction;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Book> books;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Membership> memberships;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Library library = (Library) o;
        return getID() != null && Objects.equals(getID(), library.getID());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
