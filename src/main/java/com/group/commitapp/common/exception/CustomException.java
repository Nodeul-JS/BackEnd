package com.group.commitapp.common.exception;

import com.group.commitapp.common.enums.CustomResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final CustomResponseStatus status;

	public CustomException(CustomResponseStatus status) {
		super(status.getMessage()); // Exception의 message 필드에도 기본 메시지 전달
		this.status = status;
	}
}
