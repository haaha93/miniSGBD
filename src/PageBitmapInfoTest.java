import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import manager.PageBitmapInfo;

class PageBitmapInfoTest {

	private byte[] tab = {0,0,0,0,5}; 
	private PageBitmapInfo pbi= new PageBitmapInfo(tab);
	
	@Test
	void test() {
		int i = 0;
		while (pbi.getValueAtIndexOfSlotsStatus(i++)!=0) {
			System.out.println("1");
		}
	}

}
