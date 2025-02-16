package dmu.dasom.api.domain.google.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleApiService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleApiService.class);
    private static final String APPLICATION_NAME = "Recruit Form";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${google.credentials.json}")
    private String credentialsJson;
    private Sheets sheetsService;

    // Google Sheets API 서비스 객체를 생성하는 메소드
    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            byte[] decodedBytes = Base64.getDecoder().decode(credentialsJson);
            String credentialsJson = new String(decodedBytes, StandardCharsets.UTF_8);

            System.out.println(credentialsJson); // 디코딩된 JSON 출력

            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
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
            AppendValuesResponse response = service.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();
            logger.info("Appended rows: {}", response.getUpdates().getUpdatedRows());
        } catch (GeneralSecurityException e) {
            logger.error("Failed to write data to the spreadsheet due to security issues", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error("Failed to write data to the spreadsheet due to IO issues", e);
            throw new CustomException(ErrorCode.WRITE_FAIL);
        }
    }

    // 지원자 ID를 기준으로 행번호 찾기
    public int findRowByApplicantId(String spreadsheetId, String sheetName, Long applicantId) {
        try {
            Sheets service = getSheetsService();
            String range = sheetName + "!A:A";
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    List<Object> row = values.get(i);
                    if (!row.isEmpty() && row.get(0).toString().equals(applicantId.toString())) {
                        return i + 1;
                    }
                }
            }
            return values == null ? 1 : values.size() + 1;
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Failed to find row by applicant id", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public void updateSheet(String spreadsheetId, String range, List<List<Object>> values) {
        try {
            Sheets service = getSheetsService();
            ValueRange body = new ValueRange().setValues(values);
            UpdateValuesResponse response = service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            logger.info("Updated rows: {}", response.getUpdatedRows());
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Failed to update data in the spreadsheet", e);
            throw new CustomException(ErrorCode.WRITE_FAIL);
        }
    }

}
