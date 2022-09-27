package jpanews.jpaproject1.domain;

public enum WordClass {


    NOUN("NOUN"), VERB("VERB"), ADJECTIVE("ADJECTIVE"),
    ADVERB("ADVERB"), PRONOUN("PRONOUN"), DETERMINER("DETERMINER"),
    PREPOSITION("PREPOSITION"),CONJUNCTION("CONJUNCTION"),INTERJECTION("INTERJECTION");
    //명사, 동사, 형용사, 부사, 대명사, 한정사, 전치사, 접속사, 감탄사

    private final String text;


    WordClass(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}