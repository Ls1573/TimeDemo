package com.time.memory.presenter;

import com.time.memory.model.CommentController;
import com.time.memory.model.impl.ICommentController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ICommentView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:评论
 * @date 2016/9/20 12:10
 */
public class CommentPresenter extends BasePresenter<ICommentView> {
	private ICommentController iCommentController;

	// m层
	public CommentPresenter() {
		iCommentController = new CommentController();
	}
}
