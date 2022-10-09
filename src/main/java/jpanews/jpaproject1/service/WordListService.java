package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.MemberRepository;
import jpanews.jpaproject1.repository.WordListRepository;
import jpanews.jpaproject1.repository.WordListToWordRepository;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordListService {
    private final WordRepository wordRepository;
    private final WordListRepository wordListRepository;
    private final WordListToWordRepository wlwRepository;
    private final MemberRepository memberRepository;

    //==create WordList==//
    public Long createWordList(Long memberId, List<Long> wordIdList) {

        //Refer entities
        Member member = memberRepository.findOne(memberId);

        List<WordListToWord> wlws = new ArrayList<>();
        for(Long wordId : wordIdList){
            Word word = wordRepository.findOne(wordId);
            WordListToWord wlw = WordListToWord.createWordListToWord(word);
            wlws.add(wlw);
        }
        WordList newWordList = WordList.createNewWordList(member, wlws);

        return newWordList.getId();
    }
}
