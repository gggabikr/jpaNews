package jpanews.jpaproject1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Word {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private EngType langType;

    @OneToMany
    private List<String> korean = new ArrayList<>();

    @OneToMany
    private List<String> english = new ArrayList<>();



}
