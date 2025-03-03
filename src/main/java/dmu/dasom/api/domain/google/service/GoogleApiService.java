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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class GoogleApiService {

    @Value("${google.credentials.json}")
    private String credentialsJson;

    @Value("${google.spreadsheet.id}")
    private String spreadSheetId;

    private static final String APPLICATION_NAME = "Recruit Form";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Sheets sheetsService;

    // Google Sheets API 서비스 객체를 생성하는 메소드
    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            ByteArrayInputStream decodedStream = new ByteArrayInputStream(Base64.getDecoder()
                .decode(credentialsJson));

            GoogleCredentials credentials = GoogleCredentials
                .fromStream(decodedStream)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

            sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
            )
                .setApplicationName(APPLICATION_NAME)
                .build();
        }
        return sheetsService;
    }

    public void appendToSheet(List<Applicant> applicants) {
        processSheetsUpdate(applicants, true);
    }

    public void updateSheet(List<Applicant> applicants) {
        processSheetsUpdate(applicants, false);
    }

    public int findRowIndexByStudentNo(String spreadSheetId, String sheetName, String studentNo) {
        try {
            List<List<Object>> rows = readSheet(spreadSheetId, sheetName + "!A:L"); // A열부터 L열까지 읽기

            for (int i = 0; i < rows.size(); i++) {
                List<Object> row = rows.get(i);
                if (!row.isEmpty() && row.get(2)
                    .equals(studentNo)) { // 학번(Student No)이 3번째 열(A=0 기준)
                    return i + 1;
                }
            }
        } catch (Exception e) {
            log.error("시트에서 행 찾기 실패");
        }
        return -1;
    }

    public List<List<Object>> readSheet(String spreadsheetId, String range) {
        try {
            Sheets service = getSheetsService();
            ValueRange response = service.spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute();

            return response.getValues();
        } catch (IOException | GeneralSecurityException e) {
            log.error("시트에서 데이터를 읽어오는 데 실패했습니다.");
            throw new CustomException(ErrorCode.SHEET_READ_FAIL);
        }
    }

    public int getLastRow(String spreadsheetId, String sheetName) {
        try {
            List<List<Object>> rows = readSheet(spreadsheetId, sheetName + "!A:L"); // A~L 열까지 읽기
            return rows == null ? 0 : rows.size(); // 데이터가 없으면 0 반환
        } catch (Exception e) {
            log.error("시트에서 마지막 행 찾기 실패");
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
            BatchUpdateValuesResponse response = service.spreadsheets()
                .values()
                .batchUpdate(spreadsheetId, batchUpdateRequest)
                .execute();

            log.info("시트 업데이트 성공. {}", response.getTotalUpdatedRows());
        } catch (IOException | GeneralSecurityException e) {
            log.error("시트 업데이트 실패.");
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
                        log.warn("시트에서 지원자를 찾을 수 없습니다. : {}", applicant.getStudentNo());
                        continue;
                    }
                    range = "Sheet1!A" + rowIndex + ":L" + rowIndex;
                }
                valueRanges.add(createValueRange(range, List.of(applicant.toGoogleSheetRow())));
            }

            batchUpdateSheet(spreadSheetId, valueRanges);
        } catch (Exception e) {
            log.error("시트 업데이트에 실패했습니다.", e);
            throw new CustomException(ErrorCode.SHEET_WRITE_FAIL);
        }
    }


}
