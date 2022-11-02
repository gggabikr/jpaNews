package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long Id;

    private String username;

    private String password;

    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<WordList> WordLists = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createDate;
}
