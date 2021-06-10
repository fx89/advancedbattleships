package com.advancedbattleships.utilityservices;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.utilityservices.exception.UniqueTokenProviderServiceException;
import com.advancedbattleships.utilityservices.uniquetokenprovider.UniqueTokenProviderServiceConfig;
import com.advancedbattleships.utilityservices.uniquetokenprovider.UniqueTokenProviderServiceThread;

@Service
public class UniqueTokenProviderService {
	private static int THREADS_COUNT = 25;

	private UniqueTokenProviderServiceThread[] threads = new UniqueTokenProviderServiceThread[THREADS_COUNT];

	private int currentThreadId = 0;

	@Autowired
	private UniqueTokenProviderServiceConfig config;

	@PostConstruct
	private void init() throws UniqueTokenProviderServiceException {
		for (int t = 0 ; t < THREADS_COUNT ; t++) {
			char threadId = (char)('A' + t);
			threads[t] = new UniqueTokenProviderServiceThread(config.getInstanceId(), threadId, config.getDatadir());
		}
	}

	/**
	 * Provide one new unique token. If the token is not used (i.e. due to an error
	 * in the calling process), then the token is wasted. The provider will never
	 * provide this token again.
	 * @throws UniqueTokenProviderServiceException 
	 */
	public String provide() throws UniqueTokenProviderServiceException {
		return acquireThread().provide();
	}

	/**
	 * Provide N unique tokens, for bulk operations. If the tokens are not used
	 * (i.e. due to an error in the calling process), then the tokens are wasted.
	 * The provider will never provide these tokens again.
	 * @throws UniqueTokenProviderServiceException 
	 */
	public String[] provide(int n) throws UniqueTokenProviderServiceException {
		return acquireThread().provide(n);
	}

	private synchronized UniqueTokenProviderServiceThread acquireThread() {
		currentThreadId++;

		if (currentThreadId == THREADS_COUNT) {
			currentThreadId = 0;
		}

		return threads[currentThreadId];
	}
}
