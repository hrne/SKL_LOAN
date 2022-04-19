package com.st1.itx.batch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.util.log.SysLogger;

@Service
@Scope("prototype")
public class ChunkExecListener extends SysLogger implements ChunkListener {
	@Override
	public void beforeChunk(ChunkContext context) {
		this.info("SimpleChunkListener.beforeChunk");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		this.info("SimpleChunkListener.afterChunk");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		this.info("SimpleChunkListener.afterChunkError");
	}

}