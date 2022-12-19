package jpanews.jpaproject1.domain.Words;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("E")
public class EngWord extends Word{
    private String eMeaning;

    @Override
    public String getMeaning(){
        return this.eMeaning;
    }

    @Override
    public void setMeaning(String meaning){
        setEMeaning(meaning);
    }
}
