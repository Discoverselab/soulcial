package org.springblade.modules.admin.event;

import cn.hutool.core.io.watch.Watcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 扫块
 */
@Component
@Slf4j
public class ScanBlockEvent implements ApplicationListener<ContextRefreshedEvent> {

//	@Autowired
//	private Watcher watcher;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		if (this.watcher != null) {
//			log.info("=======Initialize Block Data Watcher=====");
//			WatcherLog watcherLog = this.watcherLogService.findOne(this.coin.getName());
//			this.logger.info("watcherLog:{}", watcherLog);
//			if (watcherLog != null) {
//				this.watcher.setCurrentBlockHeight(watcherLog.getLastSyncHeight());
//			} else if (this.watcherSetting.getInitBlockHeight().equalsIgnoreCase("latest")) {
//				this.watcher.setCurrentBlockHeight(this.watcher.getNetworkBlockHeight());
//			} else {
//				Long height = Long.parseLong(this.watcherSetting.getInitBlockHeight());
//				this.watcher.setCurrentBlockHeight(height);
//			}
//
//			this.watcher.setStep(this.watcherSetting.getStep());
//			this.watcher.setCheckInterval(this.watcherSetting.getInterval());
//			this.watcher.setDepositEvent(this.depositEvent);
//			this.watcher.setCoin(this.coin);
//			this.watcher.setWatcherLogService(this.watcherLogService);
//			this.watcher.setConfirmation(this.watcherSetting.getConfirmation());
//			(new Thread(this.watcher)).start();
//		} else {
//			log.error("=====启动程序失败=====");
//		}
	}
}
