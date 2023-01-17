package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordListToWord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter @Setter
public class testQuestionObj {
    private WordListToWord wlw;

    private List<String> answerList;

    private int indexOfCorrectAns;

    @Nullable
    private int indexOfUserInput;
}
