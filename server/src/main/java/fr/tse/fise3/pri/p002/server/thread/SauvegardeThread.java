package fr.tse.fise3.pri.p002.server.thread;

import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.tse.fise3.pri.p002.server.model.Post;
import fr.tse.fise3.pri.p002.server.service.PostService;

@Component
public class SauvegardeThread implements Runnable {

	@Autowired
	private PostService postService;
	
	private BlockingQueueSingleton postsQueue;

	public SauvegardeThread() {
		this.postsQueue = BlockingQueueSingleton.getInstance();
	}

	@Override
	public void run() {
		while(HalApiRequestThread.isRunning() || SemanticApiRequestThread.isRunning()) {
			try {
				Post p = postsQueue.getPost();
				this.postService.savePost(p);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Sauvegarde terminee !");
	}

}
