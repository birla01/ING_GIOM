package com.transfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.util.SepaValidationException;

public class SepaTransferDocumentBuilderTest3 {

	public SepaTransferDocumentBuilderTest3() {
	}

	/**
	 * Test of toXml method, of class SepaTransferDocumentBuilder.
	 */
	@Test
	public void testToXml() throws Exception {
		SepaTransferDocumentData data = new SepaTransferDocumentData("MALADE51NWD", "DE89370400440532013000",
				"Hans Mustermann", "12345");
		Calendar dueDate = Calendar.getInstance();
		dueDate.set(Calendar.HOUR, 0);
		dueDate.set(Calendar.MINUTE, 0);
		dueDate.set(Calendar.SECOND, 0);
		dueDate.add(Calendar.DATE, 14);
		data.setDateOfExecution(dueDate);

		data.addPayment(createTestPayment("123.4539", "Arme Wurst", "MALADE51NWD", "DE89370400440532013000"));
		data.addPayment(createTestPayment("99.9930", "Arme Wurst2", "MALADE51NWD", "DE89370400440532013000"));
		data.addPayment(
				createTestPayment("10", "Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Name",
						"MALADE51NWD", "DE89370400440532013000"));

		String str_1 = null;
		str_1 = "<Document xmlns="+"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03"+"";
		File file = new File(System.getProperty("user.dir") + "\\input");
		for(File file_1 : file.listFiles()){
			if (file_1.getName().endsWith("txt")) {
				try (BufferedReader br = new BufferedReader(new FileReader(file_1))) {
					for (String line; (line = br.readLine()) != null;) {
					// process the line.
					String row[] = line.split(",");
					if (row[0].equals("110")) {
						System.out.println(line);
						
						SepaTransferDocumentBuilder3.toPain(data, row);
					}/* else if(row[0].equals(str_1)){
	                    SepaTransferDocumentBuilder3.toTibco(data);
					}*/
					else {
						System.out.println();
						SepaTransferDocumentBuilder3.toError(data);
					}
			}
			
				// line is not visible here.
			}catch (Exception e) {
				e.printStackTrace();
			}
		} else if(file_1.getName().endsWith("xml")){
	           File dest = new File(System.getProperty("user.dir") + "\\xml");
	           FileUtils.copyFileToDirectory(file_1, dest);
		}
		else{
            File dest = new File(System.getProperty("user.dir") + "\\exception");
			FileUtils.copyFileToDirectory(file_1, dest);
		}
	}
	}

	private SepaTransferPayment createTestPayment(String sum, String debitorName, String bic, String iban)
			throws SepaValidationException {
		SepaTransferPayment result = new SepaTransferPayment();

		result.setPayeeBic(bic);
		result.setPayeeIban(iban);
		result.setPayeeName(debitorName);
		result.setPaymentSum(new BigDecimal(sum));
		result.setReasonForPayment("test-Überweisung");
		return result;
	}

}
