package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CheckAddressRushSupportTest {
	
	private PlaceRushOrderController placeRushOrderController;

	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}

	@ParameterizedTest
	@CsvSource({
		"ha noi, true",
		"số 1 ngõ 1A Tôn Thất Tùng Hà Nội, true",
		"@gdsfg#$F hanoi, false",
		"hung yen, false",
		", false"
	})
	void test(String address, boolean expected) {
		boolean isValid = placeRushOrderController.checkAddressRushSupport(address);
		assertEquals(expected, isValid);
	}

}
