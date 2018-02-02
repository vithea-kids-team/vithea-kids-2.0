package com.l2f.vitheakids.util;

import org.springframework.http.ResponseEntity;

/***
 * Listener responsable for deliver the correct update from Async to activity
 */
public interface TaskListener {
	void onTaskStarted();
	void onTaskFinished(ResponseEntity<String> response);
}