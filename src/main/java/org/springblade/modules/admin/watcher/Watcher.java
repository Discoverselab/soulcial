package org.springblade.modules.admin.watcher;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Data
public abstract class Watcher implements Runnable{
	private boolean stop = false;

	/**
	 * 扫块间隔30秒
	 */
	private Long checkInterval = 30 * 1000L;
	private Long currentBlockHeight = 0L;
	private int step = 5;
	private int confirmation = 3;

	public void check() {
		try {
			Long networkBlockNumber = this.getNetworkBlockHeight() - (long)this.confirmation + 1L;
			if (this.currentBlockHeight < networkBlockNumber) {
				long startBlockNumber = this.currentBlockHeight + 1L;
				this.currentBlockHeight = networkBlockNumber - this.currentBlockHeight > (long)this.step ? this.currentBlockHeight + (long)this.step : networkBlockNumber;
				log.info("replay block from {} to {}", startBlockNumber, this.currentBlockHeight);
//				List<Deposit> deposits = this.replayBlock(startBlockNumber, this.currentBlockHeight);
//				if (deposits != null) {
//					this.watcherLogService.update(this.coin.getName(), this.currentBlockHeight);
//				} else {
//					log.info("扫块失败！！！");
//					this.currentBlockHeight = startBlockNumber - 1L;
//				}
			} else {
				log.info("Already latest height: {}, networkBlockHeight: {},nothing to do!", this.currentBlockHeight, networkBlockNumber);
			}
		} catch (Exception var5) {
			var5.printStackTrace();
		}

	}

//	public abstract List<Deposit> replayBlock(Long var1, Long var2);
	public abstract void replayBlock(Long var1, Long var2);

	public abstract Long getNetworkBlockHeight();

	public void run() {
		this.stop = false;
		long nextCheck = 0L;

		while(!this.stop) {
			if (nextCheck <= System.currentTimeMillis()) {
				try {
					nextCheck = System.currentTimeMillis() + this.checkInterval;
					log.info("check...");
					this.check();
				} catch (Exception var4) {
					log.info(var4.getMessage());
				}
			} else {
				try {
					Thread.sleep(Math.max(nextCheck - System.currentTimeMillis(), 100L));
				} catch (InterruptedException var5) {
					log.info(var5.getMessage());
				}
			}
		}

	}
}
