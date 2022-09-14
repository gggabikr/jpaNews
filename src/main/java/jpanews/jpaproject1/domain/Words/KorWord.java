package jpanews.jpaproject1.domain.Words;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("K")
public class KorWord extends Word{
    private String kMeaning;
}
