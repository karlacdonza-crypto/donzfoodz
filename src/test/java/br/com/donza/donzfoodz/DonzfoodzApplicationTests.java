package br.com.donza.donzfoodz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DonzfoodzApplicationTests {

	@MockBean
	private software.amazon.awssdk.services.s3.S3Client s3Client;

	@MockBean
	private software.amazon.awssdk.services.sqs.SqsClient sqsClient;

	@MockBean
	private software.amazon.awssdk.services.sns.SnsClient snsClient;

	@MockBean
	private software.amazon.awssdk.services.rds.RdsClient rdsClient;

	@Test
	void contextLoads() {
	}
}
