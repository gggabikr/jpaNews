package jpanews.jpaproject1.domain.Words;

import jpanews.jpaproject1.domain.WordClass;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "language")
public class Word {

    @Id @GeneratedValue
    @Column(name="word_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WordClass wordClass;

    @Lob
    @Column
    private String meaning;
//    public abstract String getMeaning();
//
//    public abstract void setMeaning(String meaning);

    //added it to make JPQL queries work for 'w.language'
//    @Column(insertable = false, updatable = false)
    private String language;


    //==main methods==//

}
