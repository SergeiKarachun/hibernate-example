package by.sergey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = "users")
@Builder
@Entity
public class Company implements BaseEntity<Integer>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

   /* @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    //@org.hibernate.annotations.OrderBy(clause = "username DESC, lastname ASC")
    //@OrderBy("username DESC, personalInfo.lastname ASC ")
    @OrderColumn(name = "id")
    @SortNatural
    private Set<User> users = new TreeSet<>();*/

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "username")
    @SortNatural
    private Map<String, User> users = new TreeMap<>();

    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "company_locale")
    @MapKeyColumn(name = "lang")
    private Map<String, String> locales = new HashMap();
    public void addUser(User user) {
        //users.add(user);
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }


}











