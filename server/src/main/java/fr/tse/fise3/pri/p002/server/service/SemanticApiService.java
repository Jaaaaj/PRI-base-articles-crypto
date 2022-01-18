package fr.tse.fise3.pri.p002.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import fr.tse.fise3.pri.p002.server.thread.SemanticApiRequestThread;

@Service
public class SemanticApiService {

	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private SemanticApiRequestThread semanticThread;
	
	public void start() {
		this.taskExecutor.execute(this.semanticThread);
	}
	
	public Boolean isRunning() {
		return this.semanticThread.isRunning();
	}
}
