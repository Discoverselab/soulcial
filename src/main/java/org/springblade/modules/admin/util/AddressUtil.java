package org.springblade.modules.admin.util;

import org.web3j.utils.Numeric;

public class AddressUtil {


	//校验ETH地址准确性
	public static boolean isETHAddress(String address){
		if(address == null){
			return false;
		}else {
			if(!address.startsWith("0x")){
				return false;
			}else {
				String cleanHexInput = Numeric.cleanHexPrefix(address);
				try {
					Numeric.toBigIntNoPrefix(cleanHexInput);
				}catch (NumberFormatException e){
					return false;
				}
				return cleanHexInput.length() == 40;
			}
		}
	}

}
