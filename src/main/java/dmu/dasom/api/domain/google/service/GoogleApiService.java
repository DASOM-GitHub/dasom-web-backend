package dmu.dasom.api.domain.google.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleApiService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleApiService.class);
    private static final String APPLICATION_NAME = "Recruit Form";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    @Value("${google.credentials.file.path}")
    private String credentialsFilePath;
    @Value("${google.spreadsheet.id}")
    private String spreadSheetId;
    private Sheets sheetsService;

    // Google Sheets API 서비스 객체를 생성하는 메소드
    private Sheets getSheetsService() throws IOException, GeneralSecurityException{
        if(sheetsService == null){
            ClassPathResource resource = new ClassPathResource(credentialsFilePath);
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

            sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return sheetsService;
    }

    public void writeToSheet(String spreadsheetId, String range, List<List<Object>> values) {
        try {
            Sheets service = getSheetsService();
            ValueRange body = new ValueRange().setValues(values);
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();
        }  catch (IOException | GeneralSecurityException e) {
            logger.error("구글 시트에 데이터를 쓰는 데 실패했습니다.", e);
            throw new CustomException(ErrorCode.WRITE_FAIL);
        }
    }

    public void appendToSheet(List<Applicant> applicants) {
        processSheetsUpdate(applicants, true);
    }

    public void updateSheet(List<Applicant> applicants) {
        processSheetsUpdate(applicants, false);
    }

    private int findRowIndexByStudentNo(String spreadSheetId, String sheetName, String studentNo){
        try {
            List<List<Object>> rows = readSheet(spreadSheetId, sheetName + "!A:L"); // A열부터 L열까지 읽기

            for (int i = 0; i < rows.size(); i++){
                List<Object> row = rows.get(i);
                if(!row.isEmpty() && row.get(2).equals(studentNo)){
                    return i + 1;
                }
            }
        } catch (Exception e) {
            logger.error("구글시트에서 행 찾기 실패", e);
        }
        return -1;
    }

    public List<List<Object>> readSheet(String spreadsheetId, String range) {
        try {
            Sheets service = getSheetsService();
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            return response.getValues();
        } catch (IOException | GeneralSecurityException e) {
            logger.error("시트에서 데이터를 읽어오는데 실패했습니다.", e);
            throw new CustomException(ErrorCode.SHEET_READ_FAIL);
        }
    }

    public int getLastRow(String spreadsheetId, String sheetName) {
        try {
            List<List<Object>> rows = readSheet(spreadsheetId, sheetName + "!A:L"); // A~L 열까지 읽기
            return rows == null ? 0 : rows.size(); // 데이터가 없으면 0 반환
        } catch (Exception e) {
            logger.error("Failed to retrieve last row from Google Sheet", e);
            throw new CustomException(ErrorCode.SHEET_READ_FAIL);
        }
    }

    public void batchUpdateSheet(String spreadsheetId, List<ValueRange> valueRanges) {
        try {
            Sheets service = getSheetsService();

            // BatchUpdate 요청 생성
            BatchUpdateValuesRequest batchUpdateRequest = new BatchUpdateValuesRequest()
                    .setValueInputOption("USER_ENTERED") // 사용자 입력 형식으로 값 설정
                    .setData(valueRanges); // 여러 ValueRange 추가

            // BatchUpdate 실행
            BatchUpdateValuesResponse response = service.spreadsheets().values()
                    .batchUpdate(spreadsheetId, batchUpdateRequest)
                    .execute();

            logger.info("Batch update completed. Total updated rows: {}", response.getTotalUpdatedRows());
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Batch update failed", e);
            throw new CustomException(ErrorCode.SHEET_WRITE_FAIL);
        }
    }


    private ValueRange createValueRange(String range, List<List<Object>> values) {
        return new ValueRange()
                .setRange(range)
                .setMajorDimension("ROWS") // 행 단위로 데이터 설정
                .setValues(values);
    }

    public void processSheetsUpdate(List<Applicant> applicants, boolean isAppend) {
        try {
            List<ValueRange> valueRanges = new ArrayList<>();
            int lastRow = isAppend ? getLastRow(spreadSheetId, "Sheet1") : -1;

            for (Applicant applicant : applicants) {
                String range;
                if (isAppend) {
                    range = "Sheet1!A" + (lastRow + 1);
                    lastRow++;
                } else {
                    int rowIndex = findRowIndexByStudentNo(spreadSheetId, "Sheet1", applicant.getStudentNo());
                    if (rowIndex == -1) {
                        logger.warn("구글시트에서 사용자를 찾을 수 없습니다. : {}", applicant.getStudentNo());
                        continue;
                    }
                    range = "Sheet1!A" + rowIndex + ":L" + rowIndex;
                }
                valueRanges.add(createValueRange(range, List.of(applicant.toGoogleSheetRow())));
            }

            batchUpdateSheet(spreadSheetId, valueRanges);
        } catch (Exception e) {
            logger.error("구글시트 업데이트에 실패했습니다.", e);
            throw new CustomException(ErrorCode.SHEET_WRITE_FAIL);
        }
    }

}
