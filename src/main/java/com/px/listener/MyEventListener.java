package com.px.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

public class MyEventListener implements ActivitiEventListener {

	  public void onEvent(ActivitiEvent event) {
	    switch (event.getType()) {

	      case JOB_EXECUTION_SUCCESS:
	        System.out.println("A job well done!");
	        break;

	      case JOB_EXECUTION_FAILURE:
	        System.out.println("A job has failed...");
	        break;

	      default:
	        System.out.println("Event received: " + event.getType());
	    }
	  }

	  public boolean isFailOnException() {
	    // The logic in the onEvent method of this listener is not critical, exceptions
	    // can be ignored if logging fails...
	    return false;
	  }
	}