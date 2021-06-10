package com.advancedbattleships.utilityservices.uniquetokenprovider;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.advancedbattleships.common.lang.LongTokenizer;
import com.advancedbattleships.common.lang.longtokenizer.AlphanumericLongTokenizer;
import com.advancedbattleships.utilityservices.exception.UniqueTokenProviderServiceException;

public class UniqueTokenProviderServiceThread {
	private LongTokenizer tokenizer = new AlphanumericLongTokenizer();

	private long lastId = 0L;

	private String dataFilePathName;

	/**
	 * The instance and thread IDs make sure the generated tokens are unique across
	 * the entire app ecosystem. The instance ID should ultimately come from the
	 * application.properties file specific to the service instance making use of
	 * this class. The thread ID is given upon initialization and has the role of
	 * uniquely identifying the thread within the instance. Multiple threads are
	 * required to diminish bottlenecks caused by various bulk operations, which
	 * would keep the thread busy and inaccessible until it finishes generating all
	 * the required id's.
	 */
	private char instanceId, threadId;

	public UniqueTokenProviderServiceThread(char instanceId, char threadId, String dataDirPath)
	throws UniqueTokenProviderServiceException
	{
		this.instanceId = instanceId;
		this.threadId = threadId;

		dataFilePathName = dataDirPath + "/tokenprovider_" + instanceId + threadId + ".dat";
		resolveDataFile();
		acquireLastId();
	}

	/**
	 * Provide one token
	 * @throws UniqueTokenProviderServiceException 
	 */
	public synchronized String provide() throws UniqueTokenProviderServiceException {
		lastId++;
		persistLastId();
		return "" + instanceId + threadId + tokenizer.tokenize(lastId);
	}

	/**
	 * Provide multiple tokens, for bulk operations
	 * @throws UniqueTokenProviderServiceException 
	 */
	public synchronized String[] provide(int n) throws UniqueTokenProviderServiceException {
		lastId++;

		String[] ret = new String[n];
		for (int x = 0 ; x < n ; x++) {
			ret[x] = "" + instanceId + threadId + tokenizer.tokenize(lastId);
		}

		lastId += n;
		persistLastId();

		return ret;
	}

	private void resolveDataFile() throws UniqueTokenProviderServiceException {
		if (false == Files.exists(Paths.get(dataFilePathName))) {
			try {
				persistLastId();
			} catch (UniqueTokenProviderServiceException exc) {
				throw new UniqueTokenProviderServiceException("Unable to initialize unique token service provider data file [" + dataFilePathName + "]", exc.getCause()); 
			}
		}
	}

	private void acquireLastId() throws UniqueTokenProviderServiceException {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(dataFilePathName));
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
			buffer.put(bytes);
			buffer.flip();
			lastId = buffer.getLong();
		} catch (IOException | NumberFormatException exc) {
			throw new UniqueTokenProviderServiceException("Unable to read token provider data file [" + dataFilePathName + "]", exc);
		}
	}

	private void persistLastId() throws UniqueTokenProviderServiceException {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
			buffer.putLong(lastId);
			Files.write(Paths.get(dataFilePathName), buffer.array(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException exc) {
			throw new UniqueTokenProviderServiceException("Unable to persist token to data file [" + dataFilePathName + "]", exc);
		}
	}
}
