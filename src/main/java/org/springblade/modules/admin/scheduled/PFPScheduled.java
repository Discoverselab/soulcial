package org.springblade.modules.admin.scheduled;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.dao.PFPTokenMapper;
import org.springblade.modules.admin.dao.PFPTransactionMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.PFPTokenPO;
import org.springblade.modules.admin.pojo.po.PFPTransactionPO;
import org.springblade.modules.admin.service.ETHService;
import org.springblade.modules.admin.service.NftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务
 */
@Component
@Slf4j
public class PFPScheduled {

	@Autowired
	private PFPTransactionMapper pfpTransactionMapper;

	@Autowired
	private PFPTokenMapper pfpTokenMapper;

	@Autowired
	private ETHService ethService;

	@Autowired
	private NftService nftService;


	/**
	 * 关闭超时订单
	 * 每分钟执行一次
	 */
//	@Scheduled(cron = "0 0/1 * * * ?")
//	@Scheduled(fixedDelay = 60 * 1000)
	public void closeOvertimeOrder(){
		List<PFPTransactionPO> pfpTransactionPOS = pfpTransactionMapper.selectList(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			//已下单未交易数据
			.eq(PFPTransactionPO::getTransactionStatus, 0));

		log.info("===============当前进行中的订单数："+pfpTransactionPOS.size());
		int overTimeConnt = 0;

		for (PFPTransactionPO x : pfpTransactionPOS) {
			//关闭超时订单
			boolean isOverTime = closeOverTimeOrder(x);
			if (isOverTime){
				overTimeConnt++;
			}
		}

		log.info("===============关闭超时未支付订单数："+overTimeConnt);

	}


	/**
	 * 校验已支付订单
	 * 每分钟执行一次
	 */
//	@Scheduled(cron = "0 0/1 * * * ?")
//	@Scheduled(fixedDelay = 60 * 1000)
	public void checkPayedOrder(){
		List<PFPTransactionPO> pfpTransactionPOS = pfpTransactionMapper.selectList(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			//5-已付款未验证
			.eq(PFPTransactionPO::getTransactionStatus, 5));

		log.info("===============当前已付款未确认订单数："+pfpTransactionPOS.size());
		int overTimeConnt = 0;

		for (PFPTransactionPO x : pfpTransactionPOS) {

			R<Boolean> checkBNBTransResult = ethService.checkBNBTransacation(x.getBuyerMoneyTxnHash(),x.getListPrice(),x.getFromAddress(),x.getToAddress());

			if(checkBNBTransResult.getCode() == 200){
				Boolean data = checkBNBTransResult.getData();
				if(data){
					//校验成功

					//状态变更为：已付款未交易PFP
					x.setTransactionStatus(1);
					pfpTransactionMapper.updateById(x);

					//转NFT
					R result = nftService.transferNFT(x);
					log.info("======定时任务转NFT结果："+result.getCode() + "============" + result.getMsg());
				}else {
					//付款验证失败
					x.setTransactionStatus(4);
					pfpTransactionMapper.updateById(x);

					//变更NFT状态为允许交易
					updateTokenStatus(x.getTokenId());
				}
			}else {
				//确认中，不做处理
			}

			//关闭超时订单
			boolean isOverTime = closeOverTimeOrder(x);
			if (isOverTime){
				overTimeConnt++;
			}
		}

		log.info("===============关闭超时已付款未确认订单数："+overTimeConnt);

	}

	/**
	 * 变更NFT状态为允许交易
	 */
	private void updateTokenStatus(Long tokenId){
		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		//交易状态：0-可交易
		pfpTokenPO.setStatus(0);
		pfpTokenMapper.updateById(pfpTokenPO);
	}

	/**
	 * 关闭超时订单
	 * 已超时返回true
	 * 未超时返回false
	 */
	private boolean closeOverTimeOrder(PFPTransactionPO x){
		//创建时间
		Date createTime = x.getCreateTime();
		//当前时间
		long now = System.currentTimeMillis();

		//订单超过10分钟 未验证通过
		if(now - createTime.getTime() > 10 * 60 * 1000){
			//交易取消
			x.setTransactionStatus(3);
			pfpTransactionMapper.updateById(x);

			//变更NFT状态为允许交易
			updateTokenStatus(x.getTokenId());

			return true;
		}

		return false;
	}

}
