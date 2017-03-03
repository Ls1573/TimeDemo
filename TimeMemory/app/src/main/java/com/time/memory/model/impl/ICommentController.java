package com.time.memory.model.impl;

import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.vo.CommentVo;
import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:评论
 * @date 2016/9/20 12:12
 */
public interface ICommentController {

	void reqComment(String url, String token, CommentVo commentVo, SimpleCallback callback);

	void reqRemoveComment(String url, CX02RespVo msgRequest, SimpleCallback callback);
}

