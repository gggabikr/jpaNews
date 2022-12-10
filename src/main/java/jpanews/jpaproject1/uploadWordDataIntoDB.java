package jpanews.jpaproject1;

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
                tempArr[0] = line.substring(0, indexOfBracketOpen);
//                System.out.println(tempArr[0]);

                if(indexOfBracketOpen+1 == indexOfBracketClose){
//                    System.out.println("n/a");
                    tempArr[1] = "n/a";
                }else{
//                    System.out.println(line.substring(indexOfBracketOpen+1, indexOfBracketClose-1));
                    tempArr[1] = line.substring(indexOfBracketOpen+1, indexOfBracketClose-1);
                }
                tempArr[2] = line.substring(indexOfBracketClose+1);
                System.out.println(Arrays.toString(tempArr));
                Word word = new EngWord();
                word.setName(tempArr[0]);
                case 
                word.setWordClass(tempArr[1]);

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


