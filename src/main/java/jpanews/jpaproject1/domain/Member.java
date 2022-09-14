package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "member")
    private List<WordList> WordLists = new ArrayList<>();
}
