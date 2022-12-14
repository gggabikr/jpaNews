package jpanews.jpaproject1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

@Service("fileService")
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public void uploadFile(MultipartHttpServletRequest multiRequest) throws Exception{
        System.out.println("run upload method");
        Map<String, MultipartFile> files = multiRequest.getFileMap();

        Iterator<Map.Entry<String,MultipartFile>> itr = files.entrySet().iterator();

        MultipartFile mFile;

        //upload location
        String filePath = "/Users/jasonlee/Desktop/uploadedFile";

        // 파일명이 중복되었을 경우, 사용할 스트링 객체
        String saveFileName = "", savaFilePath = "";

        System.out.println("step1");
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

            // 저장될 경로와 파일명
            String saveFilePath = filePath + File.separator + fileName;

            System.out.println("step2");
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

            System.out.println("step3");
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

                System.out.println("step4");
                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(new File(savaFilePath));
            } else {
                System.out.println("step5");
                //생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에 transferTo(File f) 메서드를 이용해서 업로드처리한다.
                mFile.transferTo(saveFile);
            }
        }
    }
}

