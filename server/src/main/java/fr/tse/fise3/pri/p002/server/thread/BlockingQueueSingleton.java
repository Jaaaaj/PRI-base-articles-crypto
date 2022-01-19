package fr.tse.fise3.pri.p002.server.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Component;

import fr.tse.fise3.pri.p002.server.model.Post;

@Component
public class BlockingQueueSingleton {
	private static BlockingQueueSingleton singleton = new BlockingQueueSingleton();
	private BlockingQueue<Post> queue;
	
	public BlockingQueueSingleton() {
		this.queue = new ArrayBlockingQueue<Post>(500);
	}
	
	public static BlockingQueueSingleton getInstance() {
		return singleton;
	}
	
	public void putPost(Post post) {
		this.queue.add(post);
	}
	
	public Post getPost() throws InterruptedException {
		return this.queue.take();
	}
}
