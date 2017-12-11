package edu.nju.onlineexam.utils.file;

import edu.nju.onlineexam.MainApplication;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 文件操作
 *
 * @author yyy
 * @create 2017-12-05 12:01
 */
public class FileUtil {

  /**
   * 下载文件
   * @param filePath 文件所在路径
   * @param userFileName 呈现给用户的文件名
   * @return 文件源
   * @throws IOException 异常
   */
  public static ResponseEntity<Resource> download(String filePath, String userFileName)
      throws IOException {
    String pathStr = MainApplication.class.getClassLoader().getResource(filePath).getPath();
    File file = new File(pathStr);
    Path path = Paths.get(file.getAbsolutePath());

    return download(Files.readAllBytes(path),userFileName);
  }

  public static ResponseEntity<Resource> download(byte[] bytes,String userFileName)
      throws IOException {
    ByteArrayResource resource = new ByteArrayResource(bytes);

    String fileName = URLEncoder.encode(userFileName, "UTF-8");

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  /**
   * 创建excel的表头
   * @param workbook workbook
   * @param sheet sheet
   * @param titles 表头列表
   */
  public static void createExcelTitle(HSSFWorkbook workbook,HSSFSheet sheet,List<String> titles){

    HSSFRow row = sheet.createRow(0);

    HSSFCellStyle style = workbook.createCellStyle();
    HSSFFont font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);

    HSSFCell cell;
    for(int i=0;i<titles.size();i++){
      cell = row.createCell(i);
      cell.setCellValue(titles.get(i));
      cell.setCellStyle(style);
    }

  }
}
