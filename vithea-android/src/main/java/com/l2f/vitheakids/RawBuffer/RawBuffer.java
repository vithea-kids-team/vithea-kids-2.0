package com.l2f.vitheakids.RawBuffer;

import java.util.concurrent.Callable;

public class RawBuffer implements Callable<String> {
	private byte[] buf;
	private int bsize;

	public RawBuffer(byte[] buf, int bsize) {
		this.buf = new byte[bsize];
		System.arraycopy(buf, 0, this.buf, 0, bsize);
		this.bsize = bsize;
	}

	@Override
	public String call() throws Exception {
		StringBuilder sentaud = new StringBuilder();
		for (int i = 0; i < bsize; i++) {
			sentaud.append(buf[i] + ",");
		}

		return sentaud.toString();
	}
	
}
//if(buf[i] == 0)
//continue;

//sentaud.append((byte)(buf[i] & (short)0xFF));		// * 32767
//sentaud.append(",");
//sentaud.append((byte)(buf[i] >> 8));		// * 32767
//sentaud.append(",");
