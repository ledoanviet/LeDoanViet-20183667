package controller;

import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.media.Media;
import entity.order.Order;

/**
 * class thêm chức năng đặt hàng nhanh
 * @author Việt
 *
 */
public class PlaceRushOrderController {
	
	/**
	 * logging
	 */
	private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());
	
	/**
	 * kiểm tra địa chỉ có hỗ trợ đặt hàng nhanh hay không
	 * @param address: địa chỉ nhận hàng
	 * @return result
	 */
	public boolean checkAddressRushSupport(String address) {
		if(address == null) return false;
    	String regex = "^[\\w\\s]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address);
        boolean isInHaNoi = address.toLowerCase().contains("ha noi") || address.toLowerCase().contains("hà nội");
        return m.matches() && isInHaNoi;
	}
	
	/**
	 * kiểm tra sản phẩm có hỗ trợ đặt hàng nhanh hay không
	 * @param media: sản phẩm trong đơn hàng
	 * @return result
	 */
	public boolean checkMediaRushSupport(Media media) {
		if(media == null) return false;
		
		return true;
	}
	
	/**
	 * tính phí ship
	 * @param order: đơn hàng sử dụng đặt hàng nhanh
	 * @return fees: phí ship mới
	 */
	public int calculateShippingFee(Order order) {
		Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
	}
	
}
