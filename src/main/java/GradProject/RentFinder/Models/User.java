package GradProject.RentFinder.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USER_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long userID;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "USER_EMAIL", unique = true)
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;
    @Column(name = "KARMA")
    private int karmaPoint;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Property> properties;
    @OneToMany(mappedBy = "reserver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "submitter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets;
    @Enumerated(EnumType.STRING)
    @Column(name="ROLE")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
