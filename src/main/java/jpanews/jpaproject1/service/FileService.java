package jpanews.jpaproject1.service;

import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Service("fileService")
@RequiredArgsConstructor
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WordRepository wordRepository;


    public String uploadFile(MultipartHttpServletRequest multiRequest) throws Exception{
        System.out.println("run upload method");
        Map<String, MultipartFile> files = multiRequest.getFileMap();

        Iterator<Map.Entry<String,MultipartFile>> itr = files.entrySet().iterator();

        MultipartFile mFile;

        //upload location
        String filePath = "/Users/jasonlee/Desktop/uploadedFile";

        // 파일명이 중복되었을 경우, 사용할 스트링 객체
        String saveFileName = "", savaFilePath = "";

        // 읽어 올 요소가 있으면 true, 없으면 false를 반환한다.
        while (itr.hasNext()) {

            Map.Entry<String, MultipartFile> entry = itr.next();

            // entry에 값을 가져온다.
            mFile = entry.getValue();

            // 파일명
            String fileName = mFile.getOriginalFilename();

            // 확장자를 제외한 파일명
            String fileCutName = fileName.substring(0, fileName.lastIndexOf("."));

            // 확장자
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);

            //확장자 제한
            if(!fileExt.equalsIgnoreCase("csv") && !fileExt.equalsIgnoreCase("xlsx") && !fileExt.equalsIgnoreCase("txt")){
                throw new IllegalStateException("File type needs to be one of following: .csv, .xlsx, .txt");
            }

            // 저장될 경로와 파일명
            String saveFilePath = filePath + File.separator + fileName;

            // filePath에 해당되는 파일의 File 객체를 생성한다.
            File fileFolder = new File(filePath);

            if (!fileFolder.exists()) {
                // 부모 폴더까지 포함하여 경로에 폴더를 만든다.
                if (fileFolder.mkdirs()) {
                    logger.info("[file.mkdirs] : Success");
                } else {
                    logger.error("[file.mkdirs] : Fail");
                }
            }

            File saveFile = new File(saveFilePath);

            // saveFile이 File이면 true, 아니면 false
            // 파일명이 중복일 경우 덮어씌우지 않고, 파일명(1).확장자, 파일명(2).확장자 와 같은 형태로 생성한다.
            if (saveFile.isFile()) {
                boolean _exist = true;

                int index = 0;

                // 동일한 파일명이 존재하지 않을때까지 반복한다.
                while (_exist) {
                    index++;

                    saveFileName = fileCutName + "(" + index + ")." + fileExt;

                    String dictFile = filePath + File.separator + saveFileName;

                    _exist = new File(dictFile).isFile();

                    if (!_exist) {
                        savaFilePath = dictFile;
                    }
                }

                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(new File(savaFilePath));
            } else {
                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(saveFile);
            }
        }
        System.out.println(savaFilePath);
        return savaFilePath;
    }

    public void uploadDataFromFile(String filePathAndName) {
        System.out.println("savedFilePathAndName: " + filePathAndName);

//        final String delimiter = "[\\(\\)]";

        try {
            File file = new File(filePathAndName);
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
                    if(tempArr[1].contains("&")){
                        int i = tempArr[1].indexOf("&");
                        tempArr[1] = tempArr[1].substring(0, i).trim();
                    }
                }
                tempArr[2] = line.substring(indexOfBracketClose+1).trim();
//                System.out.println(Arrays.toString(tempArr));
                EngWord word = new EngWord();
                word.setName(tempArr[0]);


                if(tempArr[1].contains("imp") || tempArr[1].contains("p. p") || tempArr[1].contains("p pr")| tempArr[1].length() == 0 || tempArr[1].equals("n/a")){
                    word.setWordClass(WordClass.NOTABAILABLE);
                } else if (tempArr[1].equals("n") || tempArr[1].equals("n.")){
                    word.setWordClass(WordClass.NOUN);
                } else if (tempArr[1].equals("v") || tempArr[1].contains("v.")){
                    word.setWordClass(WordClass.VERB);
                } else if (tempArr[1].equals("adv") ||tempArr[1].equals("adv.")){
                    word.setWordClass(WordClass.ADVERB);
                } else if(tempArr[1].equals("a") || tempArr[1].equals("a.")){
                    word.setWordClass(WordClass.ADJECTIVE);
                } else if (tempArr[1].equals("pl.") || tempArr[1].equals("pl")){
                    word.setWordClass(WordClass.PLURAL);
                } else if (tempArr[1].equals("prep") || tempArr[1].equals("prep.")){
                    word.setWordClass(WordClass.PREPOSITION);
                } else if (tempArr[1].contains("prefix") ||tempArr[1].equals("pref") ||tempArr[1].equals("pref.")){
                    word.setWordClass(WordClass.PREFIX);
                } else if (tempArr[1].contains("superl")){
                    word.setWordClass(WordClass.SUPERLATIVE);
                } else if (tempArr[1].contains("interj")){
                    word.setWordClass(WordClass.INTERJECTION);
                } else if (tempArr[1].contains("prep")){
                    word.setWordClass(WordClass.PREPOSITION);
                } else if (tempArr[1].contains("pl")){
                    word.setWordClass(WordClass.PLURAL);
                } else if (tempArr[1].contains("adv")){
                    word.setWordClass(WordClass.ADVERB);
                } else if (tempArr[1].contains("a")){
                    word.setWordClass(WordClass.ADJECTIVE);
                } else if (tempArr[1].contains("n") || tempArr[1].contains("n.")){
                    word.setWordClass(WordClass.NOUN);
                } else{
                    word.setWordClass(WordClass.NOTABAILABLE);
                }
                word.setEMeaning(line.substring(indexOfBracketClose+1));
//                wordRepository.save(word);
//                System.out.println(word.getName() + ", " +word.getWordClass()
//                        + ", " + word.getMeaning());
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

