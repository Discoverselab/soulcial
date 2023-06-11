package org.springblade.modules.admin.service.impl;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.dao.PFPContractMapper;
import org.springblade.modules.admin.pojo.po.PFPContractPO;
import org.springblade.modules.admin.pojo.vo.MintNftVo;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.service.PfpContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PfpContractServiceImpl implements PfpContractService {

	@Autowired
	PFPContractMapper pfpContractMapper;

	@Override
	public PFPContractPO getContract() {
		PFPContractPO pfpContractPO = pfpContractMapper.selectById(1L);
		return pfpContractPO;
	}
}
