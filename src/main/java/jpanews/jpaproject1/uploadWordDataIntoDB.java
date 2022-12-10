package jpanews.jpaproject1;

import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.WordRepository;

import java.io.*;
import java.util.Arrays;

public class uploadWordDataIntoDB {

    WordRepository wordRepository;

    public static void read(String csvFile) {
//        final String delimiter = "[\\(\\)]";

        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            System.out.println(br.readLine());
            String line = "";
            String[] tempArr = new String[3];
            while((line = br.readLine()) != null) {

                int indexOfBracketOpen = line.indexOf('(');
                int indexOfBracketClose = line.indexOf(')');
//                System.out.println(indexOfBracketOpen + "," + indexOfBracketClose);
                tempArr[0] = line.substring(0, indexOfBracketOpen).trim();
//                System.out.println(tempArr[0]);

                if(indexOfBracketOpen+1 == indexOfBracketClose){
//                    System.out.println("n/a");
                    tempArr[1] = "n/a";
                }else{
//                    System.out.println(line.substring(indexOfBracketOpen+1, indexOfBracketClose-1));
                    tempArr[1] = line.substring(indexOfBracketOpen+1, indexOfBracketClose-1).trim();
                }
                tempArr[2] = line.substring(indexOfBracketClose+1).trim();
//                System.out.println(Arrays.toString(tempArr));
                Word word = new EngWord();
                word.setName(tempArr[0]);
//                switch(tempArr[1]){
//                    case x= "n":
//                        continue;
//                    case "v":
//                    case "v. t.":
//                    case "v. i":
//                        continue;
//                    case "a":
//                        continue;
//                }


                if(tempArr[1].contains("imp") || tempArr[1].contains("p. p") || tempArr[1].length() == 0 || tempArr[1].equals("n/a")){
                    word.setWordClass(WordClass.NOTABAILABLE);
                } else if (tempArr[1].equals("n")){
                    word.setWordClass(WordClass.NOUN);
                } else if (tempArr[1].equals("v") || tempArr[1].contains("v.")){
                    word.setWordClass(WordClass.VERB);
                } else if (tempArr[1].equals("adv")){
                    word.setWordClass(WordClass.ADVERB);
                } else if (tempArr[1].equals("a")){
                    word.setWordClass(WordClass.ADJECTIVE);
                } //pl.? superl? adv. & a? p. pr. & vb. n? imp? 등등
//                word.setWordClass(tempArr[1]);
                System.out.println(word.getName() + ", " +word.getWordClass());
                //null 값이 종종 있다. n.pl. 이라는 값을 가진것 포함.
                // 이런경우는 이걸 또 하나의 배열로 봐야하나.....
//                System.out.println(line.substring(indexOfBracketClose+1));//                for(String tempStr : tempArr) {
//                    System.out.print(tempStr + "///");
//                }
//                System.out.println();
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // csv file to read
        String csvFile = "src/main/java/jpanews/jpaproject1/VocabularyData/A.csv";
        uploadWordDataIntoDB.read(csvFile);
    }
}


